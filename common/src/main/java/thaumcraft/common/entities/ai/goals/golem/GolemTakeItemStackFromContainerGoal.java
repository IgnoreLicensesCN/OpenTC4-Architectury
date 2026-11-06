package thaumcraft.common.entities.ai.goals.golem;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.EnumSet;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getCachedObjectFrom;
import static thaumcraft.common.entities.golems.GolemHelper.*;
import static thaumcraft.common.items.golem.upgrade.core.FillGolemCoreItem.AMOUNT_CONFIG_KEY;

public class GolemTakeItemStackFromContainerGoal extends Goal {
    public static class GolemTakeItemStackFromContainerWatchingItemFakeFields {
        public @NotNull ItemStack watchedItem = ItemStack.EMPTY;
    }

    protected int cooldown = 0;
    protected final BaseGolemEntity golem;

    public GolemTakeItemStackFromContainerGoal(BaseGolemEntity golem) {
        this.golem = golem;
        this.containerIterator = new MarkedContainerAndMarkerIterator(golem, DEFAULT_DISTANCE_PREDICATE);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!golem.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        var fakeFields = opentc4$getCachedObjectFrom(golem, GolemTakeItemStackFromContainerWatchingItemFakeFields.class);
        if (fakeFields == null) {
            return false;
        }
        if (fakeFields.watchedItem.isEmpty()) {
            return false;
        }
        if (!golem.hasSomethingInInventoryOrHand()) {
            return false;
        }
        return golem.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        return cooldown > 0;
    }

    //    private final List<BlockEntity> containersTempList = new ArrayList<>();
    protected @NotNull MarkedContainerAndMarkerIterator containerIterator;
    private final ByteSet colorsTemp = new ByteArraySet(17);
//    private final Set<Direction> sidesTemp = new ObjectArraySet<>(6);

    private void clearCachesForStarting() {
//        containersTempList.clear();
        colorsTemp.clear();
//        sidesTemp.clear();
    }

    @Override
    public void start() {
        super.start();

        var homePos = this.golem.getRestrictCenter();
        var facing = golem.homeDirection;
        var homeAttachedBlockPos = homePos.relative(facing.getOpposite());
        clearCachesForStarting();
        var fakeFields = opentc4$getCachedObjectFrom(golem, GolemTakeItemStackFromContainerWatchingItemFakeFields.class);
        if (fakeFields == null) {
            return;
        }
        ItemStack watchedItem = fakeFields.watchedItem;
        if (watchedItem.isEmpty()) {
            return;
        }
//        getMarkedContainersWithinDefaultRangeAsBlockEntity(containersTempList,golem);

        boolean countLimited = false;
        var configProvider = golem.configurationProviders.contains(AMOUNT_CONFIG_KEY) ? AMOUNT_CONFIG_KEY.provider : null;
        if (configProvider != null) {
            countLimited = configProvider.getBooleanFrom(golem);
        }
        containerIterator.reset();

        while (containerIterator.hasNext()) {
            var containerInfo = containerIterator.next();
            var container = containerInfo.container == null ? null : containerInfo.container.get();
            var containerBE = containerInfo.blockEntity == null ? null : containerInfo.blockEntity.get();
            if (containerBE == null || container == null) {
                continue;
            }

            var marker = containerInfo.marker;
            var side = marker.side.getOpposite();

            BlockPos containerPos = containerBE.getBlockPos();
            if (!containerPos.equals(homeAttachedBlockPos)) {
                getColorsMatching(colorsTemp, golem, watchedItem);
                for (var color : colorsTemp) {
                    if (!marker.equalsFuzzy(containerBE.getBlockPos(), containerBE.getLevel().dimension().location(), null, color)) {
                        continue;
                    }

                    ItemStack target = watchedItem.copy();
                    target.setCount(Math.min(target.getCount(),
                            countLimited
                                    ? Math.min(this.golem.getCarryPower(), target.getMaxStackSize())
                                    : this.golem.getCarryPower()));
                    ItemStack result = InventoryUtils.extractStack(
                            container,
                            target,
                            side,
                            golem.getItemStackMatcher(),
                            true);
//                        if (result == null && InventoryUtils.getDoubleChest(tile) != null) {
//                            result = InventoryUtils.extractStack(InventoryUtils.getDoubleChest(tile), target, side, this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT(), true);
//                        }

                    if (result != null) {
                        this.golem.setItemInHand(InteractionHand.MAIN_HAND, ExtendedItemStack.splitAll(result));

//                            try {
//                                if (Config.golemChestInteract) {
//                                    this.inv.openInventory();//TODO:[maybe wont finished]Container interaction
//                                }
//                            } catch (Exception ignored) {
//                            }
                        golem.level().gameEvent(golem, GameEvent.CONTAINER_OPEN, containerPos);
                        golem.level().gameEvent(golem, GameEvent.CONTAINER_CLOSE, containerPos);

//                            this.countChest = 5;
                        this.cooldown = 200;
                        fakeFields.watchedItem = ItemStack.EMPTY;
                        {
                            containerIterator.reset();
                            return;
                        }
                    }
                }
            }
        }

        containerIterator.reset();
    }

    @Override
    public void tick() {
        super.tick();
        cooldown--;
    }
}
