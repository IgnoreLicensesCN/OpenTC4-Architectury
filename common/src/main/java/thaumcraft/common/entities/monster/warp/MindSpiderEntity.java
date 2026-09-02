package thaumcraft.common.entities.monster.warp;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ThaumcraftEntities;

import java.util.Optional;
import java.util.UUID;

public class MindSpiderEntity extends Spider {
    private static final EntityDataAccessor<Byte> DATA_MIND_SPIDER_FLAGS = SynchedEntityData.defineId(MindSpiderEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> DATA_VISIBLE_TO = SynchedEntityData.defineId(MindSpiderEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public MindSpiderEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.MIND_SPIDER(), level);
    }
    public MindSpiderEntity(EntityType<? extends MindSpiderEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected int lifeSpan = Integer.MAX_VALUE;
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MIND_SPIDER_FLAGS, (byte) 0);
        this.entityData.define(DATA_VISIBLE_TO,Optional.empty());
    }


    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 1).add(Attributes.ATTACK_DAMAGE,1);
    }

    public static final int HARMLESS_FLAG = 1;
    protected byte getFlags() {
        return this.entityData.get(DATA_MIND_SPIDER_FLAGS);
    }
    public boolean isHarmless() {
        return (this.entityData.get(DATA_MIND_SPIDER_FLAGS) | HARMLESS_FLAG) == 1;
    }
    public void setHarmless(boolean harmless) {
        this.xpReward = harmless ? 0 : 1;
        this.lifeSpan = harmless ? 1200:Integer.MAX_VALUE;
        this.entityData.set(DATA_MIND_SPIDER_FLAGS, (byte)(harmless?getFlags() | HARMLESS_FLAG : (getFlags() & ~HARMLESS_FLAG)));
    }
    public @Nullable UUID getViewerUUID() {
        return this.entityData.get(DATA_VISIBLE_TO).orElse(null);
    }
    public boolean isVisibleTo(Entity entity){
        return entity.getUUID().equals(getViewerUUID());
    }
    public void setVisibleTo(Entity entity){
        this.entityData.set(DATA_VISIBLE_TO, Optional.of(entity.getUUID()));
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (isHarmless()) {
            return false;
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > this.lifeSpan && !level().isClientSide()) {
            this.discard();
        }
    }
}
