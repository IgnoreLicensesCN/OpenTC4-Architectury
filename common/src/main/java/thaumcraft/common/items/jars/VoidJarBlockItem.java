package thaumcraft.common.items.jars;

import net.minecraft.world.level.block.Block;
import thaumcraft.common.blocks.ThaumcraftBlocks;

public class VoidJarBlockItem extends EssentiaJarBlockItem {
    public VoidJarBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public VoidJarBlockItem() {
        this(ThaumcraftBlocks.ThaumcraftBlockInstances.VOID_JAR, new Properties().stacksTo(1));
    }
}
