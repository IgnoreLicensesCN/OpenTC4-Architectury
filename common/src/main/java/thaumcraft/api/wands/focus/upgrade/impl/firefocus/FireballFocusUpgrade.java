package thaumcraft.api.wands.focus.upgrade.impl.firefocus;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class FireballFocusUpgrade extends FocusUpgradeType {
    public static final FireballFocusUpgrade INSTANCE = new FireballFocusUpgrade();
    protected FireballFocusUpgrade(FocusUpgradeTypeResourceLocation id, AspectList<Aspect> aspects) {
        super(id, aspects);
    }
    protected FireballFocusUpgrade() {
        this(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "fireball"),
                UnmodifiableAspectList.of(Aspects.DARKNESS, 1)
        );
    }

    @Override
    public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return easyIncompatibleCheck(focusStack, focusItem,
                this,
                AlchemistsFireFocusUpgrade.INSTANCE,
                FireBeamFocusUpgrade.INSTANCE
        );
    }
}
