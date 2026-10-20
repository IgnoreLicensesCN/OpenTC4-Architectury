package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemRegenDelayModifierItem extends IGolemAbilityModifierItem {
    int getModifiedRegenDelay(BaseGolemEntity golem, ItemStack upgradeStack, int regenDelay);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.regenDelay += getModifiedRegenDelay(golem,upgradeStack, abilitiesData.regenDelay);
    }
}