package thaumcraft.common.entities.ai.goals.golem.miscjobs;

import com.linearity.opentc4.utils.HighestFurthestBlockFinder;
import com.linearity.opentc4.utils.random.LCGBlockPosGenerator;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.Utils;

import java.util.UUID;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_ACTION_TIMER;

public class GolemHarvestLogsGoal extends AbstractGolemGoal {
    public static final UUID LOG_HARVESTER_FAKE_PLAYER_UUID = UUID.fromString("b8fb917b-f2ff-4a04-8e97-a3ab9a7f2934");
    public GolemHarvestLogsGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public boolean canUse() {
        if (golem.tickCount % getGolemGoalDelay() != 0){
            return false;
        }
        if (!golem.getNavigation().isDone()) {
            return false;
        }

        searchLog();

        return !(searchLogResult.getX() == Integer.MAX_VALUE && searchLogResult.getY() == Integer.MAX_VALUE && searchLogResult.getZ() == Integer.MAX_VALUE);
    }

    protected int remainingTicks = 0;
    protected int breakingRemainingTicks = 0;
    protected int maxBreakTicks = 1;

    @Override
    public boolean canContinueToUse() {
        if (searchLogResult.getX() == Integer.MAX_VALUE && searchLogResult.getY() == Integer.MAX_VALUE && searchLogResult.getZ() == Integer.MAX_VALUE){
            return false;
        }
        if (remainingTicks <= 0){
            return false;
        }
        return breakingRemainingTicks > 0 || Utils.isWoodLog(golem.level(),searchLogResult) || golem.getNavigation().isInProgress();
    }

