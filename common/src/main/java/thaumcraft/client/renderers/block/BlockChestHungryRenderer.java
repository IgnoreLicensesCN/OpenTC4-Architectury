package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileChestHungry;

public class BlockChestHungryRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      TileEntityRendererDispatcher.instance.renderTileEntityAt(
              new TileChestHungry(),
              0.0F, 0.0F, 0.0F, 0.0F
      );
      GL11.glEnable(GL12.GL_RESCALE_NORMAL);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      return false;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockChestHungryRI;
   }
}
