package org.eclipse.collections.impl.map.strategy.mutable.refactoring.tableGestion;

public interface HashingStrategy<T>
{
    int computeHashCode(T object);

    boolean equals(K key1, K key2);
}

