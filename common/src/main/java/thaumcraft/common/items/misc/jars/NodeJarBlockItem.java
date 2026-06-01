package thaumcraft.common.items.misc.jars;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;
import thaumcraft.api.aspects.IAspectDisplayItem;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.NodeInfo;

import java.util.Map;
import java.util.WeakHashMap;

import static com.linearity.opentc4.Consts.NodeJarTagAccessors.NODE_INFO;

public class NodeJarBlockItem extends BlockItem implements IAspectDisplayItem<Aspect> {
    public NodeJarBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public NodeJarBlockItem() {
        this(ThaumcraftBlocks.NODE_JAR, new Properties().stacksTo(1));
    }
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.CLIENT)
    public static final Map<ItemStack,NodeInfo> stackToNodeInfoForDisplay = new WeakHashMap<>();
    public NodeInfo getNodeInfo(ItemStack stack) {
        if (stack.isEmpty()) {
            return NodeInfo.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null) {
            return NodeInfo.EMPTY;
        }
        var gotInfo = NODE_INFO.readFromCompoundTag(tag);
        if (Platform.getEnvironment() != Env.SERVER){
            stackToNodeInfoForDisplay.put(stack, gotInfo);
        }
        return gotInfo;
    }
    public void setNodeInfo(ItemStack stack,NodeInfo info) {
        if (Platform.getEnvironment() != Env.SERVER){
            stackToNodeInfoForDisplay.put(stack, info);
        }
        NODE_INFO.writeToCompoundTag(stack.getOrCreateTag(),info);
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack stack) {
        var toDisplay = stackToNodeInfoForDisplay.get(stack);
        if (toDisplay == null){
            return getNodeInfo(stack).nodeAspects;
        }
        return toDisplay.nodeAspects;
    }
}
