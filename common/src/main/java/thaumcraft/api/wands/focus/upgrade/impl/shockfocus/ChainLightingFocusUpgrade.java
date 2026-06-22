package thaumcraft.api.wands.focus.upgrade.impl.shockfocus;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.api.wands.focus.upgrade.impl.firefocus.FireballFocusUpgrade;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class ChainLightingFocusUpgrade extends FocusUpgradeType {
    public static final ChainLightingFocusUpgrade INSTANCE = new ChainLightingFocusUpgrade();
    protected ChainLightingFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected ChainLightingFocusUpgrade() {
        this(
                FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "chain_lighting"),
                UnmodifiableAspectList.of(Aspects.WEATHER, 1)
        );
    }

    @Override
    public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return easyIncompatibleCheck(focusStack, focusItem, this, EarthShockFocusUpgrade.INSTANCE);
    }
}
