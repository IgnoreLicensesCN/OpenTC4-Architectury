package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import lombok.AllArgsConstructor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.*;
import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.getTickMarkFrom;
import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.removeTickMarkFrom;

public class GolemPlaceItemStackToHomeGoalWithItemStackTickMark extends GolemPlaceItemStackToHomeGoal {

    public GolemPlaceItemStackToHomeGoalWithItemStackTickMark(BaseGolemEntity golem) {
        super(golem);
    }


    @AllArgsConstructor
    public static class ItemStackSortingTickMarkFakeField implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<ItemStack> {
        public int golemID;
        public int placeBeforeTickCount;
        public static @Nullable ItemStackSortingTickMarkFakeField getTickMarkFrom(ItemStack stack){
            return opentc4$getCachedObjectFrom(
                    stack,
                    ItemStackSortingTickMarkFakeField.class);
        }
        public static @NotNull ItemStackSortingTickMarkFakeField getOrCreateAndSetTickMarkFrom(ItemStack stack, int golemID, int placeBeforeTickCount){
            var field = getTickMarkFrom(stack);
            if(field == null){
                field = new ItemStackSortingTickMarkFakeField(golemID,placeBeforeTickCount);
                setTickMarkTo(stack,field);
            }else {
                field.golemID = golemID;
                field.placeBeforeTickCount = placeBeforeTickCount;
            }
            return field;
        }
        public static void setTickMarkTo(ItemStack stack, ItemStackSortingTickMarkFakeField fakeField){
            opentc4$putCachedObjectTo(stack,ItemStackSortingTickMarkFakeField.class,fakeField);
        }
        public static void removeTickMarkFrom(ItemStack stack){
            opentc4$removeCachedObjectFrom(stack,ItemStackSortingTickMarkFakeField.class);
        }
    }
    @Override
    protected boolean checkForExecution(ItemStack stackInHand) {
        var mark = getTickMarkFrom(stackInHand);
        if (mark != null){
            if (golem.tickCount < mark.placeBeforeTickCount && golem.getId() == mark.golemID){
                return true;
            }
            removeTickMarkFrom(stackInHand);
        }
        return canUseAdditional();
    }

    protected boolean canUseAdditional(){
        //               case 8:
        //                  return !GolemHelper.findSomethingUseCore(this.theGolem, this.theGolem.getCarried());
        //               case 10:
        //                  return !GolemHelper.findSomethingSortCore(this.theGolem, this.theGolem.getCarried());
        return false;
    }
}
