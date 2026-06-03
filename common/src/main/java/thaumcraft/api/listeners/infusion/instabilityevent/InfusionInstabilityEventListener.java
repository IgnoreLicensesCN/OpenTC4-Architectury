package thaumcraft.api.listeners.infusion.instabilityevent;

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
    public static class InfusionInstabilityEventContext{
        public final @NotNull InfusionMatrixBlockEntity infusionMatrix;
        public final @NotNull BlockPos matrixPos;
        public final @NotNull Level level;
        public final @Nullable String playerNameActivatedRecipe;
        public InfusionInstabilityEventContext(
                @NotNull InfusionMatrixBlockEntity infusionMatrix,
                @NotNull BlockPos matrixPos,
                @NotNull Level level,
                @Nullable String playerNameActivatedRecipe) {
            this.infusionMatrix = infusionMatrix;
            this.matrixPos = matrixPos;
            this.level = level;
            this.playerNameActivatedRecipe = playerNameActivatedRecipe;
        }
    }
    public abstract void onInstabilityEvent(InfusionInstabilityEventContext ctx);

    @Override
    public int compareTo(@NotNull InfusionInstabilityEventListener o) {
        return Integer.compare(this.priority, o.priority);
    }
}
