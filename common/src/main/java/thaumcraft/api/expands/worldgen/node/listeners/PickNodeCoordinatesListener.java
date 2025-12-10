package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.World;
import thaumcraft.api.expands.worldgen.node.PickNodeCoordinateContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public abstract class PickNodeCoordinatesListener implements Comparable<PickNodeCoordinatesListener> {
    @Override
    public int compareTo(PickNodeCoordinatesListener o) {
        return Integer.compare(this.priority, o.priority);
    }

    public final int priority;
    public PickNodeCoordinatesListener(int priority) {
        this.priority = priority;
    }

    public abstract PickNodeCoordinateContext[] pickNodeCoordinates(World world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);

}
