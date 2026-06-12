package thaumcraft.common.items.abstracts;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainerItem;

public interface IEssentiaFuelProviderItem extends IAspectContainerItem<Aspect> {
    int getFuelEssentiaAmount(ItemStack itemStack,Aspect aspect);
    int getMaxFuelEssentiaAmount(ItemStack itemStack,Aspect aspect);
    //return consumed
    int consumeFuelEssentiaAmount(ItemStack itemStack,Aspect aspect,int toConsume);
    //return added
    int addFuelEssentiaAmount(ItemStack itemStack,Aspect aspect,int toAdd);
}
