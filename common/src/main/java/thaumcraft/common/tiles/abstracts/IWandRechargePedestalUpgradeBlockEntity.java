package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.wands.ICentiVisContainerItem;

public interface IWandRechargePedestalUpgradeBlockEntity extends IWandRechargePedestalAspectAdder {
    default int additionalMaxDrainAmountOnce(){
        return 0;
    }

    //if not zero,return how much metAspect is consumed(do not greater than given amount)
    //if add aspect, modify both container and requiringAspects to reduce (de)serialization
    int onDrainingMeetAspect(Aspect metAspect, int metCentiVisAmount, ItemStack containerStack, ICentiVisContainerItem<? extends Aspect> container, AspectList<? extends Aspect> requiringAspects);

    //if (aspect != null && !aspect.isPrimal()) {
    //                              AspectList<Aspect>primals = ResearchManager.reduceToPrimals((new LinkedHashAspectList<>()).addAll(aspect, 1));
    //
    //                              for(Aspect aspect2 : aspectRequiring.getAspects()) {
    //                                 if (primals.get(aspect2) > 0 && node.getAspects().get(aspect) > min) {
    //                                    wand.addCentiVis(this.getStackInSlot(0), aspect2, 1, true);
    //                                    node.takeFromContainer(aspect, 1);
    //                                    this.somethingChanged = true;
    //                                    this.draining = true;
    //                                    if ((Platform.getEnvironment() == Env.CLIENT)) {
    //                                       this.drainX = co.posX;
    //                                       this.drainY = co.posY;
    //                                       this.drainZ = co.posZ;
    //                                       this.drainColor = aspect.getColor();
    //                                    }
    //                                    break drainVisLabel;
    //                                 }
    //                              }
    //                           }

}
