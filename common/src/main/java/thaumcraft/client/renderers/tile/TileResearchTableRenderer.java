package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.IScribeTools;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelResearchTable;
import thaumcraft.common.blocks.BlockTable;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.tiles.TileResearchTable;

@SideOnly(Side.CLIENT)
public class TileResearchTableRenderer extends TileEntitySpecialRenderer {
   private ModelResearchTable tableModel = new ModelResearchTable();

   public void renderTileEntityAt(TileResearchTable table, double par2, double par4, double par6, float par8) {
      int md = 0;
      if (table.getWorldObj() != null) {
         md = table.getBlockMetadata();
      }

      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/restable.png");
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      switch (md) {
         case 2:
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 3:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 4:
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      }

      this.tableModel.renderAll();
      if (table.getStackInSlot(0) != null && table.getStackInSlot(0).getItem() instanceof IScribeTools) {
         this.tableModel.renderInkwell();
         GL11.glPushMatrix();
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         GL11.glTranslatef(-0.17F, 0.1F, -0.15F);
         GL11.glRotatef(15.0F, 0.0F, 1.0F, 0.0F);
         IIcon icon = ((BlockTable)ConfigBlocks.blockTable).iconQuill;
         float f1 = icon.getMaxU();
         float f2 = icon.getMinV();
         float f3 = icon.getMinU();
         float f4 = icon.getMaxV();
         Tessellator tessellator = Tessellator.instance;
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
         ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.025F);
         GL11.glPopMatrix();
      }

      for(int a = 0; a < 6; ++a) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.1F, -0.01F - (float)a * 0.015F, 0.35F);
         GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
         GL11.glRotatef((float)(15 + a % 3 * 2), 0.0F, 0.0F, 1.0F);
         GL11.glScalef(0.5F, 0.6F, 0.6F);
         UtilsFX.renderQuad("textures/misc/parchment.png", 771, 1.0F);
         GL11.glPopMatrix();
      }

      if (table.getStackInSlot(1) != null && table.getStackInSlot(1).getItem() == ConfigItems.itemResearchNotes) {
         UtilsFX.bindTexture("textures/models/restable2.png");
         ResearchNoteData rd = ResearchManager.getData(table.getStackInSlot(1));
         int color = 10066329;
         if (rd != null) {
            color = rd.color;
         }

         this.tableModel.renderScroll(color);
      }

      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileResearchTable)tileEntity, par2, par4, par6, par8);
   }
}
