package thaumcraft.client.renderers;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import thaumcraft.client.renderers.block.HungryChestRenderer;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class ThaumcraftRenderers {

    public static void init(){
        initBlocks();
    }
    public static void initBlocks(){
        BlockEntityRendererRegistry.register(
                ThaumcraftBlockEntities.HUNGRY_CHEST,
                HungryChestRenderer::new
        );
    }
}
