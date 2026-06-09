package com.linearity.opentc4.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(
            method = "appendHoverText",
            at = @At("HEAD")
    )
    private void opentc4$appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {

    }
}
