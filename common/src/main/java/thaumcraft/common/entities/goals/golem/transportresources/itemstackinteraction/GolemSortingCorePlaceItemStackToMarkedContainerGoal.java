package thaumcraft.common.entities.goals.golem.transportresources.itemstackinteraction;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

public class GolemSortingCorePlaceItemStackToMarkedContainerGoal extends GolemPlaceItemStackToMarkedContainerGoal {
    public GolemSortingCorePlaceItemStackToMarkedContainerGoal(BaseGolemEntity golem) {
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
}
