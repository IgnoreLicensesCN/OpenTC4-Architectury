package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;

public interface IGolemBaseHomeRangeAdderItem extends IGolemAbilityModifierItem {
    int modifyGolemBaseHomeRange(BaseGolemEntity golem, ItemStack upgradeStack, int homeRange);
    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData){
        abilitiesData.homeRange = modifyGolemBaseHomeRange(golem,upgradeStack,abilitiesData.homeRange);
    }
}
