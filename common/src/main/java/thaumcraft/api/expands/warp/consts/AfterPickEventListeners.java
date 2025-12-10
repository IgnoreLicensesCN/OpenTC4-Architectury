package thaumcraft.api.expands.warp.consts;

import net.minecraft.entity.player.Player;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerAfter;

import javax.annotation.Nonnull;

import static thaumcraft.api.expands.warp.consts.WarpEvents.SPAWN_LOTS_OF_GUARDS;

public class AfterPickEventListeners {
    public static final PickWarpEventListenerAfter SPAWN_GUARD_IF_NO_EVENT = new PickWarpEventListenerAfter(0) {
        @Nonnull
        @Override
        public WarpEvent afterPickEvent(PickWarpEventContext context, WarpEvent e, Player player) {
            if (context.warp >= 92 && e == WarpEvent.EMPTY) {
                return SPAWN_LOTS_OF_GUARDS;
            }
            return e;
        }
    };
}
