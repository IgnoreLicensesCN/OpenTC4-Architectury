package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinstackhelper.MilkContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {
    @Inject(
            method = "finishUsingItem",
            at = @At("HEAD")
    )
    private void opentc4$markMilkContext(
            ItemStack stack, Level level, LivingEntity entity,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        MilkContext.FROM_MILK.set(true);//a bit trick from chatGPT
    }

    @Inject(
            method = "finishUsingItem",
            at = @At("RETURN")
    )
    private void opentc4$clearMilkContext(
            ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir
    ) {
        MilkContext.FROM_MILK.remove();//a bit trick from chatGPT
    }
}

