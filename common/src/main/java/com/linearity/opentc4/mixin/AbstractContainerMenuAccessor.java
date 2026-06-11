package com.linearity.opentc4.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor {
    @Accessor("lastSlots")
    NonNullList<ItemStack> opentc4$getLastSlots();
    @Accessor("remoteSlots")
    NonNullList<ItemStack> opentc4$getRemoteSlots();
    @Invoker("doClick")
    void opentc4$doClick(int i, int j, ClickType clickType, Player player);

}
