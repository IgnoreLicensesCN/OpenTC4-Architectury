package thaumcraft.common.blocks.technique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.blocks.worldgenerated.eldritch.EldritchObeliskBlock;
import thaumcraft.common.tiles.eldritch.EldritchPortalBlockEntity;

public class EldritchPortalBlock extends SuppressedWarningBlock implements EntityBlock {
    public EldritchPortalBlock(Properties properties) {
        super(properties);
    }
    public EldritchPortalBlock(){
        this(
                Properties.of()
                        .strength(-1,200000)
                        .noCollission()
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EldritchPortalBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EldritchPortalBlockEntity portal){
                    portal.serverTick();
                }
            });
        }
        return ((level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof EldritchPortalBlockEntity portal){
                portal.clientTick();
            }
        });
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide){
            if (level.getBlockState(pos.below()).getBlock() != ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_PORTAL()
                    || !(level.getBlockState(pos.above()).getBlock() instanceof EldritchObeliskBlock)
            ){
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }
}
