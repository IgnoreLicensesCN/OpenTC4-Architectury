package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;

import net.minecraft.world.level.Level;

public interface IEssentiaContainerItemFillerBlock<Asp extends Aspect> {
    boolean canFillEssentiaContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IEssentiaContainerItem<Asp> itemToFill,
            @NotNull("empty -> any") Asp aspect
    );
    //usually called when jar right-click block with this(maybe golem will also call this one day):
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    boolean fillEssentiaContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IEssentiaContainerItem<Asp> itemToFill,
            int minAmount
    );
}
