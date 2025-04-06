package org.eclipse.collections.impl.block.factory.comparators;

import java.util.Comparator;
import org.eclipse.collections.api.block.SerializableComparator;
import org.eclipse.collections.api.tuple.Pair;

public final class ByFirstOfPairComparator<T> implements SerializableComparator<Pair<T, ?>>
{
    private static final long serialVersionUID = 1L;
    private final Comparator<? super T> comparator;

    public ByFirstOfPairComparator(Comparator<? super T> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public int compare(Pair<T, ?> p1, Pair<T, ?> p2)
    {
        return this.comparator.compare(p1.getOne(), p2.getOne());
    }
}
