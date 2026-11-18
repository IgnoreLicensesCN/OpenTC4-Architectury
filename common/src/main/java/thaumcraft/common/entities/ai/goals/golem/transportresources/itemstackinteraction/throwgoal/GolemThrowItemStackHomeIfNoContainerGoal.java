package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.throwgoal;

import net.minecraft.world.Container;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class GolemThrowItemStackHomeIfNoContainerGoal extends AbstractGolemGoal {
    public GolemThrowItemStackHomeIfNoContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public boolean canUse() {
        var stackInHand = golem.getMainHandItem();
        if (stackInHand.isEmpty()) {
            return false;
        }
        if (golem.getRestrictCenter().distToCenterSqr(golem.position()) < maxToHomeDistanceSquaredToExecute() 
                && golem.level().dimension().location().equals(golem.homeLevelResourceLocation)){
            var bePos = golem.getHomeAttachedBlockPos();
            var be = getExistingBlockEntity(golem.level(),bePos);
            return !(be instanceof Container);
        }
        return false;
    }
    public double maxToHomeDistanceSquaredToExecute(){
        return 5;
    }

    @Override
    public void start() {
        super.start();
        this.tickCountRemaining = getMaxTickCountRemaining();
        var golemPos = golem.position();
        golem.throwItemStack(golem.getMainHandItem(),golemPos);
    }



    @Override
    public void tick() {
        super.tick();
        tickCountRemaining -= 1;
    }

    public int tickCountRemaining = 0;
    @Override
    public boolean canContinueToUse() {
        return this.canUse() && this.tickCountRemaining > 0;
    }
}
