package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface IWandUpgradeModifier {
    Map<FocusUpgradeType,Integer> modifyWandUpgrades(Map<FocusUpgradeType,Integer> wandUpgrades);
}
