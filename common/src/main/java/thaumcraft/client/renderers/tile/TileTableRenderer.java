package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelTable;
import thaumcraft.common.tiles.TileTable;

@SideOnly(Side.CLIENT)
public class TileTableRenderer extends TileEntitySpecialRenderer {
   private ModelTable tableModel = new ModelTable();

   public void renderTileEntityAt(TileTable table, double par2, double par4, double par6, float par8) {
      int md = 0;
      if (table.getWorldObj() != null) {
         md = table.getBlockMetadata();
      }

      if (md < 6) {
         GL11.glPushMatrix();
         UtilsFX.bindTexture("textures/models/table.png");
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         if (md == 1) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.tableModel.renderAll();
         GL11.glPopMatrix();
      }
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileTable)tileEntity, par2, par4, par6, par8);
   }
}
