import java.util.*;
import java.util.Map.Entry;


public class UnifiedMapWithHashingStrategy<K, V> implements Map<K, V> {
    private final HashingStrategy<? super K> hashingStrategy;
    private final HashTableManager<K, V> tableManager;
    private final CollisionHandler<K, V> collisionHandler;
    private int size = 0;


    public UnifiedMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy, int initialCapacity, float loadFactor) {
        if (hashingStrategy == null) {
            throw new IllegalArgumentException("La stratégie de hachage ne peut pas être nulle.");
        }
        this.hashingStrategy = hashingStrategy;
        this.tableManager = new HashTableManager<>(initialCapacity, loadFactor);
        this.collisionHandler = new ChainingCollisionHandler<>();
    }


    public HashTableManager<K, V> getHashTableManager() {
        return tableManager;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = tableManager.computeIndex((K) key, hashingStrategy);
        Object[] table = tableManager.getTable();
        Object keyObj = table[index];
        if (keyObj == null) {
            return false;
        }
        if (keyObj == ChainingCollisionHandler.CHAINED_MARKER) {
            Object[] chain = (Object[]) table[index + 1];
            for (int i = 0; i < chain.length; i += 2) {
                if (chain[i] == null) {
                    break;
                }
                if (hashingStrategy.equals((K) chain[i], (K) key)) {
                    return true;
                }
            }
            return false;
        } else {
            return hashingStrategy.equals((K) keyObj, (K) key);
        }
    }


    @Override
    public boolean containsValue(Object value) {
        Object[] table = tableManager.getTable();
        for (int i = 0; i < table.length; i += 2) {
            Object keyObj = table[i];
            if (keyObj == null) {
                continue;
            }
            if (keyObj == ChainingCollisionHandler.CHAINED_MARKER) {
                Object[] chain = (Object[]) table[i + 1];
                for (int j = 0; j < chain.length; j += 2) {
                    if (chain[j] == null) {
                        break;
                    }
                    if (Objects.equals(chain[j + 1], value)) {
                        return true;
                    }
                }
            } else if (Objects.equals(table[i + 1], value)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public V get(Object key) {
        int index = tableManager.computeIndex((K) key, hashingStrategy);
        Object[] table = tableManager.getTable();
        Object keyObj = table[index];
        if (keyObj == null) {
            return null;
        }
        if (keyObj == ChainingCollisionHandler.CHAINED_MARKER) {
            return collisionHandler.getCollision((K) key, index, table, hashingStrategy);
        } else {
            if (hashingStrategy.equals((K) keyObj, (K) key)) {
                return (V) table[index + 1];
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = tableManager.computeIndex(key, hashingStrategy);
        Object[] table = tableManager.getTable();
        if (table[index] == null) {
            table[index] = key;
            table[index + 1] = value;
            size++;
            return null;
        } else {
            if (table[index] != ChainingCollisionHandler.CHAINED_MARKER && hashingStrategy.equals((K) table[index], key)) {
                V oldValue = (V) table[index + 1];
                table[index + 1] = value;
                return oldValue;
            } else {
                V result = collisionHandler.putCollision(key, value, index, table, hashingStrategy);
                if (result == null) {
                    size++;
                }
                return result;
            }
        }
    }

    @Override
    public V remove(Object key) {
        int index = tableManager.computeIndex((K) key, hashingStrategy);
        Object[] table = tableManager.getTable();
        Object keyObj = table[index];
        if (keyObj == null) {
            return null;
        }
        if (keyObj != ChainingCollisionHandler.CHAINED_MARKER && hashingStrategy.equals((K) keyObj, (K) key)) {
            V oldValue = (V) table[index + 1];
            table[index] = null;
            table[index + 1] = null;
            size--;
            return oldValue;
        } else if (keyObj == ChainingCollisionHandler.CHAINED_MARKER) {
            V removed = collisionHandler.removeCollision((K) key, index, table, hashingStrategy);
            if (removed != null) {
                size--;
            }
            return removed;
        }
        return null;
    }

    @Override
    public void clear() {
        Object[] table = tableManager.getTable();
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet<>(this);
    }

    @Override
    public Collection<V> values() {
        return new ValuesCollection<>(this);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet<>(this);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
}
