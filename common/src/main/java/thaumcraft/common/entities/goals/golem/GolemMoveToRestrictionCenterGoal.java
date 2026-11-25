package thaumcraft.common.entities.goals.golem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.EnumSet;

import static com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor.NULL_POS_TO_WRITE;

public class GolemMoveToRestrictionCenterGoal extends Goal {
    public static final EnumSet<Goal.Flag> FLAGS = EnumSet.of(Goal.Flag.MOVE);
    protected final BaseGolemEntity mob;
    protected final double speedModifier;
    protected final int wantedClosingRange;
    protected final double wantedClosingRangeSqr;
    protected final double wantedTriggerRangeSqr;

    public GolemMoveToRestrictionCenterGoal(BaseGolemEntity mob, double speedModifier, double wantedClosingRange, double wantedTriggerRange) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.wantedClosingRange = (int) wantedClosingRange;
        this.wantedClosingRangeSqr = wantedClosingRange * wantedClosingRange;
        this.wantedTriggerRangeSqr = wantedTriggerRange * wantedTriggerRange;
        this.setFlags(FLAGS);
    }

    @Override
    public boolean canUse() {
        if (!this.mob.hasRestriction() || NULL_POS_TO_WRITE.equals(this.mob.getRestrictCenter())) {
            return false;
        }
        BlockPos homePos = this.mob.getRestrictCenter();
        return this.mob.blockPosition().distSqr(homePos) > wantedTriggerRangeSqr;
    }

    @Override
    public void start() {
        BlockPos homePos = this.mob.getRestrictCenter();
        var path = this.mob.getNavigation().createPath(homePos,wantedClosingRange);
        this.mob.getNavigation().moveTo(path, this.speedModifier);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.mob.hasRestriction()) return false;
        BlockPos homePos = this.mob.getRestrictCenter();
        return this.mob.blockPosition().distSqr(homePos) > wantedClosingRangeSqr && !this.mob.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }
}
