package thaumcraft.api.scan.block;

import net.minecraft.world.item.Items;
import thaumcraft.api.nodes.INodeInfoProviderBlockEntity;
import thaumcraft.api.scan.ScanManager;
import thaumcraft.api.scan.itemstack.ItemStackScanListeners;

public enum BlockPosScanListeners {
    BLOCK_ITEM_SCAN(
            new BlockPosScanListener(0) {
                @Override
                public void onBlockScan(BlockPosScanContext context) {
                    var item = context.bState.getBlock().asItem();
                    if (item != Items.AIR) {
                        if (ItemStackScanListeners.scanItemCommon(context.playerScanning,item)){
                            context.shouldBreak = true;
                        }
                    }
                }
            }
    ),
    NODE_SCAN(new BlockPosScanListener(100) {
        @Override
        public void onBlockScan(BlockPosScanContext context) {
            var level = context.playerScanning.level();
            if (level.getBlockEntity(context.pos) instanceof INodeInfoProviderBlockEntity nodeInfoProvider){
                if (ScanManager.NodeScanManager.onScanVisNode(context.playerScanning,nodeInfoProvider.getNodeInfo())){
                    context.shouldBreak = true;
                }
            }

        }
    })
    ;
    public final BlockPosScanListener listener;
    BlockPosScanListeners(BlockPosScanListener listener){
        this.listener = listener;
    }
}
