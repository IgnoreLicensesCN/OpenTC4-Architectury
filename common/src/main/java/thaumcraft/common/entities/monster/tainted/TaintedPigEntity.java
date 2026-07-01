package thaumcraft.common.entities.monster.tainted;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_PIG;
import static thaumcraft.common.entities.ThaumcraftEntities.handleGoalsForTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.handleTargetSelectorForTaintedMob;

//oh breed light can breed this,but i decide to keep that to make chaos
//however since goal removed they wont make love
public class TaintedPigEntity extends Pig implements Enemy, ITaintConvertableEntity {
    public TaintedPigEntity(Level level) {
        this(TAINTED_PIG(), level);
    }
    public TaintedPigEntity(EntityType<? extends Pig> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.275F).add(Attributes.ATTACK_DAMAGE,4);
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
    public @Nullable Pig getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return TAINTED_PIG().create(serverLevel);
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
