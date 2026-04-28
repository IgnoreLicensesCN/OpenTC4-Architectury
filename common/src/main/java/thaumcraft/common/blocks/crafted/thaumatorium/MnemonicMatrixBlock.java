package thaumcraft.common.blocks.crafted.thaumatorium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.IThaumatoriumAttachmentBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//TODO:Render
public class MnemonicMatrixBlock extends SuppressedWarningBlock implements IThaumatoriumAttachmentBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final int MATRIX_PROVIDES_RECIPE_SIZE = 2;
    public MnemonicMatrixBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(3, 17)
        );
    }
    public MnemonicMatrixBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }

    @Override
    public int getAdditionalRecipeSize(Level atlevel, BlockPos attachmentSelfPos, BlockState attachmentSelfState, BlockPos beingAttachedPos) {
        var attachDirection = attachmentSelfState.getValue(FACING);
        if (attachmentSelfPos.relative(attachDirection).equals(beingAttachedPos)) {
            return MATRIX_PROVIDES_RECIPE_SIZE;
        }
        return 0;
    }


    public static final VoxelShape MATRIX_SHAPE = Block.box(3, 3, 3, 13, 13, 13);
    @Override
    public @NotNull VoxelShape getShape(
            BlockState blockState,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CollisionContext collisionContext) {
        return MATRIX_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
            BlockState blockState,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CollisionContext collisionContext
    ) {
        return MATRIX_SHAPE;
    }

}
