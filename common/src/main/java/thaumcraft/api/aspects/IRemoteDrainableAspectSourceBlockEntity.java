package thaumcraft.api.aspects;


import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSourceS2C;

import java.util.Set;

/**
 * @author Azanor
 * <p>
 * This interface is implemented by tile entites (or possibly anything else) like jars
 * so that they can act as an essentia source for blocks like the infusion altar.
 *
 * <p>yes i renamed from IAspectSource --IgnoreLicensesCN </p>
 */
public interface IRemoteDrainableAspectSourceBlockEntity<Asp extends Aspect>
        /*extends IAspectContainerBlockEntity<Asp>*/ {
    //check with method above first.
    //@return false if drain failed
    int drainAspectRemote(Asp aspect, int amount, @Modifiable Set<IRemoteAspectDrainerBlockEntity<? extends Aspect>> metDrainers);
    @Nullable Level getLevel();
    @NotNull BlockPos getBlockPos();
    //removed cooldown
    default void playDrainEffect(IRemoteAspectDrainerBlockEntity<? extends Aspect> aspectDrainer,Aspect drainedAspect) {
        var drainerPos = aspectDrainer.getBlockPos();
        var drainedPos = this.getBlockPos();
        PacketFXEssentiaSourceS2C fxPacket = new PacketFXEssentiaSourceS2C(
                drainerPos.getX(), drainerPos.getY(), drainerPos.getZ(),
                (byte)(drainerPos.getX() - drainedPos.getX()),
                (byte)(drainerPos.getY() - drainedPos.getY()),
                (byte)(drainerPos.getZ() - drainedPos.getZ()),
                drainedAspect.getColor()
        );
        if (aspectDrainer.getLevel() instanceof ServerLevel serverLevel){
            fxPacket.sendToAllAround(serverLevel,aspectDrainer.getBlockPos(),32*32);
        }
    }
}
