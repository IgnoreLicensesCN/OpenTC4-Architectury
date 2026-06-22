package thaumcraft.api.wands.focus.upgrade.impl;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class ExtendFocusUpgrade extends FocusUpgradeType {
    public static final ExtendFocusUpgrade INSTANCE = new ExtendFocusUpgrade();
    protected ExtendFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected ExtendFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(
                Thaumcraft.MOD_ID, "extend"),
                UnmodifiableAspectList.of(Aspects.EXCHANGE, 1)
        );
    }
}
