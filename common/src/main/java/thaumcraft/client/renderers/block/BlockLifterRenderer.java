package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockCustomOreItem;
import thaumcraft.common.blocks.BlockLifter;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileLifter;

public class BlockLifterRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockLifter)block).iconBottom, ((BlockLifter)block).iconTop, ((BlockLifter)block).iconSide, ((BlockLifter)block).iconSide, ((BlockLifter)block).iconSide, ((BlockLifter)block).iconSide, false);
      Color c = new Color(BlockCustomOreItem.colors[4]);
      float r = (float)c.getRed() / 255.0F;
      float g = (float)c.getGreen() / 255.0F;
      float b = (float)c.getBlue() / 255.0F;
      GL11.glColor3f(r, g, b);
      block.setBlockBounds(0.01F, 0.9F, 0.01F, 0.99F, 0.99F, 0.99F);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockLifter)block).iconGlow, false);
      c = new Color(BlockCustomOreItem.colors[5]);
      r = (float)c.getRed() / 255.0F;
      g = (float)c.getGreen() / 255.0F;
      b = (float)c.getBlue() / 255.0F;
      GL11.glColor3f(r, g, b);
      block.setBlockBounds(0.01F, 0.1F, 0.01F, 0.99F, 0.9F, 0.99F);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockLifter)block).iconGlow, false);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int bb = setBrightness(world, x, y, z, block);
      world.getBlockMetadata(x, y, z);
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      Tessellator t = Tessellator.instance;
      t.setColorOpaque_I(BlockCustomOreItem.colors[4]);
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileLifter && !((TileLifter) te).gettingPower()) {
         bb = 180;
      }

      t.setBrightness(bb);
      if (block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
         renderer.renderFaceYPos(block, x, (float)y - 0.01F, z, ((BlockLifter)block).iconGlow);
      }

      t.setColorOpaque_I(14488063);
      if (block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
         renderer.renderFaceXPos(block, (float)x - 0.01F, y, z, ((BlockLifter)block).iconGlow);
      }

      if (block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
         renderer.renderFaceXNeg(block, (float)x + 0.01F, y, z, ((BlockLifter)block).iconGlow);
      }

      if (block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
         renderer.renderFaceZPos(block, x, y, (float)z - 0.01F, ((BlockLifter)block).iconGlow);
      }

      if (block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
         renderer.renderFaceZNeg(block, x, y, (float)z + 0.01F, ((BlockLifter)block).iconGlow);
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
      return ConfigBlocks.blockLifterRI;
   }
}
