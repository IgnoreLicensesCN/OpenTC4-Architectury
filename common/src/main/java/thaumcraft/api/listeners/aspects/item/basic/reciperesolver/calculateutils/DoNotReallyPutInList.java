package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

//use at your own risk
@Setter
public class DoNotReallyPutInList<E> implements List<E> {
    public static final DoNotReallyPutInList<?> INSTANCE = new DoNotReallyPutInList<>();
    public static  <E> DoNotReallyPutInList<E> getInstance() {
        INSTANCE.setSize(0);
        return (DoNotReallyPutInList<E>) INSTANCE;
    }
    protected int size = 0;
    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {
        size += 1;
    }

    @Override
    public E remove(int index) {
        size--;
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public @NotNull ListIterator<E> listIterator() {
        List<E> l = List.of();
        return l.listIterator();
    }

    @Override
    public @NotNull ListIterator<E> listIterator(int index) {
        List<E> l = List.of();
        return l.listIterator();
    }

    @Override
    public @NotNull List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    @Override
    public boolean add(E e) {
        size += 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        size -= 1;
        return true;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public @NotNull Object[] toArray() {
        List<E> l = List.of();
        return l.toArray();
    }

    @Override
    public @NotNull <T> T[] toArray(@NotNull T[] a) {
        List<E> l = List.of();
        return l.toArray(a);
    }
}
