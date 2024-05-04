import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
/**
 * JSON hashes/objects.
 */
public class JSONHash implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  private LinkedList<KVPair<JSONString, JSONValue>>[] table;
  private int size;
  private static final int INITIAL_CAPACITY = 16;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+
  @SuppressWarnings("unchecked")
  public JSONHash() {
    
    table = (LinkedList<KVPair<JSONString, JSONValue>>[]) new LinkedList[INITIAL_CAPACITY];
    for (int i = 0; i < INITIAL_CAPACITY; i++) {
      table[i] = new LinkedList<>();
    }

    this.size = 0;
  } 

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    Iterator<KVPair<JSONString, JSONValue>> it = this.iterator();
    StringBuilder sb = new StringBuilder("{");
    while (it.hasNext()) {
      KVPair<JSONString, JSONValue> pair = it.next();
      sb.append(pair.key().toString()).append(": ").append(pair.value().toString());
      if (it.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (!(other instanceof JSONHash)) {
      return false;
    }
    JSONHash otherHash = (JSONHash) other;
    if (this.size != otherHash.size) {
      return false;
    }

    for (int i = 0; i < this.table.length; i++) {
      for (KVPair<JSONString, JSONValue> pair : this.table[i]) {
        JSONValue otherValue = otherHash.get(pair.key());
        if (otherValue == null || !otherValue.equals(pair.value())) {
          return false;
        }
      }
    }
    return true;
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return this.table.hashCode();       
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.print(this.toString());
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString,JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  private int hash(JSONString key) {
    int hashCode = key.hashCode();
    return Math.abs(hashCode % table.length);
  }
  /**
   * Get the value associated with a key.
   */
  public JSONValue get(JSONString key) {
    int index = hash(key);
    for (KVPair<JSONString, JSONValue> pair : table[index]) {
      if (pair.key().equals(key)) {
        return pair.value();
      }
    }
    return null;
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString,JSONValue>> iterator() {
    LinkedList<KVPair<JSONString, JSONValue>> all = new LinkedList<>();
      for (LinkedList<KVPair<JSONString, JSONValue>> ht : table) {
        all.addAll(ht);
      }
    return all.iterator();
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
    int index = hash(key);
    
    // if key already exists in the table, update the value and return.
    for (KVPair<JSONString, JSONValue> pair : table[index]) {
      if (pair.key().equals(key)) {
        pair.setValue(value);
        return;
      } // if
    } // for

    // else if the value does not exist in the table, add the value to the correct index of 'table'
    table[index].add(new KVPair<>(key, value));
    // then, increase the size
    this.size++;

  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return this.size;           // STUB
  } // size()

} // class JSONHash
