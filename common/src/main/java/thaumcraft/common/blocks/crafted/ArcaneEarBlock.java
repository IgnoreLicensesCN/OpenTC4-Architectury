package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.ArcaneEarBlockEntity;

//should be called "NoteBlockListener"
public class ArcaneEarBlock extends NoteBlock implements EntityBlock {//state meaning change:POWERED -> will provide signal
    public ArcaneEarBlock(Properties properties) {
        super(properties);
    }

    public ArcaneEarBlock() {
        this(
                Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(2.5F, 10.0F)
                        .noOcclusion()
        );
    }

    @Override
    public @NotNull InteractionResult use(
            BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
    ) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (itemStack.is(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS)
                && blockHitResult.getDirection() == Direction.UP
        ) {
            return InteractionResult.PASS;
        } else if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            blockState = blockState.cycle(NOTE);
            level.setBlock(blockPos, blockState, 3);
            //we dont play note now
//            this.playNote(player, blockState, level, blockPos);
            player.awardStat(Stats.TUNE_NOTEBLOCK);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void attack(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        //dont play note
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        //we dont play note now
//        boolean bl2 = level.hasNeighborSignal(blockPos);
//        if (bl2 != blockState.getValue(POWERED)) {
//            if (bl2) {
//                this.playNote(null, blockState, level, blockPos);
//            }

//            level.setBlock(blockPos, blockState.setValue(POWERED, bl2), 3);
//        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return getSignal(blockState, blockGetter, blockPos, direction);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcaneEarBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof ArcaneEarBlockEntity arcaneEar) {
                    arcaneEar.serverTick();
                }
            });
        }
        return null;
    }
}
