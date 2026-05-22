package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//which can use wand focus
//you should drive focus yourself
public interface IWandFocusEngineItem {
    default boolean canApplyFocus(){
        return true;
    };

    @NotNull("null -> empty")
    ItemStack getFocusItemStack(ItemStack wand);
    ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus);
}
