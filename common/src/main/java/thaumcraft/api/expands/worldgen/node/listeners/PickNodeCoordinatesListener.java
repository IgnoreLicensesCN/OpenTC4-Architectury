package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.level.Level;
import thaumcraft.api.expands.worldgen.node.PickNodeCoordinateContext;


import java.util.Random;


public abstract class PickNodeCoordinatesListener implements Comparable<PickNodeCoordinatesListener> {
    @Override
    public int compareTo(PickNodeCoordinatesListener o) {
        return Integer.compare(this.priority, o.priority);
    }

    public final int priority;
    public PickNodeCoordinatesListener(int priority) {
        this.priority = priority;
    }

    public abstract PickNodeCoordinateContext[] pickNodeCoordinates(Level world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);

}
