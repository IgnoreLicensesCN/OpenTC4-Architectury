package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.wands.ICentiVisContainerItem;
import thaumcraft.api.wands.IWandComponentsOwnerItem;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.AbstractPedestalBlockEntity;
import thaumcraft.common.tiles.abstracts.IWandRechargePedestalAspectAdder;
import thaumcraft.common.tiles.abstracts.IWandRechargePedestalUpgradeBlock;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.RECHARGE_PEDESTAL_CANNOT_APPLY;
import static thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity.ALL_NODES;

public class WandRechargePedestalBlockBlockEntity extends AbstractPedestalBlockEntity implements IWandRechargePedestalAspectAdder {
    public WandRechargePedestalBlockBlockEntity(BlockEntityType<? extends WandRechargePedestalBlockBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public WandRechargePedestalBlockBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.WAND_RECHARGE_PEDESTAL, blockPos, blockState);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return itemStack.getItem() instanceof ICentiVisContainerItem<? extends Aspect> && !itemStack.is(RECHARGE_PEDESTAL_CANNOT_APPLY);
    }

    protected int tickCount = System.identityHashCode(this) & 63;

    public void serverTick() {
        if (this.level == null) {
            return;
        }
        tickCount += 1;
        if (this.tickCount % 5 != 0) {
            return;
        }
        var fillingStack = getItem(0);
        if (fillingStack.is(RECHARGE_PEDESTAL_CANNOT_APPLY)) {
            return;
        }
        if (fillingStack.getItem() instanceof ICentiVisContainerItem<? extends Aspect> centiVisContainerItem) {
            var centiVisRequiring = centiVisContainerItem.getAspectsWithRoomRemaining(fillingStack);
            if (centiVisRequiring.isEmpty()) {
                return;
            }

            var lookup = ALL_NODES.get(level);
            if (lookup != null) {
                var selfPos = getBlockPos();

                boolean nodeHarmful = centiVisContainerItem instanceof IWandComponentsOwnerItem componentsOwnerItem &&
                        componentsOwnerItem.isWandNodeHarmful(fillingStack);
                int minAmountToKeep = nodeHarmful ? 0 : 1;

                IWandRechargePedestalUpgradeBlock upgradeBlock;
                BlockPos upgradePos = selfPos.above();
                BlockState upgradeBlockState = level.getBlockState(upgradePos);
                if (upgradeBlockState.getBlock() instanceof IWandRechargePedestalUpgradeBlock upgrader) {
                    upgradeBlock = upgrader;
                } else {
                    upgradeBlock = null;
                }
                int maxDrainAmount;
                if (upgradeBlock != null) {
                    maxDrainAmount = 1 + upgradeBlock.additionalMaxDrainAmountOnce(level, upgradePos, upgradeBlockState);
                } else {
                    maxDrainAmount = 1;
                }
                boolean drained = lookup.forItemsNearPosWithBreakWithRange(
                        selfPos,
                        nodeBE -> {
                            //"Oh the nodes we've cached"--here we go again(consuming nodeBE)
                            var nodeBEPos = nodeBE.getBlockPos();
                            if (
                                    Math.abs(nodeBEPos.getX() - selfPos.getX()) > 8
                                            || Math.abs(nodeBEPos.getY() - selfPos.getY()) > 8
                                            || Math.abs(nodeBEPos.getZ() - selfPos.getZ()) > 8
                            ) {
                                return false;
                            }
                            AspectList<Aspect> aspConsumed = new LinkedHashAspectList<>();
                            var nodeBEAspects = nodeBE.getAspects();
                            boolean consumedAspect = nodeBEAspects.forEachWithBreak(
                                    (asp, amount) -> {
                                        int canUseAmount = amount - minAmountToKeep;
                                        if (centiVisRequiring.containsKey(asp)) {
                                            int remainingAmount =
                                                    addCentiVisForContainer(
                                                            asp,
                                                            Math.min(canUseAmount, maxDrainAmount) * CENTIVIS_MULTIPLIER,
                                                            fillingStack,
                                                            centiVisContainerItem,
                                                            centiVisRequiring
                                                    );
                                            if (upgradeBlock != null) {
                                                remainingAmount -=
                                                        upgradeBlock.onDrainingMeetAspectRequired(
                                                                level,
                                                                upgradePos,
                                                                upgradeBlockState,
                                                                asp,
                                                                remainingAmount,
                                                                fillingStack,
                                                                centiVisContainerItem,
                                                                centiVisRequiring,
                                                                nodeBE
                                                        );
                                            }
                                            int consumed = canUseAmount - remainingAmount;
                                            if (consumed != 0) {
                                                drainedAspect(asp, consumed);
                                                aspConsumed.addAll(asp, consumed);
                                                return true;
                                            }
                                        } else if (upgradeBlock != null) {
                                            int consumed = upgradeBlock.onDrainingMeetAspectNotRequired(
                                                    level,
                                                    upgradePos,
                                                    upgradeBlockState,
                                                    asp,
                                                    canUseAmount,
                                                    fillingStack,
                                                    centiVisContainerItem,
                                                    centiVisRequiring,
                                                    nodeBE
                                            );
                                            if (consumed != 0) {
                                                drainedAspect(asp, consumed);
                                                aspConsumed.addAll(asp, consumed);
                                                return true;
                                            }
                                        }
                                        return false;
                                    }
                            );
                            aspConsumed.forEach(nodeBEAspects::reduceAndRemoveIfNotPositive);

                            return consumedAspect;
                        },
                        8
                );
                if (drained) {
                    onDraining();
                } else {
                    onNotDraining();
                }
            }
        }
    }

    //mixin point?
    protected void drainedAspect(Aspect aspect, int drained) {
        //TODO:Client Side rendering field
        //public boolean draining = false;
        //   public int drainX = 0;
        //   public int drainY = 0;
        //   public int drainZ = 0;
        //   public int drainColor = 0;
    }

    protected void onDraining() {

    }

    protected void onNotDraining() {

    }
}
