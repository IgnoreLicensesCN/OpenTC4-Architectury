package thaumcraft.api.scan.block;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
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
                        if (ItemStackScanListeners.scanItemCommon(context.livingScanning,item)){
                            context.shouldBreak = true;
                        }
                    }
                }
            }
    ),
    NODE_SCAN(new BlockPosScanListener(100) {
        @Override
        public void onBlockScan(BlockPosScanContext context) {
            var level = context.livingScanning.level();
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, context.pos) instanceof INodeInfoProviderBlockEntity nodeInfoProvider){
                if (ScanManager.NodeScanManager.onScanVisNode(context.livingScanning,nodeInfoProvider.getNodeInfo())){
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
