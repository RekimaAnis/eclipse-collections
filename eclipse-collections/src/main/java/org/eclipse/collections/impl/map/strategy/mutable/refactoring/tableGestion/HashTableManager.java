
public class HashTableManager<K, V>
{
    private Object[] table;
    private final float loadFactor;
    private static final int MIN_VALU = 0;
    private static final int MAX_VALU = 1;

    public HashTableManager(int initialCapacity, float loadFactor)
    {
        if (initialCapacity <= MIN_VALU){
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= MIN_VALU || loadFactor > MAX_VALU){
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        int capacity = 1;
        while (capacity < initialCapacity){
            capacity <<= 1;
        }

        allocateTable(capacity << 1);
    }

    /**
     * Allocate the table with the given capacity.
     */
    private void allocateTable(int capacity){
        this.table = new Object[capacity];
    }

    /**
     * Compute the index for the given key using the provided hashing strategy.
     * @param key the key to compute the index for
     * @param hashingStrategy the hashing strategy to use
     * @return the computed index
     */
    public int computeIndex(K key, HashingStrategy<? super K> hashingStrategy)
    {
        int hash = hashingStrategy.computeHashCode(key);
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        int halfLength = table.length >> 1;
        int index = (h & (halfLength - 1)) << 1;
        return index;
    }

    /**
     * Insert a new entry into the hash table.
     * @param key the key to insert
     * @param value the value to insert
     * @param hashingStrategy the hashing strategy to use
     */
    public void rehash(HashingStrategy<? super K> hashingStrategy)
    {
        Object[] oldTable = this.table;
        int newTableSize = oldTable.length << 1;
        allocateTable(newTableSize);
        for (int i = 0; i < oldTable.length; i += 2){
            Object key = oldTable[i];
            if (key != null){
                Object value = oldTable[i + 1];
                reinsertEntry((K) key, (V) value, hashingStrategy);
            }
        }
    }

    /**
     * Get the table.
     * @return the table
     */
    public Object[] getTable()
    {
        return table;
    }

    /**
     * Reinsert an entry into the hash table.
     * @param key the key to reinsert
     * @param value the value to reinsert
     * @param hashingStrategy the hashing strategy to use
     */

    private void reinsertEntry(K key, V value, HashingStrategy<? super K> hashingStrategy)
    {
        int index = computeIndex(key, hashingStrategy);
        
        while (table[index] != null){
            index = (index + 2) % table.length;
        }
        table[index] = key;
        table[index + 1] = value;

    }

}