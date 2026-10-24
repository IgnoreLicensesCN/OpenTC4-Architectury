package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemRegenAmountModifierItem extends IGolemAbilityModifierItem {
    float getModifiedRegenAmount(BaseGolemEntity golem, ItemStack upgradeStack, float regenAmount, float defaultRegenAmount);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData) {
        abilitiesData.regenAmount = getModifiedRegenAmount(golem,upgradeStack, abilitiesData.regenAmount, golem.getBasicAbilityProperties().regenAmount);
    }
}