package thaumcraft.common.blocks.crafted.noderelated;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.NodeTransducerBlockEntity;

public class NodeTransducerBlock extends Block implements EntityBlock {
    public NodeTransducerBlock(Properties properties) {
        super(properties);
    }
    public NodeTransducerBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(3.f,25.f)
        );
    }
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0,12,0,16,16,16),
            Block.box(4,0,4,12,16,12)
    );

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }@Override
    public VoxelShape getOcclusionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new NodeTransducerBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.NODE_TRANSDUCER) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof NodeTransducerBlockEntity transducer) {
                    transducer.tick();
                }
            });
        }
        return null;
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (i == 10 && j == 10 && Platform.getEnvironment() == Env.CLIENT) {
            if (level instanceof ClientLevel clientLevel){
                var nodePos = blockPos.below();
                var nodeCenter = blockPos.below().getCenter();
                ClientFXUtils.burst(
                        clientLevel,
                        nodeCenter.x,
                        nodeCenter.y,
                        nodeCenter.z,
                        1.0F);
                clientLevel.playLocalSound(nodePos, ThaumcraftSounds.CRAFT_FAIL, SoundSource.BLOCKS, 0.5F, 1.0F, false);
            }
        }
        return super.triggerEvent(blockState, level, blockPos, i, j);
    }
}
