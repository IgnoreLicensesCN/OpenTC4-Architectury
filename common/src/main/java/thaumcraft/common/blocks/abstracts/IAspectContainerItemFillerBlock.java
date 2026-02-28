package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.utils.LogicalSide;
import com.linearity.opentc4.utils.RecommendedLogicalSide;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainerItem;

import net.minecraft.world.level.Level;

public interface IAspectContainerItemFillerBlock<Asp extends Aspect> {
    boolean canFillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Asp> itemToFill,
            Asp aspect);
    //usually called when jar right-click block with this(maybe golem will also call this one day):
    @RecommendedLogicalSide(LogicalSide.SERVER)
    boolean fillAspectContainerItem(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            ItemStack stackToFill,
            IAspectContainerItem<Asp> itemToFill,
            int minAmount
    );
}
