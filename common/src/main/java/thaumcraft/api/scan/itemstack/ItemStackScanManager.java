package thaumcraft.api.scan.itemstack;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class ItemStackScanManager {
    public static final ListenerManager<ItemStackScanListener> ITEM_STACK_SCAN_LISTENERS = new ListenerManager<>();
    static {
        for (var listenerEnum:ItemStackScanListeners.values()){
            ITEM_STACK_SCAN_LISTENERS.registerListener(listenerEnum.listener);
        }
    }

    public static void onScanItemStack(LivingEntity living, ItemStack itemStack) {
        ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) return;
        if (!info.infoOwnerScanning){
            var context = new ItemStackScanContext(living,itemStack);
            info.infoOwnerScanning = true;
            try {
                for (var listener:ITEM_STACK_SCAN_LISTENERS.getListeners()){
                    listener.onItemStackScan(context);
                    if (context.shouldBreak){
                        break;
                    }
                }
            }catch (Exception e){
                OpenTC4.LOGGER.error("Error while trying to scan itemStack for living {} {} {}", living.getName(), living.getUUID(), living.position(), e);
            }
            info.infoOwnerScanning = false;
        }

    }
}
