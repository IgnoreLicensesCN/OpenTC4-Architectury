package thaumcraft.common.items.golem.upgrade.abstracts.behavior.rangedattack;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemRangedAttackPerformerItem extends IGolemModifierItem {
    boolean canPerformRangedAttackForGolem( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f);
    void performRangedAttackForGolem( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f);
    default void performRangedAttackForGolemIfCan( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f){
        if (canPerformRangedAttackForGolem(shooter,upgradeStack,target,f)){
            performRangedAttackForGolem(shooter,upgradeStack,target,f);
        }
    };
}
