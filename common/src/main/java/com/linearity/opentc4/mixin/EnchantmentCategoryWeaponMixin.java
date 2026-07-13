package com.linearity.opentc4.mixin;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.throwables.MixinException;
import thaumcraft.common.items.abstracts.ISwordLikeItem;

@Mixin(targets = "net.minecraft.world.item.enchantment.EnchantmentCategory$6")
public class EnchantmentCategoryWeaponMixin {
    @Inject(
            method = "canEnchant",
            at = @At("HEAD"),
            cancellable = true
    )
    private void opentc4$canEnchant(Item item, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this != EnchantmentCategory.WEAPON){
            OpenTC4.LOGGER.error(new MixinException("mixin target wrong!this is not enchantmentCategory weapon"));
            return;
        }
        if (item instanceof ISwordLikeItem){
            cir.setReturnValue(true);
        }
    }

}
