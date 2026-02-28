package thaumcraft.api.aspects;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public interface IEssentiaTransportInBlockEntity {
    /**
     * @param face to connect
     * @return true if this tile able to connect to other vis users/sources on the specified side(face)
     */
    boolean isConnectable(Direction face);
    /**
     * ?
     * @param face to input from
     * @return true if this side is used to input essentia
     */
    boolean canInputFrom(Direction face);

    /**
     * Sets the amount ofAspectVisList suction this block will apply
     */
    void setSuction(Aspect aspect, int amount);
    /**
     * @param face the location from where the suction is being checked
     * @return the strength ofAspectVisList suction this block is applying.
     */
    int getSuctionAmount(Direction face);
    /**
     * Returns the type of aspect suction this block is applying.
     * @param face
     * 		the location from where the suction is being checked
     * @return
     * 		a return type of aspect, <s>null</s> empty indicates the suction is untyped and the first thing available will be drawn
     */
    @NotNull Aspect getSuctionType(Direction face);

    /**
     * add the specified amount ofAspectVisList essentia to this transport tile
     * @return how much was actually added
     */
    int addEssentia(Aspect aspect, int amount, Direction fromDirection);
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
    int getEssentiaAmount(Direction face);//TODO:Check if we need this
}
