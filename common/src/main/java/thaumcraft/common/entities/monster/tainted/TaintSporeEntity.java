package thaumcraft.common.entities.monster.tainted;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeEntityClientAccessor;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXSwarm;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.Consts.TaintSporeEntityTagAccessors.SIZE;

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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1).add(Attributes.ATTACK_DAMAGE,1);
    }
    protected void damageIfNotInTainted() {
        var level = level();
        if (tickCount % 20 == 0 && !level.getBiome(blockPosition()).is(ThaumcraftBiomeIDs.TAINT_ID)){
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

        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
        int z = MathHelper.floor_double(this.posZ);
        if (this.level().getBlock(x, y, z) == ConfigBlocks.blockTaintFibres && this.level().getBlockMetadata(x, y, z) == 4) {
            if (this.deathTime > 0) {
                this.spiderBurst();
            }
        } else {
            this.spiderBurst();
        }

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

}
