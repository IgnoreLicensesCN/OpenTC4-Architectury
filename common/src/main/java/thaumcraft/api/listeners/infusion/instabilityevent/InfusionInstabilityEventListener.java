package thaumcraft.api.listeners.infusion.instabilityevent;

import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;

public abstract class InfusionInstabilityEventListener implements Comparable<InfusionInstabilityEventListener> {
    public final int priority;
    public InfusionInstabilityEventListener(int priority) {
        this.priority = priority;
    }
    @AllArgsConstructor
    public static class InfusionInstabilityEventContext{
        public final @NotNull InfusionMatrixBlockEntity infusionMatrix;
        public final @NotNull BlockPos matrixPos;
        public final @NotNull Level level;
        public final @Nullable String playerNameActivatedRecipe;
    }
    public abstract void onInstabilityEvent(InfusionInstabilityEventContext ctx);

    @Override
    public int compareTo(@NotNull InfusionInstabilityEventListener o) {
        return Integer.compare(this.priority, o.priority);
    }
}
