package thaumcraft.api.listeners.lamp.fertility.apply;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class FertilityAffectApplier implements Comparable<FertilityAffectApplier> {
    public final int priority;

    public FertilityAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull FertilityAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(FertilityLampAffectContext<? extends Entity> context);
}
