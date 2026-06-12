package com.linearity.opentc4.utils.equip;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EquipmentSlotSlotAccess implements IPlayerEquippedSlotAccess{
    public static final EquipmentSlotSlotAccess MAIN_HAND_ACCESS = new EquipmentSlotSlotAccess(EquipmentSlot.MAINHAND);
    public static final EquipmentSlotSlotAccess OFFHAND_ACCESS = new EquipmentSlotSlotAccess(EquipmentSlot.OFFHAND);
    public static final EquipmentSlotSlotAccess FEET_ACCESS = new EquipmentSlotSlotAccess(EquipmentSlot.FEET);
    public static final EquipmentSlotSlotAccess LEGS_ACCESS = new EquipmentSlotSlotAccess(EquipmentSlot.LEGS);
    public static final EquipmentSlotSlotAccess CHESTPLATE_ACCESS = new EquipmentSlotSlotAccess(EquipmentSlot.FEET);
    public static final EquipmentSlotSlotAccess HEAD_ACCESS = new EquipmentSlotSlotAccess(EquipmentSlot.HEAD);
    public final EquipmentSlot slot;

    protected EquipmentSlotSlotAccess(EquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public ItemStack getEquippedStack(Player player) {
        return player.getItemBySlot(slot);
    }
}
