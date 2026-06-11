package com.linearity.opentc4.mixin;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotAccessor {
    @Accessor("slot")
    int opentc4$getSlotIndex();
    @Accessor("x")
    void opentc4$setX(int i);
    @Accessor("y")
    void opentc4$setY(int i);
}
