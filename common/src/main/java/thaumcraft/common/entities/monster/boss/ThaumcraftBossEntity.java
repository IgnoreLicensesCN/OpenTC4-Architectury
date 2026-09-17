package thaumcraft.common.entities.monster.boss;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.ArrayList;

import static com.linearity.opentc4.Consts.ThaumcraftBossTagAccessors.*;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.DMG_BUFF_UUIDS;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.HP_BUFF_UUIDS;

//TODO:Tag with eldritch mob
public abstract class ThaumcraftBossEntity extends Monster {
    protected Int2IntMap aggro = new Int2IntOpenHashMap();
    protected int invulnerableTicksLimit = 220;
    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(ThaumcraftBossEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_ANGER = SynchedEntityData.defineId(ThaumcraftBossEntity.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS
    )
            .setDarkenScreen(true);

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    public ThaumcraftBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 50;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_INV, 0);
        this.entityData.define(DATA_ID_ANGER, 0);
    }

    public int getAnger() {
        return this.entityData.get(DATA_ID_ANGER);
    }

    public void setAnger(int value) {
        this.entityData.set(DATA_ID_ANGER, value);
    }

    public int getInvulnerableTicks() {
        return this.entityData.get(DATA_ID_INV);
    }

    public void setInvulnerableTicks(int i) {
        this.entityData.set(DATA_ID_INV, i);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        this.setInvulnerableTicks(INVULNERABLE_TICKS.readIntFromCompoundTag(tag));
        restrictTo(HOME_POS.readFromCompoundTag(tag), HOME_SIZE.readIntFromCompoundTag(tag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        INVULNERABLE_TICKS.writeIntToCompoundTag(tag, getInvulnerableTicks());
        HOME_POS.writeToCompoundTag(tag, getRestrictCenter());
        HOME_SIZE.writeIntToCompoundTag(tag, (int) getRestrictRadius());
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (compoundTag != null) {
            HOME_POS.writeToCompoundTag(compoundTag, blockPosition());
            HOME_SIZE.writeIntToCompoundTag(compoundTag, 24);
        } else {
            restrictTo(blockPosition(), 24);
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void setCustomName(@Nullable Component arg) {
        super.setCustomName(arg);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer arg) {
        super.startSeenByPlayer(arg);
        this.bossEvent.addPlayer(arg);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer arg) {
        super.stopSeenByPlayer(arg);
        this.bossEvent.removePlayer(arg);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getInvulnerableTicks() > 0) {
            int k1 = this.getInvulnerableTicks() - 1;
            float percent = k1 / (float) invulnerableTicksLimit;
            this.bossEvent.setProgress(1.0F - percent);
//            if (k1 <= 0) {
//                this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
//                if (!this.isSilent()) {
//                    this.level().globalLevelEvent(1023, this.blockPosition(), 0);
//                }
//            }

            this.setInvulnerableTicks(k1);
            if (this.tickCount % 10 == 0) {
                this.heal(10.01F * (10.F / (float) invulnerableTicksLimit));
            }
            return;
        }
        if (this.getAnger() > 0) {
            this.setAnger(this.getAnger() - 1);
        }
        if (level().isClientSide) {
            if (random.nextInt(15) == 0 && this.getAnger() > 0) {
                double d0 = random.nextGaussian() * 0.02;
                double d1 = random.nextGaussian() * 0.02;
                double d2 = random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.ANGRY_VILLAGER,
                        this.getX() + (double) (random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / (double) 2.0F,
                        this.getBoundingBox().minY + (double) this.getBbHeight() + (double) random.nextFloat() * (double) 0.5F,
                        this.getZ() + (double) (random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / (double) 2.0F,
                        d0, d1, d2);
            }
        } else {

            if (this.tickCount % 30 == 0) {
                this.heal(1.0F);
            }
            var target = this.getTarget();
            if (target != null && this.tickCount % 20 == 0) {
                IntList dl = new IntArrayList();
                int players = 0;
                int hei = target.getId();
                int ad = this.aggro.getOrDefault(hei, 0);
                int ld = ad;
                LivingEntity newTarget = null;

                for (int ei : this.aggro.keySet()) {
                    int ca = this.aggro.get(ei);
                    if (ca > ad + 25 && (double) ca > (double) ad * 1.1 && ca > ld) {
                        var couldBeNewTarget = this.level().getEntity(hei);
                        if (couldBeNewTarget != null) {
                            if (couldBeNewTarget instanceof LivingEntity livingEntity) {
                                newTarget = livingEntity;
                            }
                        }
                        if (newTarget != null
                                && newTarget.isAlive()
                                && !(this.distanceToSqr(newTarget) > (double) 16384.0F)
                        ) {
                            hei = ei;
                            ld = ei;
                            if (newTarget instanceof Player) {
                                players += 1;
                            }
                        } else {
                            dl.add(ei);
                        }
                    }
                }

                for (int ei : dl) {
                    this.aggro.remove(ei);
                }

                if (newTarget != null && hei != target.getId()) {
                    this.setTarget(newTarget);
                }

                float om = this.getMaxHealth();
                var healthAttributeInstance = this.getAttribute(Attributes.MAX_HEALTH);
                var damageAttributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);

                if (healthAttributeInstance != null){
                    for (int a = 0; a < Math.min(5, players - 1); ++a) {
                        healthAttributeInstance.removeModifier(HP_BUFF_UUIDS[a]);
                        healthAttributeInstance.addPermanentModifier(new AttributeModifier(
                                HP_BUFF_UUIDS[a],
                                "HEALTH BUFF "+a, 50.0, AttributeModifier.Operation.ADDITION
                        ));
                    }
                }
                if (damageAttributeInstance != null){
                    for (int a = 0; a < Math.min(5, players - 1); ++a) {
                        damageAttributeInstance.removeModifier(DMG_BUFF_UUIDS[a]);
                        damageAttributeInstance.addPermanentModifier(new AttributeModifier(
                                DMG_BUFF_UUIDS[a],
                                "DAMAGE BUFF "+a, 0.5, AttributeModifier.Operation.ADDITION
                        ));
                    }
                }
                double mm = this.getMaxHealth() / om;
                this.setHealth((float) ((double) this.getHealth() * mm));
            }

        }
    }

    @Override
    public void aiStep() {
        if (this.getInvulnerableTicks() <= 0) {
            super.aiStep();
        }
    }

    @Override
    public boolean isInvulnerable() {
        return super.isInvulnerable() || this.getInvulnerableTicks() > 0;
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

        EntityUtils.entityDropSpecialItem(this, new ItemStack(ThaumcraftItemInstances.PRIME_PEARL()), this.getBbHeight() / 2.0F);
        this.spawnAtLocation(ThaumcraftItemInstances.RARE_LOOT_BAG().getDefaultInstance(), 1.5F);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!level().isClientSide) {
            if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
                int targetID = source.getEntity().getId();
                int ad = (int)damage;
                if (this.aggro.containsKey(targetID)) {
                    ad += this.aggro.get(targetID);
                }

                this.aggro.put(targetID, ad);
            }

            if (damage > 35.0F) {
                if (this.getAnger() == 0) {
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, (int)(damage / 15.0F)));
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, (int)(damage / 40.0F)));
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, (int)(damage / 40.0F)));
                    this.setAnger(200);

                    if (source.getEntity() != null && source.getEntity() instanceof Player player) {
                        player.sendSystemMessage(this.getDisplayName().copy().append( Component.translatable("tc.boss.enrage")));
                    }
                }

                damage = 35.0F;
            }
        }

        return super.hurt(source, damage);
    }
}
