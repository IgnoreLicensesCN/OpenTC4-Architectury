package thaumcraft.common.entities.ai.pech;

import net.minecraft.world.entity.ai.goal.Goal;
import thaumcraft.common.entities.monster.pech.AbstractPechEntity;

public class PechTradePlayerGoal extends Goal {
    private AbstractPechEntity villager;

    public PechTradePlayerGoal(AbstractPechEntity par1EntityVillager) {
        this.villager = par1EntityVillager;
    }

    public boolean shouldExecute() {
        if (!this.villager.isAggressive()) {
            return false;
        } else if (this.villager.isInWater()) {
            return false;
        } else if (!this.villager.isTamed()) {
            return false;
        } else if (!this.villager.onGround()) {
            return false;
        } else {
            return !this.villager.hurtMarked && this.villager.trading;
        }
    }

    @Override
    public void start() {
        this.villager.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.villager.trading = false;
    }
    @Override
    public boolean canUse() {
        return false;
    }
}
