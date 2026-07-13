package thaumcraft.api.wands.focus.upgrade.impl;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class PotencyFocusUpgrade extends FocusUpgradeType {
    public static final PotencyFocusUpgrade INSTANCE = new PotencyFocusUpgrade();
    protected PotencyFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected PotencyFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "potency"),
                UnmodifiableAspectList.of(Aspects.WEAPON, 1));
    }
}
