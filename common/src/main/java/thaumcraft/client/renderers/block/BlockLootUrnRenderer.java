package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.config.ConfigBlocks;

public class BlockLootUrnRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      world.getBlockMetadata(x, y, z);
      block.setBlockBounds(W3, 0.0F, W3, W13, W1, W13);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      block.setBlockBounds(W2, W1, W2, W14, W13, W14);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      block.setBlockBounds(W4, W13, W4, W12, 1.0F, W12);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      renderer.clearOverrideBlockTexture();
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return false;
   }

   public int getRenderId() {
      return ConfigBlocks.blockLootUrnRI;
   }
}
