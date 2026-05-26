package thaumcraft.api.aspects;

import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Set;

//Infusion Matrix/Overchanting Table
public interface IRemoteAspectDrainerBlockEntity<Asp extends Aspect> {
    //some may call different-world aspect draining?idk but imagine.
    default boolean canDrainAspectFromPos(
            Level level,
            BlockPos pos,
            Asp aspect,
            @Modifiable Set<IRemoteAspectDrainerBlockEntity<? extends Aspect>> metDrainers//plz dont make it StackOverFlow.
    ){
        metDrainers.add(this);
        var toDrainBE = level.getBlockEntity(pos);
        return toDrainBE instanceof IRemoteDrainableAspectSourceBlockEntity<? extends Aspect>
                && !(toDrainBE instanceof IRemoteAspectDrainerBlockEntity<? extends Aspect> drainer && metDrainers.contains(drainer))
                ;
    };
    //maybe suitable for thaumic insurgence InfusionInterceptor (especially GTNH one)
    //but remember to call BE update this time.
    @NotNull
    @UnmodifiableView
    AspectList<Asp> getAspectsRequired(Asp aspect);
    void drainAspectFrom(Level level, BlockPos pos, Asp aspect, int amount);
    //needs imagination to use.
    default int aspectAmountCanDrainPerTick(Asp aspect) {
        return 1;
    }
    @Nullable Level getLevel();
    @NotNull BlockPos getBlockPos();
}
