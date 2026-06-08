package thaumcraft.api.aspects.essentiabe;

import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.api.aspects.essentiabe.IRemoteDrainableEssentiaSourceBlockEntity.levelledRemoteDrainables;

//Infusion Matrix/Overchanting Table/Essentia Mirror

public interface IRemoteEssentiaDrainerBlockEntity {
    //some may call different-world aspect draining?idk but imagine.
    default boolean canDrainEssentiaFromPos(
            Level level,
            BlockPos pos,
            Aspect aspect,
            @Modifiable Set<IRemoteEssentiaDrainerBlockEntity> metDrainers//plz dont make it StackOverFlow.
    ) {
        if (pos.distSqr(getBlockPos()) > getDrainRange() * getDrainRange()) {
            return false;
        }
        metDrainers.add(this);
        var toDrainBE = level.getBlockEntity(pos);
        return toDrainBE instanceof IRemoteDrainableEssentiaSourceBlockEntity
                && !(toDrainBE instanceof IRemoteEssentiaDrainerBlockEntity drainer && metDrainers.contains(drainer));
    }

    //return amount drained
    default int drainEssentia(
            IRemoteDrainableEssentiaSourceBlockEntity drainable,
            Aspect aspect,
            int amount,
            Set<IRemoteEssentiaDrainerBlockEntity> drainerMet
    ) {
        if (!canDrainEssentiaFromPos(
                drainable.getLevel(),
                drainable.getBlockPos(),
                aspect,
                drainerMet
        )) {
            return 0;
        }
        int drained = drainable.drainEssentiaRemote(
                aspect,
                amount,
                drainerMet
        );
        if (drained > 0) {
            drainable.playDrainEffect(this, aspect);
        }
        return drained;
    }

    ;

    int getDrainRange();

    @Nullable Level getLevel();

    @NotNull BlockPos getBlockPos();

    //return drained
    //now for every drain each remote drainer would only be lookup for once for linear complexity
    // (since mirror can be remote drain proxy and mirror may drain mirror which leads to chaos)
    default int drainEssentiaRemote(Aspect aspect) {
        return drainEssentiaRemote(aspect, getDrainRange());
    }

    default int drainEssentiaRemote(Aspect aspect, int range) {
        return drainEssentiaRemote(aspect, 1, range, new HashSet<>());
    }

    //return drained
    default int drainEssentiaRemote(
            Aspect aspect,
            int amount,
            int range,
            Set<IRemoteEssentiaDrainerBlockEntity> drainerMet
    ) {
        var level = this.getLevel();
        if (level == null) return 0;
        var lookup = levelledRemoteDrainables.get(level);
        if (lookup == null) return 0;
        var drainerPos = this.getBlockPos();
        AtomicInteger toDrain = new AtomicInteger(amount);

        drainerMet.add(this);

        lookup.forItemsNearPosWithBreakWithRange(
                drainerPos,
                drainable -> {
                    int drained = this.drainEssentia(drainable, aspect, toDrain.get(), drainerMet);
                    return toDrain.addAndGet(-drained) <= 0;
                },
                range
        );
        return amount - toDrain.get();
    }
}
