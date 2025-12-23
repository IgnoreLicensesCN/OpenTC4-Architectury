package thaumcraft.client.renderers.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;
import thaumcraft.common.tiles.HungryChestBlockEntity;

public class HungryChestRenderer extends ChestRenderer<HungryChestBlockEntity> {
    public HungryChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}
