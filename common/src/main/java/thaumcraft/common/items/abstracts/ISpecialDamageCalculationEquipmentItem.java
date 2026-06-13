package thaumcraft.common.items.abstracts;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

//works for LivingEntity
//bauble part only works for player now(unless you can make others have bauble and contact me.....TLM maid?)
public interface ISpecialDamageCalculationEquipmentItem {
    //about "currentOutput"
    //if a item go first luckily it's always the damage calculated by vanilla
    //or it will get the result modified by prev ISpecialDamageCalculationEquipment
    default float modifyDamageAfterCalculatedArmorAbsorb(LivingEntity living, ItemStack selfStack, float currentOutput, DamageSource damageSource, float input){
        return currentOutput;
    }
    default float modifyDamageAfterCalculatedMagicAbsorb(LivingEntity living, ItemStack selfStack, float currentOutput, DamageSource damageSource, float input){
        return currentOutput;
    }
}
