package thaumcraft.api.aspects;

import net.minecraft.core.Direction;

public interface IEssentiaTransportOutBlockEntity {
    /**
     * @param face to connect
     * @return true if this tile able to connect to other vis users/sources on the specified side(face)
     */
    boolean isConnectable(Direction face);
    /**
     * @param face to output to
     * @return true if this side is used to output essentia
     */
    boolean canOutputTo(Direction face);

    /**
     * remove the specified amount of essentia from this transport tile
     * @return how much was actually taken
     */
    int takeEssentia(Aspect aspect, int amount, Direction face);
    /**
     * Essentia will not be drawn from this container unless the suction exceeds this amount.
     * @return the amount
     */
    int getMinimumSuctionToDrainOut();
    /**
     * What type ofAspectVisList essentia this contains
     * @param face
     * @return essentia contains
     */
    Aspect getEssentiaType(Direction face);
    /**
     * @param face
     * @return How much essentia this block contains
     */
    int getEssentiaAmount(Direction face);

}
