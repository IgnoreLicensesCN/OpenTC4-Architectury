package thaumcraft.common.blocks.crafted;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectContainerItem;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.blocks.abstracts.IAspectLabelAttachableBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.ArcaneAlembicBlockEntity;

public class ArcaneAlembicBlock extends SuppressedWarningBlock
        implements EntityBlock,
        IWandInteractableBlock,
        IAspectLabelAttachableBlock,
        IAspectContainerItemFillerBlock<Aspect>
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public ArcaneAlembicBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public ArcaneAlembicBlock() {
        this(Properties.copy(Blocks.IRON_BLOCK)
                .strength(3,17)
        );
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
        if (blockState.getBlock() == this){
            return new ArcaneAlembicBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide){
            if (level.getBlockEntity(blockPos) instanceof ArcaneAlembicBlockEntity alembic && !player.isCrouching() && player.getItemInHand(interactionHand).isEmpty()) {
                var stackInHand = player.getItemInHand(interactionHand);
                if (!player.isCrouching()) {
                    if (stackInHand.isEmpty()) {
                        var msg = Component.empty();
                        var currentAmount = alembic.getAmount();
                        var maxAmount = alembic.getMaxAmount();
                        if (!alembic.getAspect().isEmpty() && currentAmount != 0) {
                            if ((double) currentAmount < (double) maxAmount * 0.4) {
                                msg = Component.translatable("tile.alembic.msg.2");
                            } else if ((double) currentAmount < (double) maxAmount * 0.8) {
                                msg = Component.translatable("tile.alembic.msg.3");
                            } else if (currentAmount < maxAmount) {
                                msg = Component.translatable("tile.alembic.msg.4");
                            } else if (currentAmount == maxAmount) {
                                msg = Component.translatable("tile.alembic.msg.5");
                            }
                        } else {
                            msg = Component.translatable("tile.alembic.msg.1");
                        }

                        player.sendSystemMessage(msg.setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
                        level.playSound(player, blockPos, ThaumcraftSounds.ALEMBIC_KNOCK, SoundSource.BLOCKS, .2F, 1.F);
                    }
                }else {
                    if (attemptRemoveAspectLabel(level, blockPos, blockState)){
                        return InteractionResult.CONSUME;
                    }
                    if (stackInHand.isEmpty()) {
                        alembic.clear();
                        level.playSound(player, blockPos, ThaumcraftSounds.ALEMBIC_KNOCK, SoundSource.BLOCKS, .2F, 1.F);
                        level.playSound(player, blockPos, SoundEvents.PLAYER_SWIM, SoundSource.BLOCKS, .5F, 1.F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.3F);
                        return InteractionResult.CONSUME;
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (!level.isClientSide) {
            var setToDir = useOnContext.getHorizontalDirection();
            level.setBlockAndUpdate(useOnContext.getClickedPos(), level.getBlockState(useOnContext.getClickedPos()).setValue(FACING, setToDir));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean attemptAttachAspectLabel(Level level, BlockPos pos, BlockState state, Aspect labelAspect) {
        if (level.getBlockEntity(pos) instanceof ArcaneAlembicBlockEntity alembic) {
            alembic.setAspectFilter(labelAspect);
            return true;
        }
        return false;
    }

    @Override
    public boolean attemptRemoveAspectLabel(Level level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof ArcaneAlembicBlockEntity alembic && !alembic.getAspectFilter().isEmpty()) {
            alembic.setAspectFilter(Aspects.EMPTY);
            level.playSound(null,pos,ThaumcraftSounds.PAGE,SoundSource.BLOCKS, 1.0F, 1.1F);
            //TODO:Drop label stack
            return true;
        }
        return false;
    }

    @Override
    public boolean canFillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            Aspect aspect) {
        if (level.getBlockEntity(blockPos) instanceof ArcaneAlembicBlockEntity alembic) {
            return alembic.canFillAspectContainerItem(stackToFill, itemToFill, aspect);
        }
        return false;
    }

    @Override
    public void fillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill
    ) {
        if (level.isClientSide()) {
            return;
        }
        if (level.getBlockEntity(blockPos) instanceof ArcaneAlembicBlockEntity alembic) {
            alembic.fillAspectContainerItem(stackToFill, itemToFill);
        }
    }
}
