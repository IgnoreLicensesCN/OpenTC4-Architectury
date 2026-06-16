package thaumcraft.common.items.baubles.ring.runicring;

import io.wispforest.accessories.api.AccessoryItem;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;
import thaumcraft.common.runicshield.ThaumcraftRunicShieldTypes;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem.ChargeCache.getAdditionalCommonAddedForDefault;

public class ChargedRunicShieldRingItem extends AccessoryItem implements IAugmentationRunicShieldProviderItem {
    public ChargedRunicShieldRingItem(Properties properties) {
        super(properties);
    }
    public ChargedRunicShieldRingItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    private static final Object2IntMap<AbstractRunicShieldType<?>> BASIC_SHIELDS = Object2IntMaps.unmodifiable(
            new Object2IntArrayMap<>(){{
                put(ThaumcraftRunicShieldTypes.COMMON,3);
                put(ThaumcraftRunicShieldTypes.CHARGED,1);
            }}
    );
    @Override
    public @Unmodifiable Object2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack itemstack) {
        return getAdditionalCommonAddedForDefault(BASIC_SHIELDS,getAugmentationForStack(itemstack));
    }
}
