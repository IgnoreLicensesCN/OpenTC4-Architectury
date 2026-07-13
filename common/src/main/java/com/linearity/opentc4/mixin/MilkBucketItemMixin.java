package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.InMilkContextAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MilkBucketItem.class,priority = 214748364)
public class MilkBucketItemMixin {
    @Inject(
            method = "finishUsingItem",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z")
    )
    private void opentc4$markMilkContext(
            ItemStack stack, Level level, LivingEntity entity,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        ((InMilkContextAccessor)entity).opentc4$setInMilkContext(true);
    }

    @Inject(
            method = "finishUsingItem",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z",shift = At.Shift.AFTER)
    )
    private void opentc4$clearMilkContext(
            ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir
    ) {
        ((InMilkContextAccessor)entity).opentc4$setInMilkContext(false);
    }
}

