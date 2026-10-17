package thaumcraft.common.items.golem.upgrade.abstracts;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemAbilityModifierItem {
    void modifyGolemAbility(BaseGolemEntity golem,ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData);
}
