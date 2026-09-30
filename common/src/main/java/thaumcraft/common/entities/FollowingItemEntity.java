package thaumcraft.common.entities;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import thaumcraft.common.ClientFXUtils;

public class FollowingItemEntity extends ItemEntity {
    private static final EntityDataAccessor<Integer> DATA_ID_RENDER_TYPE = SynchedEntityData.defineId(FollowingItemEntity.class, EntityDataSerializers.INT);
    protected Vec3 targetPos = null;
    public Entity targetEntity = null;
//    protected int renderType = 3;
    protected int age;
    public double gravity;
    public FollowingItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
        this.age = 20;
        setRenderType(3);
    }

    public FollowingItemEntity(Level level, double d, double e, double f, ItemStack itemStack) {
        this(level, d, e, f, itemStack, level.random.nextDouble() * 0.2 - 0.1, 0.2, level.random.nextDouble() * 0.2 - 0.1);
    }

    public FollowingItemEntity(Level level, double d, double e, double f, ItemStack itemStack, double g, double h, double i) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.FOLLOWING_ITEM(), level);
        this.setPos(d, e, f);
        this.setDeltaMovement(g, h, i);
        this.setItem(itemStack);
    }

    public void setRenderType(int renderType) {
        this.entityData.set(DATA_ID_RENDER_TYPE, renderType);
    }
    public int getRenderType() {
        return this.entityData.get(DATA_ID_RENDER_TYPE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_RENDER_TYPE, 0);
    }

    @Override
    public void tick() {
        if (this.targetEntity != null || this.targetPos != null) {
            this.setNoGravity(true);
        }else {
            this.setNoGravity(false);
        }
        if (this.targetEntity != null) {
            this.targetPos = this.targetEntity.getEyePosition();
        }

        if (this.targetPos == null) {
            this.addDeltaMovement(new Vec3(0,-gravity,0));
        } else {
            var vecFromThisToTarget = this.targetPos.subtract(position());
            if (this.age > 1) {
                --this.age;
            }

            double distance = vecFromThisToTarget.lengthSqr();
            if (distance > (double)0.5F) {
                distance *= this.age;
                this.setDeltaMovement(vecFromThisToTarget.scale(1./distance));
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.1F));
                this.targetPos = null;
                this.targetEntity = null;
                this.noPhysics = false;
            }

        }
        super.tick();
        if (level().isClientSide()) {
            if (level() instanceof ClientLevel clientLevel){
                var type = this.getRenderType();
                if (type != 10) {
                    ClientFXUtils.sparkle(
                            (float)this.xOld + (this.random.nextFloat()*2-1) * 0.125F,
                            (float)this.yOld + this.getEyeHeight() + (this.random.nextFloat()*2-1) * 0.125F,
                            (float)this.zOld + (this.random.nextFloat()*2-1) * 0.125F,
                            type);
                } else {
                    ClientFXUtils.crucibleBubble(
                            clientLevel,
                            (float)this.xOld + (this.random.nextFloat()*2-1) * 0.125F,
                            (float)this.yOld + this.getEyeHeight() + (this.random.nextFloat()*2-1) * 0.125F,
                            (float)this.zOld + (this.random.nextFloat()*2-1) * 0.125F,
                            0.33F, 0.33F, 1.0F
                    );
                }
            }
        }
    }
}
