package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.gotogoal;

import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;

import java.util.Iterator;

public abstract class AbstractGolemGotoFurthestContainerGoal extends AbstractGolemGotoGoal{

    protected AbstractGolemGotoFurthestContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected @Nullable BlockPos getFurthestContainerPos(
            Iterator<GolemHelper.ContainerInfo> iterator,
            GolemHelper.Resettable iteratorResetter /* == iterator*/,
            ByteSet colors) {
        return getFurthestContainerPos(this.golem,iterator, iteratorResetter, colors);
    }

    //idk if i should make it "public static" or "protected" so this.
    public static @Nullable BlockPos getFurthestContainerPos(BaseGolemEntity golem,Iterator<GolemHelper.ContainerInfo> iterator, GolemHelper.Resettable iteratorResetter, ByteSet colors) {
        double minDistanceSqr = Double.MAX_VALUE;
        BlockPos closestContainerPos = null;
        while (iterator.hasNext()) {
            var containerInfo = iterator.next();
            var container = containerInfo.containerRef == null ? null : containerInfo.containerRef.get();
            var be = containerInfo.blockEntityRef == null ? null : containerInfo.blockEntityRef.get();
            var marker = containerInfo.marker;
            if (marker == null || container == null || be == null || be.isRemoved()) {
                continue;
            }
            if (!marker.colorEqualsFuzzy(colors)) {
                continue;
            }
            {
                var homeBEPos = golem.getHomeAttachedBlockPos();
                float dmod = golem.getHomeRange();
                BlockPos containerPos = be.getBlockPos();
                double distanceSqr = containerPos.distToCenterSqr(golem.position());
                if (distanceSqr < minDistanceSqr
                        && distanceSqr <= (dmod * dmod)
                        && !containerPos.equals(homeBEPos)
                ) {
                    minDistanceSqr = distanceSqr;
                    closestContainerPos = containerPos;
                }
            }
        }

        iteratorResetter.reset();
        return closestContainerPos;
    }
}
