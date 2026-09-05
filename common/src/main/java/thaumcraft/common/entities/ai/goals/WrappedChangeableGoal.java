package thaumcraft.common.entities.ai.goals;

import com.linearity.opentc4.mixin.GoalAccessor;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class WrappedChangeableGoal extends Goal {

    public WrappedChangeableGoal() {

    }
    protected @Nullable Goal currentStartedGoal;
    protected Goal getCurrentGoal() {
        if (currentStartedGoal == null){
            currentStartedGoal = pickGoal();
        }
        return currentStartedGoal;
    }
    protected abstract Goal pickGoal();

    @Override
    public boolean canUse() {
        return getCurrentGoal().canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return getCurrentGoal().canContinueToUse();
    }

    @Override
    public boolean isInterruptable() {
        return getCurrentGoal().isInterruptable();
    }

    @Override
    public void start() {
        getCurrentGoal().start();
    }

    @Override
    public void stop() {
        if (currentStartedGoal != null){
            currentStartedGoal.stop();
        }
        currentStartedGoal = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return getCurrentGoal().requiresUpdateEveryTick();
    }

    @Override
    public void tick() {
        getCurrentGoal().tick();
    }

    @Override
    public void setFlags(EnumSet<Flag> enumSet) {
        getCurrentGoal().setFlags(enumSet);
    }

    @Override
    public @NotNull EnumSet<Flag> getFlags() {
        return getCurrentGoal().getFlags();
    }

    @Override
    protected int adjustedTickDelay(int i) {
        return ((GoalAccessor)getCurrentGoal()).adjustedTickDelay(i);
    }
}
