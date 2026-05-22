package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface IWandUpgradeModifier {
    Map<FocusUpgradeType,Integer> modifyWandUpgrades(ItemStack componentStack,Map<FocusUpgradeType,Integer> wandUpgrades);
}
