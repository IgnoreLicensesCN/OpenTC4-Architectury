package thaumcraft.api.wands.focus.upgrade.impl.frostfocus;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class ScatterShotFocusUpgrade extends FocusUpgradeType {
    public static final ScatterShotFocusUpgrade INSTANCE = new ScatterShotFocusUpgrade();
    protected ScatterShotFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected ScatterShotFocusUpgrade() {
        this(
                FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "scatter_shot"),
                UnmodifiableAspectList.of(Aspects.COLD, 1, Aspects.WEAPON, 1)
        );
    }

    @Override
    public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return easyIncompatibleCheck(focusStack, focusItem, this, IceBoulderFocusUpgrade.INSTANCE);
    }
}
