package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;

public interface IEssentiaContainerItemFillableBlock<Asp extends Aspect> {
    boolean canBeFilledWithEssentiaContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackFiller,
            IEssentiaContainerItem<Asp> itemFiller,
            @NotNull("not empty") Asp aspect
    );
    //usually called when jar right-click block with this(maybe golem will also call this one day):
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    boolean fillWithEssentiaContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackFiller,
            IEssentiaContainerItem<Asp> itemFiller,
            Aspect aspectToFill,
            int exactAmount
    );
}
