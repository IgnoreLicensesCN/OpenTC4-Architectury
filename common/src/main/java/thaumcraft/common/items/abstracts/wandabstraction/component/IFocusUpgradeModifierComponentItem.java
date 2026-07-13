package thaumcraft.common.items.abstracts.wandabstraction.component;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;

public interface IFocusUpgradeModifierComponentItem {
    Object2IntMap<FocusUpgradeType> modifyWandUpgrades(ItemStack componentStack, Object2IntMap<FocusUpgradeType> wandUpgrades);
}
