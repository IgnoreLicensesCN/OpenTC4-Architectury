package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemRegenAmountModifierItem extends IGolemModifierItem, IGolemAbilityModifierItem {
    int getModifiedRegenAmount(BaseGolemEntity golem, ItemStack upgradeStack, int regenAmount);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.regenAmount += getModifiedRegenAmount(golem,upgradeStack, abilitiesData.regenAmount);
    }
}