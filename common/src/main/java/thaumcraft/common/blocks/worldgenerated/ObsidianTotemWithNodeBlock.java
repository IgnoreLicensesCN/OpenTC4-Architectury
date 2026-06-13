package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.nodes.INodeBlock;
import thaumcraft.client.lib.UtilsFXMigrated;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.node.ObsidianTotemNodeBlockEntity;

import static thaumcraft.common.blocks.abstracts.AbstractNodeBlock.nodeBlockOnRemove;

public class ObsidianTotemWithNodeBlock extends ObsidianTotemBlock implements EntityBlock, INodeBlock {
    public ObsidianTotemWithNodeBlock(Properties properties) {
        super(properties);
    }

    public static final SoundType TOTEM_SOUND = new SoundType(
            1.0F, // volume
            1.0F, // pitch
            ThaumcraftSounds.CRAFT_FAIL, // break
            SoundEvents.STONE_STEP, // step
            SoundEvents.STONE_PLACE, // place
            SoundEvents.STONE_HIT, // hit
            SoundEvents.STONE_FALL  // fall
    );
    public ObsidianTotemWithNodeBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
                .randomTicks()
                .sound(TOTEM_SOUND)
                .lightLevel(s -> 7).explosionResistance(999)
        );
    }

//    @Override
//    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
//        var bEntity = serverLevel.getBlockEntity(blockPos);
//        if (bEntity instanceof ObsidianTotemNodeBlockEntity node){
//            node.serverRandomTickByBlockHandle();
//        }
//    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        var bEntity = level.getBlockEntity(blockPos);
        if (bEntity instanceof ObsidianTotemNodeBlockEntity node){
            node.clientAnimateTickByBlockHandle();
        }
    }


    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        nodeBlockOnRemove(state, level, pos, newState, isMoving);
        super.onRemove(state, level, pos, newState, isMoving);
    }
    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide() && level.random.nextBoolean()) {
            UtilsFXMigrated.infusedStoneSparkle(level, blockPos.getX(),blockPos.getY(),blockPos.getZ(), 0);
        }
    }

    @Override
    public Item asItem() {
        return ThaumcraftItemInstances.OBSIDIAN_TOTEM();
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ObsidianTotemNodeBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ThaumcraftBlockEntities.BlockEntityTypeInstances.OBSIDIAN_TOTEM_NODE()){
            return null;
        }
        if (level.isClientSide){
            return null;
        }
        return (level1, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof AbstractNodeBlockEntity abstractNodeBlockEntity) {
                AbstractNodeBlockEntity.serverTick(abstractNodeBlockEntity);
            }
        };
    }

    @Override
    public boolean preventAttackFromAnotherNode() {
        return true;
    }
}
