package thaumcraft.common.items.baubles.amulet;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableObject2IntMap;
import io.wispforest.accessories.api.AccessoryItem;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;
import thaumcraft.common.runicshield.ThaumcraftRunicShieldTypes;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

public class RunicAmuletItem extends AccessoryItem implements IAugmentationRunicShieldProviderItem {
    public RunicAmuletItem(Properties properties) {
        super(properties);
    }
    public RunicAmuletItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }


    private static final CalcCacheableObject2IntMap<AbstractRunicShieldType<?>> BASIC_SHIELDS =
            new CalcCacheableObject2IntMap<>(Object2IntMaps.unmodifiable(
                    new Object2IntArrayMap<>(){{
                        put(ThaumcraftRunicShieldTypes.COMMON,8);
                    }}
            ),
                    true);
    @Override
    public @Unmodifiable CalcCacheableObject2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack itemstack) {
        return BASIC_SHIELDS.add(getRunicChargeAdditional(itemstack),SORTED_SHIELD_MAP_PROVIDER);
    }
}
