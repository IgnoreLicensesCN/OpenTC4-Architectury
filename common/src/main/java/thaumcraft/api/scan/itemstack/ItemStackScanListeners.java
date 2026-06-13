package thaumcraft.api.scan.itemstack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.api.scan.ScanManager;
import thaumcraft.api.research.ResearchAndScannedInfo;

import static thaumcraft.api.scan.ThaumcraftScannedTypes.ITEM;

public enum ItemStackScanListeners {
    SCAN_ITEM_COMMON(new ItemStackScanListener(0) {
        @Override
        public void onItemStackScan(ItemStackScanContext context) {
            if (scanItemCommon(context.playerScanning,context.stack.getItem())){
                context.shouldBreak = true;
            }
        }
    }),//TODO:[maybe wont finished]handle node item
    ;
    public final ItemStackScanListener listener;
    ItemStackScanListeners(ItemStackScanListener listener){
        this.listener = listener;
    }
    //boolean if scanned new
    public static boolean scanItemCommon(ServerPlayer player, Item item) {
        var info = ResearchAndScannedInfo.getFromPlayer(player);
        var itemID = item.arch$registryName();
        if (itemID == null) {
            return false;//oh wtf is it going on--not registered?I may have to log this
        }
        if (info.hasScannedForType(ITEM,itemID)) {
            return false;
        }
        var basicAspects = ItemBasicAspectGetter.getBasicAspectsServer(item);
        if (ScanManager.CommonScanManager.onMeetAspectsToScan(player, basicAspects)){
            info.addScannedForType(ITEM,itemID);
            return true;
        }
        return false;
    }
}
