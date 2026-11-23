package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.lib.utils.InventoryUtils;

import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.getOrCreateAndSetTickMarkFrom;

public class GolemSortingCorePlaceItemStackToHomeGoal extends GolemPlaceItemStackToHomeGoalWithItemStackTickMark {
    protected GolemHelper.ContainersWithRoomIterator containersWithRoomIterator = new GolemHelper.ContainersWithRoomIterator(golem);

    public GolemSortingCorePlaceItemStackToHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected boolean canUseAdditional() {
        return !findSomethingSortCore(this.golem, this.golem.getMainHandItem(),containersWithRoomIterator);
    }

    public static Marker findSomethingSortCoreReturnMarker(BaseGolemEntity golem, ItemStack itemToMatch, GolemHelper.ContainersWithRoomIterator iterator) {
        iterator.resetWith(itemToMatch);
        while (iterator.hasNext()) {
            var info = iterator.next();
            var be = info.blockEntityRef.get();
            var container = info.containerRef.get();
            var marker = info.marker;
            if (be == null || container == null || be.isRemoved()) {
                continue;
            }
            if (InventoryUtils.inventoryContains(container, itemToMatch, marker.getSide(), golem.getItemStackMatcher())){
                return marker;
            }
        }
        return null;
    }
    public static boolean findSomethingSortCore(BaseGolemEntity golem, ItemStack itemToMatch,GolemHelper.ContainersWithRoomIterator iterator) {
        if (findSomethingSortCoreReturnMarker(golem, itemToMatch, iterator) != null) {
            return true;
        }

        getOrCreateAndSetTickMarkFrom(
                itemToMatch,
                golem.getId(),
                golem.tickCount + Config.CONFIG_INSTANCE.getGolemIgnoreDelayTicks()
        );
        return false;
    }
}
