package org.eclipse.collections.impl.block.factory.comparators;

import org.eclipse.collections.api.block.SerializableComparator;
import org.eclipse.collections.api.set.sorted.SortedSetIterable;

public final class PowerSetComparator<T> implements SerializableComparator<SortedSetIterable<T>>
{
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(SortedSetIterable<T> setA, SortedSetIterable<T> setB)
    {
        int compareTo = Integer.compare(setA.size(), setB.size());
        if (compareTo == 0)
        {
            return setA.compareTo(setB);
        }
        return compareTo;
    }
}