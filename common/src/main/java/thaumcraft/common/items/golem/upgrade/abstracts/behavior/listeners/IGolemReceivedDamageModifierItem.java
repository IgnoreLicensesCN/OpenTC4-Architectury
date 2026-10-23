package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemReceivedDamageModifierItem extends IGolemModifierItem {
    /*NaN if make invalid*/float modifyReceivedDamage(BaseGolemEntity golem, ItemStack upgradeStack, DamageSource damageSource,/*maybe NaN*/ float damage, float defaultDamage);
}
