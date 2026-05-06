package thaumcraft.api.aspects;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public interface IEssentiaTransportConnectableBlockEntity {
    /**
     * @param face to connect
     * @return true if this tile able to connect to other vis users/sources on the specified side(face)
     */
    boolean isConnectable(@NotNull Direction face);
}
