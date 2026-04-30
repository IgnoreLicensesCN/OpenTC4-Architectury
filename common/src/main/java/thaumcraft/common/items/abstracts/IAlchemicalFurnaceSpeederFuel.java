package thaumcraft.common.items.abstracts;

import net.minecraft.world.item.ItemStack;

public interface IAlchemicalFurnaceSpeederFuel {
    default boolean canSpeedUpAlchemicalFurnace(ItemStack stack){
        return true;
    };
}
