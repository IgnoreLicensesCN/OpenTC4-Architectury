package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import com.linearity.opentc4.mixinattachments.IObjectCacheableFakeFieldOwner;
import lombok.AllArgsConstructor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.mixinattachments.IObjectCacheableFakeFieldOwner.*;
import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.getTickMarkFrom;
import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.removeTickMarkFrom;
import static thaumcraft.common.entities.golems.GolemHelper.getItemsNeeded;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY;

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

        public static boolean isOnTimeOut(BaseGolemEntity golem, ItemStack stack) {
            var fakeField = opentc4$getCachedObjectFrom(
                    stack,
                    ItemStackSortingTickMarkFakeField.class);
            if (fakeField == null) {
                return false;
            }
            return fakeField.golemID == golem.getId() && fakeField.placeBeforeTickCount < golem.tickCount;
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

    protected final List<ItemStack> defaultCanUseCache = new ArrayList<>(2);
    protected boolean canUseAdditional(){
        defaultCanUseCache.clear();
        getItemsNeeded(
                golem.inventory.iterator(),
                golem,
                golem.configurationProvidersView.contains(MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY)
                        ? MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY.provider.getBooleanFrom(golem)
                        : MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY.provider.defaultValue
                ,defaultCanUseCache,
                (g,stack) -> golem.getItemStackMatcher().test(stack,golem.getMainHandItem()),
                1
        );
        return !defaultCanUseCache.isEmpty();
    }
}
