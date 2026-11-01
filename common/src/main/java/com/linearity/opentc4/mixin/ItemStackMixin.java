package com.linearity.opentc4.mixin;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import com.linearity.opentc4.mixinaccessors.IObjectCacheableItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static net.minecraft.world.item.ItemStack.EMPTY;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IObjectCacheableItemStack, ExtendedItemStack {
    @Shadow @Final /*@Deprecated*/ private @Nullable Item item;
    @Shadow private int count;
    @Shadow private @Nullable CompoundTag tag;
    @Unique
    private final ClassToInstanceMap<Object> opentc4$objectCacheMap = MutableClassToInstanceMap.create();

    @Override
    public <T> @Nullable T opentc4$getCachedObjectFromItemStack(Class<T> clazz) {
        return opentc4$objectCacheMap.getInstance(clazz);
    }

    @Override
    public <T> void opentc4$putCachedObjectIntoItemStack(Class<T> clazz, T object) {
        opentc4$objectCacheMap.putInstance(clazz, object);
    }

    @Override
    public void opentc4$removeCachedObjectFromItemStack(Class<?> clazz) {
        opentc4$objectCacheMap.remove(clazz);
    }

    @Override
    public ItemStack opentc4$splitAll() {
        ItemStack newStack = new ItemStack(item == null? EMPTY.getItem():item,count);
        ((ExtendedItemStack)(Object)newStack).opentc4$forceSetTag(tag);
        this.count = 0;
        this.tag = null;
        return newStack;
    }

    @Override
    public ItemStack opentc4$splitAndShareTag(int amount) {
        var thiz = (ItemStack)(Object)this;
        int j = Math.min(amount, thiz.getCount());
        ItemStack itemStack = thiz.copyWithCount(j);
        thiz.shrink(j);
        return itemStack;
    }
    public ItemStack opentc4$copyAndShareTagWithCount(int i) {
        var thiz = (ItemStack)(Object)this;
        if (thiz.isEmpty()) {
            return EMPTY;
        }
        ItemStack itemStack = this.opentc4$copyAndShareTag();
        itemStack.setCount(i);
        return itemStack;

    }

    @Override
    public ItemStack opentc4$copyAndShareTag() {
        var thiz = (ItemStack)(Object)this;
        if (thiz.isEmpty()) {
            return EMPTY;
        }
        ItemStack itemStack = new ItemStack(item, this.count);
        itemStack.setPopTime(thiz.getPopTime());
        if (this.tag != null) {
            ((ExtendedItemStack)(Object)itemStack).opentc4$forceSetTag(this.tag);
        }

        return itemStack;

    }

    @Override
    public void opentc4$forceSetTag(CompoundTag compoundTag) {
        this.tag = compoundTag;
    }
}

