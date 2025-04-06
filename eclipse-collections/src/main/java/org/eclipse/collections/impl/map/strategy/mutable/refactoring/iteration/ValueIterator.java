
/**
 * Itérateur sur l'ensemble des valeurs de la map.
 */
class ValueIterator<V> implements Iterator<V> {
    private final Object[] table;
    private final int tableLength;
    private int index = 0;
    private int chainIndex = -1;
    private Object[] currentChain = null;
    private final UnifiedMapWithHashingStrategy<?, V> map;
    private int count = 0;
    private final int size;
    
    public ValueIterator(UnifiedMapWithHashingStrategy<?, V> map) {
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
    public V next() {
        if (!hasNext()) throw new NoSuchElementException();
        V result = null;
        if (currentChain != null) {
            while (chainIndex < currentChain.length) {
                Object valObj = currentChain[chainIndex + 1];
                chainIndex += 2;
                if (currentChain[chainIndex - 2] != null) {
                    result = (V) valObj;
                    count++;
                    if (chainIndex >= currentChain.length || currentChain[chainIndex] == null) {
                        currentChain = null;
                        chainIndex = -1;
                    }
                    return result;
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
                        result = (V) currentChain[chainIndex + 1];
                        chainIndex += 2;
                        count++;
                        return result;
                    } else {
                        currentChain = null;
                        chainIndex = -1;
                    }
                } else {
                    result = (V) valObj;
                    count++;
                    return result;
                }
            }
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Suppression non supportée dans cet itérateur.");
    }
}
