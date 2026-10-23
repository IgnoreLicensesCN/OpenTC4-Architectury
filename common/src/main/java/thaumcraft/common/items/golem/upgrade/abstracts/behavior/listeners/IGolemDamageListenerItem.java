package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemDamageListenerItem extends IGolemModifierItem {
    void onDamaged(BaseGolemEntity golem, ItemStack upgradeStack, DamageSource damageSource,/*won't be NaN*/ float damage);
}
