package thaumcraft.common.entities.monster;

import com.linearity.opentc4.Color;
import com.linearity.opentc4.annotations.StoleFrom;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.network.fx.PacketFXWispZapS2C;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeTags;

import java.util.EnumSet;
import java.util.List;

import static com.linearity.opentc4.Consts.WispEntityTagAccessors.OWNING_ASPECT;
import static thaumcraft.common.items.ThaumcraftItemInstances.WISP_ESSENCE;

public class WispEntity extends FlyingMob
        implements
        Enemy
{
    protected @NotNull Aspect aspect = Aspects.EMPTY;
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(
            WispEntity.class,
            EntityDataSerializers.INT
    );

    protected int aggroCooldown = 0;
    protected TargetingConditions targetConditions = TargetingConditions.forCombat().range(16).selector(null);

    public WispEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.WISP(), level);
    }

    public WispEntity(EntityType<WispEntity> entityType, Level level) {
        super(entityType, level);
        xpReward = 5;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 22.0F)
                .add(Attributes.ATTACK_DAMAGE, 3.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setAspect(OWNING_ASPECT.readFromCompoundTag(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OWNING_ASPECT.writeToCompoundTag(compoundTag,getAspect());
    }

    public int getColor() {
        return this.entityData.get(DATA_COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(DATA_COLOR, color);
    }

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
        setColor(aspect.getColor());
    }

    @StoleFrom("net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal")
    public static class WispEntityHurtByTargetGoal extends TargetGoal {
        protected final WispEntity wisp;

        public WispEntityHurtByTargetGoal(WispEntity pathfinderMob, Class<?>... classs) {
            super(pathfinderMob, true);
            this.wisp = pathfinderMob;
            this.toIgnoreDamage = classs;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
        private static final int ALERT_RANGE_Y = 10;
        private boolean alertSameType;
        private int timestamp;
        private final Class<?>[] toIgnoreDamage;
        @Nullable
        private Class<?>[] toIgnoreAlert;

        public boolean canUse() {
            int i = this.mob.getLastHurtByMobTimestamp();
            LivingEntity livingEntity = this.mob.getLastHurtByMob();
            if (i != this.timestamp && livingEntity != null) {
                if (livingEntity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                    return false;
                } else {
                    for (Class<?> class_ : this.toIgnoreDamage) {
                        if (class_.isAssignableFrom(livingEntity.getClass())) {
                            return false;
                        }
                    }

                    return this.canAttack(livingEntity, HURT_BY_TARGETING);
                }
            } else {
                return false;
            }
        }

        public WispEntityHurtByTargetGoal setAlertOthers(Class<?>... classs) {
            this.alertSameType = true;
            this.toIgnoreAlert = classs;
            return this;
        }

        public void start() {
            this.wisp.aggroCooldown = 200;

            this.mob.setTarget(this.mob.getLastHurtByMob());
            this.targetMob = this.mob.getTarget();
            this.timestamp = this.mob.getLastHurtByMobTimestamp();
            this.unseenMemoryTicks = 300;
            if (this.alertSameType) {
                this.alertOthers();
            }

            super.start();
        }

        protected void alertOthers() {
            double d = this.getFollowDistance();
            AABB aABB = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d, ALERT_RANGE_Y, d);
            List<? extends Mob> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), aABB, EntitySelector.NO_SPECTATORS);
            var var5 = list.iterator();

            while (true) {
                Mob mob;
                while (true) {
                    if (!var5.hasNext()) {
                        return;
                    }

                    mob = var5.next();
                    if (this.mob != mob && mob.getTarget() == null && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal) this.mob).getOwner() == ((TamableAnimal) mob).getOwner()) && !mob.isAlliedTo(this.mob.getLastHurtByMob())) {
                        if (this.toIgnoreAlert == null) {
                            break;
                        }

                        boolean bl = false;

                        for (Class<?> class_ : this.toIgnoreAlert) {
                            if (mob.getClass() == class_) {
                                bl = true;
                                break;
                            }
                        }

                        if (!bl) {
                            break;
                        }
                    }
                }

                this.alertOther(mob, this.mob.getLastHurtByMob());
            }
        }

        protected void alertOther(Mob mob, LivingEntity livingEntity) {
            mob.setTarget(livingEntity);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2, new WispEntityHurtByTargetGoal(this));
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
    }

    @Override
    protected int decreaseAirSupply(int i) {
        return i;
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        var level = this.level();
        if (level.isClientSide()) {
            if (level instanceof ClientLevel clientLevel) {
                ClientFXUtils.burst(clientLevel, getX(), getEyeY(), getZ(), 1.0F);
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return getBbHeight() * 0.5F;
    }

    @Override
    public void tick() {
        super.tick();
        var level = this.level();
        if (level.isClientSide()) {
            clientTick(level);
        }
    }

    public void clientTick(Level level) {
        if (level instanceof ClientLevel clientLevel) {

            if (this.tickCount <= 1) {
                ClientFXUtils.burst(clientLevel, getX(), getEyeY(), getZ(), 1.0F);
            }

            if (random.nextBoolean()) {
                Color color = new Color(getColor());
                ClientFXUtils.wispFX(clientLevel,
                        getX() + ((random.nextFloat() - random.nextFloat()) * 0.7F),
                        getEyeY() + ((random.nextFloat() - random.nextFloat()) * 0.7F),
                        getZ() + ((random.nextFloat() - random.nextFloat()) * 0.7F),
                        0.1F,
                        color.getRed() / 255.0F,
                        color.getGreen() / 255.0F,
                        color.getBlue() / 255.0F
                );
            }
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public int courseChangeCooldown = 0;
    public double waypointX = Double.NaN;
    public double waypointY = Double.NaN;
    public double waypointZ = Double.NaN;
    protected void movingTick(){
        double d = this.waypointX - this.getX();
        double d1 = this.waypointY - this.getY();
        double d2 = this.waypointZ - getZ();
        double d3 = d * d + d1 * d1 + d2 * d2;
        if (d3 < (double)1.0F || d3 > (double)3600.0F) {
            this.waypointX = getX() + (double)(random.nextFloat() * 2.0F - 1.0F) * (double)16.0F;
            this.waypointY = getY() + (double)(random.nextFloat() * 2.0F - 1.0F) * (double)16.0F;
            this.waypointZ = getZ() + (double)(random.nextFloat() * 2.0F - 1.0F) * (double)16.0F;
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += random.nextInt(5) + 2;
            d3 = MathHelper.sqrt_double(d3);
            if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3)) {
                this.addDeltaMovement(new Vec3(d*0.1/d3, d1*0.1/d3, d2*0.1/d3));
            } else {
                this.waypointX = getX();
                this.waypointY = getY();
                this.waypointZ = getZ();
            }
        }
    }
    protected boolean isCourseTraversable(double d, double d1, double d2, double d3) {
        double d4 = (this.waypointX - this.getX()) / d3;
        double d5 = (this.waypointY - this.getY()) / d3;
        double d6 = (this.waypointZ - this.getZ()) / d3;
        var axisalignedbb = this.getBoundingBox();

        for(int i = 1; (double)i < d3; ++i) {
            axisalignedbb.move(d4, d5, d6);
            for (var collided:this.level().getCollisions(this, axisalignedbb)){
                return false;//#isEmpty
            }
        }

        int x = (int)this.waypointX;
        int y = (int)this.waypointY;
        int z = (int)this.waypointZ;
        var bpos = new BlockPos.MutableBlockPos(x, y, z);
        if (!this.level().getBlockState(bpos).getFluidState().isEmpty()) {
            return false;
        } else {
            for(int a = 0; a < 11; ++a) {
                bpos.move(Direction.DOWN);
                if (!this.level().getBlockState(bpos).isAir()) {
                    return true;
                }
            }

            return false;
        }
    }

    public int prevAttackCounter = 0;
    public int attackCounter = 0;
    protected void attackTick(){
        var level = level();
        double attackrange = 16.0F;
        var target = this.getTarget();
        if (target != null) {
            if (target.isDeadOrDying()){
                this.setTarget(null);
                target = null;
            }
        }

        --this.aggroCooldown;
        if (random.nextInt(1000) == 0 && (target == null || this.aggroCooldown-- <= 0)) {
            target = level.getNearestPlayer(this.targetConditions, this, this.getX(), this.getEyeY(), this.getZ());
            setTarget(target);
            if (target != null) {
                this.aggroCooldown = 50;
            }
        }

        if (target != null && target.distanceToSqr(this) < attackrange * attackrange) {
            double d5 = target.getX() - this.getX();
            double d6 = target.getBoundingBox().minY + (double)(target.getBbHeight() / 2.0F) - (this.getY() + (double)(this.getBbHeight() / 2.0F));
            double d7 = target.getZ() - this.getZ();
            setYRot(-((float)Math.atan2(d5, d7)) * 180.0F / 3.141593F);
            if (EntityUtils.canEntityBeSeen(this,target)) {
                ++this.attackCounter;
                if (this.attackCounter == 20) {
                    playSound(ThaumcraftSounds.ZAP, 1.0F, 1.1F);
                    if (this.level() instanceof ServerLevel serverLevel){
                        new PacketFXWispZapS2C(this.getId(),target.getId()).sendToAllAround(
                                serverLevel,blockPosition(),32*32
                        );
                    }
                    float damage = (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
                    var targetMotion = target.getDeltaMovement();
                    if (!(Math.abs(targetMotion.x) > (double)0.1F)
                            && !(Math.abs(targetMotion.y) > (double)0.1F)
                            && !(Math.abs(targetMotion.z) > (double)0.1F)) {
                        if (random.nextFloat() < 0.66F) {
                            target.hurt(level.damageSources().mobAttack(this), damage + 1.0F);
                        }
                    } else if (random.nextFloat() < 0.4F) {
                        target.hurt(level.damageSources().mobAttack(this), damage);
                    }

                    this.attackCounter = -20 + random.nextInt(20);
                }
            } else if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        } else {
            var selfMotion = getDeltaMovement();
            setYRot(-((float)Math.atan2(selfMotion.x, selfMotion.z)) * 180.0F / 3.141593F);
            if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        var level = this.level();
        if (!level.isClientSide() && aspect.isEmpty()) {
            var biome = level.getBiome(blockPosition());
            if (biome.is(ThaumcraftBiomeTags.EERIE)) {
                switch (random.nextInt(6)) {
                    case 0:
                        this.setAspect(Aspects.DARKNESS);
                        break;
                    case 1:
                        this.setAspect(Aspects.UNDEAD);
                        break;
                    case 2:
                        this.setAspect(Aspects.ENTROPY);
                        break;
                    case 3:
                        this.setAspect(Aspects.ELDRITCH);
                        break;
                    case 4:
                        this.setAspect(Aspects.POISON);
                        break;
                    case 5:
                        this.setAspect(Aspects.DEATH);
                }
            } else if (random.nextInt(10) != 0) {
                var as = Aspects.getPrimalAspects().toArray(new Aspect[0]);
                this.setAspect(as[random.nextInt(as.length)]);
            } else {
                var as = Aspects.getCompoundAspects().toArray(new Aspect[0]);
                this.setAspect(as[random.nextInt(as.length)]);
            }
        }
        checkDespawn();
        this.prevAttackCounter = this.attackCounter;

        movingTick();
        attackTick();
    }

    @Override
    protected float getSoundVolume() {
        return 0.25F;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ThaumcraftSounds.WISP_LIVE;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.FIRE_EXTINGUISH;
    }

    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.WISP_DEAD;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        var aspect = getAspect();
        if (!aspect.isEmpty()){
            spawnAtLocation(WISP_ESSENCE().ofAspect(aspect),0);
        }
    }


    public static boolean checkSpawnRules(
            EntityType<WispEntity> entityType,
            ServerLevelAccessor serverLevelAccessor,
            MobSpawnType mobSpawnType,
            BlockPos blockPos,
            RandomSource randomSource
    ){
        return serverLevelAccessor.getEntitiesOfClass(
                WispEntity.class,
                AABB.ofSize(blockPos.getCenter(),16,16,16)
        ).size() < 8
                && serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL
                && isDarkEnoughToSpawn(serverLevelAccessor, blockPos, randomSource)
                && checkMobSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);
    }

    public static boolean isDarkEnoughToSpawn(ServerLevelAccessor serverLevelAccessor,BlockPos blockPos, RandomSource randomSource){

        if (serverLevelAccessor.getBrightness(LightLayer.SKY, blockPos) > randomSource.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = serverLevelAccessor.dimensionType();
            int lightValue = serverLevelAccessor.getBrightness(LightLayer.BLOCK, blockPos);
            int i = dimensionType.monsterSpawnBlockLightLimit();
            if (lightValue > i) {
                return false;
            } else {
                int j = serverLevelAccessor.getLevel().isThundering()
                        ? serverLevelAccessor.getMaxLocalRawBrightness(blockPos, 10)
                        : serverLevelAccessor.getMaxLocalRawBrightness(blockPos);
                return j <= randomSource.nextInt(8);
            }
        }
    }
}
