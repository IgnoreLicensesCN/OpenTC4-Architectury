package thaumcraft.api.scan.block;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class BlockPosScanManager {
    public static final ListenerManager<BlockPosScanListener> BLOCK_POS_SCAN_LISTENERS = new ListenerManager<>();
    static {
        for (var listenerEnum: BlockPosScanListeners.values()){
            BLOCK_POS_SCAN_LISTENERS.registerListener(listenerEnum.listener);
        }
    }

    public static void onScanBlockPos(LivingEntity living, BlockPos pos) {
        ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) return;
        if (!info.infoOwnerScanning){
            var context = new BlockPosScanContext(living,living.level().getBlockState(pos),pos);
            info.infoOwnerScanning = true;
            try {
                for (var listener: BLOCK_POS_SCAN_LISTENERS.getListeners()){
                    listener.onBlockScan(context);
                    if (context.shouldBreak){
                        break;
                    }
                }
            }catch (Exception e){
                OpenTC4.LOGGER.error("Error while trying to scan blockPos for living {} {} {} {}", living.getUUID(), living.position(), e, living.getName());
            }
            info.infoOwnerScanning = false;
        }

    }
}
