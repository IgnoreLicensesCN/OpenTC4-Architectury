package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.throwgoal;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_ACTION_TIMER;

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
        this.cooldown = 200;
        var golemPos = golem.position();
        golem.throwItemStack(golem.getMainHandItem(),golemPos);
    }



    @Override
    public void tick() {
        super.tick();
        cooldown -= 1;
    }

    public int cooldown = 0;
    @Override
    public boolean canContinueToUse() {
        return this.canUse() && this.cooldown > 0;
    }
}
