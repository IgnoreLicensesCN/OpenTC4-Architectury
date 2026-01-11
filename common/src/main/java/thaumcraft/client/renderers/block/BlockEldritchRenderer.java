package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.config.ConfigBlocks;

public class BlockEldritchRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public static float W1 = 0.0625F;
   public static float W2 = 0.125F;
   public static float W3 = 0.1875F;
   public static float W4 = 0.25F;
   public static float W5 = 0.3125F;
   public static float W6 = 0.375F;
   public static float W7 = 0.4375F;
   public static float W8 = 0.5F;
   public static float W9 = 0.5625F;
   public static float W10 = 0.625F;
   public static float W11 = 0.6875F;
   public static float W12 = 0.75F;
   public static float W13 = 0.8125F;
   public static float W14 = 0.875F;
   public static float W15 = 0.9375F;
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      if (metadata == 4 || metadata == 5) {
         block.setBlockBounds(W2, W2, W2, W14, W14, W14);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, block.getIcon(0, metadata), false);
      }

      if (metadata == 6) {
         block.setBlockBounds(W2, W2, W2, W14, W14, W14);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, block.getIcon(0, metadata), false);
      }

   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 4 || metadata == 5 || metadata == 6) {
         renderer.field_152631_f = true;
         setBrightness(world, x, y, z, block);
         float s1 = 0.0F;
         float s2 = 0.0F;
         float s3 = 0.0F;
         float s4 = 1.0F;
         float s5 = 1.0F;
         float s6 = 1.0F;
         if (!block.isBlockSolid(world, x + 1, y, z, 4)) {
            s4 -= W2;
         }

         if (!block.isBlockSolid(world, x - 1, y, z, 5)) {
            s1 += W2;
         }

         if (!block.isBlockSolid(world, x, y, z + 1, 2)) {
            s6 -= W2;
         }

         if (!block.isBlockSolid(world, x, y, z - 1, 3)) {
            s3 += W2;
         }

         if (!block.isBlockSolid(world, x, y + 1, z, 0)) {
            s5 -= W2;
         }

         if (!block.isBlockSolid(world, x, y - 1, z, 1)) {
            s2 += W2;
         }

         block.setBlockBounds(s1, s2, s3, s4, s5, s6);
         renderer.setRenderBoundsFromBlock(block);
         renderAllSides(world, x, y, z, block, renderer, false);
         renderer.field_152631_f = false;
         renderer.clearOverrideBlockTexture();
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
      }

      if (metadata == 7 || metadata == 8 || metadata == 9 || metadata == 10) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         renderer.renderStandardBlock(block, x, y, z);
         renderer.clearOverrideBlockTexture();
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
      }

      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockEldritchRI;
   }
}
