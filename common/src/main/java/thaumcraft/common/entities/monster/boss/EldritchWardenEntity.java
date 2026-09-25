package thaumcraft.common.entities.monster.boss;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.ai.goals.RangedAndMeleeAttackGoal;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.entity.IChampionModifiedNamingRuleOwner;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.entities.monster.eldritch.EldritchGuardianEntity;
import thaumcraft.common.entities.projectile.EldritchOrbEntity;
import thaumcraft.common.lib.network.fx.PacketFXBlockArcS2C;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.lib.network.fx.PacketFXSonicS2C;
import thaumcraft.common.lib.utils.EntityUtils;

import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.SAPPING_FIELD;

public class EldritchWardenEntity extends ThaumcraftBossEntity implements IChampionModifiedNamingRuleOwner, RangedAttackMob {
    public static int NAME_COUNT = 15;
    protected boolean fieldFrenzy = false;
    protected int fieldFrenzyCounter = 0;

    public EldritchWardenEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ELDRITCH_WARDEN(), level);
    }

    public EldritchWardenEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.invulnerableTicksLimit = 150;
        this.setInvulnerableTicks(this.invulnerableTicksLimit);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
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
        return ThaumcraftBossEntity.createThaumcraftBossAttributes()
                .add(Attributes.MAX_HEALTH, 200.0F)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ARMOR, 4)
                ;
    }

    @Override
    public void setNameWhenChampionModified(LivingEntity living, ChampionModifier modifier) {
        setCustomName(
                Component.translatable(
                        "entity.Thaumcraft.EldritchWarden.name",
                        Component.translatable("thaumcraft.boss.name.eldritch_warden." + random.nextInt(NAME_COUNT)),
                        modifier.getChampionModifierNameLocalized()
                )
        );
    }

    boolean lastBlast = false;

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        if (this.random.nextFloat() > 0.2F) {
            var blast = new EldritchOrbEntity(this);

            this.lastBlast = !this.lastBlast;
            this.level().broadcastEntityEvent(this, (byte) (this.lastBlast ? 16 : 15));
            int rr = this.lastBlast ? 90 : 180;
            double xx = MathHelper.cos((this.getYRot() + (float) rr) % 360.0F / 180.0F * (float) Math.PI) * 0.5F;
            double yy = this.getBbHeight() * 0.13 / 3.5;//0.057777777 * this.getBbHeight();
            double zz = MathHelper.sin((this.getYRot() + (float) rr) % 360.0F / 180.0F * (float) Math.PI) * 0.5F;
            blast.setPos(blast.getX() - xx, blast.getY() - yy, blast.getZ() - zz);
            var livingMovement = livingEntity.getDeltaMovement();
            double d0 = livingEntity.getX() + livingMovement.x - this.getX();
            double d1 = livingEntity.getY() - this.getY() - (double) (livingEntity.getBbHeight() / 2.0F);
            double d2 = livingEntity.getZ() + livingMovement.z - this.getZ();
            blast.shoot(d0, d1, d2, 1, 2);
            this.playSound(ThaumcraftSounds.EG_ATTACK, 2.0F, 1.0F + this.random.nextFloat() * 0.1F);
            this.level().addFreshEntity(blast);
        } else if (EntityUtils.canEntityBeSeen(this, livingEntity)) {
            if (level() instanceof ServerLevel serverLevel) {
                new PacketFXSonicS2C(this.getId())
                        .sendToAllAround(
                                serverLevel,
                                blockPosition(),
                                32 * 32
                        );
            }
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0));
            var warpInfo = WarpInfo.getFromLivingEntity(livingEntity);
            if (warpInfo != null) {
                int warpCount = 3 + this.level().random.nextInt(3);
                warpInfo.addTempWarpAndSync(livingEntity, warpCount);
            }

            this.playSound(ThaumcraftSounds.EG_SCREECH, 4.0F, 1.0F + this.random.nextFloat() * 0.1F);
        }

    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * (3.1F / 3.5F);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.setAbsorptionAmount((this.getAbsorptionAmount() + getAbsorptionAmountCanRegen()));
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    protected float getAbsorptionAmountCanRegen() {
        return (float) (this.getMaxHealth() * 2. / 3.);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (level().isClientSide) {
            if (b == 15) {
                EldritchGuardianEntity.ClientTickContext.setArmLiftL(this, 0.5F);
            } else if (b == 16) {
                EldritchGuardianEntity.ClientTickContext.setArmLiftR(this, 0.5F);
            } else if (b == 17) {
                EldritchGuardianEntity.ClientTickContext.setArmLiftL(this, 0.9F);
                EldritchGuardianEntity.ClientTickContext.setArmLiftR(this, 0.9F);
            } else {
                super.handleEntityEvent(b);
            }
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 500;
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
    public boolean isInvulnerable() {
        return this.fieldFrenzyCounter > 0 || super.isInvulnerable();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        var level = level();
        if (!this.isInvulnerable() && !source.is(DamageTypes.DROWN) && !source.is(DamageTypes.WITHER)) {
            boolean aef = super.hurt(source, damage);
            if (!level.isClientSide && aef && !this.fieldFrenzy && this.getAbsorptionAmount() <= 0.0F) {
                this.fieldFrenzy = true;
                this.fieldFrenzyCounter = 150;
            }

            return aef;
        } else {
            return false;
        }
    }

    @Override
    protected void customServerAiStep() {

        super.customServerAiStep();
        var bpos = blockPosition();
        for(int l = 0; l < 4; ++l) {
            var i = MathHelper.floor_double(this.getX() + (double)((float)(l % 2 * 2 - 1) * 0.25F));
            var j = MathHelper.floor_double(this.getY());
            var k = MathHelper.floor_double(this.getZ() + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
            if (this.level().getBlockState(bpos).isAir()) {
                this.level().setBlockAndUpdate(new BlockPos(i,j,k), SAPPING_FIELD().defaultBlockState());
            }
        }

        if (!level().isClientSide && this.fieldFrenzyCounter > 0) {
            if (this.fieldFrenzyCounter == 150) {
                this.teleportHome(getRestrictCenter().getCenter());
            }

            this.performFieldFrenzy();
        }

    }

    @Override
    public void aiStep() {
        if (fieldFrenzyCounter <= 0) {
            super.aiStep();
        }

        if (this.hurtTime <= 0 && this.tickCount % 25 == 0) {
            if (this.getAbsorptionAmount() < getAbsorptionAmountCanRegen()) {
                this.setAbsorptionAmount(this.getAbsorptionAmount() + 1.0F);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getInvulnerableTicks() == invulnerableTicksLimit){
            this.level().broadcastEntityEvent(this, (byte) 18);
        }
        var level = this.level();
        if (level.isClientSide){
            EldritchGuardianEntity.ClientTickContext.clientTick(this);
        }
    }

    protected void performFieldFrenzy() {
        if (this.fieldFrenzyCounter < 121 && this.fieldFrenzyCounter % 10 == 0) {
            this.level().broadcastEntityEvent(this, (byte)17);
            double radius = (double)(150 - this.fieldFrenzyCounter) / (double)8.0F;
            int d = 1 + this.fieldFrenzyCounter / 8;
            int i = MathHelper.floor_double(this.getX());
            int j = MathHelper.floor_double(this.getY());
            int k = MathHelper.floor_double(this.getZ());

            for(int q = 0; q < 180 / d; ++q) {
                double radians = Math.toRadians(q * 2 * d);
                int deltaX = (int)(radius * Math.cos(radians));
                int deltaZ = (int)(radius * Math.sin(radians));
                var pickPos = new BlockPos(i + deltaX, j, k + deltaZ);
                if (this.level().getBlockState(pickPos).isAir()
                        && this.level().getBlockState(pickPos.below()).isCollisionShapeFullBlock(this.level(),pickPos.below()))
                {
                    this.level().setBlockAndUpdate(pickPos, SAPPING_FIELD().defaultBlockState());
                    this.level().scheduleTick(pickPos, SAPPING_FIELD(), 250 + this.random.nextInt(150));
                    if (this.level() instanceof ServerLevel serverLevel){
                        if (this.random.nextFloat() < 0.3F) {
                            new PacketFXBlockArcS2C(i + deltaX, j, k + deltaZ, this.getId()).sendToAllAround(serverLevel,pickPos,32*32);
                        } else {
                            new PacketFXBlockSparkleS2C(i + deltaX, j, k + deltaZ, 0x800080).sendToAllAround(serverLevel,pickPos,32*32);
                        }
                    }
                }
            }

            this.playSound(ThaumcraftSounds.ZAP, 1.0F, 0.9F+random.nextFloat() * 0.1F);
        }

        --this.fieldFrenzyCounter;
    }

    protected void teleportHome(Vec3 teleportToPos) {
        double d3 = this.getX();
        double d4 = this.getY();
        double d5 = this.getZ();
        this.setPos(teleportToPos);
        boolean flag = false;
        int i = MathHelper.floor_double(this.getX());
        int j = MathHelper.floor_double(this.getY());
        int k = MathHelper.floor_double(this.getZ());
        if (!this.level().getBlockState(blockPosition()).isAir()) {
            boolean flag1 = false;
            int tries = 20;

            while(!flag1 && tries > 0) {
                var block = this.level().getBlockState(blockPosition().below());
                var block2 = this.level().getBlockState(blockPosition());
                if (block.getCollisionShape(level(),blockPosition().below(), CollisionContext.of(this)).isEmpty()
                        && !block2.getCollisionShape(level(),blockPosition(), CollisionContext.of(this)).isEmpty()
                ) {
                    flag1 = true;
                } else {
                    i = MathHelper.floor_double(this.getX()) + this.random.nextInt(8) - this.random.nextInt(8);
                    k = MathHelper.floor_double(this.getZ()) + this.random.nextInt(8) - this.random.nextInt(8);
                    --tries;
                }
            }

            if (flag1) {
                this.setPos((double)i + (double)0.5F, (double)j + 0.1, (double)k + (double)0.5F);
                if (this.level().getEntityCollisions(this, this.getBoundingBox()).isEmpty()) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPos(d3, d4, d5);
        } else {
            short short1 = 128;

            for(int l = 0; l < short1; ++l) {
                double d6 = (double)l / ((double)short1 - (double)1.0F);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.getX() - d3) * d6 + (this.random.nextDouble() - 0.5F) * this.getBbWidth() * 2.0F;
                double d8 = d4 + (this.getY() - d4) * d6 + this.random.nextDouble() * this.getBbHeight();
                double d9 = d5 + (this.getZ() - d5) * d6 + (this.random.nextDouble() - 0.5F) * this.getBbWidth() * 2.0F;
                this.level().addParticle(ParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
            }

            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
    }
}
