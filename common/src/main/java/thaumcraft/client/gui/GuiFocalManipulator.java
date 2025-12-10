package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerFocalManipulator;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileFocalManipulator;

@SideOnly(Side.CLIENT)
public class GuiFocalManipulator extends GuiContainer {
   private TileFocalManipulator table;
   private float xSize_lo;
   private float ySize_lo;
   int selected = -1;
   int rank = 0;
   long time;
   long nextSparkle = 0L;
   DecimalFormat myFormatter = new DecimalFormat("#######.#");
   ArrayList<FocusUpgradeType> possibleUpgrades = new ArrayList<>();
   ArrayList<FocusUpgradeType> upgrades = new ArrayList<>();
   AspectList aspects = new AspectList();
   HashMap<Long,Sparkle> sparkles = new HashMap<>();

   public GuiFocalManipulator(InventoryPlayer par1InventoryPlayer, TileFocalManipulator table) {
      super(new ContainerFocalManipulator(par1InventoryPlayer, table));
      this.table = table;
      this.xSize = 192;
      this.ySize = 233;
      if (table.size > 0) {
         this.gatherInfo();
         this.selected = table.upgrade;
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
      super.drawScreen(par1, par2, par3);
      this.xSize_lo = (float)par1;
      this.ySize_lo = (float)par2;
      int baseX = this.guiLeft;
      int baseY = this.guiTop;
      int mposx = 0;
      int mposy = 0;
      if (this.rank > 0) {
         for(int a = 0; a < this.possibleUpgrades.size(); ++a) {
            mposx = par1 - (baseX + 48 + a * 16);
            mposy = par2 - (baseY + 104);
            if (mposx >= 0 && mposy >= 0 && mposx < 16 && mposy < 16) {
               FocusUpgradeType u = this.possibleUpgrades.get(a);
               List list = new ArrayList<>();
               list.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.UNDERLINE + u.getLocalizedName());
               list.add(u.getLocalizedText());
               this.drawHoveringTextFixed(list, baseX + this.xSize - 36, baseY + 24, this.fontRendererObj, this.width - (baseX + this.xSize - 16));
            }
         }
      }

      if (this.selected >= 0) {
         mposx = par1 - (baseX + 48);
         mposy = par2 - (baseY + 48);
         if (mposx >= 0 && mposy >= 0 && mposx < 36 && mposy < 36) {
            List list = new ArrayList<>();
            list.add(StatCollector.translateToLocal("wandtable.text1"));
            this.drawHoveringText(list, par1, par2, this.fontRendererObj);
         }

         mposx = par1 - (baseX + 108);
         mposy = par2 - (baseY + 58);
         if (mposx >= 0 && mposy >= 0 && mposx < 36 && mposy < 16) {
            List list = new ArrayList<>();
            list.add(StatCollector.translateToLocal("wandtable.text2"));
            this.drawHoveringText(list, par1, par2, this.fontRendererObj);
         }

         if (this.table.size == 0 && this.rank * 8 <= this.mc.thePlayer.experienceLevel) {
            mposx = par1 - (baseX + 48);
            mposy = par2 - (baseY + 88);
            if (mposx >= 0 && mposy >= 0 && mposx < 96 && mposy < 8) {
               List list = new ArrayList<>();
               list.add(StatCollector.translateToLocal("wandtable.text3"));
               this.drawHoveringText(list, par1, par2, this.fontRendererObj);
            }
         }
      }

      for(int a = 0; a < this.upgrades.size(); ++a) {
         mposx = par1 - (baseX + 56 + a * 16);
         mposy = par2 - (baseY + 32);
         if (mposx >= 0 && mposy >= 0 && mposx < 16 && mposy < 16) {
            FocusUpgradeType u = this.upgrades.get(a);
            List list = new ArrayList<>();
            list.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.UNDERLINE + u.getLocalizedName());
            list.add(u.getLocalizedText());
            this.drawHoveringTextFixed(list, baseX + this.xSize - 36, baseY + 24, this.fontRendererObj, this.width - (baseX + this.xSize - 16));
         }
      }

   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      this.time = System.currentTimeMillis();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/gui/gui_wandtable.png");
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
      if (this.table.getStackInSlot(0) == null || this.table.rank < 0 || this.table.reset) {
         this.rank = 0;
         this.selected = -1;
         this.possibleUpgrades.clear();
         this.upgrades.clear();
         this.aspects = new AspectList();
         this.table.reset = false;
         this.table.rank = 0;
      }

