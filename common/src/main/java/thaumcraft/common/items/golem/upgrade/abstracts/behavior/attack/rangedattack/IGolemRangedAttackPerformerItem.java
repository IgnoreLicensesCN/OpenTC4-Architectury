package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemRangedAttackPerformerItem extends BaseGolemEntity.IGolemUpgradeItem {
    boolean canPerformRangedAttackForGolem( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f);
    void performRangedAttackForGolem( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f);
    default void performRangedAttackForGolemIfCan( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f){
        if (canPerformRangedAttackForGolem(shooter,upgradeStack,target,f)){
            performRangedAttackForGolem(shooter,upgradeStack,target,f);
        }
    };
}
