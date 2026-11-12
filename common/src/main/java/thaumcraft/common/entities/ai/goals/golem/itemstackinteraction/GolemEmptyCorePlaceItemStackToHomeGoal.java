package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.*;
import static thaumcraft.common.entities.golems.GolemHelper.getColorsMatching;

public class GolemEmptyCorePlaceItemStackToHomeGoal extends GolemPlaceItemStackToHomeGoalWithItemStackTickMark {

    protected GolemHelper.ContainersWithRoomIterator containersWithRoomIterator = new GolemHelper.ContainersWithRoomIterator(golem);
    public GolemEmptyCorePlaceItemStackToHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }
    protected boolean canUseAdditional(){
      return !findSomethingEmptyCore(this.golem, this.golem.getMainHandItem(),containersWithRoomIterator);
    }

    public static class FindSomethingEmptyCoreFakeField implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<BaseGolemEntity> {
        public final ByteSet colorsTemp = new ByteArraySet(17);
    }
    public static boolean findSomethingEmptyCore(
            BaseGolemEntity golem,
            ItemStack itemToMatch,
            GolemHelper.ContainersWithRoomIterator containersWithRoomIterator) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,FindSomethingEmptyCoreFakeField.class,FindSomethingEmptyCoreFakeField::new);
        fakeField.colorsTemp.clear();
        getColorsMatching(fakeField.colorsTemp,golem,itemToMatch);

        double golemRangeSq = golem.getHomeRange();
        golemRangeSq *= golemRangeSq;
        var golemHomeBEPos = golem.getHomeAttachedBlockPos();
//        for(byte color : fakeField.colorsTemp) {
            containersWithRoomIterator.resetWith(itemToMatch/*,color*/);
            while (containersWithRoomIterator.hasNext()){
                var containerInfo = containersWithRoomIterator.next();
                var container = containerInfo.containerRef == null ? null : containerInfo.containerRef.get();
                var containerBE = containerInfo.blockEntityRef == null ? null : containerInfo.blockEntityRef.get();
                if (container == null || containerBE == null || containerBE.isRemoved()) {
                    continue;
                }
                var marker = containerInfo.marker;
                if (!marker.colorEqualsFuzzy(/*color*/fakeField.colorsTemp)) {
                    continue;
                }
                BlockPos containerBEPos = containerBE.getBlockPos();
                double distance = containerBEPos.distToCenterSqr(golem.position());
                if (distance <= golemRangeSq && !containerBEPos.equals(golemHomeBEPos)) {
                    containersWithRoomIterator.reset();
                    return true;
                }
            }
            containersWithRoomIterator.reset();
//        }
        var fakeFieldToSetTickDelay = opentc4$getCachedObjectFrom(itemToMatch, ItemStackSortingTickMarkFakeFields.class);
        if (fakeFieldToSetTickDelay == null){
            fakeFieldToSetTickDelay =  new ItemStackSortingTickMarkFakeFields(
                    golem.getId(),
                    golem.tickCount + Config.CONFIG_INSTANCE.getGolemIgnoreDelayTicks()
            );
            opentc4$putCachedObjectTo(itemToMatch, ItemStackSortingTickMarkFakeFields.class,fakeFieldToSetTickDelay);
        }
        return false;
    }
}
