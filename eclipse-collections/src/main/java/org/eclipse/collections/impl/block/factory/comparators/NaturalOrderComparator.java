package org.eclipse.collections.impl.block.factory.comparators;

import org.eclipse.collections.api.block.SerializableComparator;

public final class NaturalOrderComparator<T extends Comparable<T>> implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(T o1, T o2)
    {
        if(o1 == null || o2 == null)
        {
            throw new NullPointerException();
        }
        return o1.compareTo(o2);
    }
}