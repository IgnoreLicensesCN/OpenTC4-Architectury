package thaumcraft.common.items.baubles.mundane;

import io.wispforest.accessories.api.AccessoryItem;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

public class MundaneBeltItem extends AccessoryItem implements IAugmentationRunicShieldProviderItem {
    public MundaneBeltItem(Properties properties) {
        super(properties);
    }
    public MundaneBeltItem() {
        super(new Properties().stacksTo(1));
    }

}
