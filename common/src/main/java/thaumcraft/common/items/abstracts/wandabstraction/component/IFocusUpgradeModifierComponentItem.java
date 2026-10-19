package thaumcraft.common.items.abstracts.wandabstraction.component;

import com.linearity.opentc4.annotations.Modifiable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;

public interface IFocusUpgradeModifierComponentItem {
    @Modifiable Object2IntMap<FocusUpgradeType> modifyWandUpgrades(ItemStack componentStack,@Modifiable Object2IntMap<FocusUpgradeType> wandUpgrades);
}
