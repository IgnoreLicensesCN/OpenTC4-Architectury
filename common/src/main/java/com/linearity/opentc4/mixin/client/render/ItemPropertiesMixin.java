package com.linearity.opentc4.mixin.client.render;

import com.linearity.opentc4.mixinaccessors.ItemCooldownOwnerEntity;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemProperties.class)
public class ItemPropertiesMixin {

    @ModifyArg(
            method = "registerGeneric",
            at = @At("HEAD")
    )
    private static ClampedItemPropertyFunction opentc4$modifyCooldownProperty(ClampedItemPropertyFunction before, ResourceLocation resourceLocation){
        if (resourceLocation.getPath().equals("cooldown")) {
            return ((itemStack, clientLevel, livingEntity, i) -> {
                if (livingEntity instanceof ItemCooldownOwnerEntity cooldownOwner){
                    return cooldownOwner.opentc4$getItemCooldowns().getCooldownPercent(itemStack.getItem(),0);
                }
                return before.unclampedCall(itemStack,clientLevel,livingEntity,i);
            });
        }
        return before;
    }
}
