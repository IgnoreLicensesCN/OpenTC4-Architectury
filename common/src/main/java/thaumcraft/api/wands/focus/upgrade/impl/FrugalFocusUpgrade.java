package thaumcraft.api.wands.focus.upgrade.impl;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class FrugalFocusUpgrade extends FocusUpgradeType {
    public static final FrugalFocusUpgrade INSTANCE = new FrugalFocusUpgrade();
    protected FrugalFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected FrugalFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "frugal"),
                UnmodifiableAspectList.of(Aspects.HUNGER, 1)
        );
    }
}
