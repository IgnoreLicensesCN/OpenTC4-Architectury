package thaumcraft.common.blocks;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.mixin.client.render.ModelPredicateProviderRegistryAccessor;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GrassColor;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.blocks.abstracts.AbstractCrystalBlock;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.items.abstracts.IGoggles;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.biomes.BiomeGenTaint;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static thaumcraft.common.items.ThaumcraftItemInstances.COMPASS_STONE;
import static thaumcraft.common.items.ThaumcraftItemInstances.SANITY_CHECKER;
import static thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity.ALL_NODES;

public class ThaumcraftBlockAndItemColors {

    public static void init() {
        BlockColors blockColors = Minecraft.getInstance()
                .getBlockColors();

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_CRYSTAL().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_CRYSTAL()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_CRYSTAL().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_CRYSTAL()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_CRYSTAL().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_CRYSTAL()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_CRYSTAL().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_CRYSTAL()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_CRYSTAL().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_CRYSTAL()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_CRYSTAL().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_CRYSTAL()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, pos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.MIXED_CRYSTAL().particleColors[(int) (((pos == null ? 0x39c5bb : pos.asLong() * 3129871L) ^ 0xBADC0FFEEL) % ThaumcraftBlocks.ThaumcraftBlockInstances.MIXED_CRYSTAL().particleColors.length)],
                ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS().particleColors[0], ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS()
        );

        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[0],
                ThaumcraftItemInstances.AIR_SHARD()
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[1],
                ThaumcraftItemInstances.FIRE_SHARD()
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[2],
                ThaumcraftItemInstances.WATER_SHARD()
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[3],
                ThaumcraftItemInstances.EARTH_SHARD()
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[4],
                ThaumcraftItemInstances.ORDER_SHARD()
        );
        ColorHandlerRegistry.registerItemColors(
                (itemStack, i) -> AbstractCrystalBlock.CRYSTAL_COLORS[5],
                ThaumcraftItemInstances.ENTROPY_SHARD()
        );


        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_INFUSED_STONE().rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_INFUSED_STONE()
        );

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_INFUSED_STONE().rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_INFUSED_STONE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_INFUSED_STONE().rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_INFUSED_STONE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_INFUSED_STONE().rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_INFUSED_STONE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_INFUSED_STONE().rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_INFUSED_STONE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i)
                        -> {
                    if (i == 0) {
                        return ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_INFUSED_STONE().rgbColor;
                    }
                    return 0xFFFFFF;
                }, ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_INFUSED_STONE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.WHITE_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.WHITE_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.ORANGE_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.ORANGE_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.MAGENTA_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.MAGENTA_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_BLUE_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_BLUE_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.YELLOW_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.YELLOW_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.LIME_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.LIME_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.PINK_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.PINK_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.GRAY_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.GRAY_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_GRAY_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_GRAY_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.CYAN_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.CYAN_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.PURPLE_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.PURPLE_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.BLUE_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.BLUE_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.BROWN_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.BROWN_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.GREEN_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.GREEN_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.RED_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.RED_TALLOW_CANDLE()
        );
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> ThaumcraftBlocks.ThaumcraftBlockInstances.BLACK_TALLOW_CANDLE().color,
                ThaumcraftBlocks.ThaumcraftBlockInstances.BLACK_TALLOW_CANDLE()
        );

        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) -> {
                    if (i == 1) {
                        return BiomeGenTaint.TAINT_COLOR;
                    }
                    return GrassColor.get(0.5D, 1.0D);
                },
                ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_TAINT(),
                ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS()
        );

        //TODO:[maybe wont finished]rotate the compass stone like compass(wtf)
        ModelPredicateProviderRegistryAccessor.register(
                COMPASS_STONE(),
                new ResourceLocation("near_node"), (itemStack, clientLevel, livingEntity, i) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    }
                    AtomicBoolean foundNode = new AtomicBoolean(false);

                    var hasGoggles = false;
                    for (var stack:livingEntity.getArmorSlots()){
                        if (stack.getItem() instanceof IGoggles goggles && goggles.showAsWearingGogglesOfRevealing(
                                stack,livingEntity
                        )) {
                            hasGoggles = true;
                            break;
                        }
                    }
                    boolean finalHasGoggles = hasGoggles;
                    ALL_NODES.get(clientLevel).forItemsNearPosWithBreakWithRange(
                            livingEntity.blockPosition(),
                            nodeBE -> {
                                if (nodeBE.getNodeType() == NodeType.DARK){
                                    var nodePos = nodeBE.getBlockPos();

                                    if (EntityUtils.isVisibleTo(
                                            finalHasGoggles ? (float) Math.PI/2 //added to avoid players' anger
                                            :0.66F,
                                            livingEntity,
                                            nodePos.getCenter(),
                                            256.0F
                                    )
                                    ){
                                        foundNode.set(true);//TODO:[maybe wont finished]"vote in democracy" to remove this "isVisible" condition needed
                                        return true;
                                    }
                                }
                                return false;
                            },256
                            );
                    if (foundNode.get()) {
                        return 1.0F;
                    }
                    return 0.0F;
                });
        ModelPredicateProviderRegistryAccessor.register(
                SANITY_CHECKER(),
                new ResourceLocation("warp_index"),
                (itemStack, clientLevel, livingEntity, i) -> calculateWarpProgressKindStateForLiving(livingEntity)
        );
    }

    private static final Map<LivingEntity,Integer> mappingCache = new MapMaker().weakKeys().makeMap();
    public static int calculateWarpProgressKindStateForLiving(LivingEntity livingEntity) {
        var info = WarpInfo.getFromLivingEntity(livingEntity);
        if (info == null) return 0;
        if (mappingCache.containsKey(livingEntity) && livingEntity.tickCount%20 != 0){
            return mappingCache.get(livingEntity);
        }
        int[] infoNotScaled = new int[4];
        infoNotScaled[0] = info.getPermWarp();
        infoNotScaled[1] = info.getStickyWarp();
        infoNotScaled[2] = info.getTempWarp();
        infoNotScaled[3] = Math.clamp(100-infoNotScaled[0]-infoNotScaled[1]-infoNotScaled[2],0,100);
        int total = infoNotScaled[0]+infoNotScaled[1]+infoNotScaled[2]+infoNotScaled[3];
        double[] allScaledDouble = {infoNotScaled[0]*8./total,infoNotScaled[1]*8./total,infoNotScaled[2]*8./total,infoNotScaled[3]*8./total};

        int[] totalScaled = new int[4];
        double maxReminder = -1;
        int maxReminderIndex = -1;
        for(int i=0;i<4;i++){
            totalScaled[i] = (int)allScaledDouble[i];
            double remaining = allScaledDouble[i]-totalScaled[i];
            if(remaining > maxReminder){
                maxReminder = remaining;
                maxReminderIndex = i;
            }
        }
        if (maxReminderIndex != -1){
            totalScaled[maxReminderIndex] += 1;
        }
        int resultIndex = arrayTo165Index(totalScaled);
        mappingCache.put(livingEntity,resultIndex);
        return resultIndex;
    }
    private static int arrayTo165Index(int[] arr) {
        // 假设 arr 长度为 4 且总和为 8
        int a = arr[0];
        int b = arr[1];
        int c = arr[2];

        int index = 0;

        // 1. 处理第一个数 a 带来的偏移量
        if (a > 0) {
            // 相当于执行了对 C(8-v+2, 2) 的求和化简
            index += (a * (a * a - 27 * a + 242)) / 6;
        }

        // 2. 处理第二个数 b 带来的偏移量
        if (b > 0) {
            int rem1 = 8 - a;
            // 相当于执行了对 (rem1 - v + 1) 的求sum化简
            index += b * (2 * rem1 - b + 3) / 2;
        }

        // 3. 处理第三个数 c 带来的偏移量
        index += c;

        return index;
    }
}
