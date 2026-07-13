package thaumcraft.common.items.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;

public interface ILiquidListenableItem {
    void whenInFluid(ItemEntity itemEntity, ItemStack stack, BlockPos pos, FluidState fluidState);
}
