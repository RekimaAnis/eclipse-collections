/**
 * La classe ValuesCollection fournit une vue en lecture seule sur l'ensemble des valeurs de la map.
 */
public class ValuesCollection<V> implements Collection<V> {
    private final UnifiedMapWithHashingStrategy<?, V> map;
    
    public ValuesCollection(UnifiedMapWithHashingStrategy<?, V> map) {
        this.map = map;
    }
    
    @Override
    public Iterator<V> iterator() {
        return new ValueIterator<>(map);
    }
    
    @Override
    public int size() {
        return map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return map.size() == 0;
    }
    
    @Override
    public boolean contains(Object o) {
        return map.containsValue(o);
    }
    
    @Override
    public Object[] toArray() {
        List<V> values = new ArrayList<>();
        for (V v : this) {
            values.add(v);
        }
        return values.toArray();
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        List<V> values = new ArrayList<>();
        for (V v : this) {
            values.add(v);
        }
        return values.toArray(a);
    }
    
    @Override
    public boolean add(V e) {
        throw new UnsupportedOperationException("L'ajout direct n'est pas supporté sur ValuesCollection.");
    }
    
    @Override
    public boolean remove(Object o) {
        Iterator<V> it = iterator();
        while(it.hasNext()){
            if(Objects.equals(it.next(), o)){
                it.remove();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            if(!contains(o)) return false;
        }
        return true;
    }
    
    @Override
    public boolean addAll(Collection<? extends V> c) {
        throw new UnsupportedOperationException("L'ajout direct n'est pas supporté sur ValuesCollection.");
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o: c) {
            modified |= remove(o);
        }
        return modified;
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<V> it = iterator();
        while(it.hasNext()){
            if(!c.contains(it.next())){
                it.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public void clear() {
        map.clear();
    }
}
