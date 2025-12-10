package com.linearity.opentc4.utils;

import java.util.Collection;
import java.util.List;

public class IndexPicker {

    public static int indexByTime(int length){
        if (length == 1){
            return 0;
        }
        return indexByTime(length, 1000L);
    }
    public static int indexByTime(int length,long delayMS){
        return (int) ((System.currentTimeMillis()/delayMS)%length);
    }


    public static <T> T pickByTime(T[] t){
        return pickByTime(t, 1000L);
    }
    public static <T> T pickByTime(T[] t,long delayMS){
        if (t.length==0) return null;
        return t[indexByTime(t.length, delayMS)];
    }
    public static <T> T pickByTime(List<T> t){
        return pickByTime(t, 1000L);
    }
    public static <T> T pickByTime(List<T> t,long delayMS){
        if (t.isEmpty()) return null;
        return t.get(indexByTime(t.size(), delayMS));
    }
}
