package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.PlatformUniqueUtils;
import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import dev.architectury.fluid.FluidStack;
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

import static com.linearity.opentc4.OpenTC4.DEBUG_FLAG;
import static com.linearity.opentc4.OpenTC4.DebugFlags.CANT_FILL_BACK_FLUID_WARN;
import static com.linearity.opentc4.OpenTC4.platformUtils;

@UtilityLikeAbstraction
//TODO:[maybe wont finished]anyone help me connect with pipe
public abstract class AbstractLiquidFillInBlock extends SuppressedWarningBlock
        implements EntityBlock {
    public AbstractLiquidFillInBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        var stack = player.getItemInHand(interactionHand);
        PlatformUniqueUtils.FluidInteractionManager fluidInteractionManager = platformUtils.getFluidInteractionManager();
        var fluidStack = fluidInteractionManager.copyFluidStackFromItemStack(stack);
        if (fluidStack == null || fluidStack.isEmpty()) {
            return InteractionResult.PASS;
        }
        var canDrain = fluidInteractionManager.drainFluidStackFromItemStack(stack, fluidStack, false);
        if (fluidStack.isEmpty()) {
            return InteractionResult.PASS;
        }
        var dir = blockHitResult.getDirection();
        var simulatedResult = fluidInteractionManager.insertFluidToPos(level, blockPos, dir, canDrain, false);
        if (simulatedResult > 0) {
            var reallyDrained = fluidInteractionManager.drainFluidStackFromItemStack(stack, canDrain, true);
            var reallyInserted = fluidInteractionManager.insertFluidToPos(level, blockPos, dir, reallyDrained, true);
            //fill back
            if (reallyInserted > 0) {
                var remaining = reallyDrained.getAmount() - reallyInserted;
                if (remaining > 0) {
                    FluidStack fluidStack1 = FluidStack.create(reallyDrained.getFluid(), remaining);
                    var cantFillBack = fluidInteractionManager.insertFluidStackToItemStack(stack, fluidStack1, true);
                    if (DEBUG_FLAG && CANT_FILL_BACK_FLUID_WARN &&  cantFillBack != 0){
                        OpenTC4.LOGGER.error(
                                "Can't fill back fluid {} in block {},remaining {}:{}",
                                fluidStack,blockPos,cantFillBack,new IllegalStateException()
                        );
                    }
                }
                player.swing(interactionHand);
                return InteractionResult.SUCCESS;
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
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity provider) {
            return provider.getComparatorSignal();
        }
        return 0;
    }
}
