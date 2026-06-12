package com.linearity.opentc4.utils.equip;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IPlayerEquippedSlotAccess {
    ItemStack getEquippedStack(Player player);
}
