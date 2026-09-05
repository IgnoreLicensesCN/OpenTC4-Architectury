package thaumcraft.common.entities.monster.tainted;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeSwarmerEntityClientAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXSwarm;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeTags;

import java.util.ArrayList;
import java.util.List;

public class TaintSporeSwarmerEntity extends Monster {
    private static final EntityDataAccessor<Integer> DATA_SIZE_ID = SynchedEntityData.defineId(TaintSporeSwarmerEntity.class, EntityDataSerializers.INT);


    public TaintSporeSwarmerEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINT_SPORE_SWARMER(), level);
    }

    public TaintSporeSwarmerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 75).add(Attributes.ATTACK_DAMAGE,1);
    }

    protected void damageIfNotInTainted() {
        var level = level();
        if (tickCount % 20 == 0 && !level.getBiome(blockPosition()).is(ThaumcraftBiomeTags.TAINTED)){
            this.hurt(level.damageSources().starve(), 1.0F);
        }
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (!level.isClientSide){
            damageIfNotInTainted();
        }
        sporeTick();
    }

    protected int spawnCounter = 500;
    protected void sporeTick() {
        var level = level();
//        this.func_145771_j(this.posX, this.posY, this.posZ);//push out of block
        if (this.spawnCounter > 0) {
            --this.spawnCounter;
        }

        if (this.spawnCounter <= 0 && level.getNearestPlayer(this, 16.0F) != null) {
            this.spawnCounter = 500;
            this.swarmBurst(1);
        }

        if (level.isClientSide) {
            ClientTickContext.clientTick(this);
        }

        if (this.deathTime == 1) {
            this.swarmBurst(1);
        }

    }

    public static class ClientTickContext {
        public List<FXSwarm> swarm = new ArrayList<>();
        public float displaySize = 0.0F;
        public static void clientTick(TaintSporeSwarmerEntity spore){
            var ctx = ((TaintSporeSwarmerEntityClientAccessor)spore).opentc4$getClientTickContext();
            if (ctx.displaySize < (float)spore.getSize()) {
                ctx.displaySize += 0.02F;
            }

            for(int a = 0; a < ctx.swarm.size(); ++a) {
                if (ctx.swarm.get(a) == null || (ctx.swarm.get(a)).isDead()) {
                    ctx.swarm.remove(a);
                    break;
                }
            }

            if (ctx.swarm.size() < spore.getSize() / 3 && spore.level() instanceof ClientLevel clientLevel) {
                ctx.swarm.add(ClientFXUtils.swarmParticleFX(clientLevel, spore, 0.1F, 10.0F, 0.0F));
            }
        }
        public static void clearSwarm(TaintSporeSwarmerEntity spore){
            var ctx = ((TaintSporeSwarmerEntityClientAccessor)spore).opentc4$getClientTickContext();
            ctx.swarm.clear();
        }
    }

    protected void swarmBurst(int amt) {
        var level = level();
        if (!level.isClientSide) {
            this.playSound(ThaumcraftSounds.GORE, 1.0F, 0.9F + random.nextFloat() * 0.1F);

            for(int a = 0; a < amt; ++a) {
                var swarm = new TaintSporeEntity(level);
                swarm.setPos(this.position().add(0,0.5,0));
                swarm.setXRot(random.nextFloat() * 360.0F);
                level.addFreshEntity(swarm);
            }
            level.broadcastEntityEvent(this, (byte)6);
        }

    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 6){
            ClientTickContext.clearSwarm(this);
        }else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {

        if (level().isClientSide) {
            this.sploosh(10);
        }
        return super.hurt(damageSource, f);
    }

    protected void sploosh(int amt) {
        for(int a = 0; a < amt; ++a) {
            ClientFXUtils.splooshFX(this);
        }
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SIZE_ID, 1);
    }
    public int getSize(){
        return this.entityData.get(DATA_SIZE_ID);
    }
    public void setSize(int size){
        this.entityData.set(DATA_SIZE_ID, size);
        this.xpReward = size;
    }
    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ThaumcraftSounds.ROOTS;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return ThaumcraftSounds.GORE;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.GORE;
    }
}
