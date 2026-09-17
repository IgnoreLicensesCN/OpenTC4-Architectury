package thaumcraft.common.entities.championmod.abstracts.modifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.entities.championmod.abstracts.entity.IChampionModifierOwnerLivingEntity;

public interface IDamageModifierChampionModifier {
    static float modifyDamageForChampionModifier(LivingEntity living, DamageSource damageSource, float originalOut) {
        if (!(living instanceof IChampionModifierOwnerLivingEntity championModifierOwner)){
            return originalOut;
        }
        final float[] modifiedResult = {originalOut};
        championModifierOwner.opentc4$getChampionModifiersForChecker(
                (ignoredA,ignoredB) -> true, IDamageModifierChampionModifier.class
        ).forEach(
                damageListener -> {
                    modifiedResult[0] = damageListener.championModifierOnModifyDamage(
                            living,damageSource,modifiedResult[0]
                    );
                }
        );
        return modifiedResult[0];
    }

    //returns modified damage
    float championModifierOnModifyDamage(LivingEntity victim, DamageSource source, float amount);
}
