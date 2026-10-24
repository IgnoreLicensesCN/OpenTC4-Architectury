package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

public interface IGolemHomeRangeModifierItem extends IGolemAbilityModifierItem {
    float modifyHomeRange(BaseGolemEntity baseGolemEntity, ItemStack stack, float range, float defaultRange);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData){
        abilitiesData.homeRange = modifyHomeRange(golem,upgradeStack,abilitiesData.homeRange,abilitiesData.properties.basicHomeRange);
    }
}
