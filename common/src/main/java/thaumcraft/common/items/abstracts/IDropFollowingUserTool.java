package thaumcraft.common.items.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.entities.FollowingItemEntity;

public interface IDropFollowingUserTool {
    boolean canMakeDropFollowPlayer(ItemStack usingToolStack,BlockState droppingState, ServerLevel level, BlockPos atPos, Entity entityToFollow, ItemStack stackToDrop);
    //the original one wont drop now you have to drop here if you want to drop.
    default void makeDropItemEntityAtPos(ItemStack usingToolStack, BlockState droppingState, ServerLevel level, BlockPos atPos, Entity entityToFollow, ItemStack stackToDrop){
        var fi = new FollowingItemEntity(level, atPos.getX(),atPos.getY(),atPos.getZ(), stackToDrop);
        fi.targetEntity = entityToFollow;
        fi.setRenderType(getFollowingItemEntityTailColor());
        level.addFreshEntity(fi);

    };
    default int getFollowingItemEntityTailColor(){
        return 10;
    }
}
