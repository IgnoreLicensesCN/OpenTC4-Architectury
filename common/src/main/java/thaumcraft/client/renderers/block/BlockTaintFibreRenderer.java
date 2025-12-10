package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

public class BlockTaintFibreRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      boolean fix = true;
      int metadata = world.getBlockMetadata(x, y, z);
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      setBrightness(world, x, y, z, block);
      Tessellator t = Tessellator.instance;
      if (metadata <= 4) {
         if (world.isSideSolid(x - 1, y, z, ForgeDirection.EAST, true) && world.getBlock(x - 1, y, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceXPos(block, (float)x - 0.995F, y, z, block.getIcon(0, 0));
         }

         if (world.isSideSolid(x + 1, y, z, ForgeDirection.WEST, true) && world.getBlock(x + 1, y, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceXNeg(block, (float)x + 0.995F, y, z, block.getIcon(0, 0));
         }

         if (world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH, true) && world.getBlock(x, y, z - 1) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceZPos(block, x, y, (float)z - 0.995F, block.getIcon(0, 0));
         }

         if (world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH, true) && world.getBlock(x, y, z + 1) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceZNeg(block, x, y, (float)z + 0.995F, block.getIcon(0, 0));
         }

         if (world.isSideSolid(x, y - 1, z, ForgeDirection.UP, true) && world.getBlock(x, y - 1, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceYPos(block, x, (float)y - 0.995F, z, block.getIcon(0, 0));
         }

         if (world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN, true) && world.getBlock(x, y + 1, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceYNeg(block, x, (float)y + 0.995F, z, block.getIcon(0, 0));
         }
      }

      if (metadata == 0 && Config.glowyTaint) {
         t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
         t.setBrightness(200);
         if (world.isSideSolid(x - 1, y, z, ForgeDirection.EAST, true) && world.getBlock(x - 1, y, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceXPos(block, (float)x - 0.98F, y, z, ((BlockTaintFibres)block).getOverlayBlockTexture(x, y, z, 4));
         }

         if (world.isSideSolid(x + 1, y, z, ForgeDirection.WEST, true) && world.getBlock(x + 1, y, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceXNeg(block, (float)x + 0.98F, y, z, ((BlockTaintFibres)block).getOverlayBlockTexture(x, y, z, 5));
         }

         if (world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH, true) && world.getBlock(x, y, z - 1) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceZPos(block, x, y, (float)z - 0.98F, ((BlockTaintFibres)block).getOverlayBlockTexture(x, y, z, 2));
         }

         if (world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH, true) && world.getBlock(x, y, z + 1) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceZNeg(block, x, y, (float)z + 0.98F, ((BlockTaintFibres)block).getOverlayBlockTexture(x, y, z, 3));
         }

         if (world.isSideSolid(x, y - 1, z, ForgeDirection.UP, true) && world.getBlock(x, y - 1, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceYPos(block, x, (float)y - 0.98F, z, ((BlockTaintFibres)block).getOverlayBlockTexture(x, y, z, 0));
         }

         if (world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN, true) && world.getBlock(x, y + 1, z) != ConfigBlocks.blockTaint) {
            fix = false;
            renderer.renderFaceYNeg(block, x, (float)y + 0.98F, z, ((BlockTaintFibres)block).getOverlayBlockTexture(x, y, z, 1));
         }
      }

      if ((metadata == 1 || metadata == 2) && world.isSideSolid(x, y - 1, z, ForgeDirection.UP, true)) {
         double d0 = x;
         double d1 = y;
         double d2 = z;
         long i1 = (x * 3129871L) ^ (long)z * 116129781L ^ (long)y;
         i1 = i1 * i1 * 42317861L + i1 * 11L;
         d0 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - (double)0.5F) * (double)0.5F;
         d2 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - (double)0.5F) * (double)0.5F;
         fix = false;
         renderer.drawCrossedSquares(block.getIcon(0, metadata), d0, d1, d2, 1.0F);
      }

      if (metadata == 3 || metadata == 4) {
         fix = false;
         renderer.renderCrossedSquares(block, x, y, z);
      }

      if (fix) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         renderer.setRenderBoundsFromBlock(block);
         renderer.renderStandardBlock(block, x, y, z);
      }

      renderer.clearOverrideBlockTexture();
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return false;
   }

   public int getRenderId() {
      return ConfigBlocks.blockTaintFibreRI;
   }
}
