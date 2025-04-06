import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Map;

public class WeakBoundEntry<K, V> implements Map.Entry<K, V>
{
    private final K key ;
    private K value;

    private final WeakReference<UnifiedMapWithHashingStrategy<K, V>> holder;
    private final HasingStrategy<? super K> hashingStrategy;

    public WeakBoundEntry(K key, K value, UnifiedMapWithHashingStrategy<K, V> holder, HasingStrategy<? super K> hashingStrategy)
    {
        this.key = key;
        this.value = value;
        this.holder = new WeakReference<>(holder);
        this.hashingStrategy = hashingStrategy;
    }

    @Override
    public K getKey()
    {
        return this.key;
    }

    @Override
    public V getValue()
    {
        return this.value;
    }

    @Override  
    public V setValue(V value)
    {
        V oldValue = this.value;
        this.value = value;
        UnifiedMapWithHashingStrategy<K, V> map = this.holder.get();
        if (map != null && map.containsKey(this.key)) {
            map.put(this.key, value);
        }
        return oldValue;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            K otherKey = (K) entry.getKey();    
            V otherValue = (V) entry.getValue();
            return hashingStrategy.equals(key, otherKey) && Objects.equals(value, otherValue);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashingStrategy.computeHashCode(key) ^ (value == null ? 0 : value.hashCode());
    }

    @Override
    public String toString()
    {
        return key + "=" + value;
    }
}