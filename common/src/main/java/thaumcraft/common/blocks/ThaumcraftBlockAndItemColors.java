package thaumcraft.common.blocks;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.items.ThaumcraftItems;

public class ThaumcraftBlockAndItemColors {

    public static void init() {
        BlockColors blockColors = Minecraft.getInstance()
                .getBlockColors();

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.AIR_CRYSTAL.particleColors[0], ThaumcraftBlocks.AIR_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.FIRE_CRYSTAL.particleColors[0], ThaumcraftBlocks.FIRE_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.WATER_CRYSTAL.particleColors[0], ThaumcraftBlocks.WATER_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.EARTH_CRYSTAL.particleColors[0], ThaumcraftBlocks.EARTH_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ORDER_CRYSTAL.particleColors[0], ThaumcraftBlocks.ORDER_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ENTROPY_CRYSTAL.particleColors[0], ThaumcraftBlocks.ENTROPY_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, pos, i)
                        -> ThaumcraftBlocks.MIXED_CRYSTAL.particleColors[(int) (((pos==null?0x39c5bb:pos.asLong() * 3129871L) ^ 0xBADC0FFEEL) % ThaumcraftBlocks.MIXED_CRYSTAL.particleColors.length)],
                ThaumcraftBlocks.STRANGE_CRYSTALS
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.STRANGE_CRYSTALS.particleColors[0], ThaumcraftBlocks.STRANGE_CRYSTALS
        );

        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[0],
                ThaumcraftItems.AIR_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[1],
                ThaumcraftItems.FIRE_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[2],
                ThaumcraftItems.WATER_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[3],
                ThaumcraftItems.EARTH_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[4],
                ThaumcraftItems.ORDER_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[5],
                ThaumcraftItems.ENTROPY_SHARD
        );
    }
}
