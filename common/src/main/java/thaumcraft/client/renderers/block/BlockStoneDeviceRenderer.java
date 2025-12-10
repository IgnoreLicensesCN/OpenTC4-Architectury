package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockStoneDevice;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileFluxScrubber;
import thaumcraft.common.tiles.TileFocalManipulator;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TileNodeConverter;
import thaumcraft.common.tiles.TileNodeStabilizer;

public class BlockStoneDeviceRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      if (metadata == 0) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconFurnace[1], ((BlockStoneDevice)block).iconFurnace[1], ((BlockStoneDevice)block).iconFurnace[2], ((BlockStoneDevice)block).iconFurnace[2], ((BlockStoneDevice)block).iconFurnace[2], ((BlockStoneDevice)block).iconFurnace[2], true);
      } else if (metadata == 1) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], true);
         block.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], true);
         block.setBlockBounds(0.125F, 0.75F, 0.125F, 0.875F, 1.0F, 0.875F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], ((BlockStoneDevice)block).iconPedestal[0], true);
      } else if (metadata == 2) {
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileInfusionMatrix(), 0.0F, 0.0F, 0.0F, 0.0F);
      } else if (metadata == 5) {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconWandPedestal[1], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], true);
         block.setBlockBounds(0.125F, 0.25F, 0.125F, 0.875F, 0.5F, 0.875F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconWandPedestal[1], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], true);
         block.setBlockBounds(0.25F, 0.5F, 0.25F, 0.75F, 1.0F, 0.75F);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconWandPedestal[1], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], ((BlockStoneDevice)block).iconWandPedestal[0], true);
      } else if (metadata == 8) {
         block.setBlockBounds(W5, 0.0F, W5, W11, W1, W11);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W1, 0.0F, W7, W5, W1, W9);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W11, 0.0F, W7, W15, W1, W9);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W7, 0.0F, W1, W9, W1, W5);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W7, 0.0F, W11, W9, W1, W15);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W1, W1, W7, W3, W7, W9);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W7, W1, W1, W9, W7, W3);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W13, W1, W7, W15, W7, W9);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
         block.setBlockBounds(W7, W1, W13, W9, W7, W15);
         renderer.setRenderBoundsFromBlock(block);
         drawFaces(renderer, block, ((BlockStoneDevice)block).iconWandPedestalFocus[2], ((BlockStoneDevice)block).iconWandPedestalFocus[1], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], ((BlockStoneDevice)block).iconWandPedestalFocus[0], true);
      } else if (metadata != 9 && metadata != 10) {
         if (metadata != 11) {
            if (metadata == 12) {
               block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               renderer.setRenderBoundsFromBlock(block);
               drawFaces(renderer, block, ((BlockStoneDevice)block).iconPedestal[1], ((BlockStoneDevice)block).iconSpa[1], ((BlockStoneDevice)block).iconSpa[0], ((BlockStoneDevice)block).iconSpa[0], ((BlockStoneDevice)block).iconSpa[0], ((BlockStoneDevice)block).iconSpa[0], true);
            } else if (metadata == 13) {
               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileFocalManipulator(), 0.0F, 0.0F, 0.0F, 0.0F);
            } else if (metadata == 14) {
               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileFluxScrubber(), 0.0F, 0.0F, 0.0F, 0.0F);
            }
         } else {
            try {
               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileNodeConverter(), 0.0F, 0.0F, 0.0F, 0.0F);
            }catch (Exception e) {
               e.printStackTrace();
//               throw e;
            }
         }
      } else {
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileNodeStabilizer(metadata), 0.0F, 0.0F, 0.0F, 0.0F);
      }

   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata != 0 && metadata != 12) {
         if (metadata == 1) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.125F, 0.75F, 0.125F, 0.875F, 1.0F, 0.875F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
         } else if (metadata == 5) {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.125F, 0.25F, 0.125F, 0.875F, 0.5F, 0.875F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.25F, 0.5F, 0.25F, 0.75F, 1.0F, 0.75F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
         } else if (metadata == 8) {
            block.setBlockBounds(W5, 0.0F, W5, W11, W1, W11);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W1, 0.0F, W7, W5, W1, W9);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W11, 0.0F, W7, W15, W1, W9);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W7, 0.0F, W1, W9, W1, W5);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W7, 0.0F, W11, W9, W1, W15);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W1, W1, W7, W3, W7, W9);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W7, W1, W1, W9, W7, W3);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W13, W1, W7, W15, W7, W9);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(W7, W1, W13, W9, W7, W15);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
         }
      } else {
         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         renderer.renderStandardBlock(block, x, y, z);
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
      return ConfigBlocks.blockStoneDeviceRI;
   }
}
