package thaumcraft.common.lib.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.effects.PreventMilkRemoveEffect;

import java.util.List;

public class InfectiousVisExhaustEffect extends VisExhaustEffect implements PreventMilkRemoveEffect {
    public InfectiousVisExhaustEffect() {
        super(MobEffectCategory.HARMFUL,0x665577);
    }

    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        var box = target.getBoundingBox().inflate(4.f);
        List<LivingEntity> targets = target.level().getEntities(
                EntityTypeTest.forClass(LivingEntity.class),
                box,livingEntity -> !livingEntity.hasEffect(this));
        for(LivingEntity e : targets) {
            if (amplifier > 0) {
                e.addEffect(new MobEffectInstance(this, 6000, amplifier - 1, false,true));
            } else {
                e.addEffect(new MobEffectInstance(ThaumcraftEffects.VIS_EXHAUST, 6000, 0, false,true));
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}
