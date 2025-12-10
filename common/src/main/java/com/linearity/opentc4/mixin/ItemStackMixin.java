package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.ItemStackAccessor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackAccessor {
    @Unique
    private boolean opentc4$ignoresDamageFlag = false;

    @Unique
    // Getter / Setter
    public boolean opentc4$getIgnoresDamageFlag() {
        return opentc4$ignoresDamageFlag;
    }

    @Unique
    public void opentc4$setIgnoresDamageFlag(boolean flag) {
        this.opentc4$ignoresDamageFlag = flag;
    }
}

