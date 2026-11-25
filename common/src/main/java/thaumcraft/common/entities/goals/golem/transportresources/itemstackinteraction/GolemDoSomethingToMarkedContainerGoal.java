package thaumcraft.common.entities.goals.golem.transportresources.itemstackinteraction;

import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.lang.ref.WeakReference;

import static com.linearity.opentc4.Consts.getEmptyWeakRef;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.golems.GolemHelper.DEFAULT_DISTANCE_PREDICATE;

public abstract class GolemDoSomethingToMarkedContainerGoal extends AbstractGolemGoal {
    protected int remainingTicks = 0;
    public GolemDoSomethingToMarkedContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected GolemHelper.MarkedContainerAndMarkerIterator markedContainerAndMarkerIterator = new GolemHelper.MarkedContainerAndMarkerIterator(golem,DEFAULT_DISTANCE_PREDICATE);
    protected final ByteSet colorsCache = new ByteArraySet(17);
    protected @Nullable Marker usingMarker = null;
    protected @NotNull WeakReference<BlockEntity> usingBlockEntity = getEmptyWeakRef();
    @Override
    public boolean canUse() {
        usingMarker = null;
        var golemHoldingStack = golem.getMainHandItem();
        if (golemHoldingStack.isEmpty()) {
            return false;
        }
        if (!golem.getNavigation().isDone()){
            return false;
        }
        var homeBEPos = golem.getHomeAttachedBlockPos();

        getColorsMatching(golemHoldingStack);

        markedContainerAndMarkerIterator.reset();
        while (markedContainerAndMarkerIterator.hasNext()) {
            var info = markedContainerAndMarkerIterator.next();
            var container = info.containerRef.get();
            var be = info.blockEntityRef.get();
            if (container == null || be==null || be.isRemoved()) { continue;}
            var marker = info.marker;
            if (homeBEPos.equals(marker.getPos())){continue;}
            if (marker.colorEqualsFuzzy(colorsCache)){
                ItemStack is = InventoryUtils.placeItemStackIntoInventory(golemHoldingStack, container, marker.getSide(), false);
                if (!ItemStack.isSameItem(is, golemHoldingStack) && validItemStackForContainer(is,be,container,marker.getSide())) {
                    usingMarker = marker;
                    usingBlockEntity = new WeakReference<>(be);
                    markedContainerAndMarkerIterator.reset();
                    return true;
                }
            }
        }
        markedContainerAndMarkerIterator.reset();
        return false;
    }

    protected boolean validItemStackForContainer(ItemStack is, BlockEntity be, Container container, Direction interactionDirection) {
        return true;
    }

    protected void getColorsMatching(ItemStack golemHoldingStack) {
        colorsCache.clear();
        GolemHelper.getColorsMatching(colorsCache,golem, golemHoldingStack);
    }

    @Override
    public boolean canContinueToUse() {
        if (remainingTicks == 0){
            return false;
        }
        return verifyMarkerAndBE();
    }

    protected boolean verifyMarkerAndBE() {
        if (!(
                usingMarker != null
                        && getExistingBlockEntity(golem.level(), usingMarker.getPos()) == usingBlockEntity.get()
                        && usingBlockEntity.get() instanceof BlockEntity blockEntity
                        && !blockEntity.isRemoved()
                        && blockEntity instanceof Container container
        )){
            return false;
        }
        var mainHandStack = golem.getMainHandItem();
        return !mainHandStack.isEmpty()
                && !ItemStack.isSameItem(
                InventoryUtils.placeItemStackIntoInventory(mainHandStack, container, usingMarker.getSide(), false),
                mainHandStack
        );
    }

    @Override
    public void tick() {
        super.tick();
        remainingTicks -=1;
    }

    @Override
    public void start() {
        super.start();
        remainingTicks = getMaxTickCountRemaining();
    }
}
