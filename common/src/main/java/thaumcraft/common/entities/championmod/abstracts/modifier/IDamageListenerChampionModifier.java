package thaumcraft.common.entities.championmod.abstracts.modifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IDamageListenerChampionModifier {
    void championModifierOnDamage(LivingEntity victim, DamageSource source,float amountNotReduced, float amount);
}
