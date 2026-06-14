package thaumcraft.common.blocks.crafted.essentia;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.blocks.abstracts.IEssentiaContainerItemFillerBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaReservoirBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

//TODO:Smaller bound
public class EssentiaReservoirBlock extends SuppressedWarningBlock implements
        IEssentiaContainerItemFillerBlock<Aspect>,
        IWandInteractableBlockOrBlockEntity,
        EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public EssentiaReservoirBlock(Properties properties) {
        super(properties);
    }
    public EssentiaReservoirBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(2F,17F)
        );
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity provider){
            return provider.getComparatorSignal();
        }
        return 0;
    }


    @Override
    public boolean canFillEssentiaContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IEssentiaContainerItem<Aspect> itemToFill,
            @NotNull("empty -> any") Aspect aspect) {
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof EssentiaReservoirBlockEntity reservoir) {
            return reservoir.canFillAspectContainerItem(stackToFill, itemToFill, aspect);
        }
        return false;
    }

    @Override
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    public boolean fillEssentiaContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IEssentiaContainerItem<Aspect> itemToFill,
            int minAmount
    ) {
        if (level.isClientSide()) {return false;}
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof EssentiaReservoirBlockEntity reservoir) {
            return reservoir.fillAspectContainerItem(stackToFill, itemToFill,minAmount);
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var clientSideFlag = useOnContext.getLevel().isClientSide();
        if (!clientSideFlag){
            var player = useOnContext.getPlayer();
            if (player != null) {
                var level = useOnContext.getLevel();
                var clickedFace = useOnContext.getClickedFace();
                var clickedPos = useOnContext.getClickedPos();
                var oldState = level.getBlockState(clickedPos);
                BlockState newState = oldState.setValue(FACING, player.isShiftKeyDown()?clickedFace:clickedFace.getOpposite());
                var be = LevelBlockEntityAccessing.getExistingBlockEntity(level, clickedPos);
                if (be instanceof EssentiaReservoirBlockEntity reservoir){
                    reservoir.setBlockStateAndUpdate(newState);
                }
                player.swing(useOnContext.getHand());
            }
        }
        return InteractionResult.sidedSuccess(clientSideFlag);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() != this) {
            return null;
        }
        return new EssentiaReservoirBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.BlockEntityTypeInstances.ESSENTIA_RESERVOIR()) {
            if (level.isClientSide()) {
                return (level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof EssentiaReservoirBlockEntity reservoir) {
                        reservoir.clientTick();
                    }
                };
            }else {
                return (level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof EssentiaReservoirBlockEntity reservoir) {
                        reservoir.serverTick();
                    }
                };
            }
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        var be = LevelBlockEntityAccessing.getExistingBlockEntity(level, pos);
        if (be instanceof EssentiaReservoirBlockEntity reservoir) {
            var gooAndGasAmount = Math.min(reservoir.getGooAndGasAmountOnRemove(),50);
            if (gooAndGasAmount > 0){
                level.explode(
                        null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1F,
                        Level.ExplosionInteraction.NONE
                );
            }
            for (int _gooAndGasIndex = 0;
                 _gooAndGasIndex < gooAndGasAmount;
                 _gooAndGasIndex++){
                int yOffset = level.random.nextInt(9)-4;
                var pickPos = pos.offset(
                        level.random.nextInt(9)-4,
                        yOffset,
                        level.random.nextInt(9)-4
                );

                var blockStateBeforeSet = level.getBlockState(pickPos);
                if (blockStateBeforeSet.isAir()){
                    if (yOffset < 0){
                        level.setBlockAndUpdate(pos, FluxGooBlock.fullOfGoo());
                    }else {
                        level.setBlockAndUpdate(pos, FluxGasBlock.fullOfGas());
                    }
                }
            }
        }
    }

}
