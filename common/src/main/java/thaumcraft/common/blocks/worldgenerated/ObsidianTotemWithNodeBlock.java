package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.node.ObsidianTotemNodeBlockEntity;
import thaumcraft.common.tiles.node.SilverWoodKnotNodeBlockEntity;

public class ObsidianTotemWithNodeBlock extends ObsidianTotemBlock {
    public ObsidianTotemWithNodeBlock(Properties properties) {
        super(properties);
    }
    public ObsidianTotemWithNodeBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
                .randomTicks()
                .lightLevel(s -> 7)
        );
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        var bEntity = serverLevel.getBlockEntity(blockPos);
        if (bEntity instanceof ObsidianTotemNodeBlockEntity node){
            node.serverTickByBlockHandle();
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        var bEntity = level.getBlockEntity(blockPos);
        if (bEntity instanceof ObsidianTotemNodeBlockEntity node){
            node.clientTickByBlockHandle();
        }
    }
}
