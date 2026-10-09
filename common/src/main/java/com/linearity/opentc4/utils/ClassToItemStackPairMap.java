package com.linearity.opentc4.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.linearity.opentc4.utils.collectionlike.SimplePair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

public class ClassToItemStackPairMap {
    private final ListMultimap<Class<?>, SimplePair<?, ItemStack>> delegate = ArrayListMultimap.create();

    public void checkAndPut(Class<?> clz, Item item, ItemStack stack) {
        if (clz.isInstance(item)){
            delegate.put(clz,new SimplePair<>(clz.cast(item),stack));
        }
    }

    public <T> void put(Class<T> type, SimplePair<T, ItemStack> instance) {
        delegate.put(type, instance);
    }

    public <T> void putAll(Class<T> type, Iterable<SimplePair<T, ItemStack>> instances) {
        delegate.putAll(type, instances);
    }

    @SuppressWarnings("unchecked")
    public <T> List<SimplePair<T, ItemStack>> get(Class<T> type) {
        return (List<SimplePair<T, ItemStack>>) (Object) delegate.get(type);
    }

    public void clear() {
        delegate.clear();
    }

    public static class View {
        private final ClassToItemStackPairMap delegate;
        public View(ClassToItemStackPairMap delegate) {
            this.delegate = delegate;
        }
        public @UnmodifiableView <T> List<SimplePair<T, ItemStack>> get(Class<T> type) {
            return delegate.get(type);
        }
    }
}
