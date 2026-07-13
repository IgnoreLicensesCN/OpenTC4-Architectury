package thaumcraft.api.wands.focus.upgrade.impl.excavationfocus;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class TreasureFocusUpgrade extends FocusUpgradeType {
    public static final TreasureFocusUpgrade INSTANCE = new TreasureFocusUpgrade();
    protected TreasureFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected TreasureFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "treasure"),
                UnmodifiableAspectList.of(Aspects.GREED, 1)
        );
    }
}
