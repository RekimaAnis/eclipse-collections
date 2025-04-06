/*
 * Copyright (c) 2024 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.bag.immutable;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.bag.mutable.HashBag;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.test.Verify;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.impl.factory.Iterables.iBag;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImmutableArrayBagTest extends ImmutableBagTestCase
{
    @Override
    protected ImmutableBag<String> newBag()
    {
        return ImmutableArrayBag.newBagWith("1", "2", "2", "3", "3", "3", "4", "4", "4", "4");
    }

    @SafeVarargs
    @Override
    protected final <T> ImmutableBag<T> newWith(T... littleElements)
    {
        return ImmutableArrayBag.newBagWith(littleElements);
    }

    @Override
    protected int numKeys()
    {
        return 4;
    }

    @Override
    @Test
    public void testSize()
    {
        Verify.assertIterableSize(10, this.newBag());
    }

    @Override
    @Test
    public void newWith()
    {
        super.newWith();
        int maximumUsefulArrayBagSize = ImmutableArrayBag.MAXIMUM_USEFUL_ARRAY_BAG_SIZE;
        Verify.assertInstanceOf(
                ImmutableArrayBag.class,
                Bags.immutable.ofAll(Interval.oneTo(maximumUsefulArrayBagSize - 1)).newWith(maximumUsefulArrayBagSize));
        Verify.assertInstanceOf(
                ImmutableHashBag.class,
                Bags.immutable.ofAll(Interval.oneTo(maximumUsefulArrayBagSize)).newWith(maximumUsefulArrayBagSize + 1));
        Interval items = Interval.oneTo(maximumUsefulArrayBagSize);
        Verify.assertInstanceOf(
                ImmutableHashBag.class,
                new ImmutableArrayBag<>(items.toArray(), items.toIntArray()).newWith(maximumUsefulArrayBagSize + 1));
    }

    @Override
    @Test
    public void newWithout()
    {
        super.newWithout();
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag2 = bag.newWithout("2").newWithout("2");
        assertNotEquals(bag, newBag2);
        assertEquals(newBag2.size(), bag.size() - 2);
        assertEquals(3, newBag2.sizeDistinct());
        ImmutableBag<String> newBag3 = bag.newWithout("3").newWithout("3").newWithout("3");
        assertNotEquals(bag, newBag3);
        assertEquals(newBag3.size(), bag.size() - 3);
        assertEquals(3, newBag3.sizeDistinct());
        ImmutableBag<String> newBag4 = bag.newWithout("4").newWithout("4").newWithout("4").newWithout("4");
        assertNotEquals(bag, newBag4);
        assertEquals(newBag4.size(), bag.size() - 4);
        assertEquals(3, newBag4.sizeDistinct());
        ImmutableBag<String> newBag5 = bag.newWithout("5");
        assertEquals(bag, newBag5);
    }

    @Override
    public void toStringOfItemToCount()
    {
        String actual = ImmutableArrayBag.newBagWith("1", "2", "2").toStringOfItemToCount();
        assertTrue("{1=1, 2=2}".equals(actual) || "{2=2, 1=1}".equals(actual));
    }

    @Override
    @Test
    public void toMap()
    {
        super.toMap();
        ImmutableBag<String> integers = this.newBag();
        MutableMap<String, String> map =
                integers.toMap(String::valueOf, String::valueOf);
        assertEquals(UnifiedMap.newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
    }

    @Test
    public void testNewBag()
    {
        for (int i = 1; i <= ImmutableArrayBag.MAXIMUM_USEFUL_ARRAY_BAG_SIZE + 1; i++)
        {
            Interval interval = Interval.oneTo(i);
            Verify.assertEqualsAndHashCode(HashBag.newBag(interval), Bags.immutable.ofAll(interval));
        }

        assertThrows(IllegalArgumentException.class, () -> new ImmutableArrayBag<>(new Integer[]{2, 3}, new int[]{2}));
    }

    @Override
    @Test
    public void selectInstancesOf()
    {
        super.selectInstancesOf();

        ImmutableBag<Number> numbers = ImmutableArrayBag.newBagWith(1, 2.0, 2.0, 3, 3, 3, 4.0, 4.0, 4.0, 4.0);
        assertEquals(iBag(1, 3, 3, 3), numbers.selectInstancesOf(Integer.class));
        assertEquals(iBag(2.0, 2.0, 4.0, 4.0, 4.0, 4.0), numbers.selectInstancesOf(Double.class));
    }

    @Override
    @Test
    public void groupByUniqueKey()
    {
        // Only works on bags without duplicates
        ImmutableBag<Integer> immutableBag = ImmutableArrayBag.newBagWith(1, 2, 3);
        assertEquals(Maps.immutable.of(1, 1, 2, 2, 3, 3), immutableBag.groupByUniqueKey(id -> id));
    }

    @Override
    @Test
    public void groupByUniqueKey_target()
    {
        // Only works on bags without duplicates
        ImmutableBag<Integer> immutableBag = ImmutableArrayBag.newBagWith(1, 2, 3);
        assertEquals(Maps.immutable.of(0, 0, 1, 1, 2, 2, 3, 3), immutableBag.groupByUniqueKey(id -> id, UnifiedMap.newWithKeysValues(0, 0)));
    }

    @Test
    public void topOccurrences()
    {
        MutableBag<String> mutable = HashBag.newBag();
        mutable.addOccurrences("one", 1);
        mutable.addOccurrences("two", 2);
        mutable.addOccurrences("three", 3);
        mutable.addOccurrences("four", 4);
        mutable.addOccurrences("five", 5);
        mutable.addOccurrences("six", 6);
        mutable.addOccurrences("seven", 7);
        mutable.addOccurrences("eight", 8);
        mutable.addOccurrences("nine", 9);
        mutable.addOccurrences("ten", 10);
        ImmutableBag<String> strings = ImmutableArrayBag.copyFrom(mutable);
        ImmutableList<ObjectIntPair<String>> top5 = strings.topOccurrences(5);
        Verify.assertIterableSize(5, top5);
        assertEquals("ten", top5.getFirst().getOne());
        assertEquals(10, top5.getFirst().getTwo());
        assertEquals("six", top5.getLast().getOne());
        assertEquals(6, top5.getLast().getTwo());
        Verify.assertIterableSize(0, ImmutableArrayBag.newBagWith().topOccurrences(5));
        Verify.assertIterableSize(3, this.newWith("one", "two", "three").topOccurrences(5));
        Verify.assertIterableSize(3, this.newWith("one", "two", "three").topOccurrences(1));
        Verify.assertIterableSize(3, this.newWith("one", "two", "three").topOccurrences(2));
        Verify.assertIterableSize(3, this.newWith("one", "one", "two", "three").topOccurrences(2));
        Verify.assertIterableSize(2, this.newWith("one", "one", "two", "two", "three").topOccurrences(1));
        Verify.assertIterableSize(3, this.newWith(null, "one", "two").topOccurrences(5));
        Verify.assertIterableSize(3, this.newWith(null, "one", "two").topOccurrences(1));
        Verify.assertIterableSize(3, this.newWith("one", "one", "two", "two", "three", "three").topOccurrences(1));
        Verify.assertIterableSize(0, this.newWith("one").newWithout("one").topOccurrences(0));
        Verify.assertIterableSize(0, this.newWith("one").topOccurrences(0));
        assertThrows(IllegalArgumentException.class, () -> this.newWith("one").topOccurrences(-1));
    }

    @Test
    public void bottomOccurrences()
    {
        MutableBag<String> mutable = HashBag.newBag();
        mutable.addOccurrences("one", 1);
        mutable.addOccurrences("two", 2);
        mutable.addOccurrences("three", 3);
        mutable.addOccurrences("four", 4);
        mutable.addOccurrences("five", 5);
        mutable.addOccurrences("six", 6);
        mutable.addOccurrences("seven", 7);
        mutable.addOccurrences("eight", 8);
        mutable.addOccurrences("nine", 9);
        mutable.addOccurrences("ten", 10);
        ImmutableBag<String> strings = ImmutableArrayBag.copyFrom(mutable);
        ImmutableList<ObjectIntPair<String>> bottom5 = strings.bottomOccurrences(5);
        Verify.assertIterableSize(5, bottom5);
        assertEquals("one", bottom5.getFirst().getOne());
        assertEquals(1, bottom5.getFirst().getTwo());
        assertEquals("five", bottom5.getLast().getOne());
        assertEquals(5, bottom5.getLast().getTwo());
        Verify.assertIterableSize(0, ImmutableArrayBag.newBagWith().bottomOccurrences(5));
        Verify.assertIterableSize(3, this.newWith("one", "two", "three").bottomOccurrences(5));
        Verify.assertIterableSize(3, this.newWith("one", "two", "three").bottomOccurrences(1));
        Verify.assertIterableSize(3, this.newWith("one", "two", "three").bottomOccurrences(2));
        Verify.assertIterableSize(3, this.newWith("one", "one", "two", "two", "three").bottomOccurrences(2));
        Verify.assertIterableSize(3, this.newWith("one", "one", "two", "two", "three", "three").bottomOccurrences(1));
        Verify.assertIterableSize(3, this.newWith(null, "one", "two").bottomOccurrences(5));
        Verify.assertIterableSize(3, this.newWith(null, "one", "two").bottomOccurrences(1));
        Verify.assertIterableSize(3, this.newWith("one", "one", "two", "two", "three", "three").bottomOccurrences(1));
        Verify.assertIterableSize(0, this.newWith("one").newWithout("one").bottomOccurrences(0));
        Verify.assertIterableSize(0, this.newWith("one").bottomOccurrences(0));
        assertThrows(IllegalArgumentException.class, () -> this.newWith("one").bottomOccurrences(-1));
    }

    @Override
    @Test
    public void selectUnique()
    {
        super.selectUnique();

        ImmutableBag<String> bag = this.newBag();
        ImmutableSet<String> expected = Sets.immutable.of("1");
        ImmutableSet<String> actual = bag.selectUnique();
        assertEquals(expected, actual);
    }

    @Override
    @Test
    public void distinctView()
    {
        ImmutableBag<String> bag = this.newBag();
        RichIterable<String> expected = bag.toSet();
        RichIterable<String> actual = bag.distinctView();
        // this assertion is a reminder to get rid of this test override once distinctView returns a set
        assertNotEquals(expected, actual);
        Verify.assertIterablesEqual(expected, actual);
    }

        @Test
    public void testNewBagWith() {
        ImmutableArrayBag<String> bag = ImmutableArrayBag.newBagWith("a", "b", "a");
        assertEquals(2, bag.sizeDistinct(), "Il doit y avoir 2 éléments distincts");
        assertEquals(3, bag.size(), "La taille totale doit être 3");
        assertEquals(2, bag.occurrencesOf("a"), "L'occurrence de 'a' doit être 2");
        assertEquals(1, bag.occurrencesOf("b"), "L'occurrence de 'b' doit être 1");
        assertEquals(0, bag.occurrencesOf("c"), "Aucune occurrence de 'c'");
    }

    @Test
    public void testCopyFrom() {
        HashBag<String> mutableBag = HashBag.newBag();
        mutableBag.add("x");
        mutableBag.add("y", 2);
        mutableBag.add("x", 3);
        ImmutableArrayBag<String> bag = ImmutableArrayBag.copyFrom(mutableBag);
        assertEquals(2, bag.sizeDistinct(), "Il doit y avoir 2 éléments distincts");
        assertEquals(6, bag.size(), "La taille totale doit être 6");
        assertEquals(4, bag.occurrencesOf("x"), "L'occurrence de 'x' doit être 4");
        assertEquals(2, bag.occurrencesOf("y"), "L'occurrence de 'y' doit être 2");
    }

    @Test
    public void testIsALegalLenght() {
        ImmutableArrayBag<String> bag1 = ImmutableArrayBag.newBagWith("a");
        assertFalse(bag1.IsALegalLenght(), "Un bag avec un seul élément (occurrence 1) est considéré comme illégal");
        HashBag<String> mutableBag = HashBag.newBag();
        mutableBag.add("a", 2);
        ImmutableArrayBag<String> bag2 = ImmutableArrayBag.copyFrom(mutableBag);
        assertTrue(bag2.IsALegalLenght(), "Un bag avec un seul élément mais occurrence > 1 est légal");

        ImmutableArrayBag<String> bag3 = ImmutableArrayBag.newBagWith("a", "b");
        assertTrue(bag3.IsALegalLenght(), "Un bag avec plusieurs éléments distincts est légal");
    }

    @Test
    public void testIsAnOccurencesOf() {
        ImmutableArrayBag<String> immutableBag = ImmutableArrayBag.newBagWith("a", "b", "a");
        HashBag<String> mutableBag = HashBag.newBag();
        mutableBag.add("a", 2);
        mutableBag.add("b");
        assertTrue(immutableBag.IsAnOccurencesOf(mutableBag), "Les occurrences doivent être identiques");

        mutableBag.remove("a");
        assertFalse(immutableBag.IsAnOccurencesOf(mutableBag), "Les occurrences ne sont plus identiques");
    }

    @Test
    public void testIsABagObject() {
        ImmutableArrayBag<String> immutableBag = ImmutableArrayBag.newBagWith("a", "b", "a");
        HashBag<String> mutableBag = HashBag.newBag();
        mutableBag.add("a", 2);
        mutableBag.add("b");
        assertTrue(immutableBag.isABagObject(mutableBag), "Les bags doivent être identiques en termes d'occurrences");

        mutableBag.add("b");
        assertFalse(immutableBag.isABagObject(mutableBag), "Les bags ne sont plus identiques");
    }
}
