package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemAttackFireTickModifierItem extends IGolemAbilityModifierItem {
    float modifyAttackFireTick(BaseGolemEntity golem, ItemStack upgradeStack, float attackFireTicks, float defaultAttackFireTicks);
    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.attackFireTicks = modifyAttackFireTick(golem, upgradeStack, abilitiesData.attackFireTicks, golem.getBasicAbilityProperties().attackFireTicks);
    }
}
