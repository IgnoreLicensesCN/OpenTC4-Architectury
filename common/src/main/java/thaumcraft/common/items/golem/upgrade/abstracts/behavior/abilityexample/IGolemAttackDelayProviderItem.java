package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemAttackDelayProviderItem extends IGolemAbilityModifierItem {
    float getAttackDelayModified(BaseGolemEntity golem,ItemStack upgradeStack, float attackDelay);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.attackDelay = getAttackDelayModified(golem,upgradeStack,abilitiesData.attackDelay);
    }
}