package thaumcraft.common.entities.ai.goals.golem;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.Iterator;
import java.util.function.BiPredicate;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.config.Config.CONFIG_INSTANCE;
import static thaumcraft.common.entities.golems.GolemHelper.*;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY;
import static thaumcraft.common.items.golem.upgrade.core.FillGolemCoreItem.AMOUNT_CONFIG_KEY;

public class GolemGotoContainerForFillingGoal extends Goal {
    protected final BaseGolemEntity golem;
    protected @NotNull ContainersWithGoodsIterator containersWithGoodsIterator;
    protected int tickRemaining = 0;
    protected BlockPos dest;
    
    protected final ByteSet colorsTemp = new ByteArraySet(17);

    public GolemGotoContainerForFillingGoal(BaseGolemEntity golem) {
        this.golem = golem;
        this.containersWithGoodsIterator = new ContainersWithGoodsIterator(golem);
        this.missingItemsIterableForPreciseAmount = new MissingItemsIterableForPreciseAmount(golem);
        this.missingItemsIterableForAnyAmount = new MissingItemsIterableForAnyAmount(golem);
    }


    @Override
    public boolean canContinueToUse() {
        return this.tickRemaining > 0 && !this.golem.getMoveControl().hasWanted();
    }

    @Override
    public void tick() {
        --this.tickRemaining;
        if (this.tickRemaining == 0
                && golem.position().equals(this.prevPos)
        ) {
            //back to where near home
            var homePos = golem.getRestrictCenter();//RandomPositionGenerator.findRandomTarget(this.theGolem, 2, 1);
            this.tickRemaining = 20;
            var moveControl = this.golem.getMoveControl();
            moveControl.setWantedPosition(
                    homePos.getX() + 0.5,
                    homePos.getY() + 0.5,
                    homePos.getZ() + 0.5,
                    moveControl.getSpeedModifier()
            );
        }
        super.tick();
    }

    @Override
    public void stop() {
        super.stop();
        this.tickRemaining = 0;
    }

    @Override
    public void start() {
        super.start();
        this.tickRemaining = 200;
        this.prevPos = this.golem.position();
        var moveControl = this.golem.getMoveControl();
        moveControl.setWantedPosition(
                dest.getX() + 0.5,
                dest.getY() + 0.5,
                dest.getZ() + 0.5,
                moveControl.getSpeedModifier()
        );
    }

    protected @Nullable Vec3 prevPos = null;

