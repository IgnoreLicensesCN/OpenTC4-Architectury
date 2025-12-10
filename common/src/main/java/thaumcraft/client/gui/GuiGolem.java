package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.entity.RenderGolemBase;
import thaumcraft.client.renderers.models.entities.ModelGolem;
import thaumcraft.common.container.SlotGhostFluid;
import thaumcraft.common.entities.golems.ContainerGolem;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.ItemGolemCore;
import thaumcraft.common.lib.utils.Utils;

@SideOnly(Side.CLIENT)
public class GuiGolem extends GuiContainer {
   private float xSize_lo;
   private float ySize_lo;
   private final EntityGolemBase golem;
   private int threat = -1;
   RenderLiving rgb = new RenderGolemBase(new ModelGolem(false));
   private static final ModelGolem model = new ModelGolem(true);
   private Slot theSlot;

   public GuiGolem(EntityPlayer player, EntityGolemBase e) {
      super(new ContainerGolem(player.inventory, e.inventory));
      this.golem = e;
      if (this.golem.advanced && this.golem.worldObj.rand.nextInt(4) == 0) {
         this.threat = this.golem.worldObj.rand.nextInt(9);
      }

   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      GL11.glPushMatrix();
      GL11.glScaled(0.5F, 0.5F, 0.5F);
      if (this.threat >= 0) {
         this.fontRendererObj.drawSplitString(StatCollector.translateToLocal("golemthreat." + this.threat + ".text"), 80, 22, 110, 14540253);
      } else {
         this.fontRendererObj.drawSplitString(StatCollector.translateToLocal("golemblurb." + this.golem.getCore() + ".text"), 80, 22, 110, 14540253);
      }

      if (((ContainerGolem)this.inventorySlots).maxScroll > 0) {
         this.fontRendererObj.drawString(((ContainerGolem)this.inventorySlots).currentScroll + 1 + "/" + (((ContainerGolem)this.inventorySlots).maxScroll + 1), 323, 140, 14540253);
      }

      GL11.glPopMatrix();
   }

