package thaumcraft.api.aspects.essentiabe;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.HashSet;
import java.util.Set;

import static thaumcraft.api.aspects.essentiabe.IRemoteDrainableEssentiaSourceBlockEntity.COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE;

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
        var toDrainBE = LevelBlockEntityAccessing.getExistingBlockEntity(level, pos);
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
        for (var lookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE)){

            var drainerPos = this.getBlockPos();
//            AtomicInteger toDrain = new AtomicInteger(amount);
            int toDrain = amount;

            drainerMet.add(this);

            var iter = lookup.forItemsNearPosWithRangeIterator(drainerPos,range);
            while (iter.hasNext()){
                var drainables = iter.next();
                for (var drainable:drainables){
                    int drained = this.drainEssentia(drainable, aspect, toDrain, drainerMet);
                    toDrain -= drained;
                    if (toDrain <= 0){
                        break;
                    }
                }
            }
            return amount - toDrain;
        }
        return 0;
    }
}
