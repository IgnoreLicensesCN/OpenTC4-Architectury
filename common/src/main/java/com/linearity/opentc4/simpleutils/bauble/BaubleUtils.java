package com.linearity.opentc4.simpleutils.bauble;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.linearity.opentc4.OpenTC4.platformUtils;

//since bauble separated into two parts it has migrated to platformUtils.
//however i dont want to remove it
//--ignoreLicensesCN
public class BaubleUtils {

    /**
     * iterate through every bauble itemstack of a player
     * @param player the victim
     * @param operation what will be done for every itemstack,return true inside to break the loop
     * @return whether the loop is broken by {@link BaubleConsumer#accept(EquippedBaubleSlot, ItemStack, Item)} returning true.
     */
    public static boolean forEachBauble(Player player,BaubleConsumer<Item> operation) {

        return platformUtils.forEachBauble(player, operation);
    }

    /**
     * iterate through every bauble itemstack of a player,
     * only item class meets {@code expectedItemType} will be accepted by {@link BaubleConsumer#accept(EquippedBaubleSlot, ItemStack, Item)}
     * @param player the victim
     * @param expectedItemType class to judge item type,judge with it's method {@link Class#isAssignableFrom(Class)}.
     *                         e.g. expectedItemType.isAssignableFrom(itemstack.getItem().getClass())
     * @param operation what will be done for every itemstack,return true inside to break the loop
     * @return whether the loop is broken by {@link BaubleConsumer#accept(EquippedBaubleSlot, ItemStack, Item)} returning true.
     */
    public static <T extends Item> boolean forEachBauble(Player player,Class<T> expectedItemType, BaubleConsumer<T> operation) {

        return platformUtils.forEachBauble(player,expectedItemType, operation);
    }

    /**
     * see {@link BaubleUtils#forEachBauble(Player, BaubleConsumer)},but it checks specific bauble type
     */
    public static boolean forEachBaubleWithBaubleType(String baubleType, Player player, BaubleConsumer<Item> operation) {
        return platformUtils.forEachBaubleWithType(baubleType, player, operation);
    }


    /**
     * see {@link BaubleUtils#forEachBauble(Player, Class, BaubleConsumer)},but it checks specific bauble type
     */
    public static <T extends Item> boolean forEachBaubleWithBaubleType(String baubleType,Player player,Class<T> expectedItemType, BaubleConsumer<T> operation) {
        return platformUtils.forEachBaubleWithType(baubleType, player,expectedItemType, operation);
    }
    public static String[] listBaubleTypes(LivingEntity livingEntity){
        return platformUtils.listBaubleTypes(livingEntity);
    };
}
