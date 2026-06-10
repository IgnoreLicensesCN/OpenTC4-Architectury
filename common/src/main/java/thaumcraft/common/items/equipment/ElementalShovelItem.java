package thaumcraft.common.items.equipment;

import com.google.common.collect.MapMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;

import java.util.*;

import static thaumcraft.common.items.ThaumcraftItems.ToolAndArmorMaterial.TOOL_THAUMIUM_ELEMENTAL;
import static thaumcraft.common.items.equipment.specialtool.PrimalCrusherItem.blockPosOffsetsOfDirection;

public class ElementalShovelItem extends ShovelItem {

    public ElementalShovelItem() {
        super(TOOL_THAUMIUM_ELEMENTAL, 3, -2.8f, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    protected ItemStack getRequiredItemFromInventory(Item item, Inventory inventory) {
        var matchedStack = ItemStack.EMPTY;
        for (var matchingStack : inventory.items) {
            if (!matchingStack.isEmpty() && matchingStack.getItem() == item) {
                matchedStack = matchingStack;
                break;
            }
        }
        return matchedStack;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel serverLevel)) {
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        Player player = context.getPlayer();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        InteractionHand hand = context.getHand();
        ItemStack toolStack = context.getItemInHand();
        boolean isCrouching = player.isCrouching();

        boolean didPlace = false;

        if (!isCrouching && world.getBlockEntity(pos) == null) {
            BlockState clickedState = world.getBlockState(pos);
            Block clickedBlock = clickedState.getBlock();
            var item = clickedBlock.asItem();
            if (item == Items.AIR) {
                return InteractionResult.PASS;
            }
            var inventory = player.getInventory();

            BlockPos offsetPos = pos.relative(face);
            for (var offset : blockPosOffsetsOfDirection[face.ordinal()]) {

                var matchedStack = getRequiredItemFromInventory(item, inventory);
                if (matchedStack.isEmpty()) {
                    break;
                }
                if (toolStack.isEmpty()) {
                    break;
                }

                BlockPos targetPos = offsetPos.offset(offset);
                BlockState targetState = world.getBlockState(targetPos);
                Block targetBlock = targetState.getBlock();

                if (targetState.isAir()
                        || targetState.canBeReplaced()
                        || targetBlock instanceof BushBlock
                        || targetBlock == Blocks.WATER
                ) {

                    world.setBlock(targetPos, clickedState, 3);
                    toolStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand));
                    matchedStack.shrink(1);
                    didPlace = true;
                    new PacketFXBlockSparkleS2C(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0x803200).sendToAllAround(serverLevel, targetPos, 8 * 8);

                }
            }
        }

        return didPlace ? InteractionResult.sidedSuccess(world.isClientSide) : InteractionResult.PASS;
    }


//    private boolean isEffectiveAgainst(Block block) {
//        for (Block value : isEffective) {
//            if (value == block) {
//                return true;
//            }
//        }
//
//        return false;
//    }

//    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, Player player) {
//        HitResult HitResult = BlockUtils.getTargetBlock(player.level(), player, true);
//        if (HitResult != null && HitResult.typeOfHit == MovingObjectType.BLOCK) {
//            this.side = HitResult.sideHit;
//        }
//
//        return super.onBlockStartBreak(itemstack, X, Y, Z, player);
//    }

//    public boolean onBlockDestroyed(ItemStack stack, World world, Block bi, int x, int y, int z, EntityLivingBase ent) {
//        if (ent.isSneaking()) {
//            return super.onBlockDestroyed(stack, world, bi, x, y, z, ent);
//        } else {
//            if (Platform.getEnvironment() != Env.CLIENT) {
//                int md = world.getBlockMetadata(x, y, z);
//                if (ForgeHooks.isToolEffective(stack, bi, md) || this.isEffectiveAgainst(bi)) {
//                    for (int aa = -1; aa <= 1; ++aa) {
//                        for (int bb = -1; bb <= 1; ++bb) {
//                            int xx = 0;
//                            int yy = 0;
//                            int zz = 0;
//                            if (this.side <= 1) {
//                                xx = aa;
//                                zz = bb;
//                            } else if (this.side <= 3) {
//                                xx = aa;
//                                yy = bb;
//                            } else {
//                                zz = aa;
//                                yy = bb;
//                            }
//
//                            if (!(ent instanceof Player) || world.canMineBlock((Player) ent, x + xx, y + yy, z + zz)) {
//                                Block bl = world.getBlock(x + xx, y + yy, z + zz);
//                                md = world.getBlockMetadata(x + xx, y + yy, z + zz);
//                                if (bl.getBlockHardness(world, x + xx, y + yy, z + zz) >= 0.0F && (ForgeHooks.isToolEffective(stack, bl, md) || this.isEffectiveAgainst(bl))) {
//                                    stack.damageItem(1, ent);
//                                    BlockUtils.harvestBlock(world, (Player) ent, x + xx, y + yy, z + zz, true, 3);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            return true;
//        }
//    }

    private static final Set<ItemStack> stackMiningBlocks = Collections.newSetFromMap(new MapMaker().weakKeys().makeMap());
    @Override
    public boolean mineBlock(ItemStack toolStack, Level world, BlockState minedState, BlockPos pos, LivingEntity living) {
        if (living.isShiftKeyDown()) {
            return super.mineBlock(toolStack, world, minedState, pos, living);
        }

        if (!world.isClientSide) {
            if (minedState.is(BlockTags.MINEABLE_WITH_SHOVEL) && !stackMiningBlocks.contains(toolStack)) {
                stackMiningBlocks.add(toolStack);

                Direction digDir = getDigDirection(living);
                for (var offset : blockPosOffsetsOfDirection[digDir.ordinal()]) {
                    if (toolStack.isEmpty()) {
                        break;
                    }

                    BlockPos targetPos = pos.offset(offset);

                    if (living instanceof Player player) {
                        if (!world.mayInteract(player, targetPos)) continue;
                    }

                    BlockState targetState = world.getBlockState(targetPos);

                    if (targetState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        destroyBlockWithDrops(world, targetPos, living);
                        toolStack.hurtAndBreak(1, living, e -> e.broadcastBreakEvent(living.getUsedItemHand()));

                    }
                }
                stackMiningBlocks.remove(toolStack);
            }
        }

        return super.mineBlock(toolStack, world, minedState, pos, living);
    }


    private Direction getDigDirection(LivingEntity living) {
        if (living.getXRot() > 45) return Direction.DOWN;
        if (living.getXRot() < -45) return Direction.UP;

        return living.getDirection();
    }
    private void destroyBlockWithDrops(Level world, BlockPos pos, LivingEntity living) {
        BlockState st = world.getBlockState(pos);
        if (st.getDestroySpeed(world, pos) >= 0 && !st.isAir()) {
            world.destroyBlock(pos, true, living);
        }
    }

}
