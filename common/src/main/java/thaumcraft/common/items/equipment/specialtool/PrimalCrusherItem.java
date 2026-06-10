package thaumcraft.common.items.equipment.specialtool;

import com.google.common.collect.MapMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.abstracts.IDowsingTool;
import thaumcraft.common.items.abstracts.ISwordLikeItem;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.PRIMAL_CRUSHER_MINEABLE;

//TODO:[maybe wont finished]"Vote in democracy"(third times?) to change it's feature like axe or hoe mineable?
public class PrimalCrusherItem extends DiggerItem implements ISwordLikeItem, IWarpingGear, IDowsingTool {
    public PrimalCrusherItem(float f, float g, Tier tier, TagKey<Block> tagKey, Properties properties) {
        super(f, g, tier, tagKey, properties);
    }
    public PrimalCrusherItem() {
        this(
                3.5F,
                -2.4F,
                ThaumcraftItems.ToolAndArmorMaterial.PRIMAL_VOID,
                PRIMAL_CRUSHER_MINEABLE,
                new Properties().stacksTo(1).rarity(Rarity.EPIC)
        );
    }
    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        itemStack.hurtAndBreak(1, livingEntity2, livingEntityx -> livingEntityx.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (entity != null && !level.isClientSide && entity.tickCount % 20 == 0) {
            itemStack.setDamageValue(itemStack.getDamageValue() - 1);
        }
    }

    @Override
    public int getWarp(ItemStack itemstack, @Nullable Entity entityEquipped) {
        return 2;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addWarpTooltip(itemStack, level, list, tooltipFlag);
    }

    public static final BlockPos[][] blockPosOffsetsOfDirection = new BlockPos[Direction.values().length][];
    static {
        blockPosOffsetsOfDirection[Direction.UP.ordinal()] = new BlockPos[]{
                new BlockPos(1,0,1),new BlockPos(1,0,0),new BlockPos(1,0,-1),

                new BlockPos(0,0,1),                            new BlockPos(0,0,-1),

                new BlockPos(-1,0,1),new BlockPos(-1,0,0),new BlockPos(-1,0,-1)
        };
        blockPosOffsetsOfDirection[Direction.DOWN.ordinal()] = blockPosOffsetsOfDirection[Direction.UP.ordinal()];
        blockPosOffsetsOfDirection[Direction.NORTH.ordinal()] = new BlockPos[]{
                new BlockPos(1,1,0),new BlockPos(1,0,0),new BlockPos(1,-1,0),
                new BlockPos(0,1,0),                            new BlockPos(0,-1,0),
                new BlockPos(-1,1,0),new BlockPos(-1,0,0),new BlockPos(-1,-1,0),
        };
        blockPosOffsetsOfDirection[Direction.SOUTH.ordinal()] = blockPosOffsetsOfDirection[Direction.NORTH.ordinal()];
        blockPosOffsetsOfDirection[Direction.WEST.ordinal()] = new BlockPos[]{
                new BlockPos(0,1,1),new BlockPos(0,1,0),new BlockPos(0,1,-1),
                new BlockPos(0,0,1),                            new BlockPos(0,0,-1),
                new BlockPos(0,-1,1),new BlockPos(0,-1,0),new BlockPos(0,-1,-1),
        };
        blockPosOffsetsOfDirection[Direction.WEST.ordinal()] = blockPosOffsetsOfDirection[Direction.EAST.ordinal()];
    }


    public static final Set<ItemStack> stacksBreakingNear = Collections.newSetFromMap(new MapMaker().weakKeys().makeMap());
    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (!level.isClientSide && !stacksBreakingNear.contains(itemStack) && !(livingEntity.isShiftKeyDown())) {
            stacksBreakingNear.add(itemStack);
            var dir = livingEntity.getDirection();
            var additionalToMine = blockPosOffsetsOfDirection[dir.ordinal()];
            for (var offset : additionalToMine) {
                var targetPos = blockPos.offset(offset);
                var targetState = level.getBlockState(targetPos);
                if (!targetState.isAir() && targetState.is(PRIMAL_CRUSHER_MINEABLE)){
                    if (livingEntity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.gameMode.destroyBlock(targetPos);
                    }
                }
            }
        }
        stacksBreakingNear.remove(itemStack);
        return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
    }
}
