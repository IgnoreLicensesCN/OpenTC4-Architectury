package com.linearity.opentc4.utils.collectionlike;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class AutoSortThreadSafeList<T/* extends Comparable<T>*/> implements List<T> {
    private final List<T> wrapped = new CopyOnWriteArrayList<>();
    private final Comparator<T> comparator;

    public AutoSortThreadSafeList(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    public static <T extends Comparable<T>> AutoSortThreadSafeList<T> ofComparable() {
        return new AutoSortThreadSafeList<>(Comparable::compareTo);
    }


    @Override
    public boolean removeIf(@NotNull Predicate<? super T> filter) {
        return wrapped.removeIf(filter);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return wrapped.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return wrapped.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        wrapped.forEach(action);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return wrapped.toArray();
    }

    @NotNull
    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        return wrapped.toArray(a);
    }

    @Override
    public boolean add(T t) {
        boolean added = wrapped.add(t);
        wrapped.sort(comparator);
        return added;
    }

    @Override
    public boolean remove(Object o) {
        return wrapped.remove(o);
    }


    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return wrapped.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        boolean changed = wrapped.addAll(c);
        wrapped.sort(comparator);
        return changed;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        boolean changed = wrapped.addAll(index, c);
        wrapped.sort(comparator);
        return changed;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return wrapped.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean changed = wrapped.retainAll(c);
        wrapped.sort(comparator);
        return changed;
    }

    @Override
    public void replaceAll(@NotNull UnaryOperator<T> operator) {
        wrapped.replaceAll(operator);
        wrapped.sort(comparator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        wrapped.sort(c);
    }

    @Override
    public void clear() {
        wrapped.clear();
    }

    @Override
    public T get(int index) {
        return wrapped.get(index);
    }

    @Override
    public T set(int index, T element) {
        T old = wrapped.set(index, element);
        wrapped.sort(comparator);
        return old;
    }

    @Override
    public void add(int index, T element) {
        wrapped.add(index, element);
        wrapped.sort(comparator);
    }

    @Override
    public T remove(int index) {
        return wrapped.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return wrapped.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return wrapped.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return wrapped.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return wrapped.listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return wrapped.subList(fromIndex, toIndex);
    }

    @NotNull
    @Override
    public Spliterator<T> spliterator() {
        return wrapped.spliterator();
    }

    @NotNull
    @Override
    public Stream<T> stream() {
        return wrapped.stream();
    }

    @NotNull
    @Override
    public Stream<T> parallelStream() {
        return wrapped.parallelStream();
    }
}
