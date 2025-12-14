package thaumcraft.common.lib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.effects.PreventMilkRemoveEffect;

public class UnnaturalHungerEffect extends MobEffect implements PreventMilkRemoveEffect {
    protected UnnaturalHungerEffect() {
        super(MobEffectCategory.HARMFUL, 0x446633);
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if (livingEntity instanceof Player player) {
            player.causeFoodExhaustion(0.025F * (float)(i + 1));
        }
    }
}
