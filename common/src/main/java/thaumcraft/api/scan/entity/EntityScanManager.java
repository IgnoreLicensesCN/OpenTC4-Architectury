package thaumcraft.api.scan.entity;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.researches.ResearchAndScannedInfo;

public class EntityScanManager {
    public static final ListenerManager<EntityScanListener> ENTITY_SCAN_LISTENERS = new ListenerManager<>();
    static {
        for (var listenerEnum: EntityScanListeners.values()){
            ENTITY_SCAN_LISTENERS.registerListener(listenerEnum.listener);
        }
    }

    public static void onPlayerScanEntity(ServerPlayer player, Entity entity) {
        ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromPlayer(player);
        if (!info.playerScanning){
            var context = new EntityScanContext(player,entity);
            info.playerScanning = true;
            try {
                for (var listener: ENTITY_SCAN_LISTENERS.getListeners()){
                    listener.onEntityScan(context);
                    if (context.shouldBreak){
                        break;
                    }
                }
            }catch (Exception e){
                OpenTC4.LOGGER.error("Error while trying to scan entity for player "+player.getName(),e);
            }
            info.playerScanning = false;
        }

    }
}
