package thaumcraft.common.entities.monster.tainted;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeEntityClientAccessor;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXSwarm;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.monster.tainted.converted.TaintedSpiderEntity;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeTags;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.Consts.TaintSporeEntityTagAccessors.SIZE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.MATURE_SPORE_STALK;

public class TaintSporeEntity extends Monster {
    private static final EntityDataAccessor<Integer> DATA_SIZE_ID = SynchedEntityData.defineId(TaintSporeEntity.class, EntityDataSerializers.INT);
    public TaintSporeEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINT_SPORE(), level);
    }
    public TaintSporeEntity(EntityType<? extends TaintSporeEntity> entityType, Level level) {
        super(entityType, level);
        setSize(2);
    }
    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 1).add(Attributes.ATTACK_DAMAGE,1);
    }
    protected void damageIfNotInTainted() {
        var level = level();
        if (tickCount % 20 == 0 && !level.getBiome(blockPosition()).is(ThaumcraftBiomeTags.TAINTED)){
            this.hurt(level.damageSources().starve(), 1.0F);
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
        reapplyPosition();
        refreshDimensions();
        this.xpReward = size;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose arg) {
        var size = Math.max(0.15F * (float)getSize(), 0.5F);
        return super.getDimensions(arg).scale(size);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setSize(SIZE.readIntFromCompoundTag(compoundTag));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        SIZE.writeIntToCompoundTag(compoundTag,getSize());
    }

    @Override
    public boolean isPushable() {
        return false;
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

    protected int growth = 0;

    protected void sporeTick() {
        if (this.getSize() < 10 && this.growth++ == 1200) {
            this.setSize(this.getSize() + 1);
            this.growth = 0;
        }

        if (ClientFXUtils.checkPlatformClient()) {
            ClientTickContext.clientTick(this);
        }
        
        var selfBlockPos = blockPosition();
        if (level().getBlockState(new BlockPos(
                selfBlockPos.getX(),
                MathHelper.floor_double(getBoundingBox().minY-1),
                selfBlockPos.getZ()
                )
        ).getBlock() == MATURE_SPORE_STALK()) {
            if (this.deathTime > 0) {
                this.spiderBurst();
            }
        } else {
            this.spiderBurst();
        }
    }

    @Override
    public void playerTouch(Player player) {
        this.spiderBurst();
    }

    public static class ClientTickContext {
        public List<FXSwarm> swarm = new ArrayList<>();
        public float displaySize = 0.0F;
        public static void clientTick(TaintSporeEntity spore){
            var ctx = ((TaintSporeEntityClientAccessor)spore).opentc4$getClientTickContext();
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
    }
    protected void spiderBurst() {
        var level = level();
        if (!level.isClientSide) {
            playSound(ThaumcraftSounds.GORE, 1.0F, 0.9F + random.nextFloat() * 0.1F);
            int q = this.getSize() / 3 + random.nextInt(this.getSize() / 2 + 1);

            for(int a = 0; a < q; ++a) {
                var spider = new TaintedSpiderEntity(level);
                spider.setPos(
                        position().add(
                                random.nextFloat()*2-1,
                                random.nextFloat(),
                                random.nextFloat()*2-1
                        )
                );
                spider.setXRot(random.nextFloat() * 360F);
                level.addFreshEntity(spider);
            }

            var selfBlockPos = blockPosition();
            var checkPos = new BlockPos(
                    selfBlockPos.getX(),
                    MathHelper.floor_double(getBoundingBox().minY-1),
                    selfBlockPos.getZ()
            );
            if (level().getBlockState(checkPos).getBlock() == MATURE_SPORE_STALK()) {
                level.setBlockAndUpdate(checkPos, Blocks.DIRT.defaultBlockState());
            }

            this.discard();
        } else {
            this.sploosh(50);
        }

    }


    protected void sploosh(int amt) {
        for(int a = 0; a < amt; ++a) {
            ClientFXUtils.splooshFX(this);
        }
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
        return ThaumcraftSounds.SWARM;
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
