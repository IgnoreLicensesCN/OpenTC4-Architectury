package thaumcraft.common.tiles.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class NodeBlockEntity extends AbstractNodeBlockEntity {
    public NodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.AURA_NODE, blockPos, blockState);
    }

    public NodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public boolean nodeLockApplicable() {
        return true;
    }

    @Override
    public void removeNode() {
        if (this.level != null) {
            this.level.setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NodeBlockEntity(blockPos, blockState);
    }
}
