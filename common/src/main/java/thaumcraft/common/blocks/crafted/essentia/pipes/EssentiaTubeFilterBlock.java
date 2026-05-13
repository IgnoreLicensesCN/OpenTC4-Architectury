package thaumcraft.common.blocks.crafted.essentia.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.IAspectLabelAttachableBlock;
import thaumcraft.common.tiles.crafted.essentiabe.pipes.EssentiaTubeFilterBlockEntity;

public class EssentiaTubeFilterBlock extends EssentiaTubeBlock
implements IAspectLabelAttachableBlock {

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaTubeFilterBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player != null && player.isShiftKeyDown() && player.getItemInHand(interactionHand).isEmpty()) {
            if (!level.isClientSide){
                attemptRemoveAspectLabel(level, blockPos, blockState);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide){
            attemptRemoveAspectLabel(level, pos, state);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
