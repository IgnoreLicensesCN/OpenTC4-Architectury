package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.blocks.BlockArcaneFurnace;
import thaumcraft.common.config.ConfigBlocks;

public class BlockArcaneFurnaceRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      setBrightness(world, x, y, z, block);
      int md = world.getBlockMetadata(x, y, z);
      if (md <= 9) {
         if (md == 0) {
            setBrightness(world, x, y, z, block);
            renderer.overrideBlockTexture = Blocks.lava.getBlockTextureFromSide(0);
         }

         block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         renderer.setRenderBoundsFromBlock(block);
         renderer.renderStandardBlock(block, x, y, z);
      } else if (md == 10) {
         if (world.getBlock(x - 1, y, z) == block && world.getBlockMetadata(x - 1, y, z) == 0) {
            renderer.renderFaceXPos(block, (float)x - W10, y, z, ((BlockArcaneFurnace)block).icon[13]);
            renderer.renderFaceXPos(block, (float)x - 0.8F, y, z, ((BlockArcaneFurnace)block).icon[15]);
            setBrightness(world, x, y, z, block);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceXPos(block, (float)x - 0.9F, y, z, Blocks.fire.getBlockTextureFromSide(0));
         } else if (world.getBlock(x + 1, y, z) == block && world.getBlockMetadata(x + 1, y, z) == 0) {
            renderer.renderFaceXNeg(block, (float)x + W10, y, z, ((BlockArcaneFurnace)block).icon[13]);
            renderer.renderFaceXNeg(block, (float)x + 0.8F, y, z, ((BlockArcaneFurnace)block).icon[15]);
            setBrightness(world, x, y, z, block);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceXNeg(block, (float)x + 0.9F, y, z, Blocks.fire.getBlockTextureFromSide(0));
         } else if (world.getBlock(x, y, z - 1) == block && world.getBlockMetadata(x, y, z - 1) == 0) {
            renderer.renderFaceZPos(block, x, y, (float)z - W10, ((BlockArcaneFurnace)block).icon[13]);
            renderer.renderFaceZPos(block, x, y, (float)z - 0.8F, ((BlockArcaneFurnace)block).icon[15]);
            setBrightness(world, x, y, z, block);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceZPos(block, x, y, (float)z - 0.9F, Blocks.fire.getBlockTextureFromSide(0));
         } else {
            renderer.renderFaceZNeg(block, x, y, (float)z + W10, ((BlockArcaneFurnace)block).icon[13]);
            renderer.renderFaceZNeg(block, x, y, (float)z + 0.8F, ((BlockArcaneFurnace)block).icon[15]);
            setBrightness(world, x, y, z, block);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceZNeg(block, x, y, (float)z + 0.9F, Blocks.fire.getBlockTextureFromSide(0));
         }
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
      return ConfigBlocks.blockArcaneFurnaceRI;
   }
}
