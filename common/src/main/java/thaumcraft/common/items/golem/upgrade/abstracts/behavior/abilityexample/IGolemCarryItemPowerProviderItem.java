package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemCarryItemPowerProviderItem extends IGolemAbilityModifierItem {
    float modifyCarryCount(BaseGolemEntity golem, ItemStack upgradeStack,float carry, float defaultCarry);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData) {
        abilitiesData.carryPower = modifyCarryCount(golem,upgradeStack, abilitiesData.carryPower, golem.getBasicAbilityProperties().carryPower);
    }
}