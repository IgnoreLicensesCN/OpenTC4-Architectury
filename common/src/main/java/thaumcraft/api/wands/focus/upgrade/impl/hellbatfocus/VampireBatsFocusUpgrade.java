package thaumcraft.api.wands.focus.upgrade.impl.hellbatfocus;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class VampireBatsFocusUpgrade extends FocusUpgradeType {
    public static final VampireBatsFocusUpgrade INSTANCE = new VampireBatsFocusUpgrade();
    protected VampireBatsFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected VampireBatsFocusUpgrade() {
        this(
                FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "vampire_bats"),
                UnmodifiableAspectList.of(
                        Aspects.HUNGER, 1,
                        Aspects.LIFE, 1)
        );
    }
    @Override
    public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return easyIncompatibleCheck(focusStack,focusItem,this);
    }
}
