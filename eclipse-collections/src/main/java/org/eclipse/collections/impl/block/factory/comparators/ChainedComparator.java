package org.eclipse.collections.impl.block.factory.comparators;

import java.util.Comparator;
import org.eclipse.collections.api.block.SerializableComparator;

public final class ChainedComparator<T> implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;
    private final Comparator<T>[] comparators;

    public ChainedComparator(Comparator<T>[] comparators)
    {
        this.comparators = comparators;
    }

    @Override
    public int compare(T value1, T value2)
    {
        for (Comparator<T> comparator : this.comparators)
        {
            int result = comparator.compare(value1, value2);
            if (result != 0)
            {
                return result;
            }
        }
        return 0;
    }
}
