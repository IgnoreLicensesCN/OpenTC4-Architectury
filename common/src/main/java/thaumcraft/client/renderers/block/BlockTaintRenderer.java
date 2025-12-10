package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.config.ConfigBlocks;

public class BlockTaintRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, block.getIcon(0, metadata), block.getIcon(1, metadata), block.getIcon(2, metadata), block.getIcon(3, metadata), block.getIcon(4, metadata), block.getIcon(5, metadata), false);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (block.getRenderBlockPass() == 0) {
         if (metadata == 0 || metadata == 1 || metadata == 2) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
         }
      } else if (block.getRenderBlockPass() == 1 && (metadata == 0 || metadata == 1)) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         BlockTaintFibres b = (BlockTaintFibres)ConfigBlocks.blockTaintFibres;
         Tessellator t = Tessellator.instance;
         t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
         t.setBrightness(200);
         if (block.shouldSideBeRendered(world, x + 1, y, z, ForgeDirection.EAST.ordinal())) {
            renderer.renderFaceXPos(block, x, y, z, b.getOverlayBlockTexture(x, y, z, 4));
         }

         if (block.shouldSideBeRendered(world, x - 1, y, z, ForgeDirection.WEST.ordinal())) {
            renderer.renderFaceXNeg(block, x, y, z, b.getOverlayBlockTexture(x, y, z, 5));
         }

         if (block.shouldSideBeRendered(world, x, y, z + 1, ForgeDirection.SOUTH.ordinal())) {
            renderer.renderFaceZPos(block, x, y, z, b.getOverlayBlockTexture(x, y, z, 2));
         }

         if (block.shouldSideBeRendered(world, x, y, z - 1, ForgeDirection.NORTH.ordinal())) {
            renderer.renderFaceZNeg(block, x, y, z, b.getOverlayBlockTexture(x, y, z, 3));
         }

         if (block.shouldSideBeRendered(world, x, y + 1, z, ForgeDirection.UP.ordinal())) {
            renderer.renderFaceYPos(block, x, y, z, b.getOverlayBlockTexture(x, y, z, 0));
         }

         if (block.shouldSideBeRendered(world, x, y - 1, z, ForgeDirection.DOWN.ordinal())) {
            renderer.renderFaceYNeg(block, x, y, z, b.getOverlayBlockTexture(x, y, z, 1));
         }
      }

      renderer.clearOverrideBlockTexture();
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockTaintRI;
   }
}
