public interface CollisionHandler<K,V>{
    
    V putCollision(K key, V value, int index, Object[] table,HashingStrategy<? super K> hashingStrategy);
    V getCollision(K key, int index, Object[] table, HashingStrategy<? super K> hashingStrategy);
    V removeCollision(K key, int index, Object[] table, HashingStrategy<? super K> hashingStrategy);
}