      if (this.rank > 0) {
         for(int a = 0; a < this.possibleUpgrades.size(); ++a) {
            FocusUpgradeType u = this.possibleUpgrades.get(a);
            if (this.selected == u.id) {
               this.drawTexturedModalRect(k + 48 + a * 16, l + 104, 200, 0, 16, 16);
            }
         }
      }

      if (this.rank > 0 && this.selected >= 0 && this.table.getStackInSlot(0) != null) {
         int xp = this.rank * 8;
         if (this.table.size == 0 && xp <= this.mc.thePlayer.experienceLevel) {
            this.drawTexturedModalRect(k + 48, l + 88, 8, 240, 96, 8);
         }

         this.drawTexturedModalRect(k + 108, l + 59, 200, 16, 16, 16);
         int start = 0;
         if (this.table.aspects.size() > 0) {
            for(Aspect aspect : this.table.aspects.getAspectsSorted()) {
               if (aspect != null && this.table.aspects.getAmount(aspect) != 0) {
                  int size = (int)((float)this.table.aspects.getAmount(aspect) / (float)this.table.size * 96.0F);
                  Color c = new Color(aspect.getColor());
                  GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 0.9F);
                  this.drawTexturedModalRect(k + 48 + start, l + 88, 112 + start, 240, size, 8);
                  start += size;
                  if (this.table.getWorldObj().rand.nextInt(66) == 0) {
                     float x = (float)(48 + start);
                     float y = 92.0F;
                     float xx = ((float)(46 + this.rank * 16) - x) / 9.0F;
                     float yy = (38.0F - y) / 9.0F;
                     this.sparkles.put(this.time, new Sparkle(x, y, xx, yy, (float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F));
                  }
               }
            }
         }

         this.fontRendererObj.drawStringWithShadow("" + xp, k + 125, l + 64, xp > this.mc.thePlayer.experienceLevel ? 16151160 : 10092429);
         AspectList al = this.aspects;
         if (this.table.size > 0) {
            al = this.table.aspects;
         }

         int q = 0;

         for(Aspect a : al.getAspectsSorted()) {
            if (a != null) {
               GL11.glPushMatrix();
               GL11.glTranslated(k + 49, (double)(l + 68) - (double)al.size() * (double)2.5F, 0.0F);
               GL11.glScaled(0.5F, 0.5F, 0.5F);
               this.fontRendererObj.drawStringWithShadow(a.getName(), 0, q * 10, a.getColor());
               String s = this.myFormatter.format((float)al.getAmount(a) / 100.0F);
               this.fontRendererObj.drawStringWithShadow(s, 48, q * 10, a.getColor());
               GL11.glPopMatrix();
               ++q;
            }
         }
      }

