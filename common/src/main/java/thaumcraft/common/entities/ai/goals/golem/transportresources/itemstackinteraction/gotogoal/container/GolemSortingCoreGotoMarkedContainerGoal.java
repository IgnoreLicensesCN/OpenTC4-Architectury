package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.gotogoal.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemDoSomethingToMarkedContainerGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

public class GolemSortingCoreGotoMarkedContainerGoal extends GolemDoSomethingToMarkedContainerGoal {
    public GolemSortingCoreGotoMarkedContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    protected boolean validItemStackForContainer(ItemStack is, BlockEntity be, Container container, Direction interactionDirection) {
        return super.validItemStackForContainer(is, be, container, interactionDirection)
                && InventoryUtils.inventoryContains(
                container,
                is,
                interactionDirection,
                golem.getItemStackMatcher()
        );
    }

    @Override
    public void start() {
        super.start();
        var be = usingBlockEntity.get();
        if (be != null) {
            var navi = golem.getNavigation();
            navi.moveTo(navi.createPath(be.getBlockPos(),1),golem.getMoveControl().getSpeedModifier());
        }else {
            remainingTicks = 0;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && golem.getNavigation().isInProgress();
    }
}
