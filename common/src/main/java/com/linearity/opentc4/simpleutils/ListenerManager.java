package com.linearity.opentc4.simpleutils;

import java.util.Collections;
import java.util.List;

//so it maybe just a AutoSortThreadSafeList
public class ListenerManager<T extends Comparable<T>> {
    private final List<T> listeners = new AutoSortThreadSafeList<>();
    public ListenerManager() {

    }
    public boolean registerListener(T o) {
        return listeners.add(o);
    }
    public boolean unregisterListener(T o) {
        return listeners.remove(o);
    }

    public List<T> getListeners() {
        return Collections.unmodifiableList(listeners);
    }
}
