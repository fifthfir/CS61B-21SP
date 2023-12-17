package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Ruotian Zhang
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private static final int INIT_CAPACITY = 16;
    private static final double INIT_LOADFACTOR = 0.75;

    private int initialSize;
    private double maxLoad;
    private int size;

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /** Constructors */
    public MyHashMap() {
        this(INIT_CAPACITY);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, INIT_LOADFACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
        this.size = 0;
        buckets = this.createTable(initialSize);
//        for (int i = 0; i < initialSize; i++) {
//            buckets[i] = this.createBucket();
//        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        buckets = createTable(INIT_CAPACITY);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        int i = getHashIdx(key);
        for (Node node : buckets[i]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            return;
        }
        if (value != null) {
            if (size >= maxLoad * initialSize) {
                resize(initialSize * 2);
            }
            int i = getHashIdx(key);
            Node nodeNow = getNode(key);
            if (nodeNow != null) {
                nodeNow.value = value;
            } else {
                Node toPut = createNode(key, value);
                buckets[i].add(toPut);
                size++;
            }
        }
    }
    private Node getNode(K key) {
        int bucketIdx = getHashIdx(key);
        for (Node node : buckets[bucketIdx]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private int getHashIdx(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    private void resize(int capacity) {
        Collection<Node>[] newBuckets = createTable(capacity);

        for (Collection<Node> bucket : buckets) {
            for (Node node: bucket) {
                int bucketIdx = getHashIdx(node.key);
                newBuckets[bucketIdx].add(node);
            }
        }

        this.buckets = newBuckets;
        initialSize = capacity;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        addKeys(set);
        return set;
    }
    private void addKeys(Set<K> set) {
        for (Collection<Node> bucket : buckets) {
            for (Node node: bucket) {
                if (node.key != null) {
                    set.add(node.key);
                }
            }
        }
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int bucketIdx = getHashIdx(key);
        Node toRemove = getNode(key);
        buckets[bucketIdx].remove(toRemove);
        return toRemove.value;
    }

    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        Node toRemove = getNode(key);
        if (toRemove.value != value) {
            return null;
        }
        int bucketIdx = getHashIdx(key);
        buckets[bucketIdx].remove(toRemove);
        return toRemove.value;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
