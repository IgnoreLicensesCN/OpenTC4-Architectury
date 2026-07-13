package thaumcraft.common.entities.monster.tainted;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.IExplodeOverrideCreeper;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.world.biomes.BiomeUtils;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;
import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_CREEPER;

public class TaintedCreeperEntity extends Creeper implements IExplodeOverrideCreeper, ITaintConvertableEntity {
    public TaintedCreeperEntity(Level level) {
        this(TAINTED_CREEPER(), level);
    }
    public TaintedCreeperEntity(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
    }
    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH,30)
                .add(Attributes.ATTACK_DAMAGE,2);
    }


    @Override
    public void explodeCreeperRewritten(Operation<Void> originalMethod) {
        var level = level();
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            var bpos = blockPosition();
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.MOB);
            level.getEntities(
                    LIVING_TEST,
                    new AABB(bpos).inflate(6),
                    e -> !e.getType().is(ThaumcraftEntities.EntityTags.TAINTED)
                            && e.getMobType() != MobType.UNDEAD
                            && !e.getType().is(ThaumcraftEntities.EntityTags.UNDEAD)
            ).forEach(e -> {
                e.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT(), 100, 0));
            });//TODO:[maybe wont finished]"Vote in democracy"(are u ill with it asdff?) to decide if spawnLingeringCloud instead

            for(int a = 0; a < 10; ++a) {
                if (level.random.nextBoolean()){
                    var pickPos = bpos.offset(random.nextInt(11) - 5, 0, random.nextInt(11) - 5);
                    BiomeUtils.setPosTaintAndSetTaintSourceIfNotTaint(serverLevel, pickPos);
                }
            }

            this.remove(RemovalReason.DISCARDED);
        } else {
            for(int a = 0; a < ClientFXUtils.particleCount(100); ++a) {
                ClientFXUtils.taintsplosionFX(this);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide && this.tickCount < 5) {
            for(int a = 0; a < ClientFXUtils.particleCount(10); ++a) {
                ClientFXUtils.splooshFX(this);
            }
        }
    }

    @Override
    public float getVoicePitch() {
        return 0.7F;
    }

    @Override
    public boolean canConvertToTaintedMob() {
        return false;
    }

    @Override
    public void convertToTaintedMob() {

    }
}
