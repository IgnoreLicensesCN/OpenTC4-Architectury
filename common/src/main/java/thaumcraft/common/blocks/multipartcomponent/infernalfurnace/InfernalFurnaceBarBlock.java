package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import com.linearity.opentc4.VecTransformations;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;

public class InfernalFurnaceBarBlock extends AbstractInfernalFurnaceComponent {

    public InfernalFurnaceBarBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceBarBlock() {
        super(Properties
                .copy(Blocks.STONE)
                .strength(10.0f,500.f)
                .lightLevel(s -> 13)
                .requiresCorrectToolForDrops()
        );
    }


    public static final BlockPos SELF_POS_2_1_1 = new BlockPos(2,1,1);

    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return SELF_POS_2_1_1;
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.IRON_BARS.defaultBlockState(), 3);
        }
    }

    @Override
    public boolean triggerEvent(
            BlockState state,
            Level level,
            BlockPos pos,
            int id,
            int param
    ) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            return super.triggerEvent(state, level, pos, id, param);
        }
        if (id == 1) {
            if (level instanceof ClientLevel clientLevel) {
                ClientFXUtils.blockSparkle(
                        clientLevel,
                        pos.getX(), pos.getY(), pos.getZ(),
                        16736256,
                        5
                );
            }
            return true;
        }
        return super.triggerEvent(state, level, pos, id, param);
    }
    public static final VoxelShape BASIC_SHAPE = Shapes.box(14,0,14,16,16,16);
    public static final VoxelShape SHAPE_Y_90 = Shapes.box(0, 0, 14, 2, 16, 16);
    public static final VoxelShape SHAPE_Y_180 = Shapes.box(0, 0, 0, 2, 16, 2);
    public static final VoxelShape SHAPE_Y_270 = Shapes.box(14, 0, 0, 16, 16, 2);

    public static final VoxelShape[] collisionShapeByIndex = makeShapes(1,1,16,0,16);
    public static final VoxelShape[] shapeByIndex = makeShapes(1,1,16,0,16);
    public static VoxelShape[] makeShapes(float f, float g, float h, float i, float j) {
        float k = 8.0F - f;
        float l = 8.0F + f;
        float m = 8.0F - g;
        float n = 8.0F + g;
        VoxelShape voxelShape = Block.box(k, 0.0, k, l, h, l);
        VoxelShape voxelShape2 = Block.box(m, i, 0.0, n, j, n);
        VoxelShape voxelShape3 = Block.box(m, i, m, n, j, 16.0);
        VoxelShape voxelShape4 = Block.box(0.0, i, m, n, j, n);
        VoxelShape voxelShape5 = Block.box(m, i, m, 16.0, j, n);
        VoxelShape voxelShape6 = Shapes.or(voxelShape2, voxelShape5);
        VoxelShape voxelShape7 = Shapes.or(voxelShape3, voxelShape4);
        VoxelShape[] voxelShapes = new VoxelShape[]{
                Shapes.empty(),
                voxelShape3,
                voxelShape4,
                voxelShape7,
                voxelShape2,
                Shapes.or(voxelShape3, voxelShape2),
                Shapes.or(voxelShape4, voxelShape2),
                Shapes.or(voxelShape7, voxelShape2),
                voxelShape5,
                Shapes.or(voxelShape3, voxelShape5),
                Shapes.or(voxelShape4, voxelShape5),
                Shapes.or(voxelShape7, voxelShape5),
                voxelShape6,
                Shapes.or(voxelShape3, voxelShape6),
                Shapes.or(voxelShape4, voxelShape6),
                Shapes.or(voxelShape7, voxelShape6)
        };

        for (int o = 0; o < 16; o++) {
            voxelShapes[o] = Shapes.or(voxelShape, voxelShapes[o]);
        }

        return voxelShapes;
    }


    protected @NotNull VoxelShape shapeByState(@NotNull BlockState blockState) {
        var rotation = getRotation(blockState);
        if (rotation == VecTransformations.Rotation3D.NONE){
            return BASIC_SHAPE;
        }
        if (rotation == VecTransformations.Rotation3D.Y_90){
            return SHAPE_Y_90;
        }
        if (rotation == VecTransformations.Rotation3D.Y_180){
            return SHAPE_Y_180;
        }
        if (rotation == VecTransformations.Rotation3D.Y_270){
            return SHAPE_Y_270;
        }
        throw new IllegalStateException("unexpected rotation: " + rotation);
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return shapeByState(blockState);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return shapeByState(blockState);
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }
}
