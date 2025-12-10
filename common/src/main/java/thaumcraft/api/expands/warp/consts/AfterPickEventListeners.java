package thaumcraft.api.expands.warp.consts;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerAfter;

import static thaumcraft.api.expands.warp.consts.WarpEvents.SPAWN_LOTS_OF_GUARDS;

public class AfterPickEventListeners {
    public static final PickWarpEventListenerAfter SPAWN_GUARD_IF_NO_EVENT = new PickWarpEventListenerAfter(0) {
        @NotNull
        @Override
        public WarpEvent afterPickEvent(@NotNull PickWarpEventContext context, @NotNull WarpEvent e, @NotNull Player player) {
            if (context.warp >= 92 && e == WarpEvent.EMPTY) {
                return SPAWN_LOTS_OF_GUARDS;
            }
            return e;
        }
    };
}
