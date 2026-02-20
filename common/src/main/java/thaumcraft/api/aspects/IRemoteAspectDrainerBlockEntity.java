package thaumcraft.api.aspects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

//Infusion Matrix/Overchanting Table
public interface IRemoteAspectDrainerBlockEntity<Asp extends Aspect> {
    //some may call different-world aspect draining?idk but imagine.
    boolean canDrainAspectFromPos(Level level, BlockPos pos, Asp aspect);
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
}
