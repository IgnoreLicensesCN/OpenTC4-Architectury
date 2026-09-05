package thaumcraft.common.entities.projectile;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;

import java.util.List;

public class EldritchOrbEntity extends ThrowableProjectile {
    public EldritchOrbEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ELDRITCH_ORB(),level);
    }
    public EldritchOrbEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public EldritchOrbEntity(LivingEntity living) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ELDRITCH_ORB(),living,living.level());
    }
    public EldritchOrbEntity(EntityType<? extends ThrowableProjectile> entityType, LivingEntity livingEntity, Level level) {
        super(entityType, livingEntity, level);
    }
    @Override
    protected float getGravity() {
        return 0;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 100){
            this.setRemoved(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 16) {
            if (level().isClientSide() && level() instanceof ClientLevel clientLevel) {
                for(int a = 0; a < 30; ++a) {
                    float fx = (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F;
                    float fy = (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F;
                    float fz = (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F;
                    ClientFXUtils.wispFX3(
                            clientLevel,
                            this.getX() + (double)fx,
                            this.getY() + (double)fy,
                            this.getZ() + (double)fz,
                            this.getX() + (double)(fx * 8.0F),
                            this.getY() + (double)(fy * 8.0F),
                            this.getZ() + (double)(fz * 8.0F),
                            0.3F,
                            5,
                            true,
                            0.02F
                    );
                }
            }
        } else {
            super.handleEntityEvent(b);
        }

    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!level().isClientSide && this.getOwner() != null) {
            List<Entity> list = this.level().getEntities(this.getOwner(), this.getBoundingBox().inflate(2.0F, 2.0F, 2.0F));

            for (Entity victim : list) {

                if (victim instanceof LivingEntity living &&
                        (living.getMobType() != MobType.UNDEAD
                                && !living.getType().is(ThaumcraftEntities.EntityTags.UNDEAD))
                        && !living.getType().is(ThaumcraftEntities.EntityTags.ELDRITCH)
                ) {
                    victim.hurt(
                            level().damageSources().indirectMagic(this, this.getOwner()),
                            getOwner() instanceof LivingEntity livingOwner? (float) livingOwner.getAttribute(
                                            Attributes.ATTACK_DAMAGE)
                                    .getValue() :7 * 0.666F
                    );


                    living.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 0));
                }
            }

            this.playSound(SoundEvents.LAVA_EXTINGUISH, 0.5F, 2.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.8F);
            this.tickCount = 100;
            this.level().broadcastEntityEvent(this, (byte)16);
        }

    }
}
