package org.eclipse.collections.impl.block.factory.comparators;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiPredicate;
import org.eclipse.collections.api.block.SerializableComparator;
import org.eclipse.collections.api.block.factory.SerializableComparators;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.set.sorted.SortedSetIterable;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.block.comparator.FunctionComparator;

public final class Comparators
{
    private static final SerializableComparator<?> COMPARABLE_COMPARATOR = new ComparableComparator<>();
    private static final SerializableComparator<?> NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator<>();
    private static final SerializableComparator<?> REVERSE_NATURAL_ORDER_COMPARATOR = new ReverseComparator<>(NATURAL_ORDER_COMPARATOR);
    private static final SerializableComparator<?> POWER_SET_COMPARATOR = new PowerSetComparator<>();
    private static final SerializableComparator<Collection<?>> ASCENDING_COLLECTION_SIZE_COMPARATOR = new AscendingCollectionSizeComparator();
    private static final SerializableComparator<Collection<?>> DESCENDING_COLLECTION_SIZE_COMPARATOR = new DescendingCollectionSizeComparator();

    private Comparators()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * @since 10.0
     */
    public static <T> SerializableComparator<T> comparableComparator()
    {
        return (SerializableComparator<T>) COMPARABLE_COMPARATOR;
    }

    /**
     * @since 10.0
     */
    public static <T> Comparator<? super T> comparableComparatorIfNull(Comparator<? super T> comparator)
    {
        return comparator == null ? Comparators.comparableComparator() : comparator;
    }

    /**
     * Uses the natural compareTo methods of the objects which will throw if there are any nulls.
     */
    public static <T> SerializableComparator<T> naturalOrder()
    {
        return SerializableComparators.naturalOrder();
    }

    /**
     * This method exists in order to guarantee serialization compatibility in tests.
     * @since 11.1
     * @deprecated
     */
    @Deprecated
    public static <T> SerializableComparator<T> originalNaturalOrder()
    {
        return (SerializableComparator<T>) NATURAL_ORDER_COMPARATOR;
    }

    /**
     * Uses the natural compareTo methods of the objects which will throw if there are any nulls.
     */
    public static <T> SerializableComparator<T> reverseNaturalOrder()
    {
        return SerializableComparators.reverseNaturalOrder();
    }

    /**
     * This method exists in order to guarantee serialization compatibility in tests.
     * @since 11.1
     * @deprecated
     */
    @Deprecated
    public static <T> SerializableComparator<T> originalReverseNaturalOrder()
    {
        return (SerializableComparator<T>) REVERSE_NATURAL_ORDER_COMPARATOR;
    }

    /**
     * @param comparator original comparator whose order will be reversed
     * @return A comparator that reverses the order of any other Serializable Comparator.
     */
    public static <T> SerializableComparator<T> reverse(Comparator<T> comparator)
    {
        if (comparator == null)
        {
            throw new NullPointerException();
        }
        return new ReverseComparator<>(comparator);
    }

    public static <T> SerializableComparator<T> safeNullsLow(Comparator<T> notNullSafeComparator)
    {
        return new SafeNullsLowComparator<>(notNullSafeComparator);
    }

    public static <T> SerializableComparator<T> safeNullsHigh(Comparator<T> notNullSafeComparator)
    {
        return new SafeNullsHighComparator<>(notNullSafeComparator);
    }

    @SafeVarargs
    public static <T> SerializableComparator<T> chain(Comparator<T>... comparators)
    {
        if (comparators.length == 0)
        {
            throw new IllegalArgumentException("Nothing to chain");
        }

        return new ChainedComparator<>(comparators);
    }

    public static <T, V extends Comparable<? super V>> SerializableComparator<T> fromFunctions(
            Function<? super T, ? extends V> one)
    {
        return Comparators.byFunction(one);
    }

    public static <T, V1 extends Comparable<? super V1>, V2 extends Comparable<? super V2>> SerializableComparator<T> fromFunctions(
            Function<? super T, ? extends V1> one,
            Function<? super T, ? extends V2> two)
    {
        return Comparators.chain(
                Comparators.byFunction(one),
                Comparators.byFunction(two));
    }

    public static <T, V1 extends Comparable<? super V1>, V2 extends Comparable<? super V2>, V3 extends Comparable<? super V3>> SerializableComparator<T> fromFunctions(
            Function<? super T, ? extends V1> one,
            Function<? super T, ? extends V2> two,
            Function<? super T, ? extends V3> three)
    {
        return Comparators.chain(
                Comparators.byFunction(one),
                Comparators.byFunction(two),
                Comparators.byFunction(three));
    }

    public static <T> SerializableComparator<SortedSetIterable<T>> powerSet()
    {
        return (SerializableComparator<SortedSetIterable<T>>) POWER_SET_COMPARATOR;
    }

    public static SerializableComparator<Collection<?>> ascendingCollectionSizeComparator()
    {
        return ASCENDING_COLLECTION_SIZE_COMPARATOR;
    }

    public static SerializableComparator<Collection<?>> descendingCollectionSizeComparator()
    {
        return DESCENDING_COLLECTION_SIZE_COMPARATOR;
    }

    /**
     * Creates a comparator for pairs by using an existing comparator that only compares the first element of the pair
     *
     * @param comparator original comparator that compares the first element of the pair
     * @return A comparator that compares pairs only by their first element
     */
    public static <T> SerializableComparator<Pair<T, ?>> byFirstOfPair(Comparator<? super T> comparator)
    {
        return new ByFirstOfPairComparator<>(comparator);
    }

