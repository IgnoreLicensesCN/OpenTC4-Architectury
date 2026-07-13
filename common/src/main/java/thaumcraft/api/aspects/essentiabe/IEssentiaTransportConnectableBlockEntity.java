package thaumcraft.api.aspects.essentiabe;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public interface IEssentiaTransportConnectableBlockEntity {
    /**
     * @param face to connect
     * @return true if this tile able to connect to other vis users/sources on the specified side(face)
     */
    boolean isConnectable(@NotNull Direction face);

    /**
     * What type of essentia this contains
     * @param face
     * @return essentia contains
     */
    @NotNull
    Aspect getEssentiaType(@NotNull Direction face);


    /**
     * @param face
     * @return How much essentia this block contains
     */
    int getEssentiaAmount(@NotNull Direction face);//TODO:Check if we need this
}
