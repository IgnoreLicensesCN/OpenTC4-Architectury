package thaumcraft.common.entities.goals.golem.transportresources.itemstackinteraction;

import it.unimi.dsi.fastutil.ints.IntIterator;
import lombok.AllArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;

import java.util.Iterator;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.goals.golem.transportresources.itemstackinteraction.GolemSortingCorePlaceItemStackToHomeGoal.findSomethingSortCore;
import static thaumcraft.common.entities.golems.GolemHelper.*;

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
