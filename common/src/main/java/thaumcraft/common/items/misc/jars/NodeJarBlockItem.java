package thaumcraft.common.items.misc.jars;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.NodeInfo;

import java.util.List;

import static com.linearity.opentc4.Consts.NodeJarTagAccessors.NODE_INFO;
import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class NodeJarBlockItem extends BlockItem {
    public NodeJarBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public NodeJarBlockItem() {
        this(ThaumcraftBlocks.NODE_JAR, new Properties().stacksTo(1));
    }

    public NodeInfo getNodeInfo(ItemStack stack) {
        if (stack.isEmpty()) {
            return NodeInfo.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null) {
            return NodeInfo.EMPTY;
        }
        return NODE_INFO.readFromCompoundTag(tag);
    }
    public void setNodeInfo(ItemStack stack,NodeInfo info) {
        NODE_INFO.writeToCompoundTag(stack.getOrCreateTag(),info);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        var player = Minecraft.getInstance().player;
        var nodeInfo = getNodeInfo(itemStack);
        addAspectDescriptionToList(nodeInfo.nodeAspects, player, list);
    }
}
