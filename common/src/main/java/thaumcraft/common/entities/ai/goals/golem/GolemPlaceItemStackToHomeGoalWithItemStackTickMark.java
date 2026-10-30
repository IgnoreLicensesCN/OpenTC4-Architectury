package thaumcraft.common.entities.ai.goals.golem;

import lombok.AllArgsConstructor;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableItemStack.opentc4$getCachedObjectFromItemStack;
import static com.linearity.opentc4.mixinaccessors.IObjectCacheableItemStack.opentc4$removeCachedObjectFromItemStack;

public class GolemPlaceItemStackToHomeGoalWithItemStackTickMark extends GolemPlaceItemStackToHomeGoal {

    public GolemPlaceItemStackToHomeGoalWithItemStackTickMark(BaseGolemEntity golem) {
        super(golem);
    }


    @AllArgsConstructor
    public static class ItemStackPlaceToHomeTickMarkFakeFields {
        public int golemID;
        public int placeBeforeTickCount;
    }
    @Override
    protected boolean checkForExecution(ItemStack stackInHand) {
        var mark = opentc4$getCachedObjectFromItemStack(stackInHand, ItemStackPlaceToHomeTickMarkFakeFields.class);
        if (mark != null){
            if (golem.tickCount < mark.placeBeforeTickCount && golem.getId() == mark.golemID){
                return true;
            }
            opentc4$removeCachedObjectFromItemStack(stackInHand, ItemStackPlaceToHomeTickMarkFakeFields.class);
        }
        return canUseAdditional();
    }

    protected boolean canUseAdditional(){
        //case 1:
        //                  return !GolemHelper.findSomethingEmptyCore(this.theGolem, this.theGolem.getCarried());
        //               case 8:
        //                  return !GolemHelper.findSomethingUseCore(this.theGolem, this.theGolem.getCarried());
        //               case 10:
        //                  return !GolemHelper.findSomethingSortCore(this.theGolem, this.theGolem.getCarried());
        return false;
    }
}
