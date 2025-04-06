public class EntrySet<K, V> implements Set<Map.Entry<K, V>>
{
    private final UnifiedMapWithHashingStrategy<K, V> map;

    public EntrySet(UnifiedMapWithHashingStrategy<K, V> map)
    {
        this.map = map;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator()
    {
        return new EntrySetIterator<>(this.map);
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
        if(!(o instanceof Map.Entry)){
            return false;
        }
        Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
        V value = this.map.get(entry.getKey());
        return value != null && value.equals(entry.getValue());
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        List<Map.Entry<K, V>> entries = new ArrayList<>();
        for (Map.Entry<K, V> entry : this) {
            entries.add(entry);
        }
        return entries.toArray(a);
    }

    @Override
    public Object[] toArray()
    {
        List<Map.Entry<K, V>> entries = new ArrayList<>();
        for (Map.Entry<K, V> entry : this) {
            entries.add(entry);
        }
        return entries.toArray();
    }

    @Override
    public boolean add(Map.Entry<K, V> entry)
    {
        throw new UnsupportedOperationException("Adding entries directly to the entry set is not supported.");
    }

    @Override
    public boolean remove(Object o)
    {
        if(o instanceof Map.Entry){
            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            return this.map.removeKey(entry.getKey()) != null;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c){
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
    public boolean addAll(Collection<? extends Map.Entry<K, V>> c)
    {
        throw new UnsupportedOperationException("Adding entries directly to the entry set is not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean modified = false;
        for (Iterator<Map.Entry<K, V>> it = this.iterator(); it.hasNext(); )
        {
            if (!c.contains(entry))
            {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
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

    @Override
    public void clear()
    {
        this.map.clear();
    }


}