package thaumcraft.api.expands.listeners.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.utils.HexCoord;

public abstract class WriteAspectBeforeListener implements Comparable<WriteAspectBeforeListener> {
    public final int weight;

    protected WriteAspectBeforeListener(int weight) {
        this.weight = weight;
    }

    public abstract void onEventTriggered(
            Level atLevel,
            BlockPos tablePos,
            ItemStack writeToolStack,
            ItemStack noteStack,
            ServerPlayer player,
            Aspect aspect,
            HexCoord placedAt
    );

    @Override
    public int compareTo(@NotNull WriteAspectBeforeListener o) {
        return Integer.compare(weight, o.weight);
    }
}
