import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.naming.InitialContext;

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
  private static final double LOAD_FACTOR = 0.75;

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
    return "";          // STUB
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    return true;        // STUB
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return 0;           // STUB
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
                        // STUB
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
    return null;        // STUB
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
    int keyhash = hash(key);
    
    // if key already exists in the table, update the value and return.
    for (KVPair<JSONString, JSONValue> pair : table[keyhash]) {
      if (pair.key().equals(key)) {
        pair.setValue(value);
        return;
      } // if
    } // for

    // else if the value does not exist in the table, add the value to the correct index of 'table'
    table[keyhash].add(new KVPair<JSONString,JSONValue>(key, value));
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
