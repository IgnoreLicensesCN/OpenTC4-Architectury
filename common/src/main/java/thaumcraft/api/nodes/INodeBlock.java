package thaumcraft.api.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

//implements to
public interface INodeBlock {
    default boolean preventAttackFromAnotherNode(){
        return false;
    };
    default boolean canBeConvertedToEnergizedNode(Level atLevel, BlockPos pos){
        return false;
    }
}
