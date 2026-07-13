package thaumcraft.api.wands.focus.upgrade.impl.frostfocus;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class AlchemistsFrostFocusUpgrade extends FocusUpgradeType {
    public static final AlchemistsFrostFocusUpgrade INSTANCE = new AlchemistsFrostFocusUpgrade();
    protected AlchemistsFrostFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected AlchemistsFrostFocusUpgrade() {
        this(
                FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "alchemists_frost"),
                UnmodifiableAspectList.of(Aspects.COLD, 1, Aspects.TRAP, 1)
        );
    }

}
