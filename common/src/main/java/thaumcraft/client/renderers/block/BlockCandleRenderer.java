package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import java.awt.Color;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockCandle;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;

public class BlockCandleRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      Color c = new Color(Utils.colors[metadata]);
      float r = (float)c.getRed() / 255.0F;
      float g = (float)c.getGreen() / 255.0F;
      float b = (float)c.getBlue() / 255.0F;
      GL11.glColor3f(r, g, b);
      block.setBlockBounds(W6, 0.0F, W6, W10, 0.5F, W10);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockCandle)block).icon, true);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      block.setBlockBounds(0.475F, 0.5F, 0.475F, 0.525F, W10, 0.525F);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockCandle)block).iconStub, true);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int type = 0;
      block.setBlockBounds(W6, 0.0F, W6, W10, 0.5F, W10);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      Random rr = new Random(x + (long) y * z);
      int q = 1 + rr.nextInt(5);

      for(int a = 0; a < q; ++a) {
         boolean side = rr.nextBoolean();
         int loc = 2 + rr.nextInt(2);
         if (a % 2 == 0) {
            block.setBlockBounds(W5 + W1 * (float)loc, 0.0F, side ? W5 : W10, W6 + W1 * (float)loc, W1 * (float)(1 + rr.nextInt(3)), side ? W6 : W11);
         } else {
            block.setBlockBounds(side ? W5 : W10, 0.0F, W5 + W1 * (float)loc, side ? W6 : W11, W1 * (float)(1 + rr.nextInt(3)), W6 + W1 * (float)loc);
         }
          renderer.setRenderBoundsFromBlock(block);
          renderer.renderStandardBlock(block, x, y, z);
      }

      renderer.overrideBlockTexture = ((BlockCandle)block).iconStub;
      block.setBlockBounds(0.475F, 0.5F, 0.475F, 0.525F, W10, 0.525F);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 1.0F, 1.0F, 1.0F);
      renderer.clearOverrideBlockTexture();
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockCandleRI;
   }
}
