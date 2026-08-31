package thaumcraft.common.entities.projectile.golemorb;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;

public class GolemOrbEntity extends ThrowableProjectile {
    protected @Nullable LivingEntity target = null;
    public boolean red = false;
    public GolemOrbEntity(EntityType<? extends ThrowableProjectile> entityType, LivingEntity shooter,LivingEntity target, Level level){
        super(entityType, shooter, level);
        this.target = target;
    }
    public GolemOrbEntity(LivingEntity shooter,LivingEntity target,Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.GOLEM_ORB(),shooter,target, level);
    }

    public GolemOrbEntity(EntityType<? extends GolemOrbEntity> entityType, Level level) {
        super(entityType, level);
    }
    public GolemOrbEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.GOLEM_ORB(), level);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected float getGravity() {
        return 0;
    }

    protected float getDamageMultiplier() {
        return 0.6F;
    }

    protected int getMaxTickCount(){
        return 160;
    }

    protected void onHit(HitResult mop) {
        var level = level();
        var thrower = getOwner();
        if (!level.isClientSide
                && thrower instanceof LivingEntity livingThrower
                && mop instanceof EntityHitResult entityHitResult) {
            var victim = entityHitResult.getEntity();
            victim.hurt(
                    level.damageSources().indirectMagic(victim,thrower),
                    (float) (livingThrower.getAttributes().getValue(Attributes.ATTACK_DAMAGE) * getDamageMultiplier())
            );
        }else{
            if (level instanceof ClientLevel clientLevel){
                this.playSound(ThaumcraftSounds.SHOCK,1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                var pos = position();
                ClientFXUtils.burst(clientLevel, pos.x,pos.y,pos.z, 1.0F);
            }
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= getMaxTickCount()) {
            discard();
            return;
        }

        if (this.target != null) {
            double distanceSqrToTarget = this.distanceToSqr(this.target);
            var targetPos = target.position();
            var selfPos = this.position();
            double dx = targetPos.x - selfPos.x;
            double dy = target.getEyePosition().y - selfPos.y;
            double dz = targetPos.z - selfPos.z;
            double d13 = 0.2;
            dx = dx * d13 / distanceSqrToTarget;
            dy = dy * d13 / distanceSqrToTarget;
            dz = dz * d13 / distanceSqrToTarget;
            var currentSpeed = getDeltaMovement();
            this.setDeltaMovement(
                    MathHelper.clamp_double(currentSpeed.x + dx,-0.25F,0.25F),
                    MathHelper.clamp_double(currentSpeed.y + dy,-0.25F,0.25F),
                    MathHelper.clamp_double(currentSpeed.z + dz,-0.25F,0.25F)
            );
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            var damageSourceEntity = damageSource.getDirectEntity();
            if (damageSourceEntity != null) {
                var lookVec = damageSourceEntity.getLookAngle();
                this.setDeltaMovement(lookVec.scale(0.9));
                this.playSound(ThaumcraftSounds.ZAP, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
            this.markHurt();
            return false;
        }
    }
}
