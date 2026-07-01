package thaumcraft.common.entities.monster.tainted;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;
import thaumcraft.common.entities.ai.goals.DelayControllableMeleeAttackGoal;
import thaumcraft.common.entities.ai.goals.MakeGrassTaintedGoal;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_SHEEP;
import static thaumcraft.common.entities.ThaumcraftEntities.handleTargetSelectorForTaintedMob;

//oh breed light can breed this,but i decide to keep that to make chaos
//however since goal removed they wont make love
public class TaintedSheepEntity extends Sheep implements Enemy, ITaintConvertableEntity {
    public TaintedSheepEntity(Level level) {
        this(TAINTED_SHEEP(), level);
    }
    public TaintedSheepEntity(EntityType<? extends Sheep> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40).add(Attributes.MOVEMENT_SPEED, 0.27F).add(Attributes.ATTACK_DAMAGE,6);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        goalSelector.getAvailableGoals().removeIf(wrapped -> {
            var goal = wrapped.getGoal();
            return goal instanceof PanicGoal
                    || goal instanceof TemptGoal
                    || goal instanceof BreedGoal
                    || goal instanceof FollowParentGoal
                    || goal instanceof EatBlockGoal;
        });
        goalSelector.addGoal(2, new DelayControllableMeleeAttackGoal(this, 1.0F, false).setAttackInterval(10));
        goalSelector.addGoal(5, new MakeGrassTaintedGoal(this));
        handleTargetSelectorForTaintedMob(this,targetSelector);
    }


    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable Sheep getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return TAINTED_SHEEP().create(serverLevel);
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
    public boolean canFallInLove() {
        return false;
    }
    @Override
    public int getArmorValue() {
        return 1;
    }

    @Override
    public boolean canConvertToTaintedMob() {
        return false;
    }

    @Override
    public void convertToTaintedMob() {

    }
}
