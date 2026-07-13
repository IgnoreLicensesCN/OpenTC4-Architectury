package thaumcraft.common.lib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import org.jetbrains.annotations.Nullable;
import org.jsoup.parser.Tag;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;
import thaumcraft.api.effects.IPreventMilkRemoveEffect;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.entities.ThaumcraftEntities;

import java.util.Objects;

public class FluxTaintEffect extends MobEffect implements IPreventMilkRemoveEffect
{
    public FluxTaintEffect()
    {
    	super(MobEffectCategory.HARMFUL,0x663377);
    }

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		boolean undeadFlag = Objects.equals(entity.getMobType(), MobType.UNDEAD);

		if (entity.getType().is(ThaumcraftEntities.EntityTags.TAINTED)) {
			entity.heal(1.0F);
			return;
		}
		if (undeadFlag){return;}
		entity.hurt(ThaumcraftDamageSources.getDamageSource(entity.level(), ThaumcraftDamageSources.TAINT), 1.0F);
	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int k = 40 >> amplifier;
		return k > 0 && duration % k == 0;
	}
}
