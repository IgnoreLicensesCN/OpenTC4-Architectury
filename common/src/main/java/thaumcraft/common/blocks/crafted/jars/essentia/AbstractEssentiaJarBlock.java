package thaumcraft.common.blocks.crafted.jars.essentia;

import com.linearity.opentc4.utils.LogicalSide;
import com.linearity.opentc4.utils.RecommendedLogicalSide;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.blocks.abstracts.IAspectLabelAttachableBlock;
import thaumcraft.common.blocks.crafted.jars.JarBlock;
import thaumcraft.common.items.misc.jars.EssentiaJarBlockItem;
import thaumcraft.common.tiles.crafted.jars.EssentiaJarBlockEntity;

public abstract class AbstractEssentiaJarBlock extends JarBlock
        implements EntityBlock,
        IAspectLabelAttachableBlock,
        IAspectContainerItemFillerBlock<Aspect>
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public AbstractEssentiaJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));//will affect tag dir
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

    public abstract EssentiaJarBlockItem getEssentiaJarItem();

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBlockEntity(blockPos) instanceof EssentiaJarBlockEntity jar) {
            var aspectCurrent = jar.getAspectCurrent();
            var amountCurrent = jar.getAspectAmountCurrent();
            var aspectFilter = jar.getAspectFilter();
            var jarItem = getEssentiaJarItem();
            var stackToDrop = new ItemStack(jarItem);
            if (!(amountCurrent<=0 && aspectCurrent.isEmpty() && aspectFilter.isEmpty())) {
                if (amountCurrent>0 && !aspectCurrent.isEmpty()){
                    jarItem.setAspectAndAmount(stackToDrop, aspectCurrent,amountCurrent);
                }
                if (!aspectFilter.isEmpty()){
                    jarItem.setFilter(stackToDrop, aspectFilter);
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
            @NotNull("empty -> any") Aspect aspect) {
        if (level.getBlockEntity(blockPos) instanceof EssentiaJarBlockEntity essentiaJar) {
            return essentiaJar.canFillAspectContainerItem(stackToFill, itemToFill, aspect);
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, blockState, livingEntity, itemStack);
        if (level.isClientSide) return;

        if (level.getBlockEntity(pos) instanceof EssentiaJarBlockEntity jar && itemStack.getItem() instanceof EssentiaJarBlockItem jarItem) {

            var tag = itemStack.getTag();
            if (tag == null) return;
            boolean changed = false;

            // 读取 aspect
            var jarInfo = jarItem.getJarInfo(itemStack);
            var aspect = jarInfo.aspect();
            var amount = jarInfo.amount();
            var filter = jarInfo.filter();

            if (!aspect.isEmpty() && amount > 0) {
                jar.setAspectAndAmount(aspect, amount);
                changed = true;
            }

            if (!filter.isEmpty()) {
                jar.setAspectFilter(filter);
                changed = true;
            }
            if (changed){
                jar.markDirtyAndUpdateSelf();
            }
        }
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
