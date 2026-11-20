package thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.items.golem.upgrade.core.LiquidGolemCoreItem;

import java.util.HashSet;
import java.util.Set;

import static com.linearity.opentc4.OpenTC4.platformUtils;

//TODO:[maybe wont finished]Liquid container interaction logic API
public abstract class AbstractGolemFindLiquidToGetGoal extends AbstractGolemFluidGoal{
    public AbstractGolemFindLiquidToGetGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected @Nullable Marker targetMarker = null;
    protected Set<Fluid> missingFluidCache = new HashSet<>();
    protected @Nullable Set<Fluid> missingFluids = missingFluidCache;
    protected MarkedFluidHandlerIterator fluidHandlerIterator = new MarkedFluidHandlerIterator(golem);
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
            posCacheForCanUse.set(pos);
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
}
