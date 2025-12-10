package com.linearity.opentc4.utils;

import com.linearity.opentc4.mixin.ItemStackAccessor;
import com.linearity.opentc4.mixin.ItemStackMixin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Vanilla1710Utils {
    public static boolean isItemTool(ItemStack p_77616_1_)
    {
        return p_77616_1_.getMaxStackSize() == 1 && p_77616_1_.getMaxDamage() != 0;
    }
    public static boolean isItemTool(Item p_77616_1_)
    {
        return p_77616_1_.getMaxStackSize() == 1 && p_77616_1_.getMaxDamage() != 0;
    }

    public static boolean ignoresDamage(ItemStack stack) {
        ItemStackAccessor accessor = (ItemStackAccessor)(Object) stack;
        return accessor.opentc4$getIgnoresDamageFlag();
    }

    public static void setIgnoresDamage(ItemStack stack, boolean flag) {
        ItemStackAccessor accessor = (ItemStackAccessor)(Object) stack;
        accessor.opentc4$setIgnoresDamageFlag(flag);
    }
}
