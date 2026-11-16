package thaumcraft.common.entities.ai.goals.golem.miscjobs;

import com.linearity.opentc4.utils.EasyLCGGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.special.IReplantCropUpgradeItem;
import thaumcraft.common.lib.utils.CropUtils;

import java.util.Objects;

import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_ACTION_TIMER;

public class GolemHarvestCropsGoal extends AbstractGolemGoal {
    private int delay = -1;
    private int mod = 1;
    protected int tickCountRemaining = 0;
    protected EasyLCGGenerator LCGGen = new EasyLCGGenerator((getFarmingDistance()*2+1)*(getFarmingDistance()*2+1));
    public GolemHarvestCropsGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected int getFarmingDistance() {
        return (int) (golem.getHomeRange() / 4.F);
    }
    protected int getFarmingDistanceY() {
        return 3;
    }
    protected int getReachDistance() {
        return 2;
    }
    protected int getCheckNearHorizontalOffset() {
        return 2;
    }
    protected int getCheckNearVerticalOffset() {
        return 1;
    }

    protected BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos(0, 0, 0);
    protected @Nullable BlockState targetState;
    @Override
    public boolean canUse() {

        if (this.delay < 0
                && this.golem.tickCount % getGolemGoalDelay() <= 0
                && !this.golem.getNavigation().isInProgress()
        ) {
            return this.findGrownCrop();
        } else {
            return false;
        }
    }

    protected boolean findGrownCrop() {
        var level = golem.level();
        if (level instanceof ServerLevel serverLevel){
            int xz = LCGGen.next();
            int xOffset = xz%(getFarmingDistance()*2+1);
            int zOffset = xz/(getFarmingDistance()*2+1);
            BlockPos homePos = golem.getRestrictCenter();
            int x = homePos.getX() + xOffset;
            int z = homePos.getZ() + zOffset;
            int yCenter = homePos.getY();
            for (int y = yCenter-3; y <= yCenter + 3; y++) {
                if (checkAndHarvest(serverLevel,x,y,z)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(0, 0, 0);
    private boolean checkAndHarvest(ServerLevel serverLevel,int x,int y,int z) {
        checkPos.set(x, y, z);
        if (CropUtils.isHarvestableCrop(serverLevel,checkPos,golem)){
            targetPos.set(checkPos);
            targetState = serverLevel.getBlockState(targetPos);
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.tickCountRemaining = getMaxTickCountRemaining();
        golem.getNavigation().moveTo(targetPos.getX(),targetPos.getY(),targetPos.getZ(),golem.getMoveControl().getSpeedModifier());
    }

    @Override
    public boolean canContinueToUse() {
        var level = golem.level();
        return Objects.equals(targetState,level.getBlockState(targetPos))
                && tickCountRemaining > 0
                && (golem.getNavigation().isInProgress() || delay > 0)
                && targetState != null
                ;
    }

    @Override
    public void tick() {
        super.tick();
        tickCountRemaining--;
        var level = golem.level();
        double distanceSquaredToTarget =
                targetPos.distToCenterSqr(golem.position());
        golem.getLookControl().setLookAt(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F,30,30);
        if (distanceSquaredToTarget <= getReachDistance() * getReachDistance() && targetState != null) {
            if (this.delay < 0) {
                this.delay = (int)Math.max(10.0F, (20.0F - (float)this.golem.getStrength() * 2.0F)
                        * this.targetState.getDestroySpeed(level,targetPos));
                this.mod = this.delay / Math.round((float)this.delay / 6.0F);
            }

            if (this.delay > 0) {
                if (--this.delay > 0 && this.delay % this.mod == 0 && !this.golem.getNavigation().isInProgress()) {
                    level.broadcastEntityEvent(golem,START_ACTION_TIMER);
                }

                if (this.delay == 0) {
                    tryHarvest(level,targetPos);
                    this.checkAdjacent();
                }
            }
        }
    }

    protected void tryHarvest(Level level,BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            if (CropUtils.tryHarvest(serverLevel, pos, golem)){
                for (var replantUpgradePair:golem.golemUpgradesCacheView.get(IReplantCropUpgradeItem.class)){
                    var upgrade = replantUpgradePair.getUpgradeType();
                    var stack = replantUpgradePair.getUpgradeStack();
                    if (upgrade.canTryReplant(golem,stack,pos)){
                        upgrade.checkSeedNearbyAndReplant(golem,stack,pos);
                    }
                }
            }
        }
    }

    protected void checkAdjacent(){
        if (golem.level() instanceof ServerLevel serverLevel){
            int horizontalOffset = getCheckNearHorizontalOffset();
            int verticalOffset = getCheckNearVerticalOffset();
            int farmingDistance = getFarmingDistance();
            for(int xOffset = -horizontalOffset; xOffset <= horizontalOffset; ++xOffset) {
                for(int zOffset = -horizontalOffset; zOffset <= horizontalOffset; ++zOffset) {
                    for(int yOffset = -verticalOffset; yOffset <= verticalOffset; ++yOffset) {
                        int x = targetPos.getX() + xOffset;
                        int y = targetPos.getY() + yOffset;
                        int z = targetPos.getZ() + zOffset;
                        var homePos = golem.getRestrictCenter();

                        if ((homePos.getX() - x) <=  farmingDistance
                                && (homePos.getY() - y) <= farmingDistance
                                && (homePos.getZ() - z) <= farmingDistance) {
                            if (checkAndHarvest(serverLevel,x,y,z)){
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        delay = -1;
    }
}
