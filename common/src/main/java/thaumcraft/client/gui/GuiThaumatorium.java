package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerThaumatorium;
import thaumcraft.common.tiles.TileThaumatorium;

@SideOnly(Side.CLIENT)
public class GuiThaumatorium extends GuiContainer {
   private TileThaumatorium inventory;
   private ContainerThaumatorium container = null;
   private int index = 0;
   private int lastSize = 0;
   private EntityPlayer player = null;
   int startAspect = 0;

   public GuiThaumatorium(InventoryPlayer par1InventoryPlayer, TileThaumatorium par2TileEntityFurnace) {
      super(new ContainerThaumatorium(par1InventoryPlayer, par2TileEntityFurnace));
      this.inventory = par2TileEntityFurnace;
      this.container = (ContainerThaumatorium)this.inventorySlots;
      this.container.updateRecipes();
      this.lastSize = this.container.recipes.size();
      this.player = par1InventoryPlayer.player;
      this.refreshIndex();
   }

   void refreshIndex() {
      if (this.inventory.recipeHash != null && !this.container.recipes.isEmpty()) {
         for(int a = 0; a < this.container.recipes.size(); ++a) {
            if (this.inventory.recipeHash.contains(this.container.recipes.get(a).hash)) {
               this.index = a;
               break;
            }
         }
      }

      this.startAspect = 0;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int mx, int my) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/gui/gui_thaumatorium.png");
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      GL11.glEnable(GL11.GL_BLEND);
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
      if (this.index >= this.container.recipes.size()) {
         this.index = this.container.recipes.size() - 1;
      }

      if (!this.container.recipes.isEmpty()) {
         if (this.lastSize != this.container.recipes.size()) {
            this.lastSize = this.container.recipes.size();
            this.refreshIndex();
         }

         if (this.index < 0) {
            this.index = 0;
         }

         if (this.container.recipes.size() > 1) {
            if (this.index > 0) {
               this.drawTexturedModalRect(k + 128, l + 16, 192, 16, 16, 8);
            } else {
               this.drawTexturedModalRect(k + 128, l + 16, 176, 16, 16, 8);
            }

            if (this.index < this.container.recipes.size() - 1) {
               this.drawTexturedModalRect(k + 128, l + 24, 192, 24, 16, 8);
            } else {
               this.drawTexturedModalRect(k + 128, l + 24, 176, 24, 16, 8);
            }
         }

         if (this.container.recipes.get(this.index).aspects.size() > 6) {
            if (this.startAspect > 0) {
               this.drawTexturedModalRect(k + 32, l + 40, 192, 32, 8, 16);
            } else {
               this.drawTexturedModalRect(k + 32, l + 40, 176, 32, 8, 16);
            }

            if (this.startAspect < this.container.recipes.get(this.index).aspects.size() - 1) {
               this.drawTexturedModalRect(k + 136, l + 40, 200, 32, 8, 16);
            } else {
               this.drawTexturedModalRect(k + 136, l + 40, 184, 32, 8, 16);
            }
         } else {
            this.startAspect = 0;
         }

         if (this.inventory.recipeHash != null && !this.inventory.recipeHash.isEmpty()) {
            int x = mx - (k + 112);
            int y = my - (l + 16);
            if (x >= 0 && y >= 0 && x < 16 && y < 16 || this.inventory.recipeHash.contains(this.container.recipes.get(this.index).hash)) {
               GL11.glPushMatrix();
               GL11.glEnable(GL11.GL_BLEND);
               this.drawTexturedModalRect(k + 104, l + 8, 176, 96, 48, 48);
               GL11.glDisable(GL11.GL_BLEND);
               GL11.glPopMatrix();
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            float alpha = 0.6F + MathHelper.sin((float)this.mc.thePlayer.ticksExisted / 5.0F) * 0.4F + 0.4F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            this.drawTexturedModalRect(k + 88, l + 16, 176, 56, 24, 24);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
         }

         this.drawAspects(k, l);
         this.drawOutput(k, l, mx, my);
         if (this.inventory.maxRecipes > 1) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(k + 136), (float)(l + 33), 0.0F);
            GL11.glScalef(0.5F, 0.5F, 0.0F);
            String text = this.inventory.recipeHash.size() + "/" + this.inventory.maxRecipes;
            int ll = this.fontRendererObj.getStringWidth(text) / 2;
            this.fontRendererObj.drawString(text, -ll, 0, 16777215);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }

