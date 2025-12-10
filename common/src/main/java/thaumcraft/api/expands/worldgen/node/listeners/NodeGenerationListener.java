package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
@ParametersAreNonnullByDefault
public abstract class NodeGenerationListener implements Comparable<NodeGenerationListener> {
    public final int priority;
    public NodeGenerationListener(int priority) {
        this.priority = priority;
    }

    public abstract void onGeneration(World world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);

    @Override
    public int compareTo(NodeGenerationListener o) {
        return Integer.compare(priority, o.priority);
    }
}
