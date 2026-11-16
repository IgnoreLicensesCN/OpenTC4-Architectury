package thaumcraft.api.listeners.croputils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface IReplantPlaceableCropItem {
    void opentc4$replantAt(ServerLevel level, BlockPos pos, ItemStack seedStack, Entity planter);
}
