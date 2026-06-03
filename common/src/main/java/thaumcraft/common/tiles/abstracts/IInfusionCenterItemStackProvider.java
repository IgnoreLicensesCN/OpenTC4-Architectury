package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

//oh even more AE2 tricks can be performed like "provide all items needed in a container"
public interface IInfusionCenterItemStackProvider extends Container {
    default ItemStack getCenterStack(){
        return getItem(0);
    }
    default void setCenterStack(ItemStack stack){
        setItem(0,stack);
    }
}
