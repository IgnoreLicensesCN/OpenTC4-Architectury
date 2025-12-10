package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

//which can use wand focus
public interface WandFocusEngine {
    default boolean canApplyFocus(){
        return true;
    };

    ItemStack getFocusItemStack(ItemStack wand);
    ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus);
}
