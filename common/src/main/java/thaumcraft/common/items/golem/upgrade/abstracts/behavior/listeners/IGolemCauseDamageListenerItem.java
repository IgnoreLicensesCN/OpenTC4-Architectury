package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemCauseDamageListenerItem extends BaseGolemEntity.IGolemUpgradeItem {
    void onGolemCausedDamage(BaseGolemEntity golem, ItemStack stack, LivingEntity victim, DamageSource damageSource, float f);
}