      if (this.rank > 0) {
         if (this.nextSparkle < this.time) {
            this.nextSparkle = this.time + (long)(this.table.size > 0 ? 10 : 500) + (long)this.table.getWorldObj().rand.nextInt(200);
            this.sparkles.put(this.time, new Sparkle((float) (42 + this.rank * 16 + this.table.getWorldObj().rand.nextInt(12)), (float) (34 + this.table.getWorldObj().rand.nextInt(12)), 0.0F, 0.0F, 0.5F + this.table.getWorldObj().rand.nextFloat() * 0.4F, 1.0F - this.table.getWorldObj().rand.nextFloat() * 0.4F, 1.0F - this.table.getWorldObj().rand.nextFloat() * 0.4F));
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

         for(int a = 0; a < this.possibleUpgrades.size(); ++a) {
            FocusUpgradeType u = this.possibleUpgrades.get(a);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
            this.mc.renderEngine.bindTexture(u.icon);
            UtilsFX.drawTexturedQuadFull(k + 48 + a * 16, l + 104, this.zLevel);
            GL11.glPopMatrix();
         }
      } else if (this.rank == 0 && this.table.getStackInSlot(0) != null) {
         try {
            this.gatherInfo();
         } catch (Exception ignored) {
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      for(int a = 0; a < this.upgrades.size(); ++a) {
         FocusUpgradeType u = this.upgrades.get(a);
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         this.mc.renderEngine.bindTexture(u.icon);
         UtilsFX.drawTexturedQuadFull(k + 56 + a * 16, l + 32, this.zLevel);
         GL11.glPopMatrix();
      }

      GL11.glDisable(GL11.GL_BLEND);
   }

   private void gatherInfo() {
      this.possibleUpgrades.clear();
      this.upgrades.clear();
      this.aspects = new AspectList();
      ItemFocusBasic focus = (ItemFocusBasic)this.table.getStackInSlot(0).getItem();
      short[] s = focus.getAppliedUpgrades(this.table.getStackInSlot(0));
      this.rank = 1;

      int fu;
      for(fu = 0; this.rank <= 5 && s[this.rank - 1] != -1; ++this.rank) {
         this.upgrades.add(FocusUpgradeType.types[s[this.rank - 1]]);
         ++fu;
      }

      if (fu == 5) {
         this.rank = -1;
      } else {
         FocusUpgradeType[] ut = focus.getPossibleUpgradesByRank(this.table.getStackInSlot(0), this.rank);
         if (ut == null) {
            return;
         }

          for (FocusUpgradeType focusUpgradeType : ut) {
              if (focus.canApplyUpgrade(this.table.getStackInSlot(0), Minecraft.getMinecraft().thePlayer, focusUpgradeType, this.rank)) {
                  this.possibleUpgrades.add(focusUpgradeType);
              }
          }
      }

      if (this.table.size > 0) {
         this.selected = this.table.upgrade;
      }

   }

   protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
      UtilsFX.bindTexture(ParticleEngine.particleTexture);
      Long[] keys = this.sparkles.keySet().toArray(new Long[0]);

      for(Long key : keys) {
         Sparkle s = this.sparkles.get(key);
         this.drawSparkle(s.x, s.y, s.frame, s.r, s.g, s.b);
         if (s.nextframe < this.time) {
            ++s.frame;
            s.nextframe = this.time + 50L;
            s.x += s.mx;
            s.y += s.my;
         }

         if (s.frame == 9) {
            this.sparkles.remove(key);
         } else {
            this.sparkles.put(key, s);
         }
      }

   }

   protected void mouseClicked(int mx, int my, int par3) {
      super.mouseClicked(mx, my, par3);
      int gx = (this.width - this.xSize) / 2;
      int gy = (this.height - this.ySize) / 2;
      int var7 = mx - (gx + 48);
      int var8 = my - (gy + 88);
      if (this.table.size == 0 && this.selected >= 0 && this.rank * 8 <= this.mc.thePlayer.experienceLevel && var7 >= 0 && var8 >= 0 && var7 < 96 && var8 < 8) {
         this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, this.selected);
         this.playButtonClick();
      } else {
         if (this.table.size == 0) {
            for(int a = 0; a < this.possibleUpgrades.size(); ++a) {
               FocusUpgradeType u = this.possibleUpgrades.get(a);
               var7 = mx - (gx + 48 + a * 16);
               var8 = my - (gy + 104);
               if (var7 >= 0 && var8 >= 0 && var7 < 16 && var8 < 16) {
                  this.aspects = new AspectList();
                  if (this.selected == u.id) {
                     this.selected = -1;
                  } else {
                     this.selected = u.id;
                     int amt = 200;

                     for(int q = 1; q < this.rank; ++q) {
                        amt *= 2;
                     }

                     AspectList tal = new AspectList();

                     for(Aspect as : FocusUpgradeType.types[this.selected].aspects.getAspects()) {
                        tal.add(as, amt);
                     }

                     this.aspects = ResearchManager.reduceToPrimals(tal);
                  }

                  this.playButtonClick();
                  return;
               }
            }
         }

      }
   }

