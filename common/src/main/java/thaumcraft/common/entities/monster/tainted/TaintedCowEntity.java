package thaumcraft.common.entities.monster.tainted;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_COW;
import static thaumcraft.common.entities.ThaumcraftEntities.handleGoalsForTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.handleTargetSelectorForTaintedMob;

//oh breed light can breed this,but i decide to keep that to make chaos
//however since goal removed they wont make love
public class TaintedCowEntity extends Cow implements Enemy {
    public TaintedCowEntity(Level level) {
        this(TAINTED_COW(), level);
    }
    public TaintedCowEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40).add(Attributes.MOVEMENT_SPEED, 0.27F).add(Attributes.ATTACK_DAMAGE,6);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        handleGoalsForTaintedMob(this,goalSelector);
        handleTargetSelectorForTaintedMob(this,targetSelector);
    }


    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable Cow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return TAINTED_COW().create(serverLevel);
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
}
