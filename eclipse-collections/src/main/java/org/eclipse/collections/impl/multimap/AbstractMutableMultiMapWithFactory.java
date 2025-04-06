package org.eclipse.collections.impl.multimap;

import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.multimap.AbstractMutableMultimap;
import org.eclipse.collections.impl.utility.Iterate;
import java.util.Iterator;

public abstract class AbstractMutableMultimapWithFactory<K, V, C extends Iterable<V>>
        extends AbstractMutableMultimap<K, V, C>
{

    protected AbstractMutableMultimapWithFactory(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    protected AbstractMutableMultimapWithFactory(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    public static <K, V, M extends Multimap<K, V>> M newMultimap(Class<M> multimapClass)
    {
        try
        {
            return multimapClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not instantiate multimap", e);
        }
    }

    public static <K, V, M extends Multimap<K, V>> M newMultimap(Multimap<? extends K, ? extends V> multimap, Class<M> multimapClass)
    {
        M result = newMultimap(multimapClass);
        result.putAll(multimap);
        return result;
    }

    public static <K, V, M extends Multimap<K, V>> M newMultimap(Iterable<Pair<K, V>> inputIterable, Class<M> multimapClass)
    {
        M result = newMultimap(multimapClass);
        for (Pair<K, V> pair : inputIterable)
        {
            result.add(pair.getOne(), pair.getTwo());
        }
        return result;
    }
}
