package thaumcraft.common.entities.projectile.pechfocus;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;

import java.util.List;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;

public class PechBlastEntity extends ThrowableProjectile {
    public int strength = 0;
    public int duration = 0;
    public boolean nightshade = false;
    public PechBlastEntity(EntityType<PechBlastEntity> type, Level par1World) {
        super(type, par1World);
    }

    public PechBlastEntity(LivingEntity living, Level par1World) {
        super(ThaumcraftEntities.ThaumcraftEntityTypeInstances.PECH_BLAST(), living, par1World);
        shootFromRotation(living,living.getXRot(),living.getYRot(),0,1.5F,1);
    }
    @Override
    protected void defineSynchedData() {
        
    }

    @Override
    protected float getGravity() {
        return 0.025F;
    }

    @Override
    public void tick() {
        var level = this.level();
        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel) {
                for(int a = 0; a < 3; ++a) {
                    var posWithOffset = position().offsetRandom(random,0.4F);
                    ClientFXUtils.wispFX2(clientLevel,
                            posWithOffset.x(), posWithOffset.y(), posWithOffset.z(),
                            0.3F,
                            3,
                            true,
                            true, 
                            0.02F);
                    var posMidWithOffset = position().add(xOld, yOld, zOld).scale(0.5).offsetRandom(random,0.4F);
                    ClientFXUtils.wispFX2(
                            clientLevel,
                            posMidWithOffset.x,posMidWithOffset.y,posMidWithOffset.z,
                            0.3F, 2, true, true, 0.02F
                    );
                    var posWithOffset2 = position().offsetRandom(random,0.2F).toVector3f();
                    ClientFXUtils.sparkle(
                            posWithOffset2.x, posWithOffset2.y, posWithOffset2.z,
                            5);
                }
            }
        }

        super.tick();
        if (this.tickCount > 500) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        var level = this.level();
        var selfPos = this.position();
        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel) {
                for(int a = 0; a < 9; ++a) {
                    var posOffset = Vec3.ZERO.offsetRandom(random,0.6F);
                    var posOffsetScaled = posOffset.scale(8);
                    var posWithOffset = selfPos.add(posOffset);
                    var posWithOffsetScaled = selfPos.add(posOffsetScaled);
                    ClientFXUtils.wispFX3(clientLevel,
                            posWithOffset.x,posWithOffset.y,posWithOffset.z,
                            posWithOffsetScaled.x,posWithOffsetScaled.y,posWithOffsetScaled.z,
                            0.3F, 3, true, 0.02F);
                    posOffset = Vec3.ZERO.offsetRandom(random,0.6F);
                    posOffsetScaled = posOffset.scale(8);
                    posWithOffset = selfPos.add(posOffset);
                    posWithOffsetScaled = selfPos.add(posOffsetScaled);
                    ClientFXUtils.wispFX3(clientLevel,
                            posWithOffset.x,posWithOffset.y,posWithOffset.z,
                            posWithOffsetScaled.x,posWithOffsetScaled.y,posWithOffsetScaled.z,
                            0.3F, 2, true, 0.02F);
                    posOffset = Vec3.ZERO.offsetRandom(random,0.6F);
                    posOffsetScaled = posOffset.scale(8);
                    posWithOffset = selfPos.add(posOffset);
                    posWithOffsetScaled = selfPos.add(posOffsetScaled);
                    ClientFXUtils.wispFX3(
                            clientLevel,
                            posWithOffset.x,posWithOffset.y,posWithOffset.z,
                            posWithOffsetScaled.x,posWithOffsetScaled.y,posWithOffsetScaled.z,
                            0.3F, 0, true, 0.02F);
                }
            }
        }else {
            var owner = getOwner();
            var entitiesNear = level.getEntities(LIVING_TEST,new AABB(blockPosition()).inflate(2),e ->
                    e != owner && !(e instanceof PechEntity));
            entitiesNear.forEach(living -> {
                living.hurt(level.damageSources().thrown(PechBlastEntity.this,owner),strength+2);
                if (nightshade){
                    for (var effectPair:CAN_APPLY_EFFECTS){
                        living.addEffect(new MobEffectInstance(effectPair.left(),100 + duration * 40,strength + effectPair.rightInt()));
                    }
                }else {
                    var effectPair = CAN_APPLY_EFFECTS.get(random.nextInt(CAN_APPLY_EFFECTS.size()));
                    living.addEffect(new MobEffectInstance(effectPair.left(),100 + duration * 40,strength + effectPair.rightInt()));
                }
            });
        }
        this.remove(RemovalReason.DISCARDED);
        
    }
    public static final List<ObjectIntPair<MobEffect>> CAN_APPLY_EFFECTS = List.of(
            new ObjectIntPair<>(MobEffects.MOVEMENT_SLOWDOWN,0),
            new ObjectIntPair<>(MobEffects.POISON,1),
            new ObjectIntPair<>(MobEffects.WEAKNESS,0)
    );
}
