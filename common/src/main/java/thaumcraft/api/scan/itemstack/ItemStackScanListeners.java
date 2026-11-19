package thaumcraft.api.scan.itemstack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.api.nodes.INodeInfoProviderBlockEntity;
import thaumcraft.api.research.impl.eldritch.PrimePearlResearch;
import thaumcraft.api.scan.ScanManager;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketResearchCompleteS2C;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static thaumcraft.api.scan.ThaumcraftScannedTypes.ITEM;
import static thaumcraft.common.items.ThaumcraftItemInstances.PRIME_PEARL;

public enum ItemStackScanListeners {
    SCAN_ITEM_COMMON(new ItemStackScanListener(0) {
        @Override
        public void onItemStackScan(ItemStackScanContext context) {
            scanItemCommon(context);
        }
    }),
    SCAN_NODE_INFO_PROVIDER(new ItemStackScanListener(100) {
        @Override
        public void onItemStackScan(ItemStackScanContext context) {
            scanNodeInfoProvider(context);
        }
    })
    ;

    private static void scanItemCommon(ItemStackScanContext context) {
        var item = context.stack.getItem();
        if (scanItemCommon(context.livingScanning,item)){
            var consumer = onScannedItemEvents.get(item);
            if (consumer != null) {
                consumer.accept(context);
            }
            context.shouldBreak = true;
        }
    }

    public static void scanNodeInfoProvider(ItemStackScanContext context) {
        var item = context.stack.getItem();
        if (item instanceof INodeInfoProviderBlockEntity provider){
            if (ScanManager.NodeScanManager.onScanVisNode(context.livingScanning,provider.getNodeInfo())){
                context.shouldBreak = true;
            }
        }
    }

    public final ItemStackScanListener listener;
    ItemStackScanListeners(ItemStackScanListener listener){
        this.listener = listener;
    }
    //boolean if scanned new
    public static boolean scanItemCommon(LivingEntity living, Item item) {
        var info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) return false;
        var itemID = item.arch$registryName();
        if (itemID == null) {
            return false;//oh wtf is it going on--not registered?I may have to log this
        }
        if (info.hasScannedForType(ITEM,itemID)) {
            return false;
        }
        var basicAspects = ItemBasicAspectGetter.getBasicAspectsServer(item);
        if (ScanManager.CommonScanManager.onMeetAspectsToScan(living, basicAspects)){
            info.addScannedForTypeAndTrySyncToPlayer(living,ITEM,itemID);
            return true;
        }
        return false;
    }
    //warning:may not always work
    public static final Map<Item, Consumer<ItemStackScanContext>> onScannedItemEvents =  new ConcurrentHashMap<>();
    static {
        onScannedItemEvents.put(PRIME_PEARL(),context -> {
            if (context.livingScanning instanceof ServerPlayer serverPlayer) {
                new PacketResearchCompleteS2C(PrimePearlResearch.ID).sendTo(serverPlayer);
            }
        });
    }
}
