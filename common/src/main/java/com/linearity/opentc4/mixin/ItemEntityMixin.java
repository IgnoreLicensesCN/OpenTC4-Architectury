package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.items.abstracts.ILiquidListenableItem;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(
            method = "tick",
            at=@At("HEAD")
    )
    private void opentc4$tick(CallbackInfo ci){
        var entity = (ItemEntity)(Object)this;
        var stack = entity.getItem();
        var atBPos = entity.blockPosition();
        var fluidState = entity.level().getFluidState(atBPos);
        if (!fluidState.isEmpty()){
            if (stack.getItem() instanceof ILiquidListenableItem liquidListenableItem) {
                liquidListenableItem.whenInFluid(entity, stack, atBPos, fluidState);
            }
        }
    }
}
