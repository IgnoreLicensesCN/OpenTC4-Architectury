package thaumcraft.client.renderers.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import thaumcraft.common.tiles.crafted.HungryChestBlockEntity;

public class HungryChestRenderer extends ChestRenderer<HungryChestBlockEntity> {
    public HungryChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}
