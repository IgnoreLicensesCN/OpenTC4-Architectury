package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockCustomOre;
import thaumcraft.common.blocks.BlockCustomOreItem;
import thaumcraft.common.config.ConfigBlocks;

public class BlockCustomOreRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      if (metadata == 0) {
         drawFaces(renderer, block, ((BlockCustomOre)block).icon[0], false);
      } else if (metadata == 7) {
         drawFaces(renderer, block, ((BlockCustomOre)block).icon[3], false);
      } else if (metadata < 7) {
         drawFaces(renderer, block, ((BlockCustomOre)block).icon[1], false);
         Color c = new Color(BlockCustomOreItem.colors[metadata]);
         float r = (float)c.getRed() / 255.0F;
         float g = (float)c.getGreen() / 255.0F;
         float b = (float)c.getBlue() / 255.0F;
         GL11.glColor3f(r, g, b);
         block.setBlockBounds(0.005F, 0.005F, 0.005F, 0.995F, 0.995F, 0.995F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockCustomOre)block).icon[2], false);
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }

   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int bb = setBrightness(world, x, y, z, block);
      int metadata = world.getBlockMetadata(x, y, z);
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      if (metadata != 0 && metadata < 7) {
         Tessellator t = Tessellator.instance;
         t.setColorOpaque_I(BlockCustomOreItem.colors[metadata]);
         t.setBrightness(Math.max(bb, 160));
         renderAllSides(world, x, y, z, block, renderer, ((BlockCustomOre)block).icon[2], false);
         if (Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1) {
            block.setBlockBounds(0.005F, 0.005F, 0.005F, 0.995F, 0.995F, 0.995F);
            renderer.setRenderBoundsFromBlock(block);
            t.setBrightness(bb);
            renderAllSides(world, x, y, z, block, renderer, Blocks.stone.getIcon(0, 0), false);
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
      return ConfigBlocks.blockCustomOreRI;
   }
}
