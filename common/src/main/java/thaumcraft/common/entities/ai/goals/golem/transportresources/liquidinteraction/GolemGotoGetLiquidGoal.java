package thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction;

import com.google.common.math.IntMath;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.Marker;

import java.math.RoundingMode;

public class GolemGotoGetLiquidGoal extends AbstractGolemFindLiquidToGetGoal{
    public int tickRemaining = 0;
    public BlockPos.MutableBlockPos prevPos = new BlockPos.MutableBlockPos();
    public GolemGotoGetLiquidGoal(BaseGolemEntity golem) {
        super(golem);

        this.fluidHandlerIterator = new MarkedFluidHandlerIterator(golem){
            @Override
            protected boolean isValidMarker(Marker marker) {
                var path = getPath(marker);
                if (path == null || !path.canReach()) {
                    return false;
                }
                return super.isValidMarker(marker);
            }
        };
    }

    protected @Nullable Path getPath(Marker marker) {
        return golem.getNavigation().createPath(marker.getPos(),(int)reachRange());
    }
    @Override
    public void start() {
        super.start();
        if (this.targetMarker == null) {
            tickRemaining = 0;
            return;
        }
        tickRemaining = 200;
        prevPos.set(golem.blockPosition());
        golem.getNavigation().moveTo(getPath(targetMarker),golem.getMoveControl().getSpeedModifier());
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
        prevPos.set(golem.blockPosition());
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && tickRemaining > 0 && !golem.getNavigation().isDone();
    }
}
