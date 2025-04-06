package org.eclipse.collections.impl.block.factory.comparators;

import java.util.Collection;
import org.eclipse.collections.api.block.SerializableComparator;

public final class DescendingCollectionSizeComparator implements SerializableComparator<Collection<?>>
{
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Collection<?> c1, Collection<?> c2)
    {
        return c2.size() - c1.size();
    }
}
