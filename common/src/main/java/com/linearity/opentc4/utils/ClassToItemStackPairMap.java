package com.linearity.opentc4.utils;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.linearity.opentc4.utils.collectionlike.UpgradePair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.PriorityQueue;
import java.util.Set;

public class ClassToItemStackPairMap {
    private final Multimap<Class<?>, UpgradePair<?>> delegate = Multimaps.newMultimap(new IdentityHashMap<>(),() -> new PriorityQueue<>(UpgradePair::compare));

    public void checkAndPut(Class<?> clz, Item item, ItemStack stack,int weight) {
        if (clz.isInstance(item)){
            delegate.put(clz,new UpgradePair<>(clz.cast(item),stack,weight));
        }
    }

    public <T> void put(Class<T> type, UpgradePair<T> instance) {
        delegate.put(type, instance);
    }

    public <T> void putAll(Class<T> type, Iterable<UpgradePair<T>> instances) {
        delegate.putAll(type, instances);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<UpgradePair<T>> get(Class<T> type) {
        return (Collection<UpgradePair<T>>) (Object) delegate.get(type);
    }

    public Set<Class<?>> getKeys(){
        return delegate.keySet();
    }

    public void clear() {
        delegate.clear();
    }

    public static class View {
        private final ClassToItemStackPairMap delegate;
        public View(ClassToItemStackPairMap delegate) {
            this.delegate = delegate;
        }
        public @UnmodifiableView <T> Collection<UpgradePair<T>> get(Class<T> type) {
            return delegate.get(type);
        }

        public Set<Class<?>> getKeys(){
            return delegate.getKeys();
        }
    }
}
