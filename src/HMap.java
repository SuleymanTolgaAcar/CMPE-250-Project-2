import java.util.LinkedList;
import java.util.List;

/**
 * Hash map implementation using separate chaining.
 * @param <K> key type
 * @param <V> value type
 */
public class HMap<K, V> {

    /**
     * Entry class for the HMap class.
     * Has data fields for key and value.
     */
    private class Entry{
        K key;
        V value;
        Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 101;
    private static final double DEFAULT_LOAD_FACTOR = 2;

    private int size;
    /**
     * Array of lists of entries.
     */
    private List<Entry>[] lists;

    public HMap(){
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor for the HMap class.
     * Creates a new array of lists of entries with the size of the next prime number.
     * Table size is prime, so the hash codes are relatively prime to the table size. Therefore, makes it more efficient.
     * Used via the constructor with no parameters.
     */
    public HMap(int capacity){
        lists = new List[nextPrime(capacity)];
        for(int i = 0; i < lists.length; i++){
            lists[i] = new LinkedList<>();
        }
        size = 0;
    }

    /**
     * Hash function for the HMap class.
     * Uses the default hashCode method of the key object.
     * Then, uses the modulo operator to find the index of the array.
     * @return hash value
     */
    private int hash(K key){
        int hashValue = key.hashCode();
        hashValue = hashValue % lists.length;
        if(hashValue < 0) {
            hashValue += lists.length;
        }
        return hashValue;
    }

    /**
     * Finds the next prime number after the given number.
     * @return next prime number
     */
    private int nextPrime(int n){
        if(n % 2 == 0){
            n++;
        }
        for(; !isPrime(n); n += 2){
            ;
        }
        return n;
    }

    /**
     * Checks if the given number is prime.
     * @return true if the given number is prime, false otherwise
     */
    private boolean isPrime(int n){
        if(n == 2 || n == 3){
            return true;
        }
        if(n == 1 || n % 2 == 0){
            return false;
        }
        for(int i = 3; i * i <= n; i += 2){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Puts the given key-value pair into the hash map.
     * If the key already exists, updates the value.
     * If the load factor is greater than the default load factor, rehashes the hash map.
     */
    public void put(K key, V value){
        List<Entry> list = lists[hash(key)];
        for(Entry entry : list){
            if(entry.key.equals(key)){
                entry.value = value;
                return;
            }
        }
        list.add(new Entry(key, value));
        size++;
        if(size > lists.length * DEFAULT_LOAD_FACTOR){
            rehash();
        }
    }

    /**
     * Gets the value of the given key.
     * @return value of the given key
     */
    public V get(K key){
        List<Entry> list = lists[hash(key)];
        for(Entry entry : list){
            if(entry.key.equals(key)){
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Removes the given key-value pair from the hash map.
     * @return value of the given key
     */
    public V remove(K key){
        List<Entry> list = lists[hash(key)];
        for(Entry entry : list){
            if(entry.key.equals(key)){
                list.remove(entry);
                size--;
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Checks if the hash map contains the given key.
     * @return true if the hash map contains the given key, false otherwise
     */
    public boolean containsKey(K key){
        List<Entry> list = lists[hash(key)];
        for(Entry entry : list){
            if(entry.key.equals(key)){
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the hash map.
     */
    public void clear(){
        for(int i = 0; i < lists.length; i++){
            lists[i].clear();
        }
        size = 0;
    }

    /**
     * Doubles the size of the hash map and rehashes the hash map.
     * Used when the load factor is greater than the default load factor.
     * Creates a new array of lists of entries with the size of the next prime number after the double of the current size.
     * Then, rehashes the hash map by putting the entries into the new array of lists of entries.
     */
    private void rehash(){
        List<Entry>[] oldLists = lists;
        lists = new List[nextPrime(lists.length * 2)];
        for(int i = 0; i < lists.length; i++){
            lists[i] = new LinkedList<>();
        }
        size = 0;
        for(List<Entry> list : oldLists){
            for(Entry entry : list){
                put(entry.key, entry.value);
            }
        }
    }

    /**
     * @return list of values in the hash map
     */
    public List<V> valueList(){
        List<V> valueList = new LinkedList<>();
        for(List<Entry> list : lists){
            for(Entry entry : list){
                valueList.add(entry.value);
            }
        }
        return valueList;
    }
}
