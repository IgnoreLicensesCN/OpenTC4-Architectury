package thaumcraft.common.tiles.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class SilverWoodKnotNodeBlockEntity extends AbstractNodeBlockEntity {
    public SilverWoodKnotNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.SILVERWOOD_KNOT_NODE, blockPos, blockState);
    }

    public SilverWoodKnotNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }



    @Override
    public boolean nodeLockApplicable() {
        return false;
    }

    @Override
    public void removeNode() {
        if (this.level != null) {
            this.level.setBlock(this.getBlockPos(), ThaumcraftBlocks.SILVERWOOD_LOG.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
