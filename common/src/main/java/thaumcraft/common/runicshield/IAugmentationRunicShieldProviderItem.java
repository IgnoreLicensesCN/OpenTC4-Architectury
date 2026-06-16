package thaumcraft.common.runicshield;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableObject2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static com.linearity.opentc4.Consts.AugmentationRunicShieldTagAccessors.RUNIC_AUGMENTATION_LEVEL;

public interface IAugmentationRunicShieldProviderItem extends IRunicShieldProviderItem {
    class ChargeCache{
        private static final Int2ObjectMap<CalcCacheableObject2IntMap<AbstractRunicShieldType<?>>> chargeCache = new Int2ObjectOpenHashMap<>() {
            {
                for (int i = 1; i <= 10; i++) {
                    int finalI = i;
                    put(i, new CalcCacheableObject2IntMap<>(
                            Object2IntMaps.unmodifiable(
                                    new Object2IntOpenHashMap<>() {{
                                        put(ThaumcraftRunicShieldTypes.COMMON, finalI);
                                    }}
                            ),
                            true
                    ));
                }
            }
        };
    }

    @Override
    default @Unmodifiable CalcCacheableObject2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack itemstack){
        return getRunicChargeAdditional(itemstack);
    };

    default @Unmodifiable CalcCacheableObject2IntMap<AbstractRunicShieldType<?>> getRunicChargeAdditional(ItemStack itemstack){
        int augmentation = getAugmentationForStack(itemstack);
        if (augmentation == 0){
            return CalcCacheableObject2IntMap.empty();
        }
        return ChargeCache.chargeCache.computeIfAbsent(
                augmentation,
                aug -> new CalcCacheableObject2IntMap<>(
                        Object2IntMaps.unmodifiable(
                                new Object2IntOpenHashMap<>() {{
                                    put(ThaumcraftRunicShieldTypes.COMMON, aug);
                                }}
                        ),
                        true
                )
        );
    }



    default int getAugmentationForStack(ItemStack stack){
        var tag = stack.getTag();
        if(tag == null){
            return 0;
        }
        return RUNIC_AUGMENTATION_LEVEL.readIntFromCompoundTag(tag);
    }
    default void setAugmentationForStack(ItemStack stack, int level){
        var tag = stack.getOrCreateTag();
        RUNIC_AUGMENTATION_LEVEL.writeIntToCompoundTag(tag, level);
    }
}
