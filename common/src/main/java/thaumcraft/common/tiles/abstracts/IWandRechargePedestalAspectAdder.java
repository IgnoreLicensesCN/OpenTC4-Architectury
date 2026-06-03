package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.wands.ICentiVisContainerItem;

@SuppressWarnings("unchecked")
public interface IWandRechargePedestalAspectAdder {
    //return remaining amount
    default int addAspectForContainer(Aspect toAdd, int centiVisAmount, ItemStack containerStack, ICentiVisContainerItem<? extends Aspect> container, AspectList<? extends Aspect> requiringAspects){
        int remaining = ((ICentiVisContainerItem<Aspect>)container).addCentiVis(containerStack,toAdd,centiVisAmount);
        int added = centiVisAmount - remaining;
        ((AspectList<Aspect>)requiringAspects).reduceAndRemoveIfNotPositive(toAdd,added);
        return remaining;
    }
}
