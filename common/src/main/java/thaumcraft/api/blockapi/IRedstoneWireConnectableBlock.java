package thaumcraft.api.blockapi;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IRedstoneWireConnectableBlock {
    default boolean canBeConnected(BlockState state, Direction direction){
        return true;
    }
}
