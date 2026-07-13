package com.linearity.opentc4.utils.equip.bauble;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class BaubleUtils {

    public static void forEachBaubleAndArmor(LivingEntity living, Consumer<ItemStack> consumer) {
        living.getArmorSlots().forEach(consumer);
        forEachBauble(living, (_ignored1, stack, _ignored2) -> {
            consumer.accept(stack);
            return false;
        });
    }

    /**
     * iterate through every bauble itemstack of a player
     *
     * @param living    the victim
     * @param operation what will be done for every itemstack,return true inside to break the loop
     * @return whether the loop is broken by {@link BaubleConsumer#accept(EquippedBaubleSlot, ItemStack, Object)} returning true.
     */
    public static boolean forEachBauble(LivingEntity living, BaubleConsumer<Item> operation) {
        var capability = AccessoriesCapability.get(living);
        if (capability == null) {
            return false;
        }

        for (var containerEntry : capability.getContainers().entrySet()) {
            String containerName = containerEntry.getKey();
            var container = containerEntry.getValue();
            for (var accessory : container.getAccessories()) {
                var stack = accessory.getSecond();
                if (operation.accept(new EquippedBaubleSlot(containerName, accessory.getFirst()), stack, stack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * iterate through every bauble itemstack of a player,
     * only item class meets {@code expectedItemType} will be accepted by {@link BaubleConsumer#accept(EquippedBaubleSlot, ItemStack, T)}
     *
     * @param living           the victim
     * @param expectedItemType class to judge item type,judge with it's method {@link Class#isAssignableFrom(Class)}.
     *                         e.g. expectedItemType.isAssignableFrom(itemstack.getItem().getClass())
     * @param operation        what will be done for every itemstack,return true inside to break the loop
     * @return whether the loop is broken by {@link BaubleConsumer#accept(EquippedBaubleSlot, ItemStack, T)} returning true.
     */
    public static <T> boolean forEachBauble(LivingEntity living, Class<T> expectedItemType, BaubleConsumer<T> operation) {
        var capability = AccessoriesCapability.get(living);
        if (capability == null) {
            return false;
        }

        for (var containerEntry : capability.getContainers().entrySet()) {
            String containerName = containerEntry.getKey();
            var container = containerEntry.getValue();
            for (var accessory : container.getAccessories()) {
                var stack = accessory.getSecond();
                var item = stack.getItem();
                if (expectedItemType.isInstance(item)) {
                    if (operation.accept(new EquippedBaubleSlot(containerName, accessory.getFirst()), stack, (T) stack.getItem())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * see {@link BaubleUtils#forEachBauble(LivingEntity, BaubleConsumer)},but it checks specific bauble type
     */
    public static boolean forEachBaubleWithBaubleType(String baubleType, LivingEntity living, BaubleConsumer<Item> operation) {
        var capability = AccessoriesCapability.get(living);
        if (capability == null) {
            return false;
        }
        var container = capability.getContainers().get(baubleType);
        for (var accessory : container.getAccessories()) {
            var stack = accessory.getSecond();
            if (operation.accept(new EquippedBaubleSlot(baubleType, accessory.getFirst()), stack, stack.getItem())) {
                return true;
            }
        }
        return false;
    }


    /**
     * see {@link BaubleUtils#forEachBauble(LivingEntity, Class, BaubleConsumer)},but it checks specific bauble type
     */
    public static <T> boolean forEachBaubleWithBaubleType(String baubleType, LivingEntity living, Class<T> expectedItemType, BaubleConsumer<T> operation) {
        var capability = AccessoriesCapability.get(living);
        if (capability == null) {
            return false;
        }
        var container = capability.getContainers().get(baubleType);
        for (var accessory : container.getAccessories()) {
            var stack = accessory.getSecond();
            var item = stack.getItem();
            if (expectedItemType.isInstance(item)) {
                if (operation.accept(new EquippedBaubleSlot(baubleType, accessory.getFirst()), stack, (T) stack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }
}
