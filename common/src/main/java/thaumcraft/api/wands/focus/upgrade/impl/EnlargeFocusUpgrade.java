package thaumcraft.api.wands.focus.upgrade.impl;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class EnlargeFocusUpgrade extends FocusUpgradeType {
    public static final EnlargeFocusUpgrade INSTANCE = new EnlargeFocusUpgrade();
    protected EnlargeFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected EnlargeFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(
                Thaumcraft.MOD_ID, "enlarge"),
                UnmodifiableAspectList.of(Aspects.TRAVEL, 1)
        );
    }
}
