package com.linearity.opentc4.mixinaccessors;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

import java.util.IdentityHashMap;
import java.util.Map;

//maybe not suitable to put in this package?
//but we may use in this way
//TODO:[maybe wont finished]impl everywhere (and IItemCooldownProviderItem)
public interface ItemCooldownOwnerEntity {
    ItemCooldowns opentc4$getItemCooldowns();
    Map<Item,IItemCooldownProviderItem> ITEM_COOLDOWNS = new IdentityHashMap<>();
    interface IItemCooldownProviderItem {
        int getItemCooldownForItemStackAndEntity(ItemStack itemStack, LivingEntity /*ensure ItemCooldownOwnerEntity*/ livingEntity);
    }
    static void cooldownForNotCoolingDown(ItemStack stack,/*ensure ItemCooldownOwnerEntity*/ LivingEntity living, ItemCooldowns cooldowns) {
        var item = stack.getItem();
        if (!cooldowns.isOnCooldown(item)) {
            IItemCooldownProviderItem cooldownProvider = null;
            if (item instanceof IItemCooldownProviderItem cooldownProviderItem) {
                cooldownProvider = cooldownProviderItem;
            }
            if (cooldownProvider == null){
                cooldownProvider = ITEM_COOLDOWNS.get(item);
            }
            if (cooldownProvider != null) {
                int cooldown = cooldownProvider.getItemCooldownForItemStackAndEntity(stack, living);
                if (cooldown > 0) {
                    cooldowns.addCooldown(item, cooldown);
                }
            }
        }
    }
}
