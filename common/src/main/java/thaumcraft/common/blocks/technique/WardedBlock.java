package thaumcraft.common.blocks.technique;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableOwnedBlock;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.WardedBlockEntity;

import static thaumcraft.api.ThaumcraftApiHelper.rayTraceIgnoringSource;

public class WardedBlock extends SuppressedWarningBlock implements IWandInteractableOwnedBlock {
    public WardedBlock(Properties properties) {
        super(properties);
    }
    public WardedBlock() {
        this(Properties.of().strength(-1,999.0F).noOcclusion());
    }

    @Override
    public void onOwnerClicked(UseOnContext useOnContext) {
        var pos = useOnContext.getClickedPos();
        var level = useOnContext.getLevel();
        if (level.getBlockEntity(pos) instanceof WardedBlockEntity warded) {
            level.setBlockAndUpdate(pos,warded.getStoringState());
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WardedBlockEntity(blockPos, blockState);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (!(level instanceof ClientLevel clientLevel)) return;
        var lookVec = player.getLookAngle();

        var blockHitResult = rayTraceIgnoringSource
                (level,
                        player.getEyePosition(),
                        player.getEyePosition().add(lookVec.normalize().multiply(new Vec3(10,10,10))),
                        true,
                        player
                );
        if (blockHitResult == null) {return;}
        var hitVec = blockHitResult.getLocation();


        float f = (float) (hitVec.x - blockPos.getX());
        float f1 = (float) (hitVec.y - blockPos.getY());
        float f2 = (float) (hitVec.z - blockPos.getZ());
        ClientFXUtils.blockWard(clientLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockHitResult.getDirection(), f, f1, f2);
    }

    @Override
    public boolean hasDynamicShape() {
        return false;
    }

    @Override
    public @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getBlockSupportShape(blockGetter, blockPos);
        }
        return super.getBlockSupportShape(blockState, blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {

        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getVisualShape(blockGetter, blockPos,collisionContext);
        }
        return super.getVisualShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getShape(blockGetter, blockPos,collisionContext);
        }
        return super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getCollisionShape(blockGetter, blockPos,collisionContext);
        }
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getOcclusionShape(blockGetter, blockPos);
        }
        return super.getOcclusionShape(blockState, blockGetter, blockPos);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (levelAccessor.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            var newState = warded.getStoringState().updateShape(direction,blockState2,levelAccessor,blockPos,blockPos2);
            warded.setStoringState(newState);

        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getInteractionShape(blockGetter,blockPos);
        }
        return super.getInteractionShape(blockState, blockGetter, blockPos);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().isCollisionShapeFullBlock(blockGetter,blockPos);
        }
        return super.isCollisionShapeFullBlock(blockState, blockGetter, blockPos);
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            var state = warded.getStoringState();
            return warded.getStoringState().getBlock().isOcclusionShapeFullBlock(state,blockGetter,blockPos);
        }
        return super.isOcclusionShapeFullBlock(blockState, blockGetter, blockPos);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int i, int j) {
        if (levelAccessor.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            warded.getStoringState().updateIndirectNeighbourShapes(levelAccessor, blockPos, i, j);
        }else {
            super.updateIndirectNeighbourShapes(blockState, levelAccessor, blockPos, i, j);
        }
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (level.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().triggerEvent(level,blockPos,i,j);
        }
        return super.triggerEvent(blockState, level, blockPos, i, j);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (level.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            warded.getStoringState().entityInside(level,blockPos,entity);
        }else {
            super.entityInside(blockState, level, blockPos, entity);
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            return warded.getStoringState().getSignal(blockGetter, blockPos,direction);
        }
        return super.getSignal(blockState, blockGetter, blockPos, direction);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            var storingState = warded.getStoringState();
            if (!storingState.hasAnalogOutputSignal()) {
                return 0;
            }
            return storingState.getAnalogOutputSignal(level, blockPos);
        }
        return super.getAnalogOutputSignal(blockState, level, blockPos);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (blockGetter.getBlockEntity(blockPos) instanceof WardedBlockEntity warded) {
            var storingState = warded.getStoringState();
            return storingState.getDirectSignal(blockGetter, blockPos,direction);
        }
        return super.getDirectSignal(blockState, blockGetter, blockPos, direction);
    }

}
