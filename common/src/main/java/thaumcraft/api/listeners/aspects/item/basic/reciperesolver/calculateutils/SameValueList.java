package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils;

import java.util.AbstractList;
import java.util.List;

public class SameValueList<E> extends AbstractList<E> {

    private final int size;
    private final E element;

    public SameValueList(E element){
        this(Integer.MAX_VALUE, element);
    }
    public SameValueList(int size, E element) {
        this.size = size;
        this.element = element;
    }


    @Override
    public E get(int index) {
        return element;
    }

    @Override
    public int size() {
        return size;
    }

    public List<E> asList() {
        return this;
    }
}
