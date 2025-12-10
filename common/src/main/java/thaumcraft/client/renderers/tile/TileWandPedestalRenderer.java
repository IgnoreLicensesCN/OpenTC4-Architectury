package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileWandPedestal;

@SideOnly(Side.CLIENT)
public class TileWandPedestalRenderer extends TileEntitySpecialRenderer {
   public void renderTileEntityAt(TileWandPedestal ped, double par2, double par4, double par6, float partialTicks) {
      if (ped != null && ped.getWorldObj() != null && ped.getStackInSlot(0) != null) {
         EntityItem entityitem = null;
         float ticks = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;
         GL11.glPushMatrix();
         float h = MathHelper.sin(ticks % 32767.0F / 16.0F) * 0.05F;
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.15F + h, (float)par6 + 0.5F);
         GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
         ItemStack is = ped.getStackInSlot(0).copy();
         is.stackSize = 1;
         entityitem = new EntityItem(ped.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
         entityitem.hoverStart = 0.0F;
         RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glPopMatrix();
         if (ped.draining) {
            GL11.glPushMatrix();
            UtilsFX.drawFloatyLine((double)ped.xCoord + (double)0.5F, (double)ped.yCoord + 1.65 - (double)(h * 2.0F), (double)ped.zCoord + (double)0.5F, (double)ped.drainX + (double)0.5F, (double)ped.drainY + (double)0.5F, (double)ped.drainZ + (double)0.5F, partialTicks, ped.drainColor, "textures/misc/wispy.png", -0.02F, Math.min(ticks, 10.0F) / 10.0F);
            GL11.glPopMatrix();
         }
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileWandPedestal)tileEntity, par2, par4, par6, par8);
   }
}
