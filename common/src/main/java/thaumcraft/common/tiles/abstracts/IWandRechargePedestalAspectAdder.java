package thaumcraft.common.tiles.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.annotations.forvalue.VisAmount;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICentiVisContainerItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

@SuppressWarnings("unchecked")
@UtilityLikeAbstraction(reason = "easy overriding")
public interface IWandRechargePedestalAspectAdder {
    //return remaining amount
    default @VisAmount int addCentiVisForContainer(Aspect toAdd, int centiVisAmount, ItemStack containerStack, ICentiVisContainerItem<? extends Aspect> container, CentiVisList<? extends Aspect> requiringCentiVis){
        int remainingCentiVis = ((ICentiVisContainerItem<Aspect>)container).addCentiVis(containerStack,toAdd,centiVisAmount);
        int addedCentiVis = centiVisAmount - remainingCentiVis;
        ((AspectList<Aspect>)requiringCentiVis).reduceAndRemoveIfNotPositive(toAdd,addedCentiVis);
        return Math.floorDiv(remainingCentiVis,CENTIVIS_MULTIPLIER);
    }
}