      GL11.glDisable(GL11.GL_BLEND);
   }

   private void drawAspects(int k, int l) {
      int count = 0;
      int pos = 0;
      if (this.inventory.recipeHash.contains(this.container.recipes.get(this.index).hash)) {
         for(Aspect aspect : this.container.recipes.get(this.index).aspects.getAspectsSorted()) {
            if (count >= this.startAspect) {
               GL11.glPushMatrix();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(k + 41 + 16 * pos, l + 57, 176, 8, 14, 6);
               int i1 = (int)((float)this.inventory.essentia.getAmount(aspect) / (float) this.container.recipes.get(this.index).aspects.getAmount(aspect) * 12.0F);
               Color c = new Color(aspect.getColor());
               GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
               this.drawTexturedModalRect(k + 42 + 16 * pos, l + 58, 176, 0, i1, 4);
               GL11.glPopMatrix();
               ++pos;
            }

            ++count;
            if (count >= 6 + this.startAspect) {
               break;
            }
         }
      }

      count = 0;
      pos = 0;

      for(Aspect aspect : this.container.recipes.get(this.index).aspects.getAspectsSorted()) {
         if (count >= this.startAspect) {
            UtilsFX.drawTag(k + 40 + 16 * pos, l + 40, aspect, (float) this.container.recipes.get(this.index).aspects.getAmount(aspect), 0, this.zLevel);
            ++pos;
         }

         ++count;
         if (count >= 6 + this.startAspect) {
            break;
         }
      }

   }

   private void drawOutput(int x, int y, int mx, int my) {
      GL11.glPushMatrix();
      boolean dull = false;
      if (this.inventory.recipeHash.size() < this.inventory.maxRecipes || this.inventory.recipeHash.contains(this.container.recipes.get(this.index).hash)) {
         dull = true;
         float alpha = 0.3F + MathHelper.sin((float)this.mc.thePlayer.ticksExisted / 4.0F) * 0.3F + 0.3F;
         GL11.glColor4f(0.5F, 0.5F, 0.5F, alpha);
         itemRender.renderWithColor = false;
      }

      GL11.glEnable(2896);
      GL11.glEnable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      itemRender.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, this.container.recipes.get(this.index).getRecipeOutput(), x + 112, y + 16);
      itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, this.container.recipes.get(this.index).getRecipeOutput(), x + 112, y + 16);
      int xx = mx - (x + 112);
      int yy = my - (y + 16);
      if (xx >= 0 && yy >= 0 && xx < 16 && yy < 16) {
         this.renderToolTip(this.container.recipes.get(this.index).getRecipeOutput(), mx, my);
      }

      if (dull) {
         itemRender.renderWithColor = true;
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(2896);
      GL11.glPopMatrix();
   }

   protected void mouseClicked(int mx, int my, int par3) {
      super.mouseClicked(mx, my, par3);
      int gx = (this.width - this.xSize) / 2;
      int gy = (this.height - this.ySize) / 2;
      int x = mx - (gx + 112);
      int y = my - (gy + 16);
      if (!this.container.recipes.isEmpty() && this.index >= 0 && this.index < this.container.recipes.size()) {
         if (x >= 0 && y >= 0 && x < 16 && y < 16 && (this.inventory.recipeHash.size() < this.inventory.maxRecipes || this.inventory.recipeHash.contains(this.container.recipes.get(this.index).hash))) {
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, this.index);
            this.playButtonSelect();
         }

         if (this.container.recipes.size() > 1) {
            if (this.index > 0) {
               x = mx - (gx + 128);
               y = my - (gy + 16);
               if (x >= 0 && y >= 0 && x < 16 && y < 8) {
                  --this.index;
                  this.playButtonClick();
               }
            }

            if (this.index < this.container.recipes.size() - 1) {
               x = mx - (gx + 128);
               y = my - (gy + 24);
               if (x >= 0 && y >= 0 && x < 16 && y < 8) {
                  ++this.index;
                  this.playButtonClick();
               }
            }
         }

         if (this.container.recipes.get(this.index).aspects.size() > 6) {
            if (this.startAspect > 0) {
               x = mx - (gx + 32);
               y = my - (gy + 40);
               if (x >= 0 && y >= 0 && x < 8 && y < 16) {
                  --this.startAspect;
                  this.playButtonClick();
               }
            }

            if (this.startAspect < this.container.recipes.get(this.index).aspects.size() - 1) {
               x = mx - (gx + 136);
               y = my - (gy + 40);
               if (x >= 0 && y >= 0 && x < 8 && y < 16) {
                  ++this.startAspect;
                  this.playButtonClick();
               }
            }
         }
      }

   }

   private void playButtonSelect() {
      this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "thaumcraft:hhon", 0.3F, 1.0F, false);
   }

   private void playButtonClick() {
      this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "thaumcraft:cameraclack", 0.4F, 1.0F, false);
   }
}
