package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.gotogoal.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public abstract class AbstractGolemGotoGoal extends AbstractGolemGoal {
    protected @Nullable Vec3 prevPos = null;
    protected int tickRemaining = 0;
    private @Nullable BlockPos dest;

    protected void setDest(@Nullable BlockPos dest) {
        this.dest = dest;
        if (dest != null) {
            setWantMoveTo(dest);
        }
    }
    protected void setWantMoveTo(@NotNull BlockPos dest){
        golem.getNavigation().moveTo(
                dest.getX() + 0.5,
                dest.getY() + 0.5,
                dest.getZ() + 0.5,
                golem.getMoveControl().getSpeedModifier()
        );
    }

    protected AbstractGolemGotoGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public boolean canContinueToUse() {
        return this.tickRemaining > 0 && !this.golem.getMoveControl().hasWanted();
    }

    @Override
    public void tick() {
        --this.tickRemaining;
        if (this.tickRemaining == 0
                && golem.position().equals(this.prevPos)
        ) {
            //back to where near home
            var homePos = golem.getRestrictCenter();//RandomPositionGenerator.findRandomTarget(this.theGolem, 2, 1);
            this.tickRemaining = 20;
            setWantMoveTo(homePos);
        }
        super.tick();
    }

    @Override
    public void stop() {
        super.stop();
        this.tickRemaining = 0;
        this.dest = null;
    }

    @Override
    public void start() {
        super.start();
        this.tickRemaining = 200;
        this.prevPos = this.golem.position();
        if (dest != null) {
            setWantMoveTo(dest);
        }
    }
}
