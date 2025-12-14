package thaumcraft.common.lib.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.effects.PreventMilkRemoveEffect;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;

import java.util.Objects;

public class FluxTaintEffect extends MobEffect implements PreventMilkRemoveEffect
{
    public FluxTaintEffect()
    {
    	super(MobEffectCategory.HARMFUL,0x663377);
    }

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		boolean undeadFlag = Objects.equals(entity.getMobType(), MobType.UNDEAD);

		if (entity instanceof ITaintedMob) {
			entity.heal(1.0F);
			return;
		}
		if (undeadFlag){return;}
		entity.hurt(DamageSourceThaumcraft.getDamageSource(entity.level(),DamageSourceThaumcraft.TAINT), 1.0F);
	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int k = 40 >> amplifier;
		return k > 0 && duration % k == 0;
	}
}
