package org.eclipse.collections.impl.block.factory.comparators;

import org.eclipse.collections.api.block.SerializableComparator;

public final class ComparableComparator<T> implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(T one, T two)
    {
        return ((Comparable<T>) one).compareTo(two);
    }
}
