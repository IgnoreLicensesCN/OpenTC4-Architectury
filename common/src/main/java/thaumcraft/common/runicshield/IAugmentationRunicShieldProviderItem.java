package thaumcraft.common.runicshield;

import com.google.common.collect.MapMaker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.Consts.AugmentationRunicShieldTagAccessors.RUNIC_AUGMENTATION_LEVEL;

public interface IAugmentationRunicShieldProviderItem extends IRunicShieldProviderItem {
    class ChargeCache{
        private static final Int2ObjectMap<Object2IntMap<AbstractRunicShieldType<?>>> chargeCache = new Int2ObjectOpenHashMap<>() {
            {
                for (int i = 1; i <= 10; i++) {
                    int finalI = i;
                    put(i, new Object2IntOpenHashMap<>() {{
                        put(ThaumcraftRunicShieldTypes.COMMON, finalI);
                    }});
                }
            }
        };
        private static final Map<
                Object2IntMap<AbstractRunicShieldType<?>>,
                Int2ObjectMap<Object2IntMap<AbstractRunicShieldType<?>>>
                >
                chargeCacheWithBasic = new MapMaker().weakKeys().makeMap();
        public static @Unmodifiable Object2IntMap<AbstractRunicShieldType<?>> getAdditionalCommonAddedForDefault(
                @Unmodifiable Object2IntMap<AbstractRunicShieldType<?>> defaultCharge
                /*you'd better cache it in some place and make sure it's immutable(or this thing is useless)*/,
                int additional
        ){
            if (additional==0){
                return defaultCharge;
            }
            return chargeCacheWithBasic.computeIfAbsent(
                    defaultCharge,
                    _ignored -> new Int2ObjectOpenHashMap<>()
            ).computeIfAbsent(additional,toAdd -> {
                var resultInner = new Object2IntOpenHashMap<>(defaultCharge);
                resultInner.addTo(ThaumcraftRunicShieldTypes.COMMON,toAdd);
                return Object2IntMaps.unmodifiable(resultInner);
            });
        }
    }

    @Override
    default @Unmodifiable Object2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack itemstack){
        return getRunicChargeAdditional(itemstack);
    };

    default @Unmodifiable Object2IntMap<AbstractRunicShieldType<?>> getRunicChargeAdditional(ItemStack itemstack){
        int augmentation = getAugmentationForStack(itemstack);
        if (augmentation == 0){
            return Object2IntMaps.emptyMap();
        }
        var result = ChargeCache.chargeCache.computeIfAbsent(
                augmentation,
                aug -> new Object2IntOpenHashMap<>() {{
                    put(ThaumcraftRunicShieldTypes.COMMON, aug);
                }}
        );
        CACHED_SUM.computeIfAbsent(result,_ignored -> new MapMaker().weakKeys().makeMap());
        return result;
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
