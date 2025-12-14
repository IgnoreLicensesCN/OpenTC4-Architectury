package thaumcraft.common.lib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.effects.PreventMilkRemoveEffect;

public class BlurredVisionEffect extends MobEffect  implements PreventMilkRemoveEffect {
    protected BlurredVisionEffect() {
        super(MobEffectCategory.HARMFUL, 0x808080);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {}//do nothing,just "blind" client

    @Override
    public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {}
}
