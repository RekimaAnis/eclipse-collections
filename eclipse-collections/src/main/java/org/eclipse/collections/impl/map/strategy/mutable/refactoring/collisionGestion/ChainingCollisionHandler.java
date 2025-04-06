
public class ChainingCollisionHandler<K, V> implements CollisionHandler<K, V>
{
    public static final Object CHAINED_MARKER = new Object();


    @Override
    public V putCollision(K key, V value, int index, Object[] table, HashingStrategy<? super K> hashingStrategy)
    {
        if (table[index] != CHAINED_MARKER){
            Obkect existingKey = table[index];
            Object existingValue = table[index + 1];

            Object[] chain = new Object[4];
            chain[0] = existingKey;
            chain[1] = existingValue;
            chain[2] = key;
            chain[3] = value;
            table[index] = CHAINED_MARKER;
            table[index + 1] = chain;
            return null;
        }else{
            Object[] chain = (Object[]) table[index + 1];
            for (int i = 0; i < chain.length; i += 2){
                if (chain[i]==null){
                    chain[i] = key;
                    chain[i + 1] = value;
                    return null;
                }
                if(strategy.equals((K) chain[i], key)){
                    V oldValue = (V) chain[i + 1];
                    chain[i + 1] = value;
                    return (V) oldValue;
                }
            }
            int oldLength = chain.length;
            int newLength = oldLength + 4; 
            Object[] newChain = new Object[newLength];
            System.arraycopy(chain, 0, newChain, 0, oldLength);
            newChain[oldLength] = key;
            newChain[oldLength + 1] = value;
            table[index + 1] = newChain;
            return null;
        }
    }

    @Override
    public V getCollision(K key, int index, Object[] table, HashingStrategy<? super K> strategy){

        if(table[index] != CHAINED_MARKER){
            return null;
        }
        Object[] chain = (Object[]) table[index +1];

        for(int i = 0; i<chain.length; i+=2){
            if(chain[i] == null){
                break;
            }
            if (strategy.equals((K) chain[i], key)){
                return (V) chain[i + 1];
            }
        }
        return null;
    }

    @Override
    public V removeCollision(K key, int index, Object[] table, HashingStrategy<? super K> strategy){
        if(table[index] != CHAINED_MARKER){
            return null;
        }
        Object[] chain = (Object[]) table[index +1];
        for (int i = 0; i<chain.length; i+=2){
            if(chain[i] == null){
                break;
            }
            if(strategy.equals((K) chain[i], key)){
                V oldValue = (V) chain[i+1];
                for(int j = i; j<chain.length -2; j+=2){
                    chain[j] = chain[j+2];
                    chain[j+1] = chain[j+3];
                }
                chain[chain.length - 2] = null;
                chain[chain.length - 1] = null;

                if(chain[0] == null){
                    table[index] = null;
                    table[index + 1] = null;
                }
                return oldValue;
            }
        }
        return null;
    }

}