package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.throwgoal;

import net.minecraft.world.Container;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemDoSomethingToMarkedContainerGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;


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
            golem.throwItemStack(golem.getMainHandItem(),containerCenter);
        }
    }
}
