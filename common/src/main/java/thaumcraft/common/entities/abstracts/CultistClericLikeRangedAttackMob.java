package thaumcraft.common.entities.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.golemorb.RedGolemOrbEntity;

@UtilityLikeAbstraction
public interface CultistClericLikeRangedAttackMob extends RangedAttackMob {

    double getX();
    double getY();
    double getZ();
    float getBbHeight();
    RandomSource getRandom();
    Level level();
    void playSound(SoundEvent sound, float volume, float pitch);
    LivingEntity getProjectileThrower();


    @Override
    default void performRangedAttack(LivingEntity living, float f) {
        double d0 = living.getX() - this.getX();
        double d1 = living.getBoundingBox().minY + (double)(living.getBbHeight() / 2.0F) - (this.getY() + (double)(this.getBbHeight() / 2.0F));
        double d2 = living.getZ() - this.getZ();
        var random = getRandom();
        var level = level();
        var thiz = getProjectileThrower();
        if (random.nextFloat() > 0.66F) {
            var blast = new RedGolemOrbEntity( thiz, living,level);
            var movement = blast.getDeltaMovement();
            blast.shoot(d0, d1 + (double)2.0F, d2, 0.66F, 3.0F);
            blast.setPos(blast.getX() + movement.x / (double)2.0F, blast.getY(), blast.getZ() + movement.z / (double)2.0F);
            this.playSound(ThaumcraftSounds.EGATTACK, 1.0F, 1.0F + random.nextFloat() * 0.1F);
            level.addFreshEntity(blast);
        } else {
            float f1 = MathHelper.sqrt_float(f) * 0.5F;
            this.playSound(SoundEvents.FIRE_EXTINGUISH,0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);


            for(int i = 0; i < 3; ++i) {
                var entitysmallfireball = new SmallFireball(
                        level,
                        thiz,
                        d0 + random.nextGaussian() * (double)f1,
                        d1,
                        d2 + random.nextGaussian() * (double)f1
                );
                entitysmallfireball.setPos(
                        entitysmallfireball.getX(),
                        this.getY() + (this.getBbHeight() / 2.0F) + (double)0.5F,
                        entitysmallfireball.getZ()
                );
                level.addFreshEntity(entitysmallfireball);
            }
        }
    }
}
