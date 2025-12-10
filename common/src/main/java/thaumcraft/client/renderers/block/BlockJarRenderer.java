package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileJarBrain;

public class BlockJarRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      if (metadata == 1) {
         GL11.glPushMatrix();
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileJarBrain(), 0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glEnable(32826);
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      IIcon i1 = ((BlockJar)block).iconJarTop;
      IIcon i2 = ((BlockJar)block).iconJarSide;
      if (metadata == 3) {
         i1 = ((BlockJar)block).iconJarTopVoid;
         i2 = ((BlockJar)block).iconJarSideVoid;
      }

      block.setBlockBounds(W3, 0.0F, W3, W13, W12, W13);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockJar)block).iconJarBottom, i1, i2, i2, i2, i2, true);
      block.setBlockBounds(W5, W12, W5, W11, W14, W11);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, ((BlockJar)block).iconJarBottom, i1, i2, i2, i2, i2, true);
      GL11.glPopMatrix();
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      setBrightness(world, x, y, z, block);
      world.getBlockMetadata(x, y, z);
      block.setBlockBounds(W3, 0.0F, W3, W13, W12, W13);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      block.setBlockBounds(W5, W12, W5, W11, W14, W11);
      renderer.setRenderBoundsFromBlock(block);
      renderer.renderStandardBlock(block, x, y, z);
      renderer.clearOverrideBlockTexture();
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockJarRI;
   }
}
