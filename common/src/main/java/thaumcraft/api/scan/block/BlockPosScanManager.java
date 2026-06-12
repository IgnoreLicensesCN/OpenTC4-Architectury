package thaumcraft.api.scan.block;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.common.researches.ResearchAndScannedInfo;

public class BlockPosScanManager {
    public static final ListenerManager<BlockPosScanListener> BLOCK_POS_SCAN_LISTENERS = new ListenerManager<>();
    static {
        for (var listenerEnum: BlockPosScanListeners.values()){
            BLOCK_POS_SCAN_LISTENERS.registerListener(listenerEnum.listener);
        }
    }

    public static void onPlayerScanBlockPos(ServerPlayer player, BlockPos pos) {
        ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromPlayer(player);
        if (!info.playerScanning){
            var context = new BlockPosScanContext(player,player.level().getBlockState(pos),pos);
            info.playerScanning = true;
            try {
                for (var listener: BLOCK_POS_SCAN_LISTENERS.getListeners()){
                    listener.onBlockScan(context);
                    if (context.shouldBreak){
                        break;
                    }
                }
            }catch (Exception e){
                OpenTC4.LOGGER.error("Error while trying to scan blockPos for player "+player.getName(),e);
            }
            info.playerScanning = false;
        }

    }
}
