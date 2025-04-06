package org.eclipse.collections.impl.block.factory.comparators;

import org.eclipse.collections.api.block.SerializableComparator;
import java.util.Comparator;

public final class ReverseComparator<T> implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;

    private final Comparator<T> comparator;

    public ReverseComparator(Comparator<T> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public int compare(T o1, T o2)
    {
        return this.comparator.compare(o2, o1);
    }
}