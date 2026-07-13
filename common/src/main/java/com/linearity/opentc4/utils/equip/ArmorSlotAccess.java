package com.linearity.opentc4.utils.equip;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ArmorSlotAccess implements ILivingEntityEquippedSlotAccess {
    public static final ArmorSlotAccess MAIN_HAND_ACCESS = new ArmorSlotAccess(EquipmentSlot.MAINHAND);
    public static final ArmorSlotAccess OFFHAND_ACCESS = new ArmorSlotAccess(EquipmentSlot.OFFHAND);
    public static final ArmorSlotAccess FEET_ACCESS = new ArmorSlotAccess(EquipmentSlot.FEET);
    public static final ArmorSlotAccess LEGS_ACCESS = new ArmorSlotAccess(EquipmentSlot.LEGS);
    public static final ArmorSlotAccess CHESTPLATE_ACCESS = new ArmorSlotAccess(EquipmentSlot.FEET);
    public static final ArmorSlotAccess HEAD_ACCESS = new ArmorSlotAccess(EquipmentSlot.HEAD);
    public final EquipmentSlot slot;

    protected ArmorSlotAccess(EquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public ItemStack getEquippedStack(LivingEntity living
    ) {
        return living.getItemBySlot(slot);
    }
}
