package thaumcraft.common.blocks.crafted;

import com.linearity.opentc4.utils.LogicalSide;
import com.linearity.opentc4.annotations.RecommendedLogicalSide;
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
import thaumcraft.api.aspects.IAspectContainerItem;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.EssentiaReservoirBlockEntity;

//TODO:Smaller bound
public class EssentiaReservoirBlock extends SuppressedWarningBlock implements
        IAspectContainerItemFillerBlock<Aspect>,
        IWandInteractableBlock,
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
        if (level.getBlockEntity(blockPos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity provider){
            return provider.getComparatorSignal();
        }
        return 0;
    }


    @Override
    public boolean canFillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            @NotNull("empty -> any") Aspect aspect) {
        if (level.getBlockEntity(blockPos) instanceof EssentiaReservoirBlockEntity reservoir) {
            return reservoir.canFillAspectContainerItem(stackToFill, itemToFill, aspect);
        }
        return false;
    }

    @Override
    @RecommendedLogicalSide(LogicalSide.SERVER)
    public boolean fillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            int minAmount
    ) {
        if (level.isClientSide()) {return false;}
        if (level.getBlockEntity(blockPos) instanceof EssentiaReservoirBlockEntity reservoir) {
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
                var be = level.getBlockEntity(clickedPos);
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
        if (blockEntityType == ThaumcraftBlockEntities.ESSENTIA_RESERVOIR) {
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
        var be = level.getBlockEntity(blockPos);
        if (be instanceof EssentiaReservoirBlockEntity reservoir) {
            var gooAndGasAmount = Math.min(reservoir.getGooAndGasAmountOnRemove(),50);
            if (gooAndGasAmount > 0){
                level.explode(
                        null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1F,
                        Level.ExplosionInteraction.NONE
                );
            }
            for (int _gooAndGasIndex = 0;
                 _gooAndGasIndex < gooAndGasAmount;
                 _gooAndGasIndex++){
                int yOffset = level.random.nextInt(9)-4;
                var pickPos = blockPos.offset(
                        level.random.nextInt(9)-4,
                        yOffset,
                        level.random.nextInt(9)-4
                );

                var blockStateBeforeSet = level.getBlockState(pickPos);
                if (blockStateBeforeSet.isAir()){
                    if (yOffset < 0){
                        level.setBlockAndUpdate(blockPos, FluxGooBlock.fullOfGoo());
                    }else {
                        level.setBlockAndUpdate(blockPos, FluxGasBlock.fullOfGas());
                    }
                }
            }
        }
    }

}
