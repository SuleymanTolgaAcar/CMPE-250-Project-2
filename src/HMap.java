import java.util.LinkedList;
import java.util.List;

public class HMap<K, V> {

    private class Entry{
        K key;
        V value;
        Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 101;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private List<Entry>[] lists;

    public HMap(){
        this(DEFAULT_CAPACITY);
    }

    public HMap(int capacity){
        lists = new List[nextPrime(capacity)];
        for(int i = 0; i < lists.length; i++){
            lists[i] = new LinkedList<>();
        }
        size = 0;
    }

    private int hash(K key){
        int hashValue = key.hashCode();
        hashValue = hashValue % lists.length;
        if(hashValue < 0) {
            hashValue += lists.length;
        }
        return hashValue;
    }

    private int nextPrime(int n){
        if(n % 2 == 0){
            n++;
        }
        for(; !isPrime(n); n += 2){
            ;
        }
        return n;
    }

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

    public V get(K key){
        List<Entry> list = lists[hash(key)];
        for(Entry entry : list){
            if(entry.key.equals(key)){
                return entry.value;
            }
        }
        return null;
    }

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

    public boolean containsKey(K key){
        List<Entry> list = lists[hash(key)];
        for(Entry entry : list){
            if(entry.key.equals(key)){
                return true;
            }
        }
        return false;
    }

    public void clear(){
        for(int i = 0; i < lists.length; i++){
            lists[i].clear();
        }
        size = 0;
    }

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

    public List<K> keyList(){
        List<K> keyList = new LinkedList<>();
        for(List<Entry> list : lists){
            for(Entry entry : list){
                keyList.add(entry.key);
            }
        }
        return keyList;
    }

    public List<V> valueList(){
        List<V> valueList = new LinkedList<>();
        for(List<Entry> list : lists){
            for(Entry entry : list){
                valueList.add(entry.value);
            }
        }
        return valueList;
    }

    public List<Entry> entryList(){
        List<Entry> entryList = new LinkedList<>();
        for(List<Entry> list : lists){
            for(Entry entry : list){
                entryList.add(entry);
            }
        }
        return entryList;
    }
}
