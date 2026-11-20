package thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction;

import com.linearity.opentc4.utils.HighestFurthestBlockFinder;
import dev.architectury.fluid.FluidStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.items.golem.upgrade.core.LiquidGolemCoreItem;

import java.util.HashSet;
import java.util.Set;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class GolemGetLiquidGoal extends AbstractGolemFluidGoal {
    protected MarkedFluidHandlerIterator fluidHandlerIterator = new MarkedFluidHandlerIterator(golem);
    protected Set<Fluid> missingFluidCache = new HashSet<>();
    protected @Nullable Set<Fluid> missingFluids = missingFluidCache;
    protected @Nullable Marker targetMarker = null;
    protected BlockPosOffsets offsets = new BlockPosOffsets();
    protected HighestFurthestBlockFinder.TriIntConsumer offsetsConsumer = offsets::add;
    protected boolean searchedFluidThisRun = false;

    public static class BlockPosOffsets {
        public BlockPos.MutableBlockPos posBase = new BlockPos.MutableBlockPos(0,0,0);
        IntArrayList xCoords = new IntArrayList();
        IntArrayList zCoords = new IntArrayList();
        IntArrayList yCoords = new IntArrayList();

        public void add(BlockPos pos) {
            xCoords.add(pos.getX() - posBase.getX());
            yCoords.add(pos.getY() - posBase.getX());
            zCoords.add(pos.getZ() - posBase.getX());
        }

        public void add(int x, int y, int z) {
            xCoords.add(x - posBase.getX());
            yCoords.add(y - posBase.getX());
            zCoords.add(z - posBase.getX());
        }

        public void removeLast(BlockPos.MutableBlockPos receiver) {
            int x = xCoords.removeInt(xCoords.size() - 1);
            int y = yCoords.removeInt(yCoords.size() - 1);
            int z = zCoords.removeInt(zCoords.size() - 1);
            receiver.set(x, y, z);
        }

        public boolean isEmpty() {
            return xCoords.isEmpty();
        }

        public void clear() {
            xCoords.clear();
            yCoords.clear();
            zCoords.clear();
        }

        public void sort() {
            it.unimi.dsi.fastutil.Arrays.quickSort(0, xCoords.size(),
                    (a, b) -> distSubtractionSign(xCoords.getInt(a), yCoords.getInt(a), zCoords.getInt(a), xCoords.getInt(b), yCoords.getInt(b), zCoords.getInt(b)),
                    (a, b) -> {
                        swap(xCoords, a, b);
                        swap(yCoords, a, b);
                        swap(zCoords, a, b);
                    }
            );
        }

        private static void swap(IntArrayList list, int i, int j) {
            int tmp = list.getInt(i);
            list.set(i, list.getInt(j));
            list.set(j, tmp);
        }

        private static int distSubtractionSign(int x1, int y1, int z1, int x2, int y2, int z2) {
            var value = distSubtraction(x1, y1, z1, x2, y2, z2);
            if (value == 0) {
                return 0;
            }
            if (value > 0) {
                return 1;
            }
            return -1;
        }

        private static long distSubtraction(int x1, int y1, int z1, int x2, int y2, int z2) {
            return dist(x1, y1, z1) - dist(x2, y2, z2);
        }

        private static long dist(long x1, long y1, long z1) {
            return Math.abs(x1) + Math.abs(y1) + Math.abs(z1);
        }
    }

    public GolemGetLiquidGoal(BaseGolemEntity golem) {
        super(golem);
    }

    private final BlockPos.MutableBlockPos posCacheForCanUse = new BlockPos.MutableBlockPos();

    @Override
    public boolean canUse() {
        targetMarker = null;
        var holdingFluid = LiquidGolemCoreItem.GolemHoldingFluidFakeField.getHoldingFluid(golem);
        if (holdingFluid.getAmount() >= golem.getFluidCarryPower()) {
            return false;
        }
        if (!golem.getNavigation().isDone()) {
            return false;
        }
        missingFluids = getMissingFluid(golem, missingFluidCache);
        if (missingFluids == null) {
            return false;
        }

        var level = golem.level();
        fluidHandlerIterator.reset();
        while (fluidHandlerIterator.hasNext()) {
            var fluidContainerInfo = fluidHandlerIterator.next();
            var marker = fluidContainerInfo.marker;
            BlockPos pos = marker.getPos();
            Direction dir = marker.getSide();
            posCacheForCanUse.setWithOffset(pos, dir);
            if (posCacheForCanUse.distToCenterSqr(golem.position()) < reachRangeSqr()) {
                int tanks = platformUtils.getFluidTanksFromPos(level, pos, dir);
                for (var tankIndex = 0; tankIndex < tanks; tankIndex++) {
                    var fluidInside = platformUtils.getFluidFromPos(level, pos, dir, tankIndex);
                    if (!fluidInside.isEmpty()) {
                        if (verifyFluidForMarker(fluidInside, marker)) {
                            return true;
                        }
                    }
                }
                var directlyGet = getFluidDirectlyFrom(level, pos);
                if (!directlyGet.isEmpty()) {
                    if (verifyFluidForMarker(directlyGet, marker)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean verifyFluidForMarker(FluidStack directlyGet, Marker marker) {
        if (missingFluids == null) {
            return false;
        }
        if (missingFluids.isEmpty() || missingFluids.contains(directlyGet.getFluid())) {
            targetMarker = marker;
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        tickPassed = 0;
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

    protected final BlockPos.MutableBlockPos pickLastFluidOffset = new BlockPos.MutableBlockPos();

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
                    if (!searchedFluidThisRun){
                        searchFluidSource(level, pos);
                        searchedFluidThisRun = true;
                    }

                    while ((!offsets.isEmpty() && getRemainingSpace() > 0 && gotFromMarker.isEmpty())) {
                        offsets.removeLast(pickLastFluidOffset);
                        pickLastFluidOffset.move(offsets.posBase);
                        gotFromMarker = extractFluidDirectlyFrom(level, pickLastFluidOffset, dir);
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
