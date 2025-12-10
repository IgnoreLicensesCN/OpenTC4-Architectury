package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.utils.InventoryUtils;

@SideOnly(Side.CLIENT)
public class GuiResearchPopup extends Gui {
   private Minecraft theGame;
   private int windowWidth;
   private int windowHeight;
   private ArrayList theResearch = new ArrayList<>();
   private long researchTime;
   private RenderItem itemRender;
   private static final ResourceLocation texture = new ResourceLocation("textures/gui/achievement/achievement_background.png");

   public GuiResearchPopup(Minecraft par1Minecraft) {
      this.theGame = par1Minecraft;
      this.itemRender = new RenderItem();
   }

   public void queueResearchInformation(ResearchItem research) {
      if (this.researchTime == 0L) {
         this.researchTime = Minecraft.getSystemTime();
      }

      this.theResearch.add(research);
      GuiResearchBrowser.lastX = research.displayColumn;
      GuiResearchBrowser.lastY = research.displayRow;
   }

   private void updateResearchWindowScale() {
      GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      this.windowWidth = this.theGame.displayWidth;
      this.windowHeight = this.theGame.displayHeight;
      ScaledResolution var1 = new ScaledResolution(Minecraft.getMinecraft(), this.theGame.displayWidth, this.theGame.displayHeight);
      this.windowWidth = var1.getScaledWidth();
      this.windowHeight = var1.getScaledHeight();
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0F, this.windowWidth, this.windowHeight, 0.0F, 1000.0F, 3000.0F);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
   }

   public void updateResearchWindow() {
      if (!this.theResearch.isEmpty() && this.researchTime != 0L) {
         double var1 = (double)(Minecraft.getSystemTime() - this.researchTime) / (double)3000.0F;
         if (!(var1 < (double)0.0F) && !(var1 > (double)1.0F)) {
            this.updateResearchWindowScale();
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            double var3 = var1 * (double)2.0F;
            if (var3 > (double)1.0F) {
               var3 = (double)2.0F - var3;
            }

            var3 *= 4.0F;
            var3 = (double)1.0F - var3;
            if (var3 < (double)0.0F) {
               var3 = 0.0F;
            }

            var3 *= var3;
            var3 *= var3;
            int var5 = 0;
            int var6 = -(int) (var3 * (double) 36.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553);
            this.theGame.getTextureManager().bindTexture(texture);
            GL11.glDisable(2896);
            this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);
            this.theGame.fontRenderer.drawString("Research Completed!", var5 + 30, var6 + 7, -256);
            int offset = this.theGame.fontRenderer.getStringWidth(((ResearchItem)this.theResearch.get(0)).getName());
            if (offset <= 125) {
               this.theGame.fontRenderer.drawString(((ResearchItem)this.theResearch.get(0)).getName(), var5 + 30, var6 + 18, -1);
            } else {
               float vv = 125.0F / (float)offset;
               GL11.glPushMatrix();
               GL11.glTranslatef((float)(var5 + 30), (float)(var6 + 16) + 2.0F / vv, 0.0F);
               GL11.glScalef(vv, vv, vv);
               this.theGame.fontRenderer.drawString(((ResearchItem)this.theResearch.get(0)).getName(), 0, 0, -1);
               GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            GL11.glEnable(2896);
            if (((ResearchItem)this.theResearch.get(0)).icon_item != null) {
               this.itemRender.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, InventoryUtils.cycleItemStack(((ResearchItem)this.theResearch.get(0)).icon_item), var5 + 8, var6 + 8);
            } else if (((ResearchItem)this.theResearch.get(0)).icon_resource != null) {
               Minecraft.getMinecraft().renderEngine.bindTexture(((ResearchItem)this.theResearch.get(0)).icon_resource);
               UtilsFX.drawTexturedQuadFull(var5 + 8, var6 + 8, this.zLevel);
            }

            GL11.glDisable(2896);
         } else {
            this.theResearch.remove(0);
            if (!this.theResearch.isEmpty()) {
               this.researchTime = Minecraft.getSystemTime();
            } else {
               this.researchTime = 0L;
            }
         }
      }

   }
}
