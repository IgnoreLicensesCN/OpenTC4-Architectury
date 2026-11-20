package thaumcraft.common.entities.monster.boss;

import com.linearity.opentc4.mixinaccessors.IClearGoalOnRemovedEntity;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.abstracts.ICustomSpecialDropEntity;
import thaumcraft.common.entities.abstracts.boss.IThaumcraftBossEntity;

//TODO:Tag with eldritch mob
public abstract class ThaumcraftBossEntity extends Monster implements ICustomSpecialDropEntity, IThaumcraftBossEntity, IClearGoalOnRemovedEntity {


    public static AttributeSupplier.Builder createThaumcraftBossAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }
    //search "#thaumcraft boss part" if modify below
    //#thaumcraft boss part
    protected Int2FloatMap aggro = new Int2FloatOpenHashMap();
    protected int invulnerableTicksLimit = 220;
    public static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(ThaumcraftBossEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_ID_ANGER = SynchedEntityData.defineId(ThaumcraftBossEntity.class, EntityDataSerializers.INT);

    public EntityDataAccessor<Integer> thaumcraftBoss$getAngerDataID() {
        return DATA_ID_ANGER;
    }

    public EntityDataAccessor<Integer> thaumcraftBoss$getInvulnerableTicksDataID() {
        return DATA_ID_INV;
    }

    @Override
    public Int2FloatMap thaumcraftBoss$getAggroRecords() {
        return aggro;
    }

    public ThaumcraftBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 50;
        this.thaumcraftBoss$setInvulnerableTicks(this.invulnerableTicksLimit);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        IThaumcraftBossEntity.super.thaumcraftBoss$defineSynchedData();
    }


    protected final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS
    ).setDarkenScreen(true);
    public ServerBossEvent thaumcraftBoss$getBossEvent() {
        return this.bossEvent;
    }
    public int thaumcraftBoss$getTickCount() {
        return this.tickCount;
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        thaumcraftBoss$readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        thaumcraftBoss$addAdditionalSaveData(tag);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        thaumcraftBoss$beforeFinalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void setCustomName(@Nullable Component arg) {
        super.setCustomName(arg);
        thaumcraftBoss$afterSetCustomName(arg);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        thaumcraftBoss$customServerAiStep();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer arg) {
        super.startSeenByPlayer(arg);
        thaumcraftBoss$startSeenByPlayer(arg);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer arg) {
        super.stopSeenByPlayer(arg);
        thaumcraftBoss$stopSeenByPlayer(arg);
    }

    public int thaumcraftBoss$getInvulnerableTickLimit() {
        return invulnerableTicksLimit;
    }

    @Override
    public void tick() {
        super.tick();
        IThaumcraftBossEntity.super.thaumcraftBoss$tick();
    }

    @Override
    public void aiStep() {
        if (this.thaumcraftBoss$getInvulnerableTicks() <= 0) {
            super.aiStep();
        }
    }

    @Override
    public boolean isInvulnerable() {
        return super.isInvulnerable() || this.thaumcraftBoss$getInvulnerableTicks() > 0;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {

    }

    @Override
    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource, DifficultyInstance difficultyInstance) {

    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        IThaumcraftBossEntity.super.thaumcraftBoss$customDeathLoot(damageSource,i,bl);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        damage = IThaumcraftBossEntity.super.beforeHurt(source,damage);
        return super.hurt(source, damage);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || IThaumcraftBossEntity.super.thaumcraftBoss$isAlliedTo(entity);
    }
    //end #thaumcraft boss part

}
