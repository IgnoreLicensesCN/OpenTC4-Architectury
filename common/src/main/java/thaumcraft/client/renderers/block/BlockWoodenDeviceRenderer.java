package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockWoodenDevice;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileArcaneBore;
import thaumcraft.common.tiles.TileArcaneBoreBase;
import thaumcraft.common.tiles.TileBanner;
import thaumcraft.common.tiles.TileBellows;
import thaumcraft.common.tiles.TileSensor;

public class BlockWoodenDeviceRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks) {
      if (metadata == 0) {
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileBellows(), 0.0F, 0.0F, 0.0F, 0.0F);
      } else if (metadata == 4) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileArcaneBoreBase(), 0.0F, 0.0F, 0.0F, 0.0F);
      } else if (metadata == 5) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.75F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileArcaneBore(), 0.0F, 0.0F, 0.0F, 0.0F);
      } else if (metadata == 1) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, W3, 1.0F);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[2], ((BlockWoodenDevice)block).iconAEar[3], ((BlockWoodenDevice)block).iconAEar[0], ((BlockWoodenDevice)block).iconAEar[0], ((BlockWoodenDevice)block).iconAEar[0], ((BlockWoodenDevice)block).iconAEar[0], true);
         block.setBlockBounds(W4, W3, W4, W12, 1.0F, W12);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[2], ((BlockWoodenDevice)block).iconAEar[3], ((BlockWoodenDevice)block).iconAEar[0], ((BlockWoodenDevice)block).iconAEar[0], ((BlockWoodenDevice)block).iconAEar[0], ((BlockWoodenDevice)block).iconAEar[0], true);
         block.setBlockBounds(W4, 0.5F, W1, W12, 1.0F, W3);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W5, 0.5F, W3, W11, W15, W4);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W1, 0.5F, W4, W3, 1.0F, W12);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W3, 0.5F, W5, W4, W15, W11);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W4, 0.5F, W13, W12, 1.0F, W15);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W5, 0.5F, W12, W11, W15, W13);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W13, 0.5F, W4, W15, 1.0F, W12);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
         block.setBlockBounds(W12, 0.5F, W5, W13, W15, W11);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[6], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], ((BlockWoodenDevice)block).iconAEar[5], true);
      } else if (metadata == 2) {
         GL11.glTranslatef(0.0F, 0.6F, 0.0F);
         GL11.glScalef(1.3F, 1.3F, 1.3F);
         float var6 = 0.0625F;
         block.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.125F, 1.0F - var6);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconAPPlate[0], true);
      } else if (metadata == 6) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconGreatwood, true);
      } else if (metadata == 7) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderblocks.setRenderBoundsFromBlock(block);
         drawFaces(renderblocks, block, ((BlockWoodenDevice)block).iconSilverwood, true);
      } else if (metadata == 8) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -1.0F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileBanner(), 0.0F, 0.0F, 0.0F, 0.0F);
      }

      GL11.glEnable(32826);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderblocks) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 1) {
         ((BlockWoodenDevice)block).renderState = 0;
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileSensor && ((TileSensor) tile).redstoneSignal > 0) {
            ((BlockWoodenDevice)block).renderState = 1;
         }

         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, W3, 1.0F);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W4, W3, W4, W12, 1.0F, W12);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         ((BlockWoodenDevice)block).renderState = 2;
         block.setBlockBounds(W4, 0.5F, W1, W12, 1.0F, W3);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W5, 0.5F, W3, W11, W15, W4);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W1, 0.5F, W4, W3, 1.0F, W12);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W3, 0.5F, W5, W4, W15, W11);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W4, 0.5F, W13, W12, 1.0F, W15);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W5, 0.5F, W12, W11, W15, W13);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W13, 0.5F, W4, W15, 1.0F, W12);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         block.setBlockBounds(W12, 0.5F, W5, W13, W15, W11);
         renderblocks.setRenderBoundsFromBlock(block);
         renderblocks.renderStandardBlock(block, x, y, z);
         ((BlockWoodenDevice)block).renderState = 0;
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderblocks.setRenderBoundsFromBlock(block);
         return true;
      } else if (md != 2 && md != 3 && md != 6 && md != 7) {
         return false;
      } else {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderblocks.renderStandardBlock(block, x, y, z);
         return true;
      }
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockWoodenDeviceRI;
   }
}
