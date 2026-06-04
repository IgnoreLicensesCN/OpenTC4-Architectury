package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.common.tiles.abstracts.SingleFluidContainerBlockEntity;

import static com.linearity.opentc4.OpenTC4.platformUtils;

@UtilityLikeAbstraction
//TODO:[maybe wont finished]anyone help me connect with pipe
public abstract class AbstractLiquidFillInBlock extends SuppressedWarningBlock
        implements EntityBlock
{
    public AbstractLiquidFillInBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide && level.getBlockEntity(blockPos) instanceof SingleFluidContainerBlockEntity spaBE) {
            var stack = player.getItemInHand(interactionHand);
            var fluidStack = platformUtils.copyFluidStackFromItemStack(stack);
            if (fluidStack != null && !fluidStack.isEmpty()) {
                var inserted = spaBE.insertFluid(fluidStack.getFluid(),fluidStack.getAmount());
                if (inserted != 0){
                    fluidStack.setAmount(fluidStack.getAmount()-inserted);
                    platformUtils.decreaseFluidStackToItemStack(stack,fluidStack);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
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
}
