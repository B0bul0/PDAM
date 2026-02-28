package me.bobulo.mine.pdam.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A fixed-capacity history that maintains unique elements.
 */
public class UniqueHistory<E> implements Iterable<E> {

    private final int capacity;
    private final LinkedList<E> backingList;

    public UniqueHistory(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }

        this.capacity = capacity;
        this.backingList = new LinkedList<>();
    }

    public UniqueHistory(int capacity, Collection<E> collection) {
        this(capacity);
        for (E element : collection) {
            push(element);
        }
    }

    public void push(@NotNull E element) {
        backingList.remove(element);
        backingList.addFirst(element);

        if (backingList.size() > capacity) {
            backingList.removeLast();
        }
    }

    @Nullable
    public E peek() {
        return backingList.peekFirst();
    }

    public void clear() {
        backingList.clear();
    }

    public int size() {
        return backingList.size();
    }

    public boolean isEmpty() {
        return backingList.isEmpty();
    }

    public boolean remove(@NotNull Object element) {
        return backingList.remove(element);
    }

    public boolean contains(@NotNull Object o) {
        return backingList.contains(o);
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return backingList.iterator();
    }

}