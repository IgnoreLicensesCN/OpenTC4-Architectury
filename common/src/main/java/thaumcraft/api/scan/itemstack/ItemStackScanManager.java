package thaumcraft.api.scan.itemstack;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.researches.ResearchAndScannedInfo;

public class ItemStackScanManager {
    public static final ListenerManager<ItemStackScanListener> ITEM_STACK_SCAN_LISTENERS = new ListenerManager<>();
    static {
        for (var listenerEnum:ItemStackScanListeners.values()){
            ITEM_STACK_SCAN_LISTENERS.registerListener(listenerEnum.listener);
        }
    }

    public static void onPlayerScanItemStack(ServerPlayer player, ItemStack itemStack) {
        ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromPlayer(player);
        if (!info.playerScanning){
            var context = new ItemStackScanContext(player,itemStack);
            info.playerScanning = true;
            try {
                for (var listener:ITEM_STACK_SCAN_LISTENERS.getListeners()){
                    listener.onItemStackScan(context);
                    if (context.shouldBreak){
                        break;
                    }
                }
            }catch (Exception e){
                OpenTC4.LOGGER.error("Error while trying to scan itemStack for player "+player.getName(),e);
            }
            info.playerScanning = false;
        }

    }
}
