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


        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0){
                        return ThaumcraftBlocks.AIR_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                },ThaumcraftBlocks.AIR_INFUSED_STONE
        );

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0){
                        return ThaumcraftBlocks.FIRE_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                },ThaumcraftBlocks.FIRE_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0){
                        return ThaumcraftBlocks.WATER_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                },ThaumcraftBlocks.WATER_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0){
                        return ThaumcraftBlocks.EARTH_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                },ThaumcraftBlocks.EARTH_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0){
                        return ThaumcraftBlocks.ORDER_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                },ThaumcraftBlocks.ORDER_INFUSED_STONE
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0){
                        return ThaumcraftBlocks.ENTROPY_INFUSED_STONE.rgbColor;
                    }
                    return 0xFFFFFF;
                },ThaumcraftBlocks.ENTROPY_INFUSED_STONE
        );
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.WHITE_TALLOW_CANDLE.color,ThaumcraftBlocks.WHITE_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.ORANGE_TALLOW_CANDLE.color,ThaumcraftBlocks.ORANGE_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.MAGENTA_TALLOW_CANDLE.color,ThaumcraftBlocks.MAGENTA_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.LIGHT_BLUE_TALLOW_CANDLE.color,ThaumcraftBlocks.LIGHT_BLUE_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.YELLOW_TALLOW_CANDLE.color,ThaumcraftBlocks.YELLOW_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.LIME_TALLOW_CANDLE.color,ThaumcraftBlocks.LIME_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.PINK_TALLOW_CANDLE.color,ThaumcraftBlocks.PINK_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.GRAY_TALLOW_CANDLE.color,ThaumcraftBlocks.GRAY_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.LIGHT_GRAY_TALLOW_CANDLE.color,ThaumcraftBlocks.LIGHT_GRAY_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.CYAN_TALLOW_CANDLE.color,ThaumcraftBlocks.CYAN_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.PURPLE_TALLOW_CANDLE.color,ThaumcraftBlocks.PURPLE_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.BLUE_TALLOW_CANDLE.color,ThaumcraftBlocks.BLUE_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.BROWN_TALLOW_CANDLE.color,ThaumcraftBlocks.BROWN_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.GREEN_TALLOW_CANDLE.color,ThaumcraftBlocks.GREEN_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.RED_TALLOW_CANDLE.color,ThaumcraftBlocks.RED_TALLOW_CANDLE);
        blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.BLACK_TALLOW_CANDLE.color,ThaumcraftBlocks.BLACK_TALLOW_CANDLE);
    }
}
