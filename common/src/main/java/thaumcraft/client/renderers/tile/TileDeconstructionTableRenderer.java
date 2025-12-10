package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelArcaneWorkbench;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileDeconstructionTable;

@SideOnly(Side.CLIENT)
public class TileDeconstructionTableRenderer extends TileEntitySpecialRenderer {
   private ModelArcaneWorkbench tableModel = new ModelArcaneWorkbench();
   ItemStack tm;

   public TileDeconstructionTableRenderer() {
      this.tm = new ItemStack(ConfigItems.itemThaumometer);
   }

   public void renderTileEntityAt(TileDeconstructionTable table, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/decontable.png");
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.tableModel.renderAll();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 0.92F, (float)par6 + 0.5F);
      GL11.glScaled(0.8, 0.8, 0.8);
      EntityItem entityitem = new EntityItem(table.getWorldObj(), 0.0F, 0.0F, 0.0F, this.tm);
      entityitem.hoverStart = 0.0F;
      RenderItem.renderInFrame = true;
      RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      RenderItem.renderInFrame = false;
      GL11.glPopMatrix();
      float ticks = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + par8;
      if (table != null && table.getWorldObj() != null && table.getStackInSlot(0) != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.15F, (float)par6 + 0.5F);
         GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
         ItemStack is = table.getStackInSlot(0).copy();
         is.stackSize = 1;
         entityitem = new EntityItem(table.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
         entityitem.hoverStart = MathHelper.sin(ticks / 14.0F) * 0.2F + 0.2F;
         RenderItem.renderInFrame = true;
         RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         RenderItem.renderInFrame = false;
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
      }

      if (table != null && table.getWorldObj() != null && table.aspect != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.081F, (float)par6 + 0.5F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(ticks % 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glScaled(0.024, 0.024, 0.024);
         UtilsFX.drawTag(-8, -8, table.aspect, 0.0F, 0, 0.0F, 1, 0.8F, false);
         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileDeconstructionTable)tileEntity, par2, par4, par6, par8);
   }
}
