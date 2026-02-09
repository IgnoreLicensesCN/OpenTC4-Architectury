package thaumcraft.api.listeners.worldgen.node.listeners;

import net.minecraft.world.level.Level;


import java.util.Random;

public abstract class NodeGenerationListener implements Comparable<NodeGenerationListener> {
    public final int priority;
    public NodeGenerationListener(int priority) {
        this.priority = priority;
    }

    public abstract void onGeneration(Level world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);

    @Override
    public int compareTo(NodeGenerationListener o) {
        return Integer.compare(priority, o.priority);
    }
}
