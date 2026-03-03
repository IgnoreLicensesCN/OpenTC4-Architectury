package thaumcraft.api.blockapi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface IEnchantmentPowerProviderBlock {
    float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos);
}
