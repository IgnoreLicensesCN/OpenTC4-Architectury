package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemRegenDelayModifierItem extends IGolemAbilityModifierItem {
    int getModifiedRegenDelay(BaseGolemEntity golem, ItemStack upgradeStack, int regenDelay);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.regenDelay += getModifiedRegenDelay(golem,upgradeStack, abilitiesData.regenDelay);
    }
}