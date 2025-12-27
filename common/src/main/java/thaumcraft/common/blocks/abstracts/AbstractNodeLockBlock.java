package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.api.nodes.INodeLock;

public abstract class AbstractNodeLockBlock extends Block implements INodeLock {
    public AbstractNodeLockBlock(Properties properties) {
        super(properties);
    }
    public AbstractNodeLockBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(3.f,25.f)
        );
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();//less calc,no light-blocking
    }
}
