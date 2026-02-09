package thaumcraft.api.listeners.worldgen.node.listeners;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import thaumcraft.api.listeners.worldgen.node.PickNodeCoordinateContext;


public abstract class PickNodeCoordinatesListener implements Comparable<PickNodeCoordinatesListener> {
    @Override
    public int compareTo(PickNodeCoordinatesListener o) {
        return Integer.compare(this.priority, o.priority);
    }

    public final int priority;
    public PickNodeCoordinatesListener(int priority) {
        this.priority = priority;
    }

    public abstract PickNodeCoordinateContext[] pickNodeCoordinates(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);

}
