package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface WandFocusUpgradeAccessible {

    Map<FocusUpgradeType,Integer> getWandUpgrades(ItemStack stack);
    void storeWandUpgrades(ItemStack stack, Map<FocusUpgradeType,Integer> wandUpgrades);
    void addWandUpgrade(ItemStack stack, FocusUpgradeType type);
}