    @Override
    public void stop() {
        super.stop();
        searchLogResult.set(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        remainingTicks = 0;
        breakingRemainingTicks = 0;
    }

    @Override
    public void start() {
        super.start();
        resetRemainingTicks();
        findConnectedLogs();
        golem.getNavigation().moveTo(golem.getNavigation().createPath(searchLogResult,getReachRange()),golem.getMoveControl().getSpeedModifier());
    }

    @Override
    public void tick() {
        super.tick();
        var distanceSqr = searchLogResult.distToCenterSqr(golem.position());
        golem.lookAt(EntityAnchorArgument.Anchor.EYES,searchLogResult.getCenter());
        remainingTicks -= 1;
        if (distanceSqr < getReachRangeSqr()){
            tickBreakDelay();
        }
    }

    protected int getReachRange() {
        return 2;
    }
    protected int getReachRangeSqr(){
        return getReachRange() * getReachRange();
    }

    protected void resetRemainingTicks(){
        remainingTicks = maxRemainingTick();
    }

    protected int maxRemainingTick(){
        return 200;
    }

    protected BlockPos.MutableBlockPos breakingPosCache = new BlockPos.MutableBlockPos(0, 0, 0);
    protected void doBreakLog() {
        breakingPosCache.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        var level = golem.level();
        if (!logOffsets.isEmpty() && level instanceof ServerLevel serverLevel){
            logOffsets.removeLastAsOffset(breakingPosCache);
            if (Utils.isWoodLog(level, breakingPosCache)){
                var fakePlayer = platformUtils.getFakePlayer(serverLevel,LOG_HARVESTER_FAKE_PLAYER_UUID);
                fakePlayer.moveTo(breakingPosCache.getX() + 0.5, breakingPosCache.getY() + 0.5, breakingPosCache.getZ() + 0.5);
                serverLevel.destroyBlock(breakingPosCache,true,fakePlayer);
                level.broadcastEntityEvent(golem,START_ACTION_TIMER);
                resetRemainingTicks();
            }
        }
    }

    protected BlockPos.MutableBlockPos currentBreaking = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected int particleDelay = 1;
    protected void tickBreakDelay(){
        if (breakingRemainingTicks == 0){
            doBreakLog();
            currentBreaking.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            while (!logOffsets.isEmpty()){
                logOffsets.getLastWithBase(currentBreaking);
                if (Utils.isWoodLog(golem.level(), currentBreaking)){
                    breakingRemainingTicks = getBreakLogDelayTicks(currentBreaking);
                    maxBreakTicks = breakingRemainingTicks;
                    particleDelay = getEffectDelay(breakingRemainingTicks);
                    break;
                }else {
                    currentBreaking.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
                }
            }
            return;
        }
        breakingRemainingTicks--;
        if (breakingRemainingTicks % particleDelay == 0){
            playEffectForCurrentBreaking();
        }
    }

    protected void playEffectForCurrentBreaking() {
        if (!(currentBreaking.getX() == Integer.MAX_VALUE
                && currentBreaking.getY() == Integer.MAX_VALUE
                && currentBreaking.getZ() == Integer.MAX_VALUE)
        ){
            var level = golem.level();
            level.broadcastEntityEvent(golem,START_ACTION_TIMER);
            var state = level.getBlockState(currentBreaking);
            level.levelEvent(2001, currentBreaking, Block.getId(state));
            level.destroyBlockProgress(golem.getId(),currentBreaking,getDestroyStage());
        }
    }

    public int getDestroyStage() {
        float destroyProgress = (maxBreakTicks -  this.breakingRemainingTicks) / (float)maxBreakTicks;
        return destroyProgress > 0.0F ? (int)(destroyProgress * 10.0F) : -1;
    }

    protected int getEffectDelay(int breakingTicks){
        return (int)Math.max(1,breakingTicks / (breakingTicks + 6.F));
    }

    protected int getBreakLogDelayTicks(BlockPos pos){
        var level = golem.level();
        var state = level.getBlockState(pos);
        return (int)Math.max(5.0F, (20.0F - golem.getStrength() * 3.0F) * state.getDestroySpeed(level,pos));
    }

    protected int getBreakRange() {
        return golem.getHomeRange();
    }

    protected int getSearchRange() {
        return getBreakRange() / 3;
    }

    protected int getSearchXZRange(){
        return getSearchRange();
    }

    protected int getSearchYRange(){
        return Math.max(getSearchRange()/2,1);
    }

    protected int getSearchTries(){
        return getSearchRange() * 4;
    }

    protected LCGBlockPosGenerator posGen = new LCGBlockPosGenerator(
            getSearchXZRange(),getSearchYRange(),getSearchXZRange()
    );

    protected final BlockPos.MutableBlockPos searchLogResult = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected final BlockPos.MutableBlockPos searchLogPickingPosCache = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected void searchLog(){
        searchLogResult.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        var level = golem.level();
        var golemPos = golem.position();
        for (int tries = 0; tries < getSearchTries(); ++tries) {
            posGen.moveToNextBlockPos();
            BlockPos pickedPosThisRound = posGen.resultCache;
            searchLogPickingPosCache.setWithOffset(golem.getRestrictCenter(),pickedPosThisRound);
            if (Utils.isWoodLog(level,searchLogPickingPosCache)){
                double minDistance = searchLogPickingPosCache.distToCenterSqr(golemPos);
                searchLogResult.set(searchLogPickingPosCache);
                boolean isLog = Utils.isWoodLog(level, searchLogPickingPosCache);
                if (isLog){
                    while (Utils.isWoodLog(level, searchLogPickingPosCache)) {
                        var distanceCurrent = searchLogPickingPosCache.distToCenterSqr(golemPos);
                        if (distanceCurrent <= /*this "=" might be helpful?*/ minDistance) {
                            minDistance = distanceCurrent;
                            searchLogResult.set(searchLogPickingPosCache);
                            searchLogPickingPosCache.move(Direction.DOWN);
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }


    protected HighestFurthestBlockFinder.BlockPosOffsets logOffsets = new HighestFurthestBlockFinder.BlockPosOffsets();
    protected HighestFurthestBlockFinder.TriIntConsumer offsetsConsumer = logOffsets::add;
    protected void findConnectedLogs(){
        var searcher = new HighestFurthestBlockFinder(
                golem.level(),
                (l,pos,state) -> {
                    return Utils.isWoodLog(l,pos);
                },
                getBreakRange(),
                getBreakRange(),
                offsetsConsumer
        );
        logOffsets.clear();
        logOffsets.posBase.set(searchLogResult);
        searcher.find(searchLogResult);
        logOffsets.sort();
    }
}