    @Override
    public boolean canUse() {
        if (this.golem.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
                && this.golem.tickCount % CONFIG_INSTANCE.getGolemDelay() <= 0
                && this.golem.hasSomethingInInventoryOrHand()) {
            var iter = getMissingItemsIterator();
            iter.reset();
            while (iter.hasNext()) {
                var itemMissing = iter.next();
                {
                    var fakeFields = opentc4$getOrCreateCachedObjectFrom(
                            golem,
                            GolemTakeItemStackFromContainerGoal.GolemTakeItemStackFromContainerWatchingItemFakeFields.class,
                            GolemTakeItemStackFromContainerGoal.GolemTakeItemStackFromContainerWatchingItemFakeFields::new
                    );
                    fakeFields.watchedItem = itemMissing;
                    
                }
                colorsTemp.clear();
                getColorsMatching(colorsTemp,golem, itemMissing);
                for (byte color : colorsTemp) {
                    containersWithGoodsIterator.resetWith(color);
                    double rangeMin = Double.MAX_VALUE;
                    BlockPos closestContainerPos = null;
                    while (containersWithGoodsIterator.hasNext()) {
                        var container = containersWithGoodsIterator.next();
                        if (!(container instanceof BlockEntity blockEntity)){
                            continue;
                        }
                        {
                            var homePos = this.golem.getRestrictCenter();
                            var facing = golem.homeDirection;
                            var homeBEPos = homePos.relative(facing.getOpposite());
                            float dmod = this.golem.getHomeRange();
                            BlockPos containerPos = blockEntity.getBlockPos();
                            double distanceSqr = containerPos.distToCenterSqr(this.golem.position());
                            if (distanceSqr < rangeMin
                                    && distanceSqr <=  (dmod * dmod)
                                    && !containerPos.equals(homeBEPos)
                            ) {
                                rangeMin = distanceSqr;
                                closestContainerPos = containerPos;
                            }
                        }
                    }


                    if (closestContainerPos != null) {
                        var moveControl = golem.getMoveControl();
                        dest = closestContainerPos;
                        golem.getMoveControl().setWantedPosition(
                                dest.getX() + 0.5,
                                dest.getY() + 0.5,
                                dest.getZ() + 0.5,
                                moveControl.getSpeedModifier()
                        );
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    public static class ContainersWithGoodsIterator implements Iterator<Container>, GolemHelper.Resettable {
        protected BaseGolemEntity golem;
        protected GolemHelper.MarkedContainerAndMarkerIterator markedIterator;
        protected @Nullable Container nextContainer = null;
        public byte color = 0;
        public ContainersWithGoodsIterator(BaseGolemEntity golem) {
            this.golem = golem;
            this.markedIterator = new GolemHelper.MarkedContainerAndMarkerIterator(golem,(what,ever) -> true);
        }
        public void reset() {
            this.markedIterator.reset();
        }
        public void resetWith(byte color) {
            reset();
            this.color = color;
        }

        @Override
        public boolean hasNext() {
            if (nextContainer != null){
                return true;
            }
            var golemStackMatcher = golem.getItemStackMatcher();
            while (markedIterator.hasNext()){
                var markedContainerInfo = markedIterator.next();
                var markedContainer = markedContainerInfo.container;
                var marker = markedContainerInfo.marker;
                var side = marker.side.getOpposite();
                for (var stackInGolem:golem.inventory){
                    if (stackInGolem.isEmpty()){continue;}
                    if (InventoryUtils.extractStack(markedContainer, stackInGolem, side, golemStackMatcher, false) != null) {
                        nextContainer = markedContainer;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Container next() {
            if (!hasNext()) {
                throw new IllegalStateException("no next! call hasNext() first!");
            }
            var result = nextContainer;
            nextContainer = null;
            return result;
        }
    }

    protected MissingItemsIterator getMissingItemsIterator() {
        if (/*golem.getToggles()[0]*/
                golem.configurationProviders.contains(AMOUNT_CONFIG_KEY)
                        && AMOUNT_CONFIG_KEY.provider.getBooleanFrom(golem)
        ) {//Any amount
            return missingItemsIterableForAnyAmount;
        }
        return missingItemsIterableForPreciseAmount;
    }

    protected @NotNull MissingItemsIterableForPreciseAmount missingItemsIterableForPreciseAmount;
    protected @NotNull MissingItemsIterableForAnyAmount missingItemsIterableForAnyAmount;
    
    public static abstract class MissingItemsIterator implements Iterator<ItemStack>, GolemHelper.Resettable {
    }

    public static class MissingItemsIterableForPreciseAmount extends MissingItemsIterator {
        protected BaseGolemEntity golem;
        protected int inventoryIndex;

        public MissingItemsIterableForPreciseAmount(BaseGolemEntity golem) {
            this.golem = golem;
            reset();
        }

        public void reset() {
            inventoryIndex = 0;
        }

        @Override
        public boolean hasNext() {
            var containerAtHome = getHomeContainer();
            if (containerAtHome == null) {
                return false;
            }
            
            boolean fuzzyMatch = fuzzyMatch();
            var golemStackMatcher = golem.getItemStackMatcher();
            for (int i = inventoryIndex; i < golem.inventory.size(); i++) {
                var toCheckInGolemInventory = golem.inventory.get(i);
                inventoryIndex = i;

                if (!toCheckInGolemInventory.isEmpty()) {
                    int foundAmount = 0;
                    int amountNeeded = getAmountNeededSmart(
                            golem.inventory,
                            toCheckInGolemInventory,
                            fuzzyMatch);

                    foundAmount = getFoundAmount(containerAtHome, toCheckInGolemInventory, foundAmount, fuzzyMatch, golemStackMatcher);
                    if (foundAmount > amountNeeded) {
                        continue;
                    }
                    return true;
                }

            }
            return false;

        }

        @Override
        public ItemStack next() {
            var containerAtHome = getHomeContainer();
            if (containerAtHome == null) {
                throw new IllegalStateException("No more missing items,call hasNext() first");
            }

            boolean fuzzyMatch = fuzzyMatch();
            var golemStackMatcher = golem.getItemStackMatcher();
            for (int i = inventoryIndex; i < golem.inventory.size(); i++) {
                var toCheckInGolemInventory = golem.inventory.get(i);
                if (!toCheckInGolemInventory.isEmpty()) {
                    int foundAmount = 0;
                    int amountNeeded = getAmountNeededSmart(
                            golem.inventory,
                            toCheckInGolemInventory,
                            fuzzyMatch);

                    foundAmount = getFoundAmount(containerAtHome, toCheckInGolemInventory, foundAmount, fuzzyMatch, golemStackMatcher);
                    if (/*checkNext*/foundAmount > amountNeeded) {
                        continue;
                    }
                    inventoryIndex = i + 1;
                    ItemStack ret = ((ExtendedItemStack)(Object)toCheckInGolemInventory).opentc4$copyAndShareTag();
                    ret.setCount(ret.getCount() - foundAmount);
                    return ret;
//                        ItemStack ret = toCheckInGolemInventory.copy();
//                        ret.setCount(ret.getCount() - foundAmount);
//                        qr.add(ret);
                }

            }
            throw new IllegalStateException("No more missing items,call hasNext() first");
        }

        protected int getFoundAmount(
                Container containerAtHome,
                ItemStack toCheckInGolemInventory,
                int foundAmount,
                boolean fuzzyMatch,
                BiPredicate<ItemStack, ItemStack> golemStackMatcher
        ) {
            if (containerAtHome instanceof WorldlyContainer worldlyContainer /*&& facing.ordinal() > -1*/) {
                int[] canAccessSlots = worldlyContainer.getSlotsForFace(golem.homeDirection);

                for (int canAccessSlotIndex : canAccessSlots) {
                    ItemStack consideringStack = worldlyContainer.getItem(canAccessSlotIndex);
                    if (
                            golem.getItemStackMatcher().test(toCheckInGolemInventory, consideringStack)
                    ) {
                        foundAmount += consideringStack.getCount();
                        if (foundAmount >= getAmountNeededSmart(
                                golem.inventory,
                                toCheckInGolemInventory,
                                fuzzyMatch)
                        ) {
                            break;
                        }
                    }
                }
            } else {
                int containerSize = containerAtHome.getContainerSize();
                for (int l = 0; l < containerSize; ++l) {
                    ItemStack consideringStackInContainer = containerAtHome.getItem(l);
                    if (golemStackMatcher.test(consideringStackInContainer, toCheckInGolemInventory)) {
                        foundAmount += consideringStackInContainer.getCount();
                        if (foundAmount >= getAmountNeededSmart(golem.inventory, toCheckInGolemInventory, fuzzyMatch)) {
                            break;
                        }
                    }
                }
            }
            return foundAmount;
        }


        protected boolean fuzzyMatch() {
            return golem.configurationProviders.contains(MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY)
                    && MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY.provider.getBooleanFrom(golem);
        }

        protected @Nullable Container getHomeContainer() {
            var homePos = this.golem.getRestrictCenter();
            var facing = golem.homeDirection;
            var bePos = homePos.relative(facing.getOpposite());
            var be = getExistingBlockEntity(golem.level(), bePos);
            if (be instanceof Container container) {
                return container;
            }
            return null;
        }
    }

    public static class MissingItemsIterableForAnyAmount extends MissingItemsIterator {
        protected BaseGolemEntity golem;
        protected int inventoryIndex;

        public MissingItemsIterableForAnyAmount(BaseGolemEntity golem) {
            this.golem = golem;
            reset();
        }

        public void reset() {
            inventoryIndex = 0;
        }

        @Override
        public boolean hasNext() {
            for (var i = inventoryIndex; i < golem.inventory.size(); i++) {
                ItemStack toCheck = golem.inventory.get(i);
                inventoryIndex = i;
                if (!toCheck.isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public ItemStack next() {
            for (var i = inventoryIndex; i < golem.inventory.size(); i++) {
                ItemStack toCheck = golem.inventory.get(i);
                if (!toCheck.isEmpty()) {
                    inventoryIndex = i + 1;//skip this since extracted
                    return ((ExtendedItemStack)(Object)golem.inventory.get(i)).opentc4$copyAndShareTag();
                }
            }
            throw new IllegalStateException("No more missing items,call hasNext() first");
        }
    }

}
