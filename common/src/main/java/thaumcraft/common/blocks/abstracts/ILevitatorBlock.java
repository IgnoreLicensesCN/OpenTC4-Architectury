package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ILevitatorBlock{
    int getBaseLevitatorUpliftRange(Level atLevel, BlockPos levitatorPos, BlockState levitatorState);
    default int getLevitatorUpliftRange(Level atLevel, BlockPos levitatorPos, BlockState levitatorState){
        int rangeAbove = getBaseLevitatorUpliftRange(atLevel, levitatorPos, levitatorState);
        for (int yOffset=-1; levitatorPos.getY()+yOffset >= atLevel.getMinBuildHeight();yOffset--){
            var probablyRangeAmplifierBlockPos = levitatorPos.offset(0,yOffset,0);
            var probablyRangeAmplifierBlockState = atLevel.getBlockState(probablyRangeAmplifierBlockPos);
            if (!(probablyRangeAmplifierBlockState.getBlock() instanceof ILevitatorRangeAmplifierBlock amplifierBlock)) {
                break;
            }
            int addRange = amplifierBlock.getLevitatorUpliftRangeAddition(atLevel,levitatorPos,levitatorState,probablyRangeAmplifierBlockPos,probablyRangeAmplifierBlockState);
            if (addRange <= 0){
                break;
            }
            rangeAbove += addRange;
        }
        return rangeAbove;
    }
}
