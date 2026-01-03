package me.bobulo.mine.pdam.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Thread-safe bounded list implementation using a circular buffer.
 */
public class BoundedConcurrentList<E> implements List<E> {

    private final int capacity;
    private final Object[] buffer;
    private final ReadWriteLock lock;

    private volatile int size;
    private int head; // Index of oldest element
    private int tail; // Index where next element will be inserted

    public BoundedConcurrentList(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }

        this.capacity = capacity;
        this.buffer = new Object[capacity];
        this.lock = new ReentrantReadWriteLock();
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    public BoundedConcurrentList(Collection<E> collection) {
        this(collection.size());
        addAll(collection);
    }

    /**
     * Creates a snapshot copy maintaining insertion order.
     */
    @NotNull
    public List<E> snapshot() {
        lock.readLock().lock();
        try {
            List<E> result = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                result.add(get(i));
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns maximum capacity.
     */
    public int capacity() {
        return capacity;
    }

    /* List<E> implementation */

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(get(i), o)) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.readLock().unlock();
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return snapshot().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            Object[] result = new Object[size];
            for (int i = 0; i < size; i++) {
                result[i] = get(i);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(@NotNull T[] a) {
        lock.readLock().lock();
        try {
            if (a.length < size) {
                a = (T[]) Array.newInstance(
                  a.getClass().getComponentType(), size);
            }
            for (int i = 0; i < size; i++) {
                a[i] = (T) get(i);
            }
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E element) {
        lock.writeLock().lock();
        try {
            buffer[tail] = element;
            tail = (tail + 1) % capacity;

            if (size < capacity) {
                size++;
            } else {
                head = (head + 1) % capacity;
            }

            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(get(i), o)) {
                    remove(i);
                    return true;
                }
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        lock.readLock().lock();
        try {
            for (Object e : c) {
                if (!contains(e)) {
                    return false;
                }
            }
            return true;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        lock.writeLock().lock();
        try {
            for (E e : c) {
                add(e);
            }
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("Adding at specific index not supported.");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        lock.writeLock().lock();
        try {
            boolean modified = false;
            for (int i = size - 1; i >= 0; i--) {
                if (c.contains(get(i))) {
                    remove(i);
                    modified = true;
                }
            }
            return modified;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        lock.writeLock().lock();
        try {
            boolean modified = false;
            for (int i = size - 1; i >= 0; i--) {
                if (!c.contains(get(i))) {
                    remove(i);
                    modified = true;
                }
            }
            return modified;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            Arrays.fill(buffer, null);
            size = 0;
            head = 0;
            tail = 0;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        lock.readLock().lock();
        try {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            int actualIndex = (head + index) % capacity;
            return (E) buffer[actualIndex];
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        lock.writeLock().lock();
        try {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }

            int actualIndex = (head + index) % capacity;
            E oldValue = (E) buffer[actualIndex];
            buffer[actualIndex] = element;
            return oldValue;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Adding at specific index not supported.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        lock.writeLock().lock();
        try {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }

            int actualIndex = (head + index) % capacity;
            E oldValue = (E) buffer[actualIndex];

            // Shift elements left to maintain order
            for (int i = index; i < size - 1; i++) {
                int current = (head + i) % capacity;
                int next = (head + i + 1) % capacity;
                buffer[current] = buffer[next];
            }

            tail = (tail - 1 + capacity) % capacity;
            buffer[tail] = null;
            size--;

            return oldValue;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.readLock().lock();
        try {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(get(i), o)) {
                    return i;
                }
            }
            return -1;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.readLock().lock();
        try {
            for (int i = size - 1; i >= 0; i--) {
                if (Objects.equals(get(i), o)) {
                    return i;
                }
            }
            return -1;
        } finally {
            lock.readLock().unlock();
        }
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return snapshot().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return snapshot().listIterator(index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.readLock().lock();
        try {
            if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
                throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
            }
            List<E> result = new ArrayList<>(toIndex - fromIndex);
            for (int i = fromIndex; i < toIndex; i++) {
                result.add(get(i));
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(@NotNull Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            for (int i = 0; i < size; i++) {
                action.accept(get(i));
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            boolean modified = false;
            for (int i = size - 1; i >= 0; i--) {
                if (filter.test(get(i))) {
                    remove(i);
                    modified = true;
                }
            }
            return modified;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void replaceAll(@NotNull UnaryOperator<E> operator) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < size; i++) {
                set(i, operator.apply(get(i)));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        lock.writeLock().lock();
        try {
            List<E> temp = snapshot();
            temp.sort(c);
            clear();
            addAll(temp);
        } finally {
            lock.writeLock().unlock();
        }
    }

}