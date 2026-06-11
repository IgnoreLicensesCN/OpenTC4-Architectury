package thaumcraft.common.blocks;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.GrassColor;
import thaumcraft.common.blocks.abstracts.AbstractCrystalBlock;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.world.biomes.BiomeGenTaint;

public class ThaumcraftBlockAndItemColors {

    public static void init() {
        BlockColors blockColors = Minecraft.getInstance()
                .getBlockColors();

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_CRYSTAL.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_CRYSTAL.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_CRYSTAL.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_CRYSTAL.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_CRYSTAL.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_CRYSTAL.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_CRYSTAL
        );
        blockColors.register(
                (blockState, blockAndTintGetter, pos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.MIXED_CRYSTAL.particleColors[(int) (((pos == null ? 0x39c5bb : pos.asLong() * 3129871L) ^ 0xBADC0FFEEL) % ThaumcraftBlocks.ThaumcraftBlockInstances.MIXED_CRYSTAL.particleColors.length)],
                ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS.particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS
        );

        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[0],
                ThaumcraftItems.ThaumcraftItemInstances.AIR_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[1],
                ThaumcraftItems.ThaumcraftItemInstances.FIRE_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[2],
                ThaumcraftItems.ThaumcraftItemInstances.WATER_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[3],
                ThaumcraftItems.ThaumcraftItemInstances.EARTH_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[4],
                ThaumcraftItems.ThaumcraftItemInstances.ORDER_SHARD
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[5],
                ThaumcraftItems.ThaumcraftItemInstances.ENTROPY_SHARD
        );


        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_INFUSED_STONE
        );

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.WHITE_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.WHITE_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.ORANGE_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.ORANGE_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.MAGENTA_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.MAGENTA_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_BLUE_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_BLUE_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.YELLOW_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.YELLOW_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.LIME_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.LIME_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.PINK_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.PINK_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.GRAY_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.GRAY_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_GRAY_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_GRAY_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.CYAN_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.CYAN_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.PURPLE_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.PURPLE_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.BLUE_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.BLUE_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.BROWN_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.BROWN_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.GREEN_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.GREEN_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.RED_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.RED_TALLOW_CANDLE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.BLACK_TALLOW_CANDLE.color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.BLACK_TALLOW_CANDLE
        );

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> {
                    if (i == 1) {
                        return BiomeGenTaint.TAINT_COLOR;
                    }
                    return GrassColor.get(0.5D, 1.0D);
                },
                ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_TAINT,
                ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS
        );
    }
}
