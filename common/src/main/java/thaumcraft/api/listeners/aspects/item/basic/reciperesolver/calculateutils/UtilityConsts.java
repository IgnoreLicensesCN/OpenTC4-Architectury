package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public class UtilityConsts {
    public static final  Function<ItemStack,ItemStack> VANILLA_RETURN_ITEMS = stack -> {
        if (stack == null || stack.isEmpty()){
            return ItemStack.EMPTY;
        }
        var item = stack.getItem();
        if (!item.hasCraftingRemainingItem()){
            return ItemStack.EMPTY;
        }
        var count = stack.getCount();
        var remainingItem = item.getCraftingRemainingItem();
        if (remainingItem == null){
            return ItemStack.EMPTY;
        }
        var returnStack = item.getCraftingRemainingItem().getDefaultInstance();
        returnStack.setCount(count);
        return returnStack;
    };

    public static final List<Function<ItemStack,ItemStack>> VANILLA_RETURN_ITEMS_LIST = new SameValueList<>(VANILLA_RETURN_ITEMS);
    public static final List<List<Function<ItemStack,ItemStack>>> VANILLA_RETURN_ITEMS_LIST_LIST = new SameValueList<>(VANILLA_RETURN_ITEMS_LIST);

}
