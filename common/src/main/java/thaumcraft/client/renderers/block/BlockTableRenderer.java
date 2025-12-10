package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileDeconstructionTable;
import thaumcraft.common.tiles.TileResearchTable;
import thaumcraft.common.tiles.TileTable;

public class BlockTableRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {
   TileResearchTable trt = new TileResearchTable();

   public BlockTableRenderer() {
      this.trt.contents[0] = new ItemStack(ConfigItems.itemInkwell);
      this.trt.contents[1] = new ItemStack(ConfigItems.itemResearchNotes);
   }

   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
      GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      if (metadata == 0) {
         TileEntityRendererDispatcher.instance.renderTileEntityAt(
                 new TileTable(),
                 0.0F, 0.0F, 0.0F, 0.0F
         );
      } else if (metadata == 14) {
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileDeconstructionTable(), 0.0F, 0.0F, 0.0F, 0.0F);
      } else if (metadata == 15) {
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileArcaneWorkbench(), 0.0F, 0.0F, 0.0F, 0.0F);
      } else {
         GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
         TileEntityRendererDispatcher.instance.renderTileEntityAt(this.trt, 0.0F, 0.0F, 0.0F, 0.0F);
      }

      GL11.glEnable(GL12.GL_RESCALE_NORMAL);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      return false;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return ConfigBlocks.blockTableRI;
   }
}
