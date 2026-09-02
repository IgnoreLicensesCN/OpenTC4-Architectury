package thaumcraft.common.entities.monster.eldritch;

import com.linearity.opentc4.mixinaccessors.cliententity.EldritchGuardianEntityClientAccessor;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.DoorBreakingMonster;
import thaumcraft.common.entities.ai.goals.RangedAndMeleeAttackGoal;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.entities.projectile.EldritchOrbEntity;
import thaumcraft.common.lib.network.fx.PacketFXSonicS2C;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.common.lib.network.playerdata.syncdata.PacketSyncWarpS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketChangeWarpS2C;
import thaumcraft.common.lib.utils.EntityUtils;

import static com.linearity.opentc4.Consts.EldritchGuardianEntityTagAccessors.HOME_POS;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;
import static thaumcraft.common.items.ThaumcraftItemInstances.WISP_ESSENCE;

public class EldritchGuardianEntity extends DoorBreakingMonster implements RangedAttackMob {
    protected int homeRange = 8;
    public EldritchGuardianEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ELDRITCH_GUARDIAN(), level);
    }
    public EldritchGuardianEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 20;
        this.setCanPickUpLoot(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new RangedAndMeleeAttackGoal(this, 1.0F, 20, 40, 24.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, CultistEntity.class, true));
    }


    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE,7)
                .add(Attributes.ARMOR, 4)
                .add(MOVEMENT_SPEED,0.28);
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.9333333f;
    }
    public static boolean checkSpawnRules(EntityType<? extends Monster> arg, ServerLevelAccessor arg2, MobSpawnType arg3, BlockPos arg4, RandomSource arg5){
        return arg2.getEntitiesOfClass(
                EldritchGuardianEntity.class,
                AABB.ofSize(arg4.getCenter(),32,16,32)
        ).isEmpty() && checkMonsterSpawnRules(arg, arg2, arg3, arg4, arg5);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasRestriction();
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.restrictTo(HOME_POS.readFromCompoundTag(compoundTag),homeRange);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        HOME_POS.writeToCompoundTag(compoundTag,getRestrictCenter());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        var result = super.doHurtTarget(entity);
        if (result && this.getRemainingFireTicks() > 0){
            int i = level().getDifficulty().getId();
            if (random.nextFloat() < i * 0.3F){
                entity.setSecondsOnFire(2 * i);
            }
        }
        return result;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 500;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (damageSource.is(DamageTypes.MAGIC)){
            f /= 2;
        }
        return super.hurt(damageSource, f);
    }

    @Override
    public void tick() {
        super.tick();
        var level = this.level();
        if (level.isClientSide){
            ClientTickContext.clientTick(this);
        }else {
            var difficulty = level.getDifficulty();
            if (level.dimension() != Config.dimensionOuterId
                    && (this.tickCount == 0 || this.tickCount % 100 == 0)
                    && difficulty != Difficulty.EASY
            ) {
                double d6 = difficulty == Difficulty.HARD ? (double)576.0F : (double)256.0F;

                for (var player:level.players()){

                    if (player.isAlive() && player instanceof ServerPlayer serverPlayer) {
                        double d5 = player.distanceToSqr(this);
                        if (d5 < d6) {
                            new PacketMiscEventS2C((short) 2).sendTo(serverPlayer);
                        }
                    }
                }
            }
        }
    }

    public static class ClientTickContext {
        public float armLiftL = 0.0F;
        public float armLiftR = 0.0F;

        public static void clientTick(EldritchGuardianEntity guardian){
            var ctx = ((EldritchGuardianEntityClientAccessor)guardian).opentc4$getClientTickContext();
            if (ctx.armLiftL > 0.0F) {
                ctx.armLiftL -= 0.05F;
            }

            if (ctx.armLiftR > 0.0F) {
                ctx.armLiftR -= 0.05F;
            }
            var random = guardian.getRandom();
            float x = (float) (guardian.getX() + ((random.nextFloat()*2 -1) * 0.2F));
            float z = (float) (guardian.getZ() + ((random.nextFloat()*2 -1) * 0.2F));
            if (guardian.level() instanceof ClientLevel clientLevel){
                ClientFXUtils.wispFXEG(
                        clientLevel,
                        x, (float)(guardian.getY() + 0.22 * guardian.getBbHeight()),
                        z,
                        guardian);
            }
        }

        public static void setArmLiftL(EldritchGuardianEntity guardian,float armLiftL){
            var ctx = ((EldritchGuardianEntityClientAccessor)guardian).opentc4$getClientTickContext();
            ctx.armLiftL = armLiftL;
        }
        public static void setArmLiftR(EldritchGuardianEntity guardian, float armLiftR){
            var ctx = ((EldritchGuardianEntityClientAccessor)guardian).opentc4$getClientTickContext();
            ctx.armLiftR = armLiftR;
        }
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ThaumcraftSounds.EG_IDLE;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.EG_DEATH;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        var result = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if (this.level().dimension() == Config.dimensionOuterId) {
            int bh = (int)this.getAttribute(Attributes.MAX_HEALTH).getBaseValue() / 2;
            this.setAbsorptionAmount(this.getAbsorptionAmount() + (float)bh);
        }
        return result;
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (level().isClientSide){
            if (b == 15) {
                ClientTickContext.setArmLiftL(this,0.5F);
            } else if (b == 16) {
                ClientTickContext.setArmLiftR(this,0.5F);
            } else if (b == 17) {
                ClientTickContext.setArmLiftL(this,0.9F);
                ClientTickContext.setArmLiftR(this,0.9F);
            } else {
                super.handleEntityEvent(b);
            }
        }else {
            super.handleEntityEvent(b);
        }
    }

    //TODO:Spawn ignores light level


    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        if (this.random.nextBoolean()) {
            this.spawnAtLocation(WISP_ESSENCE().ofAspect(Aspects.UNDEAD));
        }

        if (this.random.nextBoolean()) {
            this.spawnAtLocation(WISP_ESSENCE().ofAspect(Aspects.ELDRITCH));
        }
    }

    boolean lastBlast = false;
    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        if (this.random.nextFloat() > 0.1F) {
            var blast = new EldritchOrbEntity(this);

            this.lastBlast = !this.lastBlast;
            this.level().broadcastEntityEvent(this, (byte)(this.lastBlast ? 16 : 15));
            int rr = this.lastBlast ? 90 : 180;
            double xx = MathHelper.cos((this.getYRot() + (float)rr) % 360.0F / 180.0F * (float)Math.PI) * 0.5F;
            double yy = 0.057777777 * this.getBbHeight();
            double zz = MathHelper.sin((this.getYRot() + (float)rr) % 360.0F / 180.0F * (float)Math.PI) * 0.5F;
            blast.setPos(blast.getX() - xx, blast.getY() - yy, blast.getZ() - zz);
            var livingMovement = livingEntity.getDeltaMovement();
            double d0 = livingEntity.getX() + livingMovement.x - this.getX();
            double d1 = livingEntity.getY() - this.getY() - (double)(livingEntity.getBbHeight() / 2.0F);
            double d2 = livingEntity.getZ() + livingMovement.z - this.getZ();
            blast.shoot(d0, d1, d2, 1,2);
            this.playSound(ThaumcraftSounds.EG_ATTACK, 2.0F, 1.0F + this.random.nextFloat() * 0.1F);
            this.level().addFreshEntity(blast);
        } else if (EntityUtils.canEntityBeSeen(this,livingEntity)) {
            if (level() instanceof ServerLevel serverLevel) {
                new PacketFXSonicS2C(this.getId())
                        .sendToAllAround(
                                serverLevel,
                                blockPosition(),
                                32*32
                        );
            }
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 0));
            var warpInfo = WarpInfo.getFromLivingEntity(livingEntity);
            if (warpInfo != null) {
                int warpCount = 1 + this.level().random.nextInt(3);
                warpInfo.addTempWarp(warpCount);
                if (livingEntity instanceof ServerPlayer serverPlayer) {

                    new PacketSyncWarpS2C(warpInfo).sendTo(serverPlayer);
                    new PacketChangeWarpS2C((byte)2, warpCount).sendTo(serverPlayer);
                }
            }

            this.playSound(ThaumcraftSounds.EG_SCREECH, 3.0F, 1.0F + this.random.nextFloat() * 0.1F);
        }

    }
}
