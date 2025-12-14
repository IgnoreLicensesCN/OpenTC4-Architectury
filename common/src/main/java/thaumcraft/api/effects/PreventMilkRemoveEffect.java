package thaumcraft.api.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public interface PreventMilkRemoveEffect {
    default boolean preventMilkRemove(MobEffectInstance instance, LivingEntity effectOwner) {
        return true;
    };
}
