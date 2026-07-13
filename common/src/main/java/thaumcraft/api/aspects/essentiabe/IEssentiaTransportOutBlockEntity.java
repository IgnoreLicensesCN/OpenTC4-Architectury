package thaumcraft.api.aspects.essentiabe;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public interface IEssentiaTransportOutBlockEntity extends IEssentiaTransportConnectableBlockEntity{
    /**
     * @param face to output to
     * @return true if this side is used to output essentia
     */
    boolean canOutputTo(@NotNull Direction face);

    /**
     * remove the specified amount of essentia from this transport tile
     * @return how much was actually taken
     */
    //directly take
    int takeEssentia(Aspect aspect, int amount, @NotNull Direction outputToDirection);
    /**
     * remove the specified amount of essentia from this transport tile
     * @return how much was actually taken
     */
    //considered suction
    default int takeEssentiaWithSuction(int drainerSuction,Aspect aspect, int amount,@NotNull Direction outputToDirection){
        if (!this.canOutputTo(outputToDirection)) {
            return 0;
        }
        if (drainerSuction < this.getMinimumSuctionToDrainOut()){
            return 0;
        }
        if (this instanceof IEssentiaTransportInBlockEntity inBE){
            if (drainerSuction <= inBE.getSuctionAmount(outputToDirection)){
                return 0;
            }
        }
        return takeEssentia(aspect, amount, outputToDirection);
    };
    /**
     * Essentia will not be drawn from this container unless the suction exceeds this amount.
     * @return the amount
     */
    int getMinimumSuctionToDrainOut();

}
