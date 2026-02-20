package thaumcraft.common.blocks.technique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//tile.blockAiry.12
public class ImpassableBlock extends SuppressedWarningBlock {
    public ImpassableBlock(Properties properties) {
        super(properties);
    }
    public ImpassableBlock() {
        this(
                BlockBehaviour.Properties.of()
                        .strength(-1,Float.MAX_VALUE)
                        .noCollission()
        );
    }
    public static final VoxelShape SHAPE = Shapes.empty();

    @Override
    public @NotNull VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SHAPE;
    }
}
