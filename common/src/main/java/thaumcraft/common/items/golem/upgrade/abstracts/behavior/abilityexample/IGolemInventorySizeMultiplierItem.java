package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemInventorySizeMultiplierItem extends IGolemAbilityModifierItem {
    float modifyInventorySizeMultiplier(BaseGolemEntity golem, ItemStack upgradeStack,float inventorySizeMultiplier, float basicInventorySizeMultiplier);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData) {
        abilitiesData.inventorySizeMultiplier = modifyInventorySizeMultiplier(golem,upgradeStack,abilitiesData.inventorySizeMultiplier, abilitiesData.properties.basicInventorySizeMultiplier);
    }
}
