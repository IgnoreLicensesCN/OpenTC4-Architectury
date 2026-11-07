package thaumcraft.common.entities.ai.goals.golem;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.entity.ai.goal.Goal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.EnumSet;

@UtilityLikeAbstraction
public abstract class AbstractGolemGoal extends Goal {
    public static final EnumSet<Flag> FLAGS = EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK);
    protected final BaseGolemEntity golem;

    protected AbstractGolemGoal(BaseGolemEntity golem) {
        this.golem = golem;
        setFlags(FLAGS);
    }
}
