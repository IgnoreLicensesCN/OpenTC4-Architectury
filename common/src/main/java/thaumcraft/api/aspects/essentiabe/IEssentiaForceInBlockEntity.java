package thaumcraft.api.aspects.essentiabe;

import org.jetbrains.annotations.ApiStatus;
import thaumcraft.api.aspects.Aspect;

//usually use IEssentiaTransportInBlockEntity
//impl this means BE accepts force-add(without considering suction limits) aspect
@ApiStatus.Experimental
public interface IEssentiaForceInBlockEntity<Asp extends Aspect> {
    /**
     * This method is used to add a certain amount of aspect an aspect to the tile entity.
     * @param aspect to add
     * @param amount to add
     * @return the amount of aspect left over that could not be added.(remaining to add)
     */
    int addIntoContainer(Asp aspect, int amount);
    /**
     * This method is used to determine of aspect a specific aspect can be added to this container.
     * @param aspect to check
     * @return true if can add into
     */
    boolean doesContainerAccept(Asp aspect);

    default int addIntoContainerIfAccept(Asp aspect, int amount){
        if (!doesContainerAccept(aspect)) {
            return amount;
        }
        return addIntoContainer(aspect, amount);
    }
}
