package thaumcraft.common.tiles.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class NodeBlockEntity extends AbstractNodeBlockEntity {
    public NodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.AURA_NODE(), blockPos, blockState);
    }

    public NodeBlockEntity(BlockEntityType<? extends NodeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public boolean nodeLockApplicable() {
        return true;
    }

    @Override
    public void removeNode() {
        if (this.level != null) {
            level.destroyBlock(getBlockPos(), true);
        }
    }
}
