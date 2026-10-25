package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemDoHurtTargetListenerItem extends IGolemModifierItem {
    void onDoHurtTarget(BaseGolemEntity golem, ItemStack upgradeStack, Entity entity);
}
