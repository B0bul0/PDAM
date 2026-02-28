package me.bobulo.mine.pdam.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * A high-performance thread-safe fixed-capacity circular history.
 */
public class FixedCircularHistory<E> {

    private final Object[] buffer;
    private final int capacity;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private int head;
    private int size = 0;

    public FixedCircularHistory(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity >= 1");
        }

        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public void push(@NotNull E element) {
        lock.writeLock().lock();
        try {
            buffer[head] = element;

            // Move head forward, wrapping around if needed
            head = (head + 1) % capacity;

            if (size < capacity) {
                size++;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        lock.readLock().lock();
        try {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }

            // Calculate the real index in the circular buffer
            int realIndex = (head - size + index + capacity) % capacity;

            return (E) buffer[realIndex];
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public void forEach(@NotNull Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            final int currentSize = this.size;
            final int currentHead = this.head;
            final int cap = this.capacity;

            for (int i = 0; i < currentSize; i++) {
                int realIndex = (currentHead - currentSize + i + cap) % cap;
                action.accept((E) this.buffer[realIndex]);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public List<E> snapshot() {
        lock.readLock().lock();
        try {
            Object[] snapshot = new Object[size];
            for (int i = 0; i < size; i++) {
                int realIndex = (head - size + i + capacity) % capacity;
                snapshot[i] = buffer[realIndex];
            }
            return Arrays.asList((E[]) snapshot);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        return size;
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            size = 0;
            head = 0;
            Arrays.fill(buffer, null);
        } finally {
            lock.writeLock().unlock();
        }
    }

}