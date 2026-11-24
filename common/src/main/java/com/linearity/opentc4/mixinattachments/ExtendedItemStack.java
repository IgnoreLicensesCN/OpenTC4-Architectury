package com.linearity.opentc4.mixinattachments;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public interface ExtendedItemStack {
    ItemStack opentc4$splitAll();
    ItemStack opentc4$splitAndShareTag(int amount);
    ItemStack opentc4$copyAndShareTagWithCount(int amount);
    ItemStack opentc4$copyAndShareTag();
    void opentc4$setTo(ItemStack stack);
    void opentc4$setToShareTag(ItemStack stack);

    @ApiStatus.Internal
    void opentc4$forceSetTag(CompoundTag compoundTag);

    static ItemStack splitAll(ItemStack stack) {
        return ((ExtendedItemStack)(Object)stack).opentc4$splitAll();
    }

    static ItemStack split(ItemStack stack, int amount) {
        if (amount == stack.getCount()) {
            return splitAll(stack);
        }
        return stack.split(amount);
    }

    static ItemStack splitAndShareTag(ItemStack stack, int amount) {
        return ((ExtendedItemStack)(Object)stack).opentc4$splitAndShareTag(amount);
    }

    static ItemStack copyAndShareTag(ItemStack stack) {
        return ((ExtendedItemStack)(Object)stack).opentc4$copyAndShareTag();
    }

    static void setTo(ItemStack beingSet,ItemStack dataFrom){
        ((ExtendedItemStack)(Object)beingSet).opentc4$setTo(dataFrom);
    }
    static void setToShareTag(ItemStack beingSet,ItemStack dataFrom){
        ((ExtendedItemStack)(Object)beingSet).opentc4$setToShareTag(dataFrom);
    }
}
