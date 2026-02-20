package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;

public interface IAspectLabelAttachableBlock {
    //then maybe someone would make a jar-label bag with lots of labels
    //which can be switched by using sneak and mouse-scrolling.
    //so we can stick lots of label in a bag to avoid lots labels in inventory.
    //@return boolean--should consume label(just recommend to consume you can also not to do so)
    boolean attemptAttachAspectLabel(Level level, BlockPos pos, BlockState state, Aspect labelAspect);
    //@return removed existing label
    boolean attemptRemoveAspectLabel(Level level, BlockPos pos, BlockState state);
}
