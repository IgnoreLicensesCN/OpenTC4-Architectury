package thaumcraft.common.entities.ai.goals.golem;

import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableEntity.opentc4$getCachedObjectFromEntity;
import static thaumcraft.common.entities.golems.GolemHelper.*;

public class GolemTakeItemStackFromContainerGoal extends Goal {
    public static class GolemTakeItemStackFromContainerWatchingItemFakeFields {
        public @NotNull ItemStack watchedItem = ItemStack.EMPTY;
    }

    protected int cooldown = 0;
    protected final BaseGolemEntity golem;
    public GolemTakeItemStackFromContainerGoal(BaseGolemEntity golem){
        this.golem = golem;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!golem.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        var fakeFields = opentc4$getCachedObjectFromEntity(golem, GolemTakeItemStackFromContainerWatchingItemFakeFields.class);
        if (fakeFields == null) {
            return false;
        }
        if (fakeFields.watchedItem.isEmpty()) {
            return false;
        }
        if (golem.inventory.stream().allMatch(ItemStack::isEmpty)) {
            return false;
        }
        return golem.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        return cooldown > 0;
    }

    private final List<BlockEntity> containersTempList = new ArrayList<>();
    private final ByteSet colorsTemp = new ByteArraySet(17);
    private final Set<Direction> sidesTemp = new ObjectArraySet<>(6);

    private void clearCachesForStarting(){
        containersTempList.clear();
        colorsTemp.clear();
        sidesTemp.clear();
    }

    @Override
    public void start() {
        super.start();

        var homePos = this.golem.getRestrictCenter();
        var facing = golem.homeDirection;
        var homeAttachedBlockPos = homePos.relative(facing.getOpposite());
        clearCachesForStarting();
        var fakeFields = opentc4$getCachedObjectFromEntity(golem, GolemTakeItemStackFromContainerWatchingItemFakeFields.class);
        if (fakeFields == null) {
            return;
        }
        ItemStack watchedItem = fakeFields.watchedItem;
        if (watchedItem.isEmpty()) {
            return;
        }
        getMarkedContainersWithinDefaultRangeAsBlockEntity(containersTempList,golem);
        for (var containerBE : this.containersTempList) {
            if (!(containerBE instanceof Container container)) {
                continue;
            }
            BlockPos containerPos = containerBE.getBlockPos();
            if (!containerPos.equals(homeAttachedBlockPos)) {
                getColorsMatching(colorsTemp,golem, watchedItem);
                for (var color:colorsTemp) {
                    getMarkedSides(sidesTemp,golem,containerBE,color);
                    for (var side:sidesTemp){
                        ItemStack target = watchedItem.copy();
                        target.setCount(Math.min(target.getCount(), Math.min(this.golem.getCarryPower(),target.getMaxStackSize())));// = this.theGolem.getToggles()[0] ? this.theGolem.getCarrySpace() : Math.min(target.stackSize, this.theGolem.getCarrySpace());
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
                            this.golem.setItemInHand(InteractionHand.MAIN_HAND,result.split(result.getCount()));

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
                            return;
                        }
                    }
                }
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        cooldown--;
    }
}
