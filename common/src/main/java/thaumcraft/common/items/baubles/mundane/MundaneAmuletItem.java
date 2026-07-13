package thaumcraft.common.items.baubles.mundane;

import io.wispforest.accessories.api.AccessoryItem;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

public class MundaneAmuletItem extends AccessoryItem implements IAugmentationRunicShieldProviderItem {
    public MundaneAmuletItem(Properties properties) {
        super(properties);
    }
    public MundaneAmuletItem() {
        super(new Properties().stacksTo(1));
    }

}
