package thaumcraft.common.entities.monster.tainted;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_SPIDER;
import static thaumcraft.common.entities.ThaumcraftEntities.handleGoalsForTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.handleTargetSelectorForTaintedMob;


public class TaintedSpiderEntity extends Spider implements ITaintConvertableEntity {
    public TaintedSpiderEntity(Level level) {
        this(TAINTED_SPIDER(), level);
    }
    public TaintedSpiderEntity(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 5).add(Attributes.ATTACK_DAMAGE,2);
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
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide && this.tickCount < 5) {
            for(int a = 0; a < ClientFXUtils.particleCount(10); ++a) {
                ClientFXUtils.splooshFX(this);
            }
        }
    }

    @Override
    public int getExperienceReward() {
        return 2;
    }

    @Override
    public float getVoicePitch() {
        return 0.7F;
    }

    @Override
    public float getScale() {
        return 0.4F;
    }

    @Override
    public boolean canConvertToTaintedMob() {
        return false;
    }

    @Override
    public void convertToTaintedMob() {

    }
}
