package com.linearity.opentc4.utils.vanilla1710;

import com.linearity.opentc4.mixinaccessors.ItemStackAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Vanilla1710Utils {
    public static boolean isItemTool(ItemStack stack)
    {
        return stack.getMaxStackSize() == 1 && stack.getMaxDamage() != 0;
    }
    public static boolean isItemTool(Item p_77616_1_)
    {
        return p_77616_1_.getMaxStackSize() == 1 && p_77616_1_.getMaxDamage() != 0;
    }

    @Deprecated(forRemoval = true)
    public static boolean ignoresDamage(ItemStack stack) {
        ItemStackAccessor accessor = (ItemStackAccessor)(Object) stack;
        return accessor.opentc4$getIgnoresDamageFlag();
    }

    @Deprecated(forRemoval = true)
    public static void setIgnoresDamage(ItemStack stack, boolean flag) {
        ItemStackAccessor accessor = (ItemStackAccessor)(Object) stack;
        accessor.opentc4$setIgnoresDamageFlag(flag);
    }
}
