package thaumcraft.common.items.abstracts;

import net.minecraft.world.item.ItemStack;

public interface IAlchemicalFurnaceSpeeder {
    default boolean canSpeedUpAlchemicalFurnace(ItemStack stack){
        return true;
    };
}
