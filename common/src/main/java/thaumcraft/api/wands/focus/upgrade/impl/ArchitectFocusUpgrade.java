package thaumcraft.api.wands.focus.upgrade.impl;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class ArchitectFocusUpgrade extends FocusUpgradeType {
    public static final ArchitectFocusUpgrade INSTANCE = new ArchitectFocusUpgrade();
    protected ArchitectFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected ArchitectFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "architect"),
                UnmodifiableAspectList.of(Aspects.CRAFT, 1)
        );
    }
}
