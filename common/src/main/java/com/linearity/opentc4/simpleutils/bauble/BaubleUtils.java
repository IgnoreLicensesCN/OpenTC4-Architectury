package simpleutils.bauble;

import baubles.api.BaublesApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;



import static baubles.api.expanded.BaubleExpandedSlots.getCurrentlyRegisteredTypes;
import static baubles.api.expanded.BaubleExpandedSlots.getIndexesOfAssignedSlotsOfType;


public class BaubleUtils {

    /**
     * iterate through every bauble itemstack of a player
     * @param player the victim
     * @param operation what will be done for every itemstack,return true inside to break the loop
     * @return whether the loop is broken by {@link BaubleConsumer#accept(int, ItemStack, Object)} returning true.
     */
    public static boolean forEachBauble(Player player,BaubleConsumer<Item> operation) {
        for(String baubleType:getCurrentlyRegisteredTypes()) {
            if (forEachBaubleWithBaubleType(baubleType,player,operation)){
                return true;
            }
        }
        return false;
    }

    /**
     * iterate through every bauble itemstack of a player,
     * only item class meets {@code expectedItemType} will be accepted by {@link BaubleConsumer#accept(int, ItemStack, Object)}
     * @param player the victim
     * @param expectedItemType class to judge item type,judge with it's method {@link Class#isAssignableFrom(Class)}.
     *                         e.g. expectedItemType.isAssignableFrom(itemstack.getItem().getClass())
     * @param operation what will be done for every itemstack,return true inside to break the loop
     * @return whether the loop is broken by {@link BaubleConsumer#accept(int, ItemStack, Object)} returning true.
     */
    public static <T> boolean forEachBauble(Player player,Class<T> expectedItemType, BaubleConsumer<T> operation) {
        for(String baubleType:getCurrentlyRegisteredTypes()) {
            if (forEachBaubleWithBaubleType(baubleType,player,expectedItemType,operation)){
                return true;
            }
        }
        return false;
    }

    /**
     * see {@link BaubleUtils#forEachBauble(Player, BaubleConsumer)},but it checks specific bauble type
     */
    public static boolean forEachBaubleWithBaubleType(String baubleType, Player player, BaubleConsumer<Item> operation) {
        Container baubles = BaublesApi.getBaubles(player);

        for (int a:getIndexesOfAssignedSlotsOfType(baubleType)){
            ItemStack stack = baubles.getStackInSlot(a);
            if (stack == null) {continue;}
            if (operation.accept(a,stack, stack.getItem())){
                return true;
            }
        }
        return false;
    }


    /**
     * see {@link BaubleUtils#forEachBauble(Player, Class, BaubleConsumer)},but it checks specific bauble type
     */
    public static <T> boolean forEachBaubleWithBaubleType(String baubleType,Player player,Class<T> expectedItemType, BaubleConsumer<T> operation) {
        Container baubles = BaublesApi.getBaubles(player);

        for (int a:getIndexesOfAssignedSlotsOfType(baubleType)){
            ItemStack stack = baubles.getStackInSlot(a);
            if (stack == null) {continue;}
            Item stackItem = stack.getItem();
            if (expectedItemType.isAssignableFrom(stackItem.getClass())) {
                if (operation.accept(a,stack, (T) stackItem)){
                    return true;
                }
            }
        }
        return false;
    }
}
