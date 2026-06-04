package thaumcraft.common.tiles.abstracts;

import com.linearity.opentc4.annotations.forvalue.VisAmount;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICentiVisContainerItem;

public interface IWandRechargePedestalUpgradeBlock extends IWandRechargePedestalAspectAdder {
    default @VisAmount int additionalMaxDrainAmountOnce(Level atLevel, BlockPos pos, BlockState state){
        return 0;
    }

    //if not zero,return how much metAspect is consumed(do not greater than given amount)
    //if add aspect, modify both container and requiringAspects to reduce (de)serialization

    default @VisAmount int onDrainingMeetAspectNotRequired(
            Level atLevel,
            BlockPos pos,
            BlockState state,
            Aspect metAspect,
            /*node harmful limit calculated*/ int metVisAmountCanDrain,
            ItemStack containerStack,
            ICentiVisContainerItem<? extends Aspect> container,
            CentiVisList<? extends Aspect> requiringCentiVis,
            AbstractNodeBlockEntity nodeBE
    ){
        return 0;
    }

    default @VisAmount int onDrainingMeetAspectRequired(
            Level level,
            BlockPos pos,
            BlockState state,
            Aspect metAspect,
            int remainingAmount,
            ItemStack containerStack,
            ICentiVisContainerItem<? extends Aspect> container,
            CentiVisList<?> requiringCentiVis,
            AbstractNodeBlockEntity nodeBE
    ){
        return 0;
    }

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
