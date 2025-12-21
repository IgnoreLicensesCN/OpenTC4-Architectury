package thaumcraft.common.tiles.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class ObsidianTotemNodeBlockEntity extends AbstractNodeBlockEntity {
    public ObsidianTotemNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.OBSIDIAN_TOTEM_NODE, blockPos, blockState);
    }

    public ObsidianTotemNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public boolean nodeLockApplicable() {
        return false;
    }

    @Override
    public void removeNode() {
        if (this.level != null) {
            this.level.setBlock(this.getBlockPos(), ThaumcraftBlocks.OBSIDIAN_TOTEM.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
