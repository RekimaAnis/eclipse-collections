import java.util.*;
import java.lang.reflect.Array;

public class KeySet<K, V> implements Set<K>
{
    private final UnifiedMapWithHashingStrategy<K, V> map;

    public KeySet(UnifiedMapWithHashingStrategy<K, V> map)
    {
        this.map = map;
    }

    @Override
    public Iterator<K> iterator()
    {
        return new KeySetIterator<>(this.map);
    }

    @Override
    public int size()
    {
        return this.map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.map.size == 0;
    }

    @Override
    public boolean contains(Object o)
    {
        return this.map.containsKey(o);
    }

    @Override
    public Object[] toArray(){
        List<K> keys = new ArrayList<>();
        for( K key : this){
            keys.add(key);
        }
        return keys.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a){
       List<K> keys = new ArrayList<>();
        for( K key : this){
            keys.add(key);
        }
        return keys.toArray(a);
    }

    @Override
    public boolean add(K k)
    {
        throw new UnsupportedOperationException("Adding keys directly to the key set is not supported.");
    }

    @Override
    public boolean remove(Object o)
    {
        return map.removeKey(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object o : c)
        {
            if (!this.contains(o))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends K> c)
    {
        throw new UnsupportedOperationException("Adding keys directly to the key set is not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean modified = false;
        Iterator<K> it = this.iterator();
        while (it.hasNext()){
            if (!c.contains(it.next())){
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c){
        boolean modified = false;
        for (Object o : c)
        {
            if (this.remove(o))
            {
                modified = true;
            }
        }
        return modified;
    }

}