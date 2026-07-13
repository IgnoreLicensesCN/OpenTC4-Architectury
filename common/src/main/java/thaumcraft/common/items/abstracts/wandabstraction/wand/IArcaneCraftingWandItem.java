package thaumcraft.common.items.abstracts.wandabstraction.wand;

import net.minecraft.world.item.ItemStack;

//which can be used for arcane crafting
public interface IArcaneCraftingWandItem {

    //yeah you can just call it
    default boolean canInsertIntoArcaneCraftingTable(ItemStack wandStack){
        return true;
    };
}
