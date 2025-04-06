package org.eclipse.collections.impl.block.factory.comparators;

import java.util.function.BiPredicate;
import org.eclipse.collections.api.block.SerializableComparator;

public final class FromPredicateSerializableComparator<T> implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;
    private final BiPredicate<? super T, ? super T> isBeforePredicate;

    public FromPredicateSerializableComparator(BiPredicate<? super T, ? super T> isBeforePredicate)
    {
        this.isBeforePredicate = isBeforePredicate;
    }

    @Override
    public int compare(T o1, T o2)
    {
        if (this.isBeforePredicate.test(o1, o2))
        {
            return -1;
        }
        if (this.isBeforePredicate.test(o2, o1))
        {
            return 1;
        }
        return 0;
    }
}
