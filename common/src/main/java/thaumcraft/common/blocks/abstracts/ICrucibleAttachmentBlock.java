package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

public interface ICrucibleAttachmentBlock {
    default void onCrucibleHeating(
            Level level,
            BlockPos attachmentPos,
            BlockState attachmentState,
            CrucibleBlockEntity crucibleBlockEntity){
        crucibleBlockEntity.heat += 2;
    };
    default void onCrucibleNotHeating(
            Level level,
            BlockPos attachmentPos,
            BlockState attachmentState,
            CrucibleBlockEntity crucibleBlockEntity){
    };
    //return value will be set to capacity
    default int onCalculatingCrucibleCapacity(
            Level level,
            BlockPos attachmentPos,
            BlockState attachmentState,
            CrucibleBlockEntity crucibleBlockEntity,
            int currentCalculatedCapacity){
        return currentCalculatedCapacity;
    };


}
