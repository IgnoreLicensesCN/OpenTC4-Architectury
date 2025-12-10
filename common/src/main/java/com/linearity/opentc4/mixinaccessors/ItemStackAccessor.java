package com.linearity.opentc4.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

public interface ItemStackAccessor {
    boolean opentc4$getIgnoresDamageFlag();

    void opentc4$setIgnoresDamageFlag(boolean flag);
}
