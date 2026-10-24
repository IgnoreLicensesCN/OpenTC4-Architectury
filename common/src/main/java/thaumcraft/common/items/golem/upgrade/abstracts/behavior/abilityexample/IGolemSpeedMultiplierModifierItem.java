package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

public interface IGolemSpeedMultiplierModifierItem extends IGolemAbilityModifierItem {
    /*NaN if make invalid*/float modifySpeedMultiplier(BaseGolemEntity golem, ItemStack upgradeStack,float speedMultiplier,float basicSpeedMultiplier);
    default void modifyGolemAbility(BaseGolemEntity golem,ItemStack upgradeStack, GolemAbilitiesData abilitiesData){
        abilitiesData.speedMultiplier = modifySpeedMultiplier(golem,upgradeStack,abilitiesData.speedMultiplier,abilitiesData.properties.basicSpeedMultiplier);
    }
}