   private void playButtonClick() {
      this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "thaumcraft:cameraclack", 0.4F, 1.0F, false);
   }

   private void drawSparkle(double x, double y, int frame, float r, float g, float b) {
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glColor4f(r, g, b, 0.9F);
      GL11.glTranslated(x, y, 200.0F);
      Tessellator tessellator = Tessellator.instance;
      float var8 = (float)frame / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.4375F;
      float var11 = var10 + 0.0624375F;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(220);
      tessellator.setColorRGBA_F(r, g, b, 0.9F);
      tessellator.addVertexWithUV(-4.0F, 4.0F, this.zLevel, var9, var11);
      tessellator.addVertexWithUV(4.0F, 4.0F, this.zLevel, var9, var10);
      tessellator.addVertexWithUV(4.0F, -4.0F, this.zLevel, var8, var10);
      tessellator.addVertexWithUV(-4.0F, -4.0F, this.zLevel, var8, var11);
      tessellator.draw();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
   }

   protected void drawHoveringTextFixed(List<String> listin, int x, int y, FontRenderer font, int width) {
      if (!listin.isEmpty()) {
         List<String> list = new ArrayList<>();

         for(String s : listin) {
            s = this.trimStringNewline(s);

             list.addAll((List<String>) font.listFormattedStringToWidth(s, width));
         }

         GL11.glDisable(32826);
         RenderHelper.disableStandardItemLighting();
         GL11.glDisable(2896);
         GL11.glDisable(2929);
         int k = 0;

         for(String s : list) {
            int l = font.getStringWidth(s);
            if (l > k) {
               k = l;
            }
         }

         int j2 = x + 12;
         int k2 = y - 12;
         int i1 = 8;
         if (list.size() > 1) {
            i1 += 2 + (list.size() - 1) * 10;
         }

         this.zLevel = 300.0F;
         itemRender.zLevel = 300.0F;
         int j1 = -267386864;
         this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
         this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
         this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
         this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
         this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
         int k1 = 1347420415;
         int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
         this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
         this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
         this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
         this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

         for(int i2 = 0; i2 < list.size(); ++i2) {
            String s1 = list.get(i2);
            font.drawStringWithShadow(s1, j2, k2, -1);
            if (i2 == 0) {
               k2 += 2;
            }

            k2 += 10;
         }

         this.zLevel = 0.0F;
         itemRender.zLevel = 0.0F;
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         RenderHelper.enableStandardItemLighting();
         GL11.glEnable(32826);
      }

   }

   private String trimStringNewline(String p_78273_1_) {
      while(p_78273_1_ != null && p_78273_1_.endsWith("\n")) {
         p_78273_1_ = p_78273_1_.substring(0, p_78273_1_.length() - 1);
      }

      return p_78273_1_;
   }

   private static class Sparkle {
      float x;
      float y;
      float mx;
      float my;
      float r;
      float g;
      float b;
      long nextframe;
      int frame;

      public Sparkle(float x, float y, float mx, float my, float r, float g, float b) {
         this.x = x;
         this.y = y;
         this.mx = mx;
         this.my = my;
         this.frame = 0;
         this.r = r;
         this.g = g;
         this.b = b;
         this.nextframe = System.currentTimeMillis() + 50L;
      }
   }
}
