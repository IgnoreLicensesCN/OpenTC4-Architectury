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

public class IceBoulderFocusUpgrade extends FocusUpgradeType {
    public static final IceBoulderFocusUpgrade INSTANCE = new IceBoulderFocusUpgrade();
    protected IceBoulderFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected IceBoulderFocusUpgrade() {
        this(
                FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "ice_boulder"),
                UnmodifiableAspectList.of(Aspects.COLD, 1, Aspects.CRYSTAL, 1)
        );
    }

    @Override
    public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return easyIncompatibleCheck(focusStack, focusItem, this, ScatterShotFocusUpgrade.INSTANCE);
    }
}
