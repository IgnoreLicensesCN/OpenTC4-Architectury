package thaumcraft.common.entities.projectile.shockfocus;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import tc4tweak.ConfigurationHandler;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.utils.EntityUtils;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ENTITY_TEST;

public class ShockOrbEntity extends ThrowableProjectile {
    public int area = 4;
    public int damage = 5;

    public ShockOrbEntity(Level par1World) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.SHOCK_ORB(), par1World);
    }

    public ShockOrbEntity(EntityType<ShockOrbEntity> type, Level par1World) {
        super(type, par1World);
    }

    public ShockOrbEntity(LivingEntity par2EntityLiving, Level par1World) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.SHOCK_ORB(), par2EntityLiving, par1World);
    }

    public ShockOrbEntity(EntityType<ShockOrbEntity> type, LivingEntity par2EntityLiving, Level par1World) {
        super(type, par2EntityLiving, par1World);
    }

    public ShockOrbEntity(Level par1World, double par2, double par4, double par6) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.SHOCK_ORB(), par2, par4, par6, par1World);
    }

    public ShockOrbEntity(EntityType<ShockOrbEntity> type, double par2, double par4, double par6, Level par1World) {
        super(type, par2, par4, par6, par1World);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 500) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    public static boolean canEarthShockHurt(Entity entity) {
        return switch (ConfigurationHandler.INSTANCE.getEarthShockHarmMode()) {
            case OnlyLiving -> entity instanceof LivingEntity;
            case ExceptItemXp -> !(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrb);
            default -> true;
        };
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        this.markHurt();
        var kickingEntity = damageSource.getEntity();
        if (kickingEntity != null) {
            var lookVec = kickingEntity.getLookAngle();
            this.setDeltaMovement(lookVec.scale(0.9));
            this.level().playSound(
                    null,blockPosition(), ThaumcraftSounds.ZAP, SoundSource.NEUTRAL, 1.0F, 1.0F + (this.random.nextFloat()) * 0.4F - 0.2F);

            return true;
        }
        return false;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        var level = this.level();
        var pos = blockPosition();
        if (!level.isClientSide) {
            var aabb = new AABB(pos).inflate(area);
            level.getEntities(
                    ENTITY_TEST,
                    aabb,
                    ShockOrbEntity::canEarthShockHurt
            ).forEach(entity -> entity.hurt(
                    level.damageSources().indirectMagic(
                            ShockOrbEntity.this,getOwner()
                    ),
                    damage
            ));

            for(int a = 0; a < 20; ++a) {
                var pickPos = pos.offset(
                        this.random.nextInt(2*this.area) - this.area,this.area,this.random.nextInt(2*this.area) - this.area
                ).mutable();
                var stateCurrent = level.getBlockState(pickPos);
                while (
                        stateCurrent.isAir()
                                && (pickPos.getY() > pos.getY() - this.area)
                ) {
                    pickPos.move(Direction.DOWN);
                    stateCurrent = level.getBlockState(pickPos);
                }
                var stateAbove = level.getBlockState(pickPos.above());
                if (stateAbove.isAir()
                        && !level.getBlockState(pickPos).isAir()
                        && EntityUtils.canEntityBeSeen(this, pickPos.getX() + 0.5F, pickPos.getY() + 1.5F, pickPos.getZ() + 0.5F)
                ) {
                    level.setBlockAndUpdate(pickPos, ThaumcraftBlocks.ThaumcraftBlockInstances.STATIC_FIELD().defaultBlockState());
                }
            }
        }else {
            if (level instanceof ClientLevel clientLevel) {
                ClientFXUtils.burst(clientLevel, this.getX(), this.getY(), this.getZ(), 3.0F);
                level.playSound(null,blockPosition(), ThaumcraftSounds.SHOCK,SoundSource.PLAYERS
                        , 1.0F, 1.0F + (this.random.nextFloat()) * 0.4F - 0.2F);
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }
}
