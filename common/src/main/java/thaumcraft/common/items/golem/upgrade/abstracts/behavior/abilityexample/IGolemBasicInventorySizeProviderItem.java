package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

@UtilityLikeAbstraction
public interface IGolemBasicInventorySizeProviderItem extends IGolemAbilityModifierItem {
    float modifyBasicInventorySize(BaseGolemEntity golem, ItemStack upgrade, float basicInventorySize);

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData){
        abilitiesData.inventorySize = modifyBasicInventorySize(golem, upgradeStack, abilitiesData.inventorySize);
    }
}
