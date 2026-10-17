package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemStrengthModifierItem extends IGolemAbilityModifierItem {
    int getStrengthModified(BaseGolemEntity golem,ItemStack upgradeStack, int strength);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.strength = getStrengthModified(golem,upgradeStack,abilitiesData.strength);
    }
}