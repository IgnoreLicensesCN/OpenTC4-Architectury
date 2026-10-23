package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.ItemCooldownOwnerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin implements ItemCooldownOwnerEntity.IItemCooldownProviderItem {

    @Inject(
            method = "finishUsingItem", at = @At(value = "TAIL")
    )
    public void opentc4$cooldownChorusFruitForItemCooldownOwner(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (!level.isClientSide()) {
            if (livingEntity instanceof ItemCooldownOwnerEntity cooldownOwnerEntity) {
                ItemCooldownOwnerEntity.cooldownForNotCoolingDown(itemStack,livingEntity,cooldownOwnerEntity.opentc4$getItemCooldowns());
            }
        }
    }

    @Override
    public int getItemCooldownForItemStackAndEntity(ItemStack itemStack, LivingEntity livingEntity) {
        return 20;
    }
}
