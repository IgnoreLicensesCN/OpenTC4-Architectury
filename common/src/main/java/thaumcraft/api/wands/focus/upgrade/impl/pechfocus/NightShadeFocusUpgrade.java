package thaumcraft.api.wands.focus.upgrade.impl.pechfocus;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class NightShadeFocusUpgrade extends FocusUpgradeType {
    public static final NightShadeFocusUpgrade INSTANCE = new NightShadeFocusUpgrade();
    protected NightShadeFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected NightShadeFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "night_shade"),
                UnmodifiableAspectList.of(
                        Aspects.LIFE, 1,
                        Aspects.POISON, 1,
                        Aspects.MAGIC, 1
                )
        );
    }

    @Override
    public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return easyIncompatibleCheck(focusStack,focusItem,this);
    }
}
