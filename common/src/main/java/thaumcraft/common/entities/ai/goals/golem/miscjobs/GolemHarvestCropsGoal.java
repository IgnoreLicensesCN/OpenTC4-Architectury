package thaumcraft.common.entities.ai.goals.golem.miscjobs;

import com.linearity.opentc4.utils.EasyLCGGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.CropUtils;

import java.util.Collections;
import java.util.Random;

public class GolemHarvestCropsGoal extends AbstractGolemGoal {
    private int delay;
    protected EasyLCGGenerator LCGGen = new EasyLCGGenerator((getFarmingDistance()*2+1)*(getFarmingDistance()*2+1));
    protected GolemHarvestCropsGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected int getFarmingDistance() {
        return (int) (golem.getHomeRange() / 4.F);
    }
    protected int getFarmingDistanceY() {
        return 3;
    }

    protected BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos(0, 0, 0);
    protected @Nullable BlockState targetState;
    @Override
    public boolean canUse() {

        if (this.delay < 0
                && this.golem.tickCount % getGolemGoalDelay() <= 0
                && !this.golem.getNavigation().isInProgress()
        ) {
            ;
            return this.findGrownCrop();
        } else {
            return false;
        }
    }

    protected boolean findGrownCrop() {
        int xz = LCGGen.next();
        int xOffset = xz%(getFarmingDistance()*2+1);
        int zOffset = xz/(getFarmingDistance()*2+1);
        BlockPos homePos = golem.getRestrictCenter();
        int x = homePos.getX() + xOffset;
        int z = homePos.getZ() + zOffset;
        int yCenter = homePos.getY();
        for (int y = yCenter-3; y <= yCenter + 3; y++) {

        }

        for(int y = this.theGolem.getHomePosition().posY - 3; y <= this.theGolem.getHomePosition().posY + 3; ++y) {
            if (CropUtils.isGrownCrop(this.theWorld, x, y, z)) {
                return Vec3.createVectorHelper(x, y, z);
            }
        }

        return null;
    }
}
