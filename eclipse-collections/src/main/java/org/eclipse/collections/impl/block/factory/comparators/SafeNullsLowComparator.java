package org.eclipse.collections.impl.block.factory.comparators;

import java.util.Comparator;
import org.eclipse.collections.api.block.SerializableComparator;

public final class SafeNullsLowComparator<T> implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;

    private final Comparator<T> notNullSafeComparator;

    public SafeNullsLowComparator(Comparator<T> notNullSafeComparator)
    {
        this.notNullSafeComparator = notNullSafeComparator;
    }

    @Override
    public int compare(T o1, T o2)
    {
        if (value1 != null && value2 != null)
        {
            return this.notNullSafeComparator.compare(value1, value2);
        }

        if (value1 == null && value2 == null)
        {
            return 0;
        }

        return value1 == null ? -1 : 1;
    }


}