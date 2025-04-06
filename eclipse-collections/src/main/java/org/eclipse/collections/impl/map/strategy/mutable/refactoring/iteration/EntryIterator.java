public class EntryIterator<K, V> implements Iterator<Map.Entry<K, V>> {
    
    private final Object[] table;
    private final int tableLength;
    private int index = 0;
    private int chainIndex = -1;
    private Object[] currentChain = null;
    private final UnifiedMapWithHashingStrategy<K, V> map;
    private int count = 0;
    private final int size;
    
    public EntryIterator(UnifiedMapWithHashingStrategy<K, V> map) {
        this.map = map;
        this.table = map.getHashTableManager().getTable();
        this.tableLength = table.length;
        this.size = map.size();
    }
    
    @Override
    public boolean hasNext() {
        return count < size;
    }
    
    @Override
    public Map.Entry<K, V> next() {
        if (!hasNext()) throw new NoSuchElementException();
        K key = null;
        V value = null;
        if (currentChain != null) {
            while (chainIndex < currentChain.length) {
                Object keyObj = currentChain[chainIndex];
                Object valObj = currentChain[chainIndex + 1];
                chainIndex += 2;
                if (keyObj != null) {
                    key = (K) keyObj;
                    value = (V) valObj;
                    count++;
                    if (chainIndex >= currentChain.length || currentChain[chainIndex] == null) {
                        currentChain = null;
                        chainIndex = -1;
                    }
                    return new SimpleEntry<>(key, value);
                }
            }
            currentChain = null;
            chainIndex = -1;
        }
        while (index < tableLength) {
            Object keyObj = table[index];
            Object valObj = table[index + 1];
            index += 2;
            if (keyObj != null) {
                if (keyObj == ChainingCollisionHandler.CHAINED_MARKER) {
                    currentChain = (Object[]) valObj;
                    chainIndex = 0;
                    if (currentChain[chainIndex] != null) {
                        key = (K) currentChain[chainIndex];
                        value = (V) currentChain[chainIndex + 1];
                        chainIndex += 2;
                        count++;
                        return new SimpleEntry<>(key, value);
                    } else {
                        currentChain = null;
                        chainIndex = -1;
                    }
                } else {
                    key = (K) keyObj;
                    value = (V) valObj;
                    count++;
                    return new SimpleEntry<>(key, value);
                }
            }
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Suppression non supportée dans cet itérateur.");
    }
    
    /**
     * Implémentation simple de Map.Entry.
     */
    private static class SimpleEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        
        public SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return key;
        }
        
        @Override
        public V getValue() {
            return value;
        }
        
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
        }
        
        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }
        
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}