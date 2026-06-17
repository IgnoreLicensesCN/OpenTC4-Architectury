package thaumcraft.api.aspects.essentiabe;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public interface IEssentiaTransportInBlockEntity extends IEssentiaTransportConnectableBlockEntity{
    /**
     * ?
     * @param face to input from
     * @return true if this side is used to input essentia
     */
    boolean canInputFrom(@NotNull Direction face);

    /**
     * Sets the amount of suction this block will apply
     */
    void setSuction(@NotNull Aspect aspect, int amount);
    /**
     * @param face the location from where the suction is being checked
     * @return the strength of suction this block is applying.
     */
    int getSuctionAmount(@NotNull Direction face);
    /**
     * Returns the type of aspect suction this block is applying.
     * @param face
     * 		the location from where the suction is being checked
     * @return
     * 		a return type of aspect, <s>null</s> empty indicates the suction is untyped and the first thing available will be drawn
     */
    @NotNull("empty -> any") Aspect getSuctionType(@NotNull Direction face);

    /**
     * add the specified amount of essentia to this transport tile
     * @return how much was actually added
     */
    int addEssentia(@NotNull Aspect aspect, int amount,@NotNull Direction fromDirection);
}
