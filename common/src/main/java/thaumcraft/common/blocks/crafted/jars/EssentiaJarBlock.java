package thaumcraft.common.blocks.crafted.jars;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainerItem;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.blocks.abstracts.IAspectLabelAttachableBlock;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.EssentiaJarBlockEntity;

import static com.linearity.opentc4.Consts.EssentiaJarTagAccessors.*;

public class EssentiaJarBlock extends JarBlock
        implements EntityBlock,
        IAspectLabelAttachableBlock,
        IAspectContainerItemFillerBlock<Aspect>
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public EssentiaJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));//will affect tag dir
    }
    public EssentiaJarBlock() {
        this(JAR_PROPERTIES);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new EssentiaJarBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level != null && !level.isClientSide && blockEntityType == ThaumcraftBlockEntities.ESSENTIA_JAR) {
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EssentiaJarBlockEntity jar) {
                    jar.serverTick();
                }
            };
        }
        return null;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBlockEntity(blockPos) instanceof EssentiaJarBlockEntity jar) {
            var aspectCurrent = jar.getAspectCurrent();
            var amountCurrent = jar.getAspectAmountCurrent();
            var aspectFilter = jar.getAspectFilter();
            var stackToDrop = new ItemStack(ThaumcraftItems.ESSENTIA_JAR);
            if (!(amountCurrent<=0 && aspectCurrent.isEmpty() && aspectFilter.isEmpty())) {
                if (amountCurrent>0 && !aspectCurrent.isEmpty()){
                    ASPECT.writeToCompoundTag(stackToDrop.getOrCreateTag(), aspectCurrent);
                    AMOUNT.writeToCompoundTag(stackToDrop.getOrCreateTag(), amountCurrent);
                }
                if (!aspectFilter.isEmpty()){
                    ASPECT_FILTER.writeToCompoundTag(stackToDrop.getOrCreateTag(), aspectFilter);
                }
            }
            var posCenter = blockPos.getCenter();
            level.addFreshEntity(new ItemEntity(level,posCenter.x,posCenter.y,posCenter.z,stackToDrop));
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public boolean canFillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            Aspect aspect) {
        if (level.getBlockEntity(blockPos) instanceof EssentiaJarBlockEntity essentiaJar) {
            return essentiaJar.canFillAspectContainerItem(stackToFill, itemToFill, aspect);
        }
        return false;
    }
    public static void playJarSound(Level level,BlockPos pos, float p_72980_8_) {
        level.playSound(null,pos, ThaumcraftSounds.JAR,SoundSource.BLOCKS, p_72980_8_, 1.0F);
    }

    @Override
    public boolean fillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            int minAmount
    ) {
        if (level.getBlockEntity(blockPos) instanceof EssentiaJarBlockEntity essentiaJar) {
            return essentiaJar.fillAspectContainerItem(stackToFill, itemToFill,minAmount);
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player != null){
            if (player.isCrouching()){
                if (attemptRemoveAspectLabel(level, blockPos, blockState)){
                    playJarSound(level, blockPos, 1.0F);
                } else {
                    var usingStack = player.getItemInHand(interactionHand);
                    if (usingStack.isEmpty()){
                        if (level.getBlockEntity(blockPos) instanceof EssentiaJarBlockEntity essentiaJar){
                            essentiaJar.clear();
                            playJarSound(level, blockPos, 0.4F);
                            level.playSound(
                                    null,
                                    blockPos,
                                    SoundEvents.PLAYER_SWIM,
                                    SoundSource.BLOCKS,
                                    .5F,
                                    1.F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.3F
                            );
                        }
                    }
                }
            }
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState arg) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return !(level.getBlockEntity(pos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity signalProvider)
                ? 0 : signalProvider.getComparatorSignal();
    }
}
