package thaumcraft.common.items.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface IRedirectBreakPosItem {

    BlockPos redirectBreakPosToPos(BlockPos originalPos, Entity entityUsingTool /*player for now*/, ItemStack usingStack);
}
