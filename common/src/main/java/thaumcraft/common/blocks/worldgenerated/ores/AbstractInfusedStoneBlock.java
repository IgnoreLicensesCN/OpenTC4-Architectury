package thaumcraft.common.blocks.worldgenerated.ores;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class AbstractInfusedStoneBlock extends DropExperienceBlock {
    public static final IntProvider EXP_DROP = ConstantInt.of(1);
    public final int rgbColor;

    public AbstractInfusedStoneBlock(Properties properties, int rgbColor) {
        super(properties, EXP_DROP);
        this.rgbColor = rgbColor;
    }

    public AbstractInfusedStoneBlock(int rgbColor) {
        super(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)
                .sound(SoundType.STONE)
                .strength(5.f, 1.5f), EXP_DROP
        );
        this.rgbColor = rgbColor;
    }
    @Override
    protected void tryDropExperience(ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, IntProvider intProvider) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0) {
            int i = intProvider.sample(serverLevel.random);
            if (i > 0) {
                this.popExperience(serverLevel, blockPos, i
                        + serverLevel.random.nextInt(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemStack) + 2)
                );
            }
        }
    }
}
