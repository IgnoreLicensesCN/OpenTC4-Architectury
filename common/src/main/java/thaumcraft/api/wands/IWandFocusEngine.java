package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//which can use wand focus
public interface IWandFocusEngine {
    default boolean canApplyFocus(){
        return true;
    };

    @NotNull("null -> empty")
    ItemStack getFocusItemStack(ItemStack wand);
    ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus);
}
