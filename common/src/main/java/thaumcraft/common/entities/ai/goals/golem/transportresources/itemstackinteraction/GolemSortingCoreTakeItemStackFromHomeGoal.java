package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import it.unimi.dsi.fastutil.ints.IntIterator;
import lombok.AllArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.isOnTimeOut;
import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemSortingCorePlaceItemStackToHomeGoal.findSomethingSortCore;
import static thaumcraft.common.entities.golems.GolemHelper.*;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY;

public class GolemSortingCoreTakeItemStackFromHomeGoal extends GolemTakeItemStackFromHomeGoal {

    public GolemSortingCoreTakeItemStackFromHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }

    public static class WorldlyContainerItemStackIterator extends ContainerItemStackIterator{
        public @NotNull Direction direction;
        public WorldlyContainerItemStackIterator(@NotNull WorldlyContainer container, @NotNull Direction direction) {
            super(container,new IntArrayIterator(container.getSlotsForFace(direction)));
            this.direction = direction;
        }
    }

    @AllArgsConstructor
    public static class ContainerItemStackIterator implements Iterator<ItemStack> {
        public @NotNull Container container;
        protected @NotNull IntIterator iterator;
        public ContainerItemStackIterator(@NotNull Container container) {
            this(container,new IntRangeIterator(container.getContainerSize()));
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public ItemStack next() {
            return container.getItem(iterator.nextInt());
        }
    }
    @Override
    protected @NotNull Iterator<ItemStack> getItemsToSearchAsNeeded() {
        if (getExistingBlockEntity(golem.level(),golem.getHomeAttachedBlockPos()) instanceof Container container) {
            if (container instanceof WorldlyContainer worldlyContainer) {
                return new WorldlyContainerItemStackIterator(worldlyContainer,golem.homeDirection);
            }
            return new ContainerItemStackIterator(container);
        }
        return super.getItemsToSearchAsNeeded();
    }

    protected GolemHelper.ContainersWithRoomIterator containersWithRoomIterator = new GolemHelper.ContainersWithRoomIterator(golem);
    @Override
    protected boolean findSomething(ItemStack extracted) {
        return findSomethingSortCore(golem,golem.getMainHandItem(),containersWithRoomIterator);
    }
}
