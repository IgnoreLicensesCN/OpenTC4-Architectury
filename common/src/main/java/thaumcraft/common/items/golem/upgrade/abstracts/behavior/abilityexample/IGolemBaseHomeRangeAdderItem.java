package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemBaseHomeRangeAdderItem extends IGolemAbilityModifierItem {
    float modifyGolemBaseHomeRange(BaseGolemEntity golem, ItemStack upgradeStack, float homeRange, float defaultHomeRange);
    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData){
        abilitiesData.homeRange = modifyGolemBaseHomeRange(golem,upgradeStack,abilitiesData.homeRange, golem.getBasicAbilityProperties().homeRange);
    }
}
