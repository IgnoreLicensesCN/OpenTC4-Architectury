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
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelArcaneWorkbench;
import thaumcraft.common.tiles.TileFocalManipulator;

@SideOnly(Side.CLIENT)
public class TileFocalManipulatorRenderer extends TileEntitySpecialRenderer {
   private ModelArcaneWorkbench tableModel = new ModelArcaneWorkbench();
   EntityItem entityitem = null;

   public void renderTileEntityAt(TileFocalManipulator table, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/wandtable.png");
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.tableModel.renderAll();
      GL11.glPopMatrix();
      if (table.getWorldObj() != null && table.getStackInSlot(0) != null && table.getStackInSlot(0).getItem() instanceof ItemFocusBasic) {
         float ticks = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + par8;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
         GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
         ItemStack is = table.getStackInSlot(0).copy();
         this.entityitem = new EntityItem(table.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
         this.entityitem.hoverStart = MathHelper.sin(ticks / 14.0F) * 0.2F + 0.2F;
         RenderItem.renderInFrame = true;
         RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         RenderItem.renderInFrame = false;
         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileFocalManipulator)tileEntity, par2, par4, par6, par8);
   }
}
