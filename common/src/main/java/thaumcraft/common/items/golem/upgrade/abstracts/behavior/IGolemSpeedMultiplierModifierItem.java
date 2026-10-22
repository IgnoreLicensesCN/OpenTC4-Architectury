package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemSpeedMultiplierModifierItem extends IGolemAbilityModifierItem {
    /*NaN if make invalid*/float modifySpeedMultiplier(BaseGolemEntity golem, ItemStack upgradeStack,float speedMultiplier);
    default void modifyGolemAbility(BaseGolemEntity golem,ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData){
        abilitiesData.speedMultiplier = modifySpeedMultiplier(golem,upgradeStack,abilitiesData.speedMultiplier);
    }
}
