package thaumcraft.common.blocks.crafted.infusion;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.abstracts.AbstractPedestalBlock;
import thaumcraft.common.tiles.crafted.infusion.ArcanePedestalBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class ArcanePedestalBlock extends AbstractPedestalBlock implements EntityBlock {
    public ArcanePedestalBlock(Properties properties) {
        super(properties);
    }
    public ArcanePedestalBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
        );
    }

    //TODO:Better shape
    public static final VoxelShape SHAPE = Shapes.box(0.25F, 0.0F, 0.25F, 0.75F, 0.99F, 0.75F);

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof Container container){
            Containers.dropContents(level,pos,container);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcanePedestalBlockEntity(blockPos, blockState);
    }


    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos pos, int i, int j) {
        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel){
                ClientFXUtils.blockSparkle(
                        clientLevel,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        0xb680ff,
                        2
                );
            }
        }
        return super.triggerEvent(blockState, level, pos, i, j);
    }
}
