import java.io.PrintWriter;

/**
 * JSON strings.
 */
public class JSONString implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying string.
   */
  String value;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Build a new JSON string for a particular string.
   */
  public JSONString(String value) {
    this.value = value;
  } // JSONString(String)

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    StringBuilder ret = new StringBuilder();
    String parseContent;

    // Escape the escape character first
    parseContent = value.replace("\\", "\\\\");

    // Replace special characters
    parseContent = parseContent.replace("\"", "\\\"");  // Escape double quotes
    parseContent = parseContent.replace("\n", "\\n");  // Escape newlines
    parseContent = parseContent.replace("\r", "\\r");  // Escape carriage returns
    parseContent = parseContent.replace("\t", "\\t");  // Escape tabs
    parseContent = parseContent.replace("\b", "\\b");  // Escape backspace
    parseContent = parseContent.replace("\f", "\\f");  // Escape formfeed

    // Adding first and last quotation mark
    ret.append("\"").append(parseContent).append("\"");

    return ret.toString();
  }

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || this.getClass() != other.getClass()) {
      return false;
    }

    JSONString that = (JSONString) other;
    return this.value.equals(that.value);
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return value.hashCode();
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.println(this.toString());
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public String getValue() {
    return this.value;
  } // getValue()

} // class JSONString
