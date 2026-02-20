package thaumcraft.common.blocks.crafted.noderelated.visnet;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.visnet.VisNetChargeRelayBlockEntity;
import thaumcraft.common.tiles.crafted.visnet.VisNetRelayBlockEntity;

import static thaumcraft.common.blocks.crafted.noderelated.visnet.VisNetRelayBlock.COLOR;
import static thaumcraft.common.blocks.crafted.noderelated.visnet.VisNetRelayBlock.COLOR_TYPES;

public class VisNetChargeRelayBlock extends SuppressedWarningBlock implements IWandInteractableBlock, EntityBlock {
    public VisNetChargeRelayBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(COLOR, 0)
        );
    }
    public VisNetChargeRelayBlock() {
        this(
                Properties.of()
                        .lightLevel(state -> 10)//i'm lazy to check if it's valid
                        .sound(SoundType.METAL)
                        .strength(3,17)
        );
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }
    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        var state = level.getBlockState(useOnContext.getClickedPos());
        state = state.setValue(COLOR, (state.getValue(COLOR) + 1)%COLOR_TYPES);
        level.setBlockAndUpdate(useOnContext.getClickedPos(), state);
        if (level.getBlockEntity(useOnContext.getClickedPos()) instanceof VisNetRelayBlockEntity relay) {
            relay.removeThisNode();
            relay.nodeRefresh=true;
            relay.markDirtyAndUpdateSelf();
            level.playSound(null,relay.getBlockPos(), ThaumcraftSounds.CRYSTAL, SoundSource.BLOCKS, 0.2F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }
    public static final VoxelShape SHAPE = Block.box(
            5, 8, 5,
            11, 16, 11
    );

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new VisNetChargeRelayBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.VIS_CHARGE_RELAY && blockState.getBlock() == this && level != null) {
            if (!level.isClientSide) {
                return ((level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof VisNetChargeRelayBlockEntity relay) {
                        relay.tick();
                    }
                });
            }else {
                return ((level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof VisNetChargeRelayBlockEntity relay) {
                        relay.clientTick();
                    }
                });
            }
        }
        return null;
    }
}
