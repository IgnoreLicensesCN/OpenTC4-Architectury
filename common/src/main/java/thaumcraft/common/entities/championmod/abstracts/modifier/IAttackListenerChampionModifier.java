package thaumcraft.common.entities.championmod.abstracts.modifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IAttackListenerChampionModifier {
    void championModifierOnAttack(LivingEntity victim,LivingEntity attacker, DamageSource source,float amountNotReduced, float amount);
}
