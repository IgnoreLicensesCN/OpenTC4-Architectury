package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.gotogoal.container;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.GolemTakeItemStackFromContainerGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.function.BiPredicate;

import static com.linearity.opentc4.Consts.getEmptyWeakRef;
import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.golems.GolemHelper.getAmountNeededSmart;
import static thaumcraft.common.entities.golems.GolemHelper.getColorsMatching;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.PRECISE_OR_ANY_AMOUNT_CONFIG_KEY;

public class GolemGotoContainerForFillingGoal extends AbstractGolemGotoFurthestContainerGoal {
    protected @NotNull ContainersWithGoodsIterator containersWithGoodsIterator = new ContainersWithGoodsIterator(golem);
    protected final ByteSet colorsTemp = new ByteArraySet(17);

    public GolemGotoContainerForFillingGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public boolean canUse() {
        if (this.golem.getMainHandItem().isEmpty()
                && this.golem.tickCount % getGolemGoalDelay() <= 0
                && this.golem.hasSomethingInInventoryOrHand()) {
            var iter = getMissingItemsIterator();
            iter.reset();

            while (iter.hasNext()) {
                var itemMissing = iter.next();
                if (itemMissing.isEmpty()) {
                    continue;
                }

                colorsTemp.clear();
                getColorsMatching(colorsTemp, golem, itemMissing);
                containersWithGoodsIterator.reset();
                BlockPos closestContainerPos = getFurthestContainerPos(containersWithGoodsIterator,containersWithGoodsIterator,colorsTemp);

                if (closestContainerPos != null) {
                    {
                        var fakeFields = opentc4$getOrCreateCachedObjectFrom(
                                golem,
                                GolemTakeItemStackFromContainerGoal.GolemTakeItemStackFromContainerWatchingItemFakeFields.class,
                                GolemTakeItemStackFromContainerGoal.GolemTakeItemStackFromContainerWatchingItemFakeFields::new
                        );
                        fakeFields.watchedItem = itemMissing;
                    }
                    setDest(closestContainerPos);
                    {
                        iter.reset();
                        return true;
                    }
                }

            }
            iter.reset();
            return false;
        }
        return false;
    }

    public static class ContainersWithGoodsIterator implements Iterator<GolemHelper.ContainerInfo>, GolemHelper.Resettable {
        protected final BaseGolemEntity golem;
        protected GolemHelper.MarkedContainerAndMarkerIterator markedIterator;
        protected GolemHelper.ContainerInfo resultCache = new GolemHelper.ContainerInfo();
        protected GolemHelper.ContainerInfo resultCacheOut = new GolemHelper.ContainerInfo();
        public byte color = 0;

