package thaumcraft.api.listeners.warp.consts;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.listeners.warp.PickWarpEventContext;
import thaumcraft.api.listeners.warp.listeners.PickWarpEventListenerBefore;
import thaumcraft.api.warp.WarpInfo;

public class BeforePickEventListeners {
    public static final PickWarpEventListenerBefore CALCULATE_WARP_AND_COUNTER = new PickWarpEventListenerBefore(1) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            e.warp = Math.min(100, (e.warp + e.warp + e.warpCounter) / 3);
            e.warpCounter = (int)((double)e.warpCounter - Math.max(5.0F, Math.sqrt(e.warpCounter) * (double)2.0F));
            WarpInfo.getFromPlayer(player).setWarpEventCounter(e.warpCounter);
        }
    };
}
