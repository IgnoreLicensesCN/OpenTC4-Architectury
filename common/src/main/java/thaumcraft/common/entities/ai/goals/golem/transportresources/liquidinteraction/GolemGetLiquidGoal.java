package thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction;

import com.linearity.opentc4.utils.blocksearch.BlockPosOffsets;
import com.linearity.opentc4.utils.blocksearch.HighestFurthestBlockFinder;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemBasicUpgradeItem;
import thaumcraft.common.items.golem.upgrade.core.LiquidGolemCoreItem;

import static thaumcraft.common.items.ThaumcraftItemInstances.ORDER_GOLEM_UPGRADE;

public class GolemGetLiquidGoal extends AbstractGolemFindLiquidToGetGoal {
    protected BlockPosOffsets offsets = new BlockPosOffsets();
    protected HighestFurthestBlockFinder.TriIntConsumer offsetsConsumer = offsets::add;
    protected boolean searchedFluidThisRun = false;

    public GolemGetLiquidGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public void stop() {
        tickPassed = 0;
        targetMarker = null;
    }

    protected int tickPassed = 0;

    @Override
    public boolean canContinueToUse() {
        return tickPassed < golemTakeFluidDelay()*2;
    }

    protected int getMaxHeight(){
        return golem.getHomeRange();
    }
    protected int getMaxXZManhattanDistance(){
        return 2 * golem.getHomeRange();
    }

    protected long getRemainingSpace(){
        var holdingFluid = LiquidGolemCoreItem.GolemHoldingFluidFakeField.getHoldingFluid(golem);
        return golem.getFluidCarryPower() - holdingFluid.getAmount();
    }

    protected int golemTakeFluidDelay(){
        return 10;
    }

    protected final BlockPos.MutableBlockPos pickLastFluidPos = new BlockPos.MutableBlockPos();

    @Override
    public void tick() {
        super.tick();
        tickPassed++;
        if (tickPassed < golemTakeFluidDelay()){return;}

        if (targetMarker != null) {
            var holdingFluid = LiquidGolemCoreItem.GolemHoldingFluidFakeField.getHoldingFluid(golem);
            var remainingSpace = getRemainingSpace();
            if (remainingSpace > 0) {
                var level = golem.level();
                var pos = targetMarker.getPos();
                var dir = targetMarker.getSide();
                var gotFromMarker = extractFluidFrom(level, pos, dir, remainingSpace, true);
                if (gotFromMarker.isEmpty()) {
                    if (golem.golemUpgradesCacheView.get(IGolemBasicUpgradeItem.class)
                            .stream()
                            .anyMatch(pair -> pair.getUpgradeStack().getItem() == ORDER_GOLEM_UPGRADE())){
                        if (!searchedFluidThisRun){
                            searchFluidSource(level, pos);
                            searchedFluidThisRun = true;
                        }

                        while ((!offsets.isEmpty() && getRemainingSpace() > 0 && gotFromMarker.isEmpty())) {
                            offsets.removeLastWithBase(pickLastFluidPos);
                            gotFromMarker = extractFluidDirectlyFrom(level, pickLastFluidPos, dir);
                        }
                    }
                }
                if (!gotFromMarker.isEmpty()) {
                    tickPassed = 0;
                    addHoldingFluid(holdingFluid, gotFromMarker);
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        searchedFluidThisRun = false;
        tickPassed = 0;
    }

    protected void searchFluidSource(Level level, BlockPos fromPos) {
        var searcher = new HighestFurthestBlockFinder(
                golem.level(),
                (l,pos,state) -> {
                    var got = getFluidDirectlyFrom(l, pos);
                    return got != null && !got.isEmpty();
                },
                getMaxHeight(),
                getMaxXZManhattanDistance(),
                offsetsConsumer
        );
        offsets.clear();
        offsets.posBase.set(fromPos);
        searcher.find(fromPos);
        offsets.sort();
    }

    protected void addHoldingFluid(FluidStack holdingFluid, FluidStack toAddInAndSetWith) {
        var holdingAmount = holdingFluid.isEmpty() ? 0 : holdingFluid.getAmount();
        if (toAddInAndSetWith != null && !toAddInAndSetWith.isEmpty() &&
                (toAddInAndSetWith.getFluid() == holdingFluid.getFluid() || holdingFluid.isEmpty())) {
            if (toAddInAndSetWith.getAmount() > 200){
                golem.playSound(SoundEvents.GENERIC_SWIM,0.2F,(golem.getRandom().nextFloat() * 0.6F) + 0.7F);
            }
            toAddInAndSetWith.setAmount(toAddInAndSetWith.getAmount() + holdingAmount);
            LiquidGolemCoreItem.GolemHoldingFluidFakeField.setHoldingFluid(golem, toAddInAndSetWith);
        }
    }
}
