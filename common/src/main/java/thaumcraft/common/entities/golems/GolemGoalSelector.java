package thaumcraft.common.entities.golems;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.jetbrains.annotations.ApiStatus;
import thaumcraft.common.lib.resourcelocations.GoalResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GolemGoalSelector {
    private final Map<GoalResourceLocation, Goal> goals = new HashMap<>();
    private final GoalSelector wrappedSelector;

    public GolemGoalSelector(GoalSelector wrappedSelector) {
        this.wrappedSelector = wrappedSelector;
    }

    public void addGoalIfNotExist(int i, GoalResourceLocation id, Function<BaseGolemEntity, Goal> goalProvider, BaseGolemEntity golem) {
        if (this.goals.containsKey(id)) {
            return;
        }
        addGoal(i, id, goalProvider.apply(golem));
    }

    public void addGoal(int i, GoalResourceLocation id, Goal goal) {
        var oldGoal = this.goals.put(id, goal);
        if (oldGoal != null) {
            wrappedSelector.removeGoal(oldGoal);
        }
        wrappedSelector.addGoal(i, goal);
    }

    public void removeGoal(GoalResourceLocation id) {
        var gotGoal = goals.remove(id);
        if (gotGoal != null) {
            wrappedSelector.removeGoal(gotGoal);
        }
    }

    public void removeAllGoals(Predicate<Goal> predicate) {
        List<GoalResourceLocation> toRemove = new ArrayList<>();
        for (var goalPair : goals.entrySet()) {
            if (predicate.test(goalPair.getValue())) {
                toRemove.add(goalPair.getKey());
                wrappedSelector.removeGoal(goalPair.getValue());
            }
        }
        toRemove.forEach(goals::remove);
    }

    @ApiStatus.Internal
    public void tick() {
        wrappedSelector.tick();
    }

    @ApiStatus.Internal
    public void tickRunningGoals(boolean forceTick) {
        wrappedSelector.tickRunningGoals(forceTick);
    }

    @ApiStatus.Internal
    public Set<WrappedGoal> getAvailableGoals() {
        return wrappedSelector.getAvailableGoals();
    }

    @ApiStatus.Internal
    public Stream<WrappedGoal> getRunningGoals() {
        return wrappedSelector.getRunningGoals();
    }

    @ApiStatus.Internal
    public void setNewGoalRate(int i) {
        wrappedSelector.setNewGoalRate(i);
    }

    @ApiStatus.Internal
    public void disableControlFlag(Goal.Flag flag) {
        wrappedSelector.disableControlFlag(flag);
    }

    @ApiStatus.Internal
    public void enableControlFlag(Goal.Flag flag) {
        wrappedSelector.enableControlFlag(flag);
    }

    @ApiStatus.Internal
    public void setControlFlag(Goal.Flag flag, boolean bl) {
        wrappedSelector.setControlFlag(flag, bl);
    }
}
