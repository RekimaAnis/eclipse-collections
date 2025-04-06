
public class KeyIterator<K> implements Iterator<K>
{
    private final Object[] table;
    private final int tableLength;
    private int index = 0;
    private int chainIndex = -1;
    private Object[] currentChain = null;
    private final HashingStrategy<? super K> hashingStrategy;
    private final int size;
    private int currentSize = 0;

    public KeyIterator(UnifiedMapWithHashingStrategy<K, ?> map)
    {
        this.table = map.getTable();
        this.tableLength = table.length;
        this.map = map;
        this.size = map.size();
    }
    
    @Override
    public boolean hasNext(){
        return count < size;
    }

    @Override
        public K next() {
        if (!hasNext()) throw new NoSuchElementException();
        K result = null;
        if (currentChain != null) {
            while (chainIndex < currentChain.length) {
                Object keyObj = currentChain[chainIndex];
                chainIndex += 2;
                if (keyObj != null) {
                    result = (K) keyObj;
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
            Object valueObj = table[index + 1];
            index += 2;
            if (keyObj != null) {
                if (keyObj == ChainingCollisionHandler.CHAINED_MARKER) {
                    currentChain = (Object[]) valueObj;
                    chainIndex = 0;
                    if (currentChain[chainIndex] != null) {
                        result = (K) currentChain[chainIndex];
                        chainIndex += 2;
                        count++;
                        return result;
                    } else {
                        currentChain = null;
                        chainIndex = -1;
                    }
                } else {
                    result = (K) keyObj;
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