package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.blocks.BlockCosmeticOpaque;
import thaumcraft.common.config.ConfigBlocks;

public class BlockCosmeticOpaqueRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      renderer.setRenderBoundsFromBlock(block);
      drawFaces(renderer, block, block.getIcon(0, metadata), false);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      if (block instanceof BlockCosmeticOpaque) {
         setBrightness(world, x, y, z, block);
         int metadata = world.getBlockMetadata(x, y, z);
         if (((BlockCosmeticOpaque)block).currentPass != 1) {
            renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return false;
         } else {
            if (metadata <= 1) {
               block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               renderer.setRenderBoundsFromBlock(block);
               renderer.renderStandardBlock(block, x, y, z);
            } else if (metadata == 2) {
               block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               renderer.setRenderBoundsFromBlock(block);

               for(int d = 0; d < 6; ++d) {
                  ForgeDirection dir1 = ForgeDirection.getOrientation(d);
                  if (block.shouldSideBeRendered(world, x + dir1.offsetX, y + dir1.offsetY, z + dir1.offsetZ, d)) {
                     switch (d) {
                        case 0:
                           renderer.renderFaceYNeg(block, x, y, z, block.getIcon(world, x, y, z, d));
                           break;
                        case 1:
                           renderer.renderFaceYPos(block, x, y, z, block.getIcon(world, x, y, z, d));
                           break;
                        case 2:
                           renderer.renderFaceZNeg(block, x, y, z, block.getIcon(world, x, y, z, d));
                           break;
                        case 3:
                           renderer.renderFaceZPos(block, x, y, z, block.getIcon(world, x, y, z, d));
                           break;
                        case 4:
                           renderer.renderFaceXNeg(block, x, y, z, block.getIcon(world, x, y, z, d));
                           break;
                        case 5:
                           renderer.renderFaceXPos(block, x, y, z, block.getIcon(world, x, y, z, d));
                     }

                     renderer.flipTexture = false;
                  }
               }
            }

            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockCosmeticOpaqueRI;
   }
}
