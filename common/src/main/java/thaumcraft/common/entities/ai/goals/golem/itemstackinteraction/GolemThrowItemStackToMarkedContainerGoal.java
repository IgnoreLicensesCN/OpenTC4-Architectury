package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import net.minecraft.world.Container;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_ACTION_TIMER;


public class GolemThrowItemStackToMarkedContainerGoal extends GolemDoSomethingToMarkedContainerGoal {
    protected int cooldown = 0;
    public GolemThrowItemStackToMarkedContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public void tick() {
        super.tick();
        cooldown-=1;
    }

    @Override
    public void start() {
        super.start();
        if (usingMarker == null){
            return;
        }
        var usingBE = usingBlockEntity.get();
        if (usingBE instanceof Container){
            var containerCenter = usingBE.getBlockPos().getCenter();
            var golemPos = this.golem.position();
            var movementX = containerCenter.x - golemPos.x;
            var movementY = containerCenter.y - golemPos.y;
            var movementZ = containerCenter.z - golemPos.z;
            var distScaled = Math.sqrt(movementX * movementX + movementY * movementY + movementZ * movementZ)/3.;
            var itemEntity = golem.spawnAtLocation(ExtendedItemStack.splitAll(golem.getMainHandItem()),golem.getEyeHeight());
            if (itemEntity != null){
                itemEntity.setDeltaMovement(movementX / distScaled, movementY / distScaled, movementZ / distScaled);
                this.golem.level().broadcastEntityEvent(golem, START_ACTION_TIMER);
            }
        }
    }
}
