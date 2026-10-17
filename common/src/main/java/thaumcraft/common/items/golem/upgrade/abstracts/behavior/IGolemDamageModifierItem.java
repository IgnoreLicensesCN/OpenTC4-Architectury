package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemDamageModifierItem extends IGolemModifierItem {
    /*NaN if make invalid*/float modifyDamage(BaseGolemEntity golem, ItemStack upgradeStack, DamageSource damageSource,/*maybe NaN*/ float damage);
}
