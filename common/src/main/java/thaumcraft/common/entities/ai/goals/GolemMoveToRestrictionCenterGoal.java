package thaumcraft.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.EnumSet;

import static com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor.NULL_POS_TO_WRITE;

public class GolemMoveToRestrictionCenterGoal extends Goal {
    private final BaseGolemEntity mob;
    private final double speedModifier;
    private final double wantedClosingRangeSqr;
    private final double wantedTriggerRangeSqr;

    public GolemMoveToRestrictionCenterGoal(BaseGolemEntity mob, double speedModifier, double wantedClosingRange, double wantedTriggerRange) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.wantedClosingRangeSqr = wantedClosingRange * wantedClosingRange;
        this.wantedTriggerRangeSqr = wantedTriggerRange * wantedTriggerRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
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
        this.mob.getNavigation().moveTo(homePos.getX() + 0.5D, homePos.getY(), homePos.getZ() + 0.5D, this.speedModifier);
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
