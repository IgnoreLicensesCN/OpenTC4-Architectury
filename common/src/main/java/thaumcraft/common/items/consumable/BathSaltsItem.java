package thaumcraft.common.items.consumable;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.FluidState;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.items.abstracts.ILiquidListenableItem;

public class BathSaltsItem extends Item implements ILiquidListenableItem {
    public BathSaltsItem(Properties properties) {
        super(properties);
    }
    public BathSaltsItem() {
        this(new Properties().rarity(Rarity.RARE));
    }

    @Override
    public void whenInFluid(ItemEntity itemEntity, ItemStack stack, BlockPos pos, FluidState fluidState) {
        if (fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
            stack.shrink(1);
            itemEntity.level().setBlockAndUpdate(pos, ThaumcraftFluids.PURE_FLUID_SOURCE.defaultFluidState().createLegacyBlock());
        }
    }

    //they say there's a logic that this will be consumed and add effect for player but i didn't find it.
}
