package thaumcraft.common.lib.effects;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.effects.PreventMilkRemoveEffect;

public class SunScornedEffect extends MobEffect implements PreventMilkRemoveEffect {
    protected SunScornedEffect() {
        super(MobEffectCategory.HARMFUL, 0xf8d86a);
    }

    @Override
    public void applyEffectTick(LivingEntity target, int par2) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            var blockPos = target.blockPosition();
            var level = target.level();
            float f = (level.getBrightness(LightLayer.SKY,blockPos) + level.getBrightness(LightLayer.BLOCK,blockPos)) / 15.0F;
            if (f > 0.5F && target.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F
                    && level.canSeeSky(blockPos)) {
                target.setSecondsOnFire(4);
            } else if (f < 0.25F && target.getRandom().nextFloat() > f * 2.0F) {
                target.heal(1.0F);
            }
        }

    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {}
}
