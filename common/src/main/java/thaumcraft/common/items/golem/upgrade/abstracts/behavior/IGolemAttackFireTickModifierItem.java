package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

public interface IGolemAttackFireTickModifierItem extends IGolemAbilityModifierItem {
    int modifyAttackFireTick(BaseGolemEntity golem, ItemStack upgradeStack, int attackFireTicks);
    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        abilitiesData.attackFireTicks = modifyAttackFireTick(golem, upgradeStack, abilitiesData.attackFireTicks);
    }
}
