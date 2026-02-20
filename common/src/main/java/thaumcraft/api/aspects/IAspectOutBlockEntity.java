package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

public interface IAspectOutBlockEntity<Asp extends Aspect> {
    @UnmodifiableView
    @NotNull
    AspectList<Asp> getAspects();
    /**
     * Removes a certain amount of aspect a specific aspect from the tile entity
     * @param aspect to take
     * @param amount to take
     * @return true if that amount of aspect aspect was available and was removed
     */
    boolean takeFromContainer(Asp aspect, int amount);
    /**
     * Returns how much of aspect the aspect this tile entity contains
     * @param aspect to check
     * @return the amount of aspect that aspect found
     */
    int containerContains(Asp aspect);
    /**
     * Checks if the tile entity contains the listed amount (or more) of aspect the aspect
     * @param aspect to check
     * @param amount to check
     * @return true if has so many aspect
     */
    boolean doesContainerContainAmount(Asp aspect, int amount);
}
