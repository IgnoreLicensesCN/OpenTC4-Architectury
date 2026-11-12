package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import lombok.AllArgsConstructor;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getCachedObjectFrom;
import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$removeCachedObjectFrom;

public class GolemPlaceItemStackToHomeGoalWithItemStackTickMark extends GolemPlaceItemStackToHomeGoal {

    public GolemPlaceItemStackToHomeGoalWithItemStackTickMark(BaseGolemEntity golem) {
        super(golem);
    }


    @AllArgsConstructor
    public static class ItemStackSortingTickMarkFakeFields implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<ItemStack> {
        public int golemID;
        public int placeBeforeTickCount;
    }
    @Override
    protected boolean checkForExecution(ItemStack stackInHand) {
        var mark = opentc4$getCachedObjectFrom(stackInHand, ItemStackSortingTickMarkFakeFields.class);
        if (mark != null){
            if (golem.tickCount < mark.placeBeforeTickCount && golem.getId() == mark.golemID){
                return true;
            }
            opentc4$removeCachedObjectFrom(stackInHand, ItemStackSortingTickMarkFakeFields.class);
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
