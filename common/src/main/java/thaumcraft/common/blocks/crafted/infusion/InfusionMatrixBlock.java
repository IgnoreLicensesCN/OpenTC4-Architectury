package thaumcraft.common.blocks.crafted.infusion;

import com.linearity.opentc4.simpleutils.SimplePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.common.blocks.crafted.infusion.InfusionPillarBlock.ABOVE;
import static thaumcraft.common.blocks.crafted.infusion.InfusionPillarBlock.FACING;

public class InfusionMatrixBlock extends SuppressedWarningBlock implements EntityBlock, IWandInteractableBlockOrBlockEntity {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public InfusionMatrixBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }
    public InfusionMatrixBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
                        .lightLevel(s -> 10)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }
    public static final List<SimplePair<BlockPos,BlockState>> OFFSET_AND_STATE_TO_CHECK;
    static {
        List<SimplePair<BlockPos,BlockState>> list = new ArrayList<>(8);
        var pillarDefaultState = ThaumcraftBlocks.ThaumcraftBlockInstances.INFUSION_PILLAR.defaultBlockState();
        for (var dir: Direction.Plane.HORIZONTAL) {
            var dirRotated90 = dir.getClockWise();
            list.add(new SimplePair<>(
                    new BlockPos(0,-1,0).relative(dir).relative(dirRotated90),pillarDefaultState.setValue(FACING,dir).setValue(ABOVE,true)
            ));
            list.add(new SimplePair<>(
                    new BlockPos(0,-2,0).relative(dir).relative(dirRotated90),pillarDefaultState.setValue(FACING,dir).setValue(ABOVE,false)
            ));
        }
        OFFSET_AND_STATE_TO_CHECK = List.copyOf(list);
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean checkPillar(LevelAccessor level, BlockPos selfPos, BlockState selfState) {
        for (var pillarPosStatePair: OFFSET_AND_STATE_TO_CHECK) {
            if (level.getBlockState(pillarPosStatePair.a().offset(selfPos)) != pillarPosStatePair.b()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        if (!checkPillar(serverLevel, blockPos, blockState)) {
            if (!serverLevel.isClientSide
                    && serverLevel.getBlockEntity(blockPos) instanceof InfusionMatrixBlockEntity infusionMatrixBlockEntity) {
                infusionMatrixBlockEntity.cancelCrafting();
            }
            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(LIT, false));
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState prevState, Direction changeFromDirection, BlockState neighborState, LevelAccessor level, BlockPos selfPos, BlockPos changedPos) {
        if (!checkPillar(level, selfPos, prevState)) {
            if (!level.isClientSide() && level.getBlockEntity(selfPos) instanceof InfusionMatrixBlockEntity infusionMatrixBlockEntity) {
                infusionMatrixBlockEntity.cancelCrafting();
            }
            return prevState.setValue(LIT, false);
        }
        return prevState.setValue(LIT, true);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfusionMatrixBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return (level1, blockPos, blockState1, be) -> {
                if (be instanceof InfusionMatrixBlockEntity infusionMatrixBlockEntity) {
                    infusionMatrixBlockEntity.serverTick();
                }
            };
        }
        return (level1, blockPos, blockState1, be) -> {
            if (be instanceof InfusionMatrixBlockEntity infusionMatrixBlockEntity) {
                infusionMatrixBlockEntity.clientTick();
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof InfusionMatrixBlockEntity infusionMatrixBlockEntity) {
            infusionMatrixBlockEntity.cancelCrafting();
            if (infusionMatrixBlockEntity.isCrafting()){
                var center = pos.getCenter();
                level.explode(null,center.x,center.y,center.z,2.0F,true, Level.ExplosionInteraction.MOB);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (!level.isClientSide){
            var clickedPos = useOnContext.getClickedPos();
            var clickedState = level.getBlockState(clickedPos);
            if (clickedState.getBlock() instanceof InfusionMatrixBlock infusionMatrixBlock && infusionMatrixBlock.checkPillar(level, clickedPos, clickedState)) {
                level.setBlockAndUpdate(clickedPos,clickedState.setValue(LIT, true));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
