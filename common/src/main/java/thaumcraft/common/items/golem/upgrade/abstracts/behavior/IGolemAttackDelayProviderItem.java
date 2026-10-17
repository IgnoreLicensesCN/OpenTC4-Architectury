package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemAttackDelayProviderItem extends IGolemAbilityModifierItem {
    int getAttackDelayModified(BaseGolemEntity golem,ItemStack upgradeStack, int attackDelay);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.attackDelay = getAttackDelayModified(golem,upgradeStack,abilitiesData.attackDelay);
    }
}