    /**
     * Creates a comparator for pairs by using an existing comparator that only compares the second element of the pair
     *
     * @param comparator original comparator that compares the second element of the pair
     * @return A comparator that compares pairs only by their second element
     */
    public static <T> SerializableComparator<Pair<?, T>> bySecondOfPair(Comparator<? super T> comparator)
    {
        return new BySecondOfPairComparator<>(comparator);
    }

    /**
     * @param isBeforePredicate a predicate that returns true if the first argument is less than, or should appear before, the second argument
     * @since 12.0.0
     */
    public static <T> SerializableComparator<T> fromPredicate(BiPredicate<? super T, ? super T> isBeforePredicate)
    {
        return new FromPredicateSerializableComparator<>(isBeforePredicate);
    }

    public static <T, V extends Comparable<? super V>> SerializableComparator<T> byFunction(Function<? super T, ? extends V> function)
    {
        SerializableComparator<T> comparator = Comparators.getPrimitiveFunctionComparator(function);
        if (comparator != null)
        {
            return comparator;
        }
        return Comparators.byFunction(function, Comparators.naturalOrder());
    }

    /**
     * This signature is kept for serialization backwards compatibility.
     * @since 11.1
     * @deprecated
     */
    @Deprecated
    public static <T, V extends Comparable<? super V>> SerializableComparator<T> originalByFunction(Function<? super T, ? extends V> function)
    {
        SerializableComparator<T> comparator = Comparators.getPrimitiveFunctionComparator(function);
        if (comparator != null)
        {
            return comparator;
        }
        return Comparators.originalByFunction(function, Comparators.originalNaturalOrder());
    }

    private static <T, V extends Comparable<? super V>> SerializableComparator<T> getPrimitiveFunctionComparator(Function<? super T, ? extends V> function)
    {
        if (function instanceof BooleanFunction)
        {
            return Functions.toBooleanComparator((BooleanFunction<T>) function);
        }
        if (function instanceof ByteFunction)
        {
            return Functions.toByteComparator((ByteFunction<T>) function);
        }
        if (function instanceof CharFunction)
        {
            return Functions.toCharComparator((CharFunction<T>) function);
        }
        if (function instanceof DoubleFunction)
        {
            return Functions.toDoubleComparator((DoubleFunction<T>) function);
        }
        if (function instanceof FloatFunction)
        {
            return Functions.toFloatComparator((FloatFunction<T>) function);
        }
        if (function instanceof IntFunction)
        {
            return Functions.toIntComparator((IntFunction<T>) function);
        }
        if (function instanceof LongFunction)
        {
            return Functions.toLongComparator((LongFunction<T>) function);
        }
        if (function instanceof ShortFunction)
        {
            return Functions.toShortComparator((ShortFunction<T>) function);
        }
        return null;
    }

    public static <T, V extends Comparable<? super V>> SerializableComparator<T> byFunctionNullsLast(Function<? super T, ? extends V> function)
    {
        return Comparators.byFunction(
                function,
                Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public static <T, V extends Comparable<? super V>> SerializableComparator<T> byFunctionNullsFirst(Function<? super T, ? extends V> function)
    {
        return Comparators.byFunction(
                function,
                Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    public static <T> SerializableComparator<T> byBooleanFunction(BooleanFunction<T> function)
    {
        return Functions.toBooleanComparator(function);
    }

    public static <T> SerializableComparator<T> byByteFunction(ByteFunction<T> function)
    {
        return Functions.toByteComparator(function);
    }

    public static <T> SerializableComparator<T> byCharFunction(CharFunction<T> function)
    {
        return Functions.toCharComparator(function);
    }

    public static <T> SerializableComparator<T> byDoubleFunction(DoubleFunction<T> function)
    {
        return Functions.toDoubleComparator(function);
    }

    public static <T> SerializableComparator<T> byFloatFunction(FloatFunction<T> function)
    {
        return Functions.toFloatComparator(function);
    }

    public static <T> SerializableComparator<T> byIntFunction(IntFunction<T> function)
    {
        return Functions.toIntComparator(function);
    }

    public static <T> SerializableComparator<T> byLongFunction(LongFunction<T> function)
    {
        return Functions.toLongComparator(function);
    }

    public static <T> SerializableComparator<T> byShortFunction(ShortFunction<T> function)
    {
        return Functions.toShortComparator(function);
    }

    /**
     * Original signature accepted a Comparator which may not be Serializable. This signature is kept for
     * backwards compatibility.
     */
    public static <T, V> SerializableComparator<T> byFunction(Function<? super T, ? extends V> function, Comparator<V> comparator)
    {
        if (comparator instanceof SerializableComparator)
        {
            return SerializableComparators.byFunction(function, (SerializableComparator<V>) comparator);
        }
        return Comparators.originalByFunction(function, comparator);
    }

    /**
     * Original signature accepted a Comparator which may not be Serializable. This signature is kept for
     * backwards compatibility.
     * @deprecated
     */
    @Deprecated
    public static <T, V> SerializableComparator<T> originalByFunction(Function<? super T, ? extends V> function, Comparator<V> comparator)
    {
        return new FunctionComparator<>(function, comparator);
    }

    public static boolean nullSafeEquals(Object value1, Object value2)
    {
        return value1 == null ? value2 == null : value1.equals(value2);
    }

    public static <T extends Comparable<T>> int nullSafeCompare(T value1, T value2)
    {
        if (value1 != null && value2 != null)
        {
            return value1.compareTo(value2);
        }

        if (value1 == null && value2 == null)
        {
            return 0;
        }

        return value1 == null ? -1 : 1;
    }
}
