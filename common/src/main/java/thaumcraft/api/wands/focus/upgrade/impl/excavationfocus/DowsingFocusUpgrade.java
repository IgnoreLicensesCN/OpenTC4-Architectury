package thaumcraft.api.wands.focus.upgrade.impl.excavationfocus;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class DowsingFocusUpgrade extends FocusUpgradeType {
    public static final DowsingFocusUpgrade INSTANCE = new DowsingFocusUpgrade();
    protected DowsingFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected DowsingFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "dowsing"),
                UnmodifiableAspectList.of(Aspects.MINE, 1)
        );
    }
}
