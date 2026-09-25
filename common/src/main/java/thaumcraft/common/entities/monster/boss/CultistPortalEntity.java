package thaumcraft.common.entities.monster.boss;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.monster.cultists.CultistClericEntity;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.entities.monster.cultists.CultistKnightEntity;
import thaumcraft.common.lib.network.fx.PacketFXBlockArcS2C;
import thaumcraft.common.lib.utils.EntityUtils;

import static com.linearity.opentc4.Consts.CultistPortalEntityTagAccessors.STAGE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.CRATE_LOOT;
import static thaumcraft.common.blocks.ThaumcraftBlocks.setCultistBanner;
import static thaumcraft.common.blocks.crafted.loot.DefaultAbstractLootBlock.DEFAULT_RARITY_PROPERTY;

public class CultistPortalEntity extends ThaumcraftBossEntity {
    protected int stage = 0;
    protected int stagecounter = 200;
    public int pulse = 0;

    public CultistPortalEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.CULTIST_PORTAL(), level);
    }

    public CultistPortalEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 30;
        setNoGravity(true);
    }

    @Override
    public int getArmorValue() {
        return 5;
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return ThaumcraftBossEntity.createThaumcraftBossAttributes().add(Attributes.MAX_HEALTH, 500).add(Attributes.ATTACK_DAMAGE, 0).add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void moveTo(double d, double e, double f) {
    }

    @Override
    protected void customServerAiStep() {
    }

    @Override
    public boolean addEffect(MobEffectInstance mobEffectInstance, @Nullable Entity entity) {
        return false;
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!level().isClientSide) {
            var pos = position();
            this.level().explode(this, pos.x, pos.y, pos.z, 2.0F, false, Level.ExplosionInteraction.NONE);
        }
        super.die(damageSource);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 16) {
            this.pulse = 10;
            level().addParticle(
                    ParticleTypes.EXPLOSION,
                    getX(),
                    getEyeY(),
                    getZ(),
                    1.0D, 0.0D, 0.0D
            );
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 540;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ThaumcraftSounds.MONOLITH;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return ThaumcraftSounds.ZAP;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.SHOCK;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        STAGE.writeIntToCompoundTag(tag, stage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        stage = STAGE.readIntFromCompoundTag(tag);
    }

    protected int getTiming() {
        var pos = position();
        var l = EntityUtils.getEntitiesInRange(this.level(), pos.x, pos.y, pos.z, this, CultistEntity.class, 32.0F);
        return l.size() * 20;
    }

    protected void spawnMinions() {
        CultistEntity cultist = null;
        if ((double) this.random.nextFloat() > 0.33) {
            cultist = new CultistKnightEntity(this.level());
        } else {
            cultist = new CultistClericEntity(this.level());
        }
        var pos = position().add(random.nextFloat() * 2 - 1, 0.25, random.nextFloat() * 2 - 1);
        cultist.setPos(pos);
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    pos.x, cultist.getEyeY(), pos.z,
                    1,
                    0.5D, 0.5D, 0.5D,
                    0.0D
            );
        }
        cultist.restrictTo(blockPosition(), 32);
        if (this.level() instanceof ServerLevelAccessor serverLevelAccessor){
            cultist.finalizeSpawn(serverLevelAccessor,serverLevelAccessor.getCurrentDifficultyAt(blockPosition()), MobSpawnType.SPAWNER,null,null);
        }
        this.level().addFreshEntity(cultist);
        cultist.playSound(ThaumcraftSounds.WAND_FAIL, 1.0F, 1.0F);
        if (this.stage > 12) {
            this.hurt(level().damageSources().fellOutOfWorld(), (float) (5 + this.random.nextInt(5)));
        }

    }

    protected void spawnBoss() {
        var cultist = new CultistLeaderEntity(this.level());
        var pos = position().add(random.nextFloat() * 2 - 1, 0.25, random.nextFloat() * 2 - 1);
        cultist.setPos(pos);
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    pos.x, cultist.getEyeY(), pos.z,
                    1,
                    0.5D, 0.5D, 0.5D,
                    0.0D
            );
        }
        cultist.restrictTo(blockPosition(), 32);
        this.level().addFreshEntity(cultist);
        cultist.playSound(ThaumcraftSounds.WAND_FAIL, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        var blockPos = blockPosition();
        if (!level.isClientSide) {
            if (this.stagecounter <= 0) {
                if (level.getNearestPlayer(this, 48.0F) != null) {
                    level.broadcastEntityEvent(this, (byte) 16);
                    switch (this.stage) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            this.stagecounter = 15 + this.random.nextInt(10 - this.stage) - this.stage;
                            this.spawnMinions();
                            break;
                        case 12:
                            this.stagecounter = 50 + this.getTiming() * 2 + this.random.nextInt(50);
                            this.spawnBoss();
                        default:
                            int t = this.getTiming();
                            this.stagecounter = t + this.random.nextInt(5 + t / 3);
                            this.spawnMinions();
                            break;
                    }

                    ++this.stage;
                } else {
                    this.stagecounter = 30 + this.random.nextInt(30);
                }
            } else {
                --this.stagecounter;
                if (this.stagecounter == 160 && this.stage == 0) {
                    level.broadcastEntityEvent(this, (byte) 16);
                    for (var dir:Direction.Plane.HORIZONTAL) {
                        var setAtPos = blockPos.offset(-dir.getStepX()*6,0,dir.getStepZ()*6);
                        setCultistBanner(level, setAtPos, dir);
                        if (level instanceof ServerLevel serverLevel) {
                            new PacketFXBlockArcS2C(setAtPos.getX(),setAtPos.getY(),setAtPos.getZ(),getId()).sendToAllAround(serverLevel,blockPos,32*32);
                        }
                        this.playSound(ThaumcraftSounds.WAND_FAIL, 1.0F, 1.0F);
                    }
                }

                if (this.stagecounter > 20 && this.stagecounter < 150 && this.stage == 0 && this.stagecounter % 13 == 0) {
                    var pickPos = blockPos.offset(random.nextInt(10)-5,0,random.nextInt(10)-5);
                    int a = pickPos.getX();
                    int b = pickPos.getZ();
                    if (a !=  blockPos.getX() && b != blockPos.getZ() && level.getBlockState(pickPos).isAir()) {
                        level.broadcastEntityEvent(this, (byte) 16);
                        float rr = level.random.nextFloat();
                        var md = rr < 0.05F ? 2 : (rr < 0.2F ? 1 : 0);
                        var stateToSet = CRATE_LOOT().defaultBlockState().setValue(DEFAULT_RARITY_PROPERTY,md);
                        level.setBlockAndUpdate(pickPos, stateToSet);
                        if (level instanceof ServerLevel serverLevel) {
                            new PacketFXBlockArcS2C(pickPos.getX(),pickPos.getY(),pickPos.getZ(),getId()).sendToAllAround(serverLevel,blockPos,32*32);
                        }
                        this.playSound(ThaumcraftSounds.WAND_FAIL, 1.0F, 1.0F);
                    }
                }
            }

            if (this.stage < 12) {
                this.heal(1.0F);
            }
        }

        if (this.pulse > 0) {
            --this.pulse;
        }


    }

    @Override
    public void playerTouch(Player p) {
        super.playerTouch(p);

        if (this.distanceToSqr(p) < (double)3.0F && p.hurt(level().damageSources().indirectMagic(this, this), 8.0F)) {
            this.playSound(ThaumcraftSounds.ZAP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F);
        }
    }
}
