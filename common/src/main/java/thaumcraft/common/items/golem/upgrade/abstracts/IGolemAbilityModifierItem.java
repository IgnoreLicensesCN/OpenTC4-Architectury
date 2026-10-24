package thaumcraft.common.items.golem.upgrade.abstracts;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;

//maybe you dont have to modify ability data?it just a lifecycle-point
public interface IGolemAbilityModifierItem extends IGolemModifierItem{
    void modifyGolemAbility(BaseGolemEntity golem,ItemStack upgradeStack, GolemAbilitiesData abilitiesData);
}
