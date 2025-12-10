package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerSpa;
import thaumcraft.common.tiles.TileSpa;

@SideOnly(Side.CLIENT)
public class GuiSpa extends GuiContainer {
   private TileSpa spa;
   private float xSize_lo;
   private float ySize_lo;

   public GuiSpa(InventoryPlayer par1InventoryPlayer, TileSpa teSpa) {
      super(new ContainerSpa(par1InventoryPlayer, teSpa));
      this.spa = teSpa;
   }

   public void drawScreen(int par1, int par2, float par3) {
      super.drawScreen(par1, par2, par3);
      this.xSize_lo = (float)par1;
      this.ySize_lo = (float)par2;
      int baseX = this.guiLeft;
      int baseY = this.guiTop;
      int mposx = par1 - (baseX + 104);
      int mposy = par2 - (baseY + 10);
      if (mposx >= 0 && mposy >= 0 && mposx < 10 && mposy < 55) {
         List list = new ArrayList<>();
         FluidStack fluid = this.spa.tank.getFluid();
         if (fluid != null) {
            list.add(fluid.getFluid().getLocalizedName(fluid));
            list.add(fluid.amount + " mb");
            this.drawHoveringText(list, par1, par2, this.fontRendererObj);
         }
      }

      mposx = par1 - (baseX + 88);
      mposy = par2 - (baseY + 34);
      if (mposx >= 0 && mposy >= 0 && mposx < 10 && mposy < 10) {
         List list = new ArrayList<>();
         if (this.spa.getMix()) {
            list.add(StatCollector.translateToLocal("text.spa.mix.true"));
         } else {
            list.add(StatCollector.translateToLocal("text.spa.mix.false"));
         }

         this.drawHoveringText(list, par1, par2, this.fontRendererObj);
      }

   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/gui/gui_spa.png");
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
      if (this.spa.getMix()) {
         this.drawTexturedModalRect(k + 89, l + 35, 208, 16, 8, 8);
      } else {
         this.drawTexturedModalRect(k + 89, l + 35, 208, 32, 8, 8);
      }

      if (this.spa.tank.getFluidAmount() > 0) {
         FluidStack fluid = this.spa.tank.getFluid();
         if (fluid != null) {
            IIcon icon = fluid.getFluid().getIcon();
            if (icon != null) {
               float bar = (float)this.spa.tank.getFluidAmount() / (float)this.spa.tank.getCapacity();
               GL11.glPushMatrix();
               GL11.glTranslatef((float)(this.guiLeft + 107), (float)(this.guiTop + 15), 0.0F);
               renderFluid(icon);
               GL11.glPopMatrix();
               UtilsFX.bindTexture("textures/gui/gui_spa.png");
               this.drawTexturedModalRect(k + 107, l + 15, 107, 15, 10, (int)(48.0F - 48.0F * bar));
            }
         }
      }

      this.drawTexturedModalRect(k + 106, l + 11, 232, 0, 10, 55);
      GL11.glDisable(GL11.GL_BLEND);
   }

   public static void renderFluid(IIcon icon) {
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      Tessellator tessellator = Tessellator.instance;
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMinU();
      float f4 = icon.getMaxV();
      GL11.glScalef(8.0F, 8.0F, 8.0F);

      for(int a = 0; a < 6; ++a) {
         tessellator.startDrawingQuads();
         tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
         tessellator.setNormal(0.0F, 0.0F, 1.0F);
         tessellator.addVertexWithUV(0.0F, 1 + a, 0.0F, f1, f4);
         tessellator.addVertexWithUV(1.0F, 1 + a, 0.0F, f3, f4);
         tessellator.addVertexWithUV(1.0F, a, 0.0F, f3, f2);
         tessellator.addVertexWithUV(0.0F, a, 0.0F, f1, f2);
         tessellator.draw();
      }

   }

   protected void mouseClicked(int mx, int my, int par3) {
      super.mouseClicked(mx, my, par3);
      int gx = (this.width - this.xSize) / 2;
      int gy = (this.height - this.ySize) / 2;
      int var7 = mx - (gx + 89);
      int var8 = my - (gy + 35);
      if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 8) {
         this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 1);
         this.playButtonClick();
      }
   }

   private void playButtonClick() {
      this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "thaumcraft:cameraclack", 0.4F, 1.0F, false);
   }
}
