package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

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
    public abstract boolean check(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen);
}
