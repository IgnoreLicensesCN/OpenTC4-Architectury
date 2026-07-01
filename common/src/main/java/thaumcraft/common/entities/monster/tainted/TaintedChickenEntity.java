package thaumcraft.common.entities.monster.tainted;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_CHICKEN;
import static thaumcraft.common.entities.ThaumcraftEntities.handleGoalsForTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.handleTargetSelectorForTaintedMob;

public class TaintedChickenEntity extends Chicken implements Enemy, ITaintConvertableEntity {
    public TaintedChickenEntity(Level level) {
        this(TAINTED_CHICKEN(), level);
    }
    public TaintedChickenEntity(EntityType<? extends Chicken> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.MOVEMENT_SPEED, 0.4F).add(Attributes.ATTACK_DAMAGE,3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        handleGoalsForTaintedMob(this,goalSelector);
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.3F));
        handleTargetSelectorForTaintedMob(this,targetSelector);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable Chicken getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return TAINTED_CHICKEN().create(serverLevel);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide && this.tickCount < 5) {
            for(int a = 0; a < ClientFXUtils.particleCount(10); ++a) {
                ClientFXUtils.splooshFX(this);
            }
        }
        this.eggTime = 0x7ffff;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public int getArmorValue() {
        return 2;
    }

    @Override
    public boolean canConvertToTaintedMob() {
        return false;
    }

    @Override
    public void convertToTaintedMob() {

    }
}
