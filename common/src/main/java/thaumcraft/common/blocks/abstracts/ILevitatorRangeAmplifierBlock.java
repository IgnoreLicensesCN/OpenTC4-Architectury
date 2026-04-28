package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ILevitatorRangeAmplifierBlock {
    int UP_BLOCKS_PER_LIFTER = 10;
    default int getLevitatorUpliftRangeAddition(Level atLevel, BlockPos highestLevitatorPos, BlockState highestLevitatorState,BlockPos thisBlockPos,BlockState thisBlockState){
        if (atLevel.hasNeighborSignal(thisBlockPos)) {
            return 0;
        }
        return UP_BLOCKS_PER_LIFTER;
    }
}
