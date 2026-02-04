package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

//which can be used for arcane crafting
public interface IArcaneCraftingWand {

    //yeah you can just call it
    default boolean canInsertIntoArcaneCraftingTable(ItemStack wandStack){
        return true;
    };
}
