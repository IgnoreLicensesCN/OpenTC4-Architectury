package thaumcraft.common.items.baubles.mundane;

import io.wispforest.accessories.api.AccessoryItem;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

public class MundaneRingItem extends AccessoryItem implements IAugmentationRunicShieldProviderItem {
    public MundaneRingItem(Properties properties) {
        super(properties);
    }
    public MundaneRingItem() {
        super(new Properties().stacksTo(1));
    }

}