   public void drawScreen(int par1, int par2, float par3) {
      super.drawScreen(par1, par2, par3);
      this.xSize_lo = (float)par1;
      this.ySize_lo = (float)par2;
      int baseX = this.guiLeft;
      int baseY = this.guiTop;
      int slots = this.golem.inventory.slotCount;
      int typeLoc = this.golem.getGolemType().ordinal() * 24;
      if (this.golem.getCore() > -1 && ItemGolemCore.hasInventory(this.golem.getCore()) && this.golem.getUpgradeAmount(5) > 0) {
         for(int a = 0; a < Math.min(6, slots); ++a) {
            int mposx = par1 - (baseX + 96 + a / 2 * 28);
            int mposy = par2 - (baseY + 4 + a % 2 * 31);
            if (mposx >= 0 && mposy >= 0 && mposx < 24 && mposy < 12) {
               String text = "Any color";
               if (this.golem.getColors(a + ((ContainerGolem)this.inventorySlots).currentScroll * 6) >= 0) {
                  text = Utils.colorNames[this.golem.getColors(a + ((ContainerGolem)this.inventorySlots).currentScroll * 6)];
               }

               int size = this.fontRendererObj.getStringWidth(text);
               this.fontRendererObj.drawString(text, baseX + 133 - size / 2, baseY - 6, 16645629);
            }
         }
      }

   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      int baseX = this.guiLeft;
      int baseY = this.guiTop;
      int var10000 = par2 - (baseX + 139);
      var10000 = par3 - (baseY + 10);
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      UtilsFX.bindTexture("textures/gui/guigolem.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawTexturedModalRect(baseX, baseY, 0, 0, this.xSize, this.ySize);
      int slots = this.golem.inventory.slotCount;
      int typeLoc = this.golem.getGolemType().ordinal() * 24;
      IIcon icon = null;
      if (this.golem.getCore() > -1 && ItemGolemCore.hasInventory(this.golem.getCore())) {
         for(int a = 0; a < Math.min(6, slots); ++a) {
            this.drawTexturedModalRect(baseX + 96 + a / 2 * 28, baseY + 12 + a % 2 * 31, 184, typeLoc, 24, 24);
            if (this.golem.getUpgradeAmount(4) > 0) {
               this.drawTexturedModalRect(baseX + 96 + a / 2 * 28, baseY + 4 + a % 2 * 31, 72, 168, 24, 12);
               int color = this.golem.getColors(a + ((ContainerGolem)this.inventorySlots).currentScroll * 6);
               if (color > -1) {
                  Color c = new Color(Utils.colors[color]);
                  float r = (float)c.getRed() / 255.0F;
                  float g = (float)c.getGreen() / 255.0F;
                  float b = (float)c.getBlue() / 255.0F;
                  GL11.glColor4f(r, g, b, 1.0F);
                  this.drawTexturedModalRect(baseX + 105 + a / 2 * 28, baseY + 7 + a % 2 * 31, 0, 176, 6, 6);
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               }
            }

            if (this.golem.getCore() == 5) {
               FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.golem.inventory.getStackInSlot(a + ((ContainerGolem)this.inventorySlots).currentScroll * 6));
               if (fluid != null) {
                  icon = fluid.getFluid().getIcon();
                  if (icon != null) {
                     GL11.glPushMatrix();
                     GL11.glTranslatef((float)(baseX + 108 + a / 2 * 28), (float)(baseY + 24 + a % 2 * 31), 0.0F);
                     UtilsFX.renderQuadCenteredFromIcon(true, icon, 16.0F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
                     GL11.glPopMatrix();
                     UtilsFX.bindTexture("textures/gui/guigolem.png");
                  }
               }
            }
         }

         if (slots > 6) {
            if (((ContainerGolem)this.inventorySlots).currentScroll > 0) {
               this.drawTexturedModalRect(baseX + 111, baseY + 68, 0, 200, 24, 8);
            } else {
               this.drawTexturedModalRect(baseX + 111, baseY + 68, 0, 208, 24, 8);
            }

            if (((ContainerGolem)this.inventorySlots).currentScroll < ((ContainerGolem)this.inventorySlots).maxScroll) {
               this.drawTexturedModalRect(baseX + 135, baseY + 68, 24, 200, 24, 8);
            } else {
               this.drawTexturedModalRect(baseX + 135, baseY + 68, 24, 208, 24, 8);
            }
         }
      }

      if (this.golem.getCore() == 4 && this.golem.getUpgradeAmount(4) > 0) {
         this.drawTexturedModalRect(baseX + 104, baseY + 5, 8, 168, 8, 8);
         this.drawTexturedModalRect(baseX + 104, baseY + 21, 8, 168, 8, 8);
         this.drawTexturedModalRect(baseX + 104, baseY + 37, 8, 168, 8, 8);
         this.drawTexturedModalRect(baseX + 104, baseY + 53, 8, 168, 8, 8);
         if (this.golem.canAttackHostiles()) {
            this.drawTexturedModalRect(baseX + 104, baseY + 5, 8, 176, 8, 8);
         }

         if (this.golem.canAttackAnimals()) {
            this.drawTexturedModalRect(baseX + 104, baseY + 21, 8, 176, 8, 8);
         }

         if (this.golem.canAttackPlayers()) {
            this.drawTexturedModalRect(baseX + 104, baseY + 37, 8, 176, 8, 8);
         }

         if (this.golem.canAttackCreepers()) {
            this.drawTexturedModalRect(baseX + 104, baseY + 53, 8, 176, 8, 8);
         }

         this.fontRendererObj.drawString("Monsters", baseX + 122, baseY + 6, 16764108);
         this.fontRendererObj.drawString("Animals", baseX + 122, baseY + 22, 16777164);
         this.fontRendererObj.drawString("Players", baseX + 122, baseY + 38, 13421823);
         this.fontRendererObj.drawString("Creepers", baseX + 122, baseY + 54, 13434828);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.golem.getCore() == 0) {
         this.drawTexturedModalRect(baseX + 62, baseY + 54, 8, 168, 8, 8);
         String text = "Precise amount";
         if (!this.golem.getToggles()[0]) {
            this.drawTexturedModalRect(baseX + 62, baseY + 54, 8, 176, 8, 8);
         } else {
            text = "Any amount";
         }

         GL11.glPushMatrix();
         GL11.glTranslatef((float)(baseX + 66), (float)(baseY + 48), 0.0F);
         GL11.glScalef(0.5F, 0.5F, 0.0F);
         int size = this.fontRendererObj.getStringWidth(text);
         this.fontRendererObj.drawString(text, -size / 2, 0, 16645629);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      if (this.golem.getCore() == 8) {
         this.drawTexturedModalRect(baseX + 42, baseY + 40, 8, 168, 8, 8);
         String text1 = "Block";
         if (!this.golem.getToggles()[0]) {
            this.drawTexturedModalRect(baseX + 42, baseY + 40, 8, 176, 8, 8);
         } else {
            text1 = "Empty space";
         }

         this.drawTexturedModalRect(baseX + 42, baseY + 50, 8, 168, 8, 8);
         String text2 = "Right click";
         if (!this.golem.getToggles()[1]) {
            this.drawTexturedModalRect(baseX + 42, baseY + 50, 8, 176, 8, 8);
         } else {
            text2 = "Left click";
         }

         this.drawTexturedModalRect(baseX + 42, baseY + 60, 8, 168, 8, 8);
         String text3 = "Not sneaking";
         if (!this.golem.getToggles()[2]) {
            this.drawTexturedModalRect(baseX + 42, baseY + 60, 8, 176, 8, 8);
         } else {
            text3 = "Sneaking";
         }

         GL11.glPushMatrix();
         GL11.glTranslatef((float)(baseX + 53), (float)(baseY + 42), 0.0F);
         GL11.glScalef(0.5F, 0.5F, 0.0F);
         this.fontRendererObj.drawString(text1, 0, 0, 16645629);
         this.fontRendererObj.drawString(text2, 0, 20, 16645629);
         this.fontRendererObj.drawString(text3, 0, 40, 16645629);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      if (this.golem.getUpgradeAmount(5) > 0 && ItemGolemCore.canSort(this.golem.getCore())) {
         int shiftx = this.golem.getCore() == 10 ? 66 : 180;
         int shifty = this.golem.getCore() == 10 ? 12 : 0;
         this.drawTexturedModalRect(baseX + shiftx, baseY + 24 + shifty, 8, 168, 8, 8);
         String text1 = "Use Ore dictionary";
         if (this.golem.checkOreDict()) {
            this.drawTexturedModalRect(baseX + shiftx, baseY + 24 + shifty, 8, 176, 8, 8);
         }

         this.drawTexturedModalRect(baseX + shiftx, baseY + 34 + shifty, 8, 168, 8, 8);
         String text2 = "Ignore item damage";
         if (this.golem.ignoreDamage()) {
            this.drawTexturedModalRect(baseX + shiftx, baseY + 34 + shifty, 8, 176, 8, 8);
         }

         this.drawTexturedModalRect(baseX + shiftx, baseY + 44 + shifty, 8, 168, 8, 8);
         String text3 = "Ignore NBT values";
         if (this.golem.ignoreNBT()) {
            this.drawTexturedModalRect(baseX + shiftx, baseY + 44 + shifty, 8, 176, 8, 8);
         }

         GL11.glPushMatrix();
         GL11.glTranslatef((float)(baseX + shiftx + 10), (float)(baseY + 26 + shifty), 0.0F);
         GL11.glScalef(0.5F, 0.5F, 0.0F);
         this.fontRendererObj.drawString(text1, 0, 0, this.golem.checkOreDict() ? 16645629 : 6710886);
         this.fontRendererObj.drawString(text2, 0, 20, this.golem.ignoreDamage() ? 16645629 : 6710886);
         this.fontRendererObj.drawString(text3, 0, 40, this.golem.ignoreNBT() ? 16645629 : 6710886);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
      this.drawGolem(this.mc, baseX + 51, baseY + 75, 30, (float)(baseX + 51) - this.xSize_lo, (float)(baseY + 75 - 50) - this.ySize_lo);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void drawGolem(Minecraft mc, int par1, int par2, int par3, float par4, float par5) {
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      ScaledResolution var7 = new ScaledResolution(Minecraft.getMinecraft(), this.mc.displayWidth, this.mc.displayHeight);
      GL11.glViewport((var7.getScaledWidth() - 320) / 2 * var7.getScaleFactor(), (var7.getScaledHeight() - 240) / 2 * var7.getScaleFactor(), 320 * var7.getScaleFactor(), 240 * var7.getScaleFactor());
      GL11.glTranslatef(-0.34F, 0.23F, 0.0F);
      GLU.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
      float var8 = 1.0F;
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      RenderHelper.enableStandardItemLighting();
      GL11.glTranslatef(-1.5F, -1.0F, -12.0F);
      GL11.glScalef(var8, var8, var8);
      float var9 = 5.0F;
      GL11.glScalef(var9, var9, var9);
      float f2 = this.golem.renderYawOffset;
      float f3 = this.golem.rotationYaw;
      float f4 = this.golem.rotationPitch;
      float f5 = this.golem.prevRotationYawHead;
      float f6 = this.golem.rotationYawHead;
      this.golem.renderYawOffset = -20.0F;
      this.golem.rotationYaw = 0.0F;
      this.golem.rotationPitch = 0.0F;
      this.golem.prevRotationYawHead = -5.0F;
      this.golem.rotationYawHead = -5.0F;
      RenderManager.instance.renderEntityWithPosYaw(this.golem, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
      this.golem.renderYawOffset = f2;
      this.golem.rotationYaw = f3;
      this.golem.rotationPitch = f4;
      this.golem.prevRotationYawHead = f5;
      this.golem.rotationYawHead = f6;
      GL11.glDisable(32826);
      RenderHelper.disableStandardItemLighting();
      GL11.glMatrixMode(5889);
      GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      RenderHelper.disableStandardItemLighting();
      GL11.glPopMatrix();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   protected void mouseClicked(int par1, int par2, int par3) {
      super.mouseClicked(par1, par2, par3);
      int baseX = (this.width - this.xSize) / 2;
      int baseY = (this.height - this.ySize) / 2;
      int slots = this.golem.inventory.slotCount;
      int typeLoc = this.golem.getGolemType().ordinal() * 24;
      if (this.golem.getCore() > -1 && ItemGolemCore.hasInventory(this.golem.getCore())) {
         for(int a = 0; a < Math.min(6, slots); ++a) {
            if (this.golem.getUpgradeAmount(4) > 0) {
               int var7 = par1 - (baseX + 96 + a / 2 * 28);
               int var8 = par2 - (baseY + 4 + a % 2 * 31);
               if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 12) {
                  this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, a + ((ContainerGolem)this.inventorySlots).currentScroll * 6);
                  return;
               }

               var7 = par1 - (baseX + 96 + 16 + a / 2 * 28);
               var8 = par2 - (baseY + 4 + a % 2 * 31);
               if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 12) {
                  this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, a + slots + ((ContainerGolem)this.inventorySlots).currentScroll * 6);
                  return;
               }
            }
         }

         if (slots > 6) {
            int var7 = par1 - (baseX + 111);
            int var8 = par2 - (baseY + 68);
            if (var7 >= 0 && var8 >= 0 && var7 < 24 && var8 < 8 && ((ContainerGolem)this.inventorySlots).currentScroll > 0) {
               this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 66);
               --((ContainerGolem)this.inventorySlots).currentScroll;
               ((ContainerGolem)this.inventorySlots).refreshInventory();
               return;
            }

            var7 = par1 - (baseX + 135);
            var8 = par2 - (baseY + 68);
            if (var7 >= 0 && var8 >= 0 && var7 < 24 && var8 < 8 && ((ContainerGolem)this.inventorySlots).currentScroll < ((ContainerGolem)this.inventorySlots).maxScroll) {
               this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 67);
               ++((ContainerGolem)this.inventorySlots).currentScroll;
               ((ContainerGolem)this.inventorySlots).refreshInventory();
               return;
            }
         }
      }

      if (this.golem.getCore() == 4) {
         for(int a = 0; a < 4; ++a) {
            int var7 = par1 - (baseX + 104);
            int var8 = par2 - (baseY + 5 + 16 * a);
            if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 8) {
               this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 51 + a);
               return;
            }
         }
      }

