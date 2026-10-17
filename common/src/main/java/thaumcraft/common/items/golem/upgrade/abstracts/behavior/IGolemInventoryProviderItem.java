package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemInventoryProviderItem extends IGolemModifierItem , IGolemAbilityModifierItem {
    int getInventorySizeCanProvide(BaseGolemEntity golem, ItemStack upgradeStack);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.inventorySize += getInventorySizeCanProvide(golem,upgradeStack);
    }
}
