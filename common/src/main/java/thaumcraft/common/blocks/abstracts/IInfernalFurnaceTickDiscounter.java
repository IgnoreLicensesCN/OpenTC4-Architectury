package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IInfernalFurnaceTickDiscounter {
    int getInfernalFurnaceTickDiscount(Level level, BlockState state, BlockPos pos, Direction furnaceBlockExposedDirection);
}
