package thaumcraft.api.nodes;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.lib.NodeInfo;

public interface INodeInfoProviderItem {
    NodeInfo getNodeInfo(ItemStack stack);
}
