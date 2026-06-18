package thaumcraft.api.scan.entity;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class EntityScanManager {
    public static final ListenerManager<EntityScanListener> ENTITY_SCAN_LISTENERS = new ListenerManager<>();
    static {
        for (var listenerEnum: EntityScanListeners.values()){
            ENTITY_SCAN_LISTENERS.registerListener(listenerEnum.listener);
        }
    }

    public static void onScanEntity(LivingEntity living, Entity entity) {
        ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) return;
        if (!info.infoOwnerScanning){
            var context = new EntityScanContext(living,entity);
            info.infoOwnerScanning = true;
            try {
                for (var listener: ENTITY_SCAN_LISTENERS.getListeners()){
                    listener.onEntityScan(context);
                    if (context.shouldBreak){
                        break;
                    }
                }
            }catch (Exception e){
                OpenTC4.LOGGER.error("Error while trying to scan entity for {} {} {}", living.getName(), living.getUUID(), living.position(), e);
            }
            info.infoOwnerScanning = false;
        }

    }
}
