package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

public interface IAspectInBlockEntity<Asp extends Aspect> {
    /**
     * This method is used to add a certain amount of aspect an aspect to the tile entity.
     * @param aspect to add
     * @param amount to add
     * @return the amount of aspect left over that could not be added.
     */
    int addIntoContainer(Asp aspect, int amount);
    /**
     * This method is used to determine of aspect a specific aspect can be added to this container.
     * @param aspect to check
     * @return true if can add into
     */
    boolean doesContainerAccept(Asp aspect);

}
