package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.World;
import thaumcraft.api.expands.worldgen.node.CreateNodeContext;

public abstract class CreateNodeListener implements Comparable<CreateNodeListener> {
    public final int priority;
    public CreateNodeListener(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(CreateNodeListener o) {
        return Integer.compare(this.priority, o.priority);
    }

    /**
     * @return true if you want to stop listeners after it.
     */
    public abstract boolean onCreateNode(World atWorld, CreateNodeContext context);
}