        public ContainersWithGoodsIterator(BaseGolemEntity golem) {
            this.golem = golem;
            this.markedIterator = new GolemHelper.MarkedContainerAndMarkerIterator(golem, (what, ever) -> true);
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
            if (resultCache.verify()) {
                return true;
            }
            var golemStackMatcher = golem.getItemStackMatcher();
            while (markedIterator.hasNext()) {
                var markedContainerInfo = markedIterator.next();
                var markedContainer = markedContainerInfo.containerRef.get();
                if (markedContainer == null) {
                    continue;
                }
                var marker = markedContainerInfo.marker;
                var side = marker.side.getOpposite();
                for (var stackInGolem : golem.inventory) {
                    if (stackInGolem.isEmpty()) {
                        continue;
                    }
                    if (InventoryUtils.extractStack(markedContainer, stackInGolem, side, golemStackMatcher, false) != null) {
                        resultCache.setTo(markedContainerInfo);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public GolemHelper.ContainerInfo next() {
            if (!hasNext()) {
                throw new IllegalStateException("no next! call hasNext() first!");
            }
            resultCacheOut.setTo(resultCache);
            resultCache.reset();
            return resultCacheOut;
        }
    }

    protected MissingItemsIterator getMissingItemsIterator() {
        if (/*golem.getToggles()[0]*/
                golem.configurationProviders.contains(PRECISE_OR_ANY_AMOUNT_CONFIG_KEY)
                        && PRECISE_OR_ANY_AMOUNT_CONFIG_KEY.provider.getBooleanFrom(golem)
        ) {//Any amount
            return missingItemsIteratorForAnyAmount;
        }
        return missingItemsIteratorForPreciseAmount;
    }

    protected @NotNull MissingItemsIteratorForPreciseAmount missingItemsIteratorForPreciseAmount = new MissingItemsIteratorForPreciseAmount(golem);
    protected @NotNull MissingItemsIteratorForAnyAmount missingItemsIteratorForAnyAmount = new MissingItemsIteratorForAnyAmount(golem);

    public static abstract class MissingItemsIterator implements Iterator<ItemStack>, GolemHelper.Resettable {
        protected @NotNull WeakReference<ItemStack> result = getEmptyWeakRef();
        public boolean alreadyHaveResult() {
            return result.get() != null;
        }

        @Override
        public ItemStack next() {
            var resultStack = result.get();
            if (resultStack == null){
                if (!hasNext()) {
                    throw new IllegalStateException("call hasNext() first");
                }
                resultStack = result.get();
            }
            result = getEmptyWeakRef();
            if (resultStack == null) {
                resultStack = ItemStack.EMPTY;
//                throw new IllegalStateException("call hasNext() first");
            }
            return resultStack;
        }
    }

    public static class MissingItemsIteratorForPreciseAmount extends MissingItemsIterator {
        protected final BaseGolemEntity golem;
        protected int inventoryIndex;

        public MissingItemsIteratorForPreciseAmount(BaseGolemEntity golem) {
            this.golem = golem;
            reset();
        }

        public void reset() {
            inventoryIndex = 0;
        }

        @Override
        public boolean hasNext() {
            if (alreadyHaveResult()){
                return true;
            }
            var containerAtHome = getHomeContainer();
            if (containerAtHome == null) {
                return false;
            }

            boolean fuzzyMatch = fuzzyMatch();
            var golemStackMatcher = golem.getItemStackMatcher();
            for (; inventoryIndex < golem.inventory.size(); inventoryIndex++) {
                var toCheckInGolemInventory = golem.inventory.get(inventoryIndex);

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

                    ItemStack ret = ((ExtendedItemStack) (Object) toCheckInGolemInventory).opentc4$copyAndShareTag();
                    ret.setCount(ret.getCount() - foundAmount);
                    inventoryIndex += 1;
                    result = new WeakReference<>(ret);
                    return true;
                }

            }
            return false;

        }
        protected int getFoundAmount(
                Container containerAtHome,
                ItemStack toCheckInGolemInventory,
                int foundAmount,
                boolean fuzzyMatch,
                BiPredicate<ItemStack, ItemStack> golemStackMatcher
        ) {
            if (containerAtHome instanceof WorldlyContainer worldlyContainer /*&& facing.ordinal() > -1*/) {
                int[] canAccessSlots = worldlyContainer.getSlotsForFace(golem.homeDirection.getOpposite());

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
            var bePos = golem.getHomeAttachedBlockPos();
            var be = getExistingBlockEntity(golem.level(), bePos);
            if (be instanceof Container container) {
                return container;
            }
            return null;
        }
    }

    public static class MissingItemsIteratorForAnyAmount extends MissingItemsIterator {
        protected final BaseGolemEntity golem;
        protected int inventoryIndex;

        public MissingItemsIteratorForAnyAmount(BaseGolemEntity golem) {
            this.golem = golem;
            reset();
        }

        public void reset() {
            inventoryIndex = 0;
        }

        @Override
        public boolean hasNext() {
            if (result.get() != null) {
                return true;
            }
            int size = golem.inventory.size();
            for (;inventoryIndex < size; inventoryIndex++) {
                ItemStack toCheck = golem.inventory.get(inventoryIndex);
                if (!toCheck.isEmpty()) {
                    result = new WeakReference<>(toCheck);
                    inventoryIndex += 1;//skip this since extracted
                    return true;
                }
            }
            return false;
        }
    }

}
