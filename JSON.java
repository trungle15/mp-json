import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * Utilities for our simple implementation of JSON.
 */
public class JSON {
  // +---------------+-----------------------------------------------
  // | Static fields |
  // +---------------+

  /**
   * The current position in the input.
   */
  static int pos;

  // +----------------+----------------------------------------------
  // | Static methods |
  // +----------------+

  /**
   * Parse a string into JSON.
   */
  public static JSONValue parse(String source) throws ParseException, IOException {
    return parse(new StringReader(source));
  } // parse(String)

  /**
   * Parse a file into JSON.
   */
  public static JSONValue parseFile(String filename) throws ParseException, IOException {
    FileReader reader = new FileReader(filename);
    JSONValue result = parse(reader);
    reader.close();
    return result;
  } // parseFile(String)

  /**
   * Parse JSON from a reader.
   */
  public static JSONValue parse(Reader source) throws ParseException, IOException {
    pos = 0;
    JSONValue result = parseKernel(source);
    if (-1 != skipWhitespace(source)) {
      throw new ParseException("Characters remain at end", pos);
    }
    return result;
  } // parse(Reader)

  // +---------------+-----------------------------------------------
  // | Local helpers |
  // +---------------+

  /**
   * Parse JSON from a reader, keeping track of the current position
   */
  static JSONValue parseKernel(Reader source) throws ParseException, IOException {
    int ch;
    ch = skipWhitespace(source);
    if (-1 == ch) {
      throw new ParseException("Unexpected end of file", pos);
    }
    switch (ch) {
      // Array
      case '[':
        return parseJSONArray(source);
      // String
      case '"':
        return parseJSONString(source);
      // Constants
      case 't':
      case 'f':
      case 'n':
        return parseJSONConstant(source, ch);
      case '-': // numbers are ascii index 48-57 --> 48=0 57=9
      case '0': case '1': case '2': case '3': case '4':  
      case '5': case '6': case '7': case '8': case '9':
        return parseJSONNumber(source, ch);
      case '{':
        return parseJSONHash(source);
    }
    throw new ParseException("Unimplemented", pos);
  } // parseKernel


  ///////////////////////////////////////////////
  /////// HELPER PARSER FUNCTIONS //////////////
  static JSONValue parseJSONString(Reader source) throws IOException{
    int integer;
    String str = "";
    while ((integer = source.read()) != '\"') {
        char ch = (char) integer;
        str += ch;
    }
    return new JSONString(str);
  }

  // this would be called when something starts with '-' or [0-9]
  static JSONValue parseJSONNumber(Reader source, int initial_ch) throws IOException {
    int integer = initial_ch;
    String str = "";
    if ((char) integer == '-') { // if the initial_ch is "-", just receive a new character.
      integer = source.read();
    }
    while (integer >= 48 && integer <= 57){
      char ch = (char) integer;
      str += ch;
      integer = source.read();
    }
    return new JSONInteger(str);
  }
  /**
   * "if you see an open bracket, you should create a new JSONArray and then 
   * recursively parse the next values you see, stopping when you hit an end brace"
   */
  static JSONValue parseJSONArray (Reader source) throws IOException, ParseException {
    JSONArray arr = new JSONArray();
    int ch = skipWhitespace(source);
    // construct JSON Array recursively
    // every individual element in the array should be a JSONValue

    if (ch == ']') {
      return arr;
    }

    while (true) {
      JSONValue value = parseKernel(source);
      arr.add(value);

      ch = skipWhitespace(source);
      if (ch == ']') {
        break;
      } else if (ch == ',') {
        ch = skipWhitespace(source);
      } else {
        throw new ParseException("Expected ',' or ']' in array", pos);
      }
    }

    return arr;
  }

  static JSONValue parseJSONHash(Reader source) {
    // STUB
    return new JSONHash();
  }

  static JSONValue parseJSONConstant(Reader source, int firstChar) throws IOException, ParseException {
    StringBuilder str = new StringBuilder();
    str.append((char) firstChar);

    for (int i = 0; i < 4; i++) {
      int ch = source.read();
      if (ch != -1) {
        str.append((char) ch);
      }
    } 

    String constant = str.toString();

    if ("true".equals(constant)) {
      return JSONConstant.TRUE;
    } else if ("false".equals(constant)) {
      return JSONConstant.FALSE;
    } else if ("null".equals(constant)) {
      return JSONConstant.NULL;
    } else {
      throw new ParseException("Unexpected constant: " + constant, pos);
    }
  }

  /**
   * Get the next character from source, skipping over whitespace.
   */
  static int skipWhitespace(Reader source) throws IOException {
    int ch;
    do {
      ch = source.read();
      ++pos;
    } while (isWhitespace(ch));
    return ch;
  } // skipWhitespace(Reader)

  /**
   * Determine if a character is JSON whitespace (newline, carriage return,
   * space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
