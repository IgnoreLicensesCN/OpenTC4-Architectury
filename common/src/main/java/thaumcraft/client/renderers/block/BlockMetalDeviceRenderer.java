package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockMetalDevice;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileBrainbox;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileMagicWorkbenchCharger;
import thaumcraft.common.tiles.TileVisRelay;

public class BlockMetalDeviceRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      if (metadata != 0 && metadata != 6) {
         if (metadata == 1) {
            GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileAlembic(), 0.0F, 0.0F, 0.0F, 0.0F);
            GL11.glEnable(32826);
         } else if (metadata == 5) {
            GL11.glTranslatef(0.0F, -0.3F, 0.0F);
            block.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[8], false);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[9], false);
         } else if (metadata == 7) {
            block.setBlockBounds(W4, W2, W4, W12, W14, W12);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[11], ((BlockMetalDevice)block).icon[11], ((BlockMetalDevice)block).icon[10], ((BlockMetalDevice)block).icon[10], ((BlockMetalDevice)block).icon[10], ((BlockMetalDevice)block).icon[10], true);
         } else if (metadata == 8) {
            block.setBlockBounds(W4, W2, W4, W12, W14, W12);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[13], ((BlockMetalDevice)block).icon[13], ((BlockMetalDevice)block).icon[12], ((BlockMetalDevice)block).icon[12], ((BlockMetalDevice)block).icon[12], ((BlockMetalDevice)block).icon[12], true);
         } else if (metadata == 9) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[16], true);
         } else if (metadata == 3) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[22], true);
         } else if (metadata == 12) {
            block.setBlockBounds(W3, W3, W3, W13, W13, W13);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[17], true);
         } else if (metadata == 13) {
            block.setBlockBounds(W4, W2, W4, W12, W14, W12);
            renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockMetalDevice)block).icon[19], ((BlockMetalDevice)block).icon[19], ((BlockMetalDevice)block).icon[18], ((BlockMetalDevice)block).icon[18], ((BlockMetalDevice)block).icon[18], ((BlockMetalDevice)block).icon[18], true);
         } else if (metadata == 14) {
            GL11.glPushMatrix();
            GL11.glScaled(1.5F, 1.5F, 1.5F);
            GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileVisRelay(), 0.0F, 0.0F, 0.0F, 0.0F);
            GL11.glEnable(32826);
            GL11.glPopMatrix();
         } else if (metadata == 2) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileMagicWorkbenchCharger(), 0.0F, 0.0F, 0.0F, 0.0F);
            GL11.glEnable(32826);
            GL11.glPopMatrix();
         }
      } else {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockMetalDevice)block).icon[2], ((BlockMetalDevice)block).icon[4], ((BlockMetalDevice)block).icon[3], ((BlockMetalDevice)block).icon[3], ((BlockMetalDevice)block).icon[3], ((BlockMetalDevice)block).icon[3], true);
      }

   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 0) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         renderer.renderStandardBlock(block, x, y, z);
         IIcon innerSide = ((BlockMetalDevice)block).icon[5];
         IIcon bottom = ((BlockMetalDevice)block).icon[6];
         float f5 = 0.123F;
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileCrucible && ((TileCrucible) te).aspects.size() > 0) {
            setBrightness(world, x, y, z, block);
         }

         renderer.renderFaceXPos(block, (float)x - 1.0F + f5, y, z, innerSide);
         renderer.renderFaceXNeg(block, (float)x + 1.0F - f5, y, z, innerSide);
         renderer.renderFaceZPos(block, x, y, (float)z - 1.0F + f5, innerSide);
         renderer.renderFaceZNeg(block, x, y, (float)z + 1.0F - f5, innerSide);
         renderer.renderFaceYPos(block, x, (float)y - 1.0F + 0.25F, z, bottom);
         renderer.renderFaceYNeg(block, x, (float)y + 1.0F - 0.75F, z, bottom);
      } else if (metadata != 5 && metadata != 6) {
         if (metadata != 7 && metadata != 8 && metadata != 13) {
            if (metadata != 3 && metadata != 9) {
               if (metadata == 12) {
                  block.setBlockBounds(W3, W3, W3, W13, W13, W13);
                  renderer.setRenderBoundsFromBlock(block);
                  renderer.renderStandardBlock(block, x, y, z);
                  TileEntity te = world.getTileEntity(x, y, z);
                  if (te instanceof TileBrainbox) {
                     switch (((TileBrainbox)te).facing) {
                        case UP:
                           block.setBlockBounds(W6, W13, W6, W10, 1.0F, W10);
                           renderer.setRenderBoundsFromBlock(block);
                           renderer.renderStandardBlock(block, x, y, z);
                           break;
                        case DOWN:
                           block.setBlockBounds(W6, 0.0F, W6, W10, W3, W10);
                           renderer.setRenderBoundsFromBlock(block);
                           renderer.renderStandardBlock(block, x, y, z);
                           break;
                        case EAST:
                           block.setBlockBounds(W13, W6, W6, 1.0F, W10, W10);
                           renderer.setRenderBoundsFromBlock(block);
                           renderer.renderStandardBlock(block, x, y, z);
                           break;
                        case WEST:
                           block.setBlockBounds(0.0F, W6, W6, W3, W10, W10);
                           renderer.setRenderBoundsFromBlock(block);
                           renderer.renderStandardBlock(block, x, y, z);
                           break;
                        case SOUTH:
                           block.setBlockBounds(W6, W6, W13, W10, W10, 1.0F);
                           renderer.setRenderBoundsFromBlock(block);
                           renderer.renderStandardBlock(block, x, y, z);
                           break;
                        case NORTH:
                           block.setBlockBounds(W6, W6, 0.0F, W10, W10, W3);
                           renderer.setRenderBoundsFromBlock(block);
                           renderer.renderStandardBlock(block, x, y, z);
                     }
                  }
               }
            } else {
               block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               renderer.setRenderBoundsFromBlock(block);
               renderer.renderStandardBlock(block, x, y, z);
            }
         } else {
            block.setBlockBounds(W4, W2, W4, W12, W14, W12);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
         }
      } else {
         setBrightness(world, x, y, z, block);
         block.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         renderer.renderStandardBlock(block, x, y, z);
         renderer.renderFaceXPos(block, (float)x - 1.002F + W3, y, z, block.getBlockTextureFromSide(0));
         renderer.renderFaceXNeg(block, (float)x + 1.002F - W3, y, z, block.getBlockTextureFromSide(0));
         renderer.renderFaceZPos(block, x, y, (float)z - 1.002F + W3, block.getBlockTextureFromSide(0));
         renderer.renderFaceZNeg(block, x, y, (float)z + 1.002F - W3, block.getBlockTextureFromSide(0));
         renderer.renderFaceXPos(block, (float)x - 1.002F + W9, y, z, block.getBlockTextureFromSide(0));
         renderer.renderFaceXNeg(block, (float)x + 1.002F - W9, y, z, block.getBlockTextureFromSide(0));
         renderer.renderFaceZPos(block, x, y, (float)z - 1.002F + W9, block.getBlockTextureFromSide(0));
         renderer.renderFaceZNeg(block, x, y, (float)z + 1.002F - W9, block.getBlockTextureFromSide(0));
         if (metadata == 6) {
            block.setBlockBounds(W1, W14, W1, W15, W15, W15);
            renderer.setRenderBoundsFromBlock(block);
            renderer.overrideBlockTexture = ((BlockMetalDevice)block).icon[9];
            renderer.renderStandardBlock(block, x, y, z);
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
      return ConfigBlocks.blockMetalDeviceRI;
   }
}
