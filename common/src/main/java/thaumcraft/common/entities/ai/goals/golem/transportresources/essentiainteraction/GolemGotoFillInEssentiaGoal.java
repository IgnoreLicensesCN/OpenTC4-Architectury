package thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public class GolemGotoFillInEssentiaGoal extends AbstractGolemSearchToFillInEssentiaGoal{
    public GolemGotoFillInEssentiaGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected @Nullable Path getPath(BlockPos pos) {
        return golem.getNavigation().createPath(pos,(int)reachRange());
    }
    protected int tickRemaining;
    @Override
    public void start() {
        super.start();
        var pos = searchResultReceiver.getBlockPos();
        if (pos.getX() == Integer.MAX_VALUE && pos.getY() == Integer.MAX_VALUE && pos.getZ() == Integer.MAX_VALUE) {
            tickRemaining = 0;
            return;
        }
        tickRemaining = 200;
        golem.getNavigation().moveTo(getPath(pos),golem.getMoveControl().getSpeedModifier());
    }

    @Override
    public void stop() {
        super.stop();
        tickRemaining = 0;
    }

    @Override
    public void tick() {
        super.tick();
        tickRemaining--;
    }
}
