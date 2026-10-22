package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemCauseDamageListenerItem extends IGolemModifierItem {
    void onGolemCausedDamage(BaseGolemEntity golem, ItemStack stack, LivingEntity victim, DamageSource damageSource, float f);
}
