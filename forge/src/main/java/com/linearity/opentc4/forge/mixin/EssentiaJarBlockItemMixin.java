package com.linearity.opentc4.forge.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.items.misc.EssentiaJarBlockItem;

import static com.linearity.opentc4.Consts.EssentiaJarTagAccessors.AMOUNT;
import static com.linearity.opentc4.Consts.EssentiaJarTagAccessors.ASPECT;

@Mixin(EssentiaJarBlockItem.class)
public class EssentiaJarBlockItemMixin {
    /**
     * @author IgnoreLicensesCN
     * @reason need a way to get empty jar stack to 64--at least in forge
     */
    @Overwrite
    public int getMaxStackSize(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) {
            return 64;
        }
        if (!ASPECT.compoundTagHasKey(tag) || !AMOUNT.compoundTagHasKey(tag)) {
            return 64;
        }
        if (ASPECT.readFromCompoundTag(tag).isEmpty() || AMOUNT.readFromCompoundTag(tag) == 0) {
            return 64;
        }
        return 1;
    }
}
