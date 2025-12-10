package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.World;

import java.util.Random;

public abstract class DoGenerateCondition implements Comparable<DoGenerateCondition> {
    public final int priority;
    public DoGenerateCondition(int priority) {
        this.priority = priority;
    }
    public int compareTo(DoGenerateCondition o) {
        return Integer.compare(priority, o.priority);
    }

    /**
     * all conditions like this will be checked,if one is false,cancel this generation.
     * @param world
     * @param random
     * @param chunkX
     * @param chunkZ
     * @param auraGen
     * @param newGen
     * @return
     */
    public abstract boolean check(World world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);
}