      if (this.golem.getCore() == 0) {
         int var7 = par1 - (baseX + 62);
         int var8 = par2 - (baseY + 54);
         if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 8) {
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 50);
            return;
         }
      }

      if (this.golem.getCore() == 8) {
         int var7 = par1 - (baseX + 42);
         int var8 = par2 - (baseY + 40);
         if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 8) {
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 50);
            return;
         }

         var7 = par1 - (baseX + 42);
         var8 = par2 - (baseY + 50);
         if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 8) {
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 51);
            return;
         }

         var7 = par1 - (baseX + 42);
         var8 = par2 - (baseY + 60);
         if (var7 >= 0 && var8 >= 0 && var7 < 8 && var8 < 8) {
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 52);
            return;
         }
      }

      if (this.golem.getUpgradeAmount(5) > 0 && ItemGolemCore.canSort(this.golem.getCore())) {
         int shiftx = this.golem.getCore() == 10 ? 66 : 180;
         int shifty = this.golem.getCore() == 10 ? 12 : 0;

         for(int a = 0; a < 3; ++a) {
            int var7 = par1 - (baseX + shiftx);
            int var8 = par2 - (baseY + 24 + a * 10 + shifty);
            if (var7 >= 0 && var8 >= 0 && var7 < 64 && var8 < 8) {
               this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 55 + a);
               return;
            }
         }
      }

   }

   protected void renderToolTip(ItemStack par1ItemStack, int par2, int par3) {
      if (this.golem.getCore() == 5 && this.theSlot instanceof SlotGhostFluid) {
         List<String> list = new ArrayList<>();
         FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(par1ItemStack);
         if (fluid != null) {
            list.add(fluid.getFluid().getLocalizedName(fluid));
            FontRenderer font = par1ItemStack.getItem().getFontRenderer(par1ItemStack);
            this.drawHoveringText(list, par2, par3, font == null ? this.fontRendererObj : font);
         }
      } else {
         super.renderToolTip(par1ItemStack, par2, par3);
      }

   }
}
