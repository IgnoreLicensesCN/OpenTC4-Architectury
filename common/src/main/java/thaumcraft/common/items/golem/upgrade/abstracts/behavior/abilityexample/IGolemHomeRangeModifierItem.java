package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemHomeRangeModifierItem extends IGolemAbilityModifierItem {
    float modifyHomeRange(BaseGolemEntity baseGolemEntity, ItemStack stack, float range, float defaultRange);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData){
        abilitiesData.homeRange = modifyHomeRange(golem,upgradeStack,abilitiesData.homeRange,abilitiesData.properties.basicHomeRange);
    }
}
