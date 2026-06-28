package thaumcraft.common.entities.monster.tainted;

import com.google.common.collect.ImmutableList;
import com.linearity.opentc4.mixin.BrainProviderAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.abstracts.IBrainGoalsOverrideVillager;
import thaumcraft.common.entities.abstracts.IZombieConvertableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_VILLAGER;

//rewritten in lazy
public class TaintedVillagerEntity extends Villager implements IBrainGoalsOverrideVillager, Enemy, IZombieConvertableEntity {
    public TaintedVillagerEntity(Level level) {
        this(TAINTED_VILLAGER(),level);
    }
    public TaintedVillagerEntity(Level level, VillagerType villagerType) {
        this(TAINTED_VILLAGER(),level,villagerType);
    }

    public TaintedVillagerEntity(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
    }

    public TaintedVillagerEntity(EntityType<? extends Villager> entityType, Level level, VillagerType villagerType) {
        super(entityType, level, villagerType);
        setPersistenceRequired();
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    @Override
    protected Brain.@NotNull Provider<Villager> brainProvider() {
        var parentResult = super.brainProvider();
        var parentResultAccessor = (BrainProviderAccessor)(Object)parentResult;
        var oldMemoryTypes = parentResultAccessor.opentc4$getMemoryTypes();
        List<MemoryModuleType<?>> memoryTypes = new ArrayList<>(oldMemoryTypes.size() + 1);
        memoryTypes.addAll(oldMemoryTypes);
        memoryTypes.add(MemoryModuleType.ATTACK_TARGET);
        parentResultAccessor.opentc4$setMemoryTypes(memoryTypes);
        return parentResult;
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Villager.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 30.0F)
                .add(Attributes.ATTACK_DAMAGE, 4);
    }

    @Override
    public void registerBrainGoalsRewritten(Brain<? extends Villager> brain, Operation<Void> originalMethod) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                999,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.create(target -> !isNearestValidAttackTarget(this, target)),
                        BehaviorBuilder.triggerIf(_i -> true, BackUpIfTooClose.create(5, 0.75F)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(20)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
        originalMethod.call(brain);
    }


    protected boolean isNearestValidAttackTarget(Villager villager, LivingEntity arg2) {
        return findNearestValidAttackTarget(villager).filter(arg2x -> arg2x == arg2).isPresent();
    }
    protected Optional<? extends LivingEntity> findNearestValidAttackTarget(Villager arg) {
        var brain = arg.getBrain();

        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(arg, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(arg, optional.get())) {
            return optional;
        } else {
            return brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
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

    @Nullable
    public Villager getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        double d = this.random.nextDouble();
        VillagerType villagerType;
        if (d < 0.5) {
            villagerType = VillagerType.byBiome(serverLevel.getBiome(this.blockPosition()));
        } else if (d < 0.75) {
            villagerType = this.getVillagerData().getType();
        } else {
            villagerType = ((Villager)ageableMob).getVillagerData().getType();
        }

        Villager villager = new TaintedVillagerEntity(EntityType.VILLAGER, serverLevel, villagerType);
        villager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(villager.blockPosition()), MobSpawnType.BREEDING, null, null);
        return villager;
    }

    @Override
    public boolean zombify(ServerLevel level, Zombie killer) {
        //TODO:[maybe wont finished] zombify tainted villager or leave blank
        return false;
    }

}
