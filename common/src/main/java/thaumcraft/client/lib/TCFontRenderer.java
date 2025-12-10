package thaumcraft.client.lib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TCFontRenderer {
   private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
   public static final ResourceLocation FONT_NORMAL = new ResourceLocation("textures/font/ascii.png");
   public static final ResourceLocation FONT_GALACTIC = new ResourceLocation("textures/font/ascii_sga.png");
   private int[] charWidth = new int[256];
   public int FONT_HEIGHT = 9;
   public Random fontRandom = new Random();
   private byte[] glyphWidth = new byte[65536];
   private int[] colorCode = new int[32];
   private ResourceLocation locationFontTexture;
   private final TextureManager renderEngine;
   private float posX;
   private float posY;
   private boolean unicodeFlag;
   private boolean bidiFlag;
   private float red;
   private float blue;
   private float green;
   private float alpha;
   private int textColor;
   private boolean randomStyle = false;
   private boolean boldStyle = false;
   private boolean italicStyle = false;
   private boolean underlineStyle = false;
   private boolean strikethroughStyle = false;
   boolean uniflagInit = false;
   ArrayList inserts = new ArrayList<>();

   public TCFontRenderer(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3RenderEngine, boolean par4) {
      this.locationFontTexture = par2ResourceLocation;
      this.renderEngine = par3RenderEngine;
      this.unicodeFlag = par4;
      this.uniflagInit = par4;
      this.readFontData();
      par3RenderEngine.bindTexture(this.locationFontTexture);

      for(int i = 0; i < 32; ++i) {
         int j = (i >> 3 & 1) * 85;
         int k = (i >> 2 & 1) * 170 + j;
         int l = (i >> 1 & 1) * 170 + j;
         int i1 = (i & 1) * 170 + j;
         if (i == 6) {
            k += 85;
         }

         if (par1GameSettings.anaglyph) {
            int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
            int k1 = (k * 30 + l * 70) / 100;
            int l1 = (k * 30 + i1 * 70) / 100;
            k = j1;
            l = k1;
            i1 = l1;
         }

         if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
         }

         this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
      }

   }

   public void readFontData() {
      this.readGlyphSizes();
      this.readFontTexture();
   }

   private void readFontTexture() {
      BufferedImage bufferedimage;
      try {
         bufferedimage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
      } catch (IOException ioexception) {
         throw new RuntimeException(ioexception);
      }

      int i = bufferedimage.getWidth();
      int j = bufferedimage.getHeight();
      int[] aint = new int[i * j];
      bufferedimage.getRGB(0, 0, i, j, aint, 0, i);

      for(int k = 0; k < 256; ++k) {
         int l = k % 16;
         int i1 = k / 16;

         int j1;
         for(j1 = 7; j1 >= 0; --j1) {
            int k1 = l * 8 + j1;
            boolean flag = true;

            for(int l1 = 0; l1 < 8; ++l1) {
               int i2 = (i1 * 8 + l1) * i;
               int j2 = aint[k1 + i2] & 255;
                if (j2 > 0) {
                    flag = false;
                    break;
                }
            }

            if (!flag) {
               break;
            }
         }

         if (k == 32) {
            j1 = 2;
         }

         this.charWidth[k] = j1 + 2;
      }

   }

   private void readGlyphSizes() {
      try {
         InputStream inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
         inputstream.read(this.glyphWidth);
      } catch (IOException ioexception) {
         throw new RuntimeException(ioexception);
      }
   }

   private float renderCharAtPos(int par1, char par2, boolean par3) {
      return par2 == ' ' ? 4.0F : (par1 > 0 && !this.unicodeFlag ? this.renderDefaultChar(par1 + 32, par3) : this.renderUnicodeChar(par2, par3));
   }

   private float renderDefaultChar(int par1, boolean par2) {
      float f = (float)(par1 % 16 * 8);
      float f1 = (float)(par1 / 16 * 8);
      float f2 = par2 ? 1.0F : 0.0F;
      this.renderEngine.bindTexture(this.locationFontTexture);
      float f3 = (float)this.charWidth[par1] - 0.01F;
      GL11.glBegin(5);
      GL11.glTexCoord2f(f / 128.0F, f1 / 128.0F);
      GL11.glVertex3f(this.posX + f2, this.posY, 0.0F);
      GL11.glTexCoord2f(f / 128.0F, (f1 + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX - f2, this.posY + 7.99F, 0.0F);
      GL11.glTexCoord2f((f + f3) / 128.0F, f1 / 128.0F);
      GL11.glVertex3f(this.posX + f3 + f2, this.posY, 0.0F);
      GL11.glTexCoord2f((f + f3) / 128.0F, (f1 + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX + f3 - f2, this.posY + 7.99F, 0.0F);
      GL11.glEnd();
      return (float)this.charWidth[par1];
   }

   private ResourceLocation getUnicodePageLocation(int par1) {
      if (unicodePageLocations[par1] == null) {
         unicodePageLocations[par1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", par1));
      }

      return unicodePageLocations[par1];
   }

   private void loadGlyphTexture(int par1) {
      this.renderEngine.bindTexture(this.getUnicodePageLocation(par1));
   }

   private float renderUnicodeChar(char par1, boolean par2) {
      if (this.glyphWidth[par1] == 0) {
         return 0.0F;
      } else {
         int i = par1 / 256;
         this.loadGlyphTexture(i);
         int j = this.glyphWidth[par1] >>> 4;
         int k = this.glyphWidth[par1] & 15;
         float f = (float)j;
         float f1 = (float)(k + 1);
         float f2 = (float)(par1 % 16 * 16) + f;
         float f3 = (float)((par1 & 255) / 16 * 16);
         float f4 = f1 - f - 0.02F;
         float f5 = par2 ? 1.0F : 0.0F;
         GL11.glBegin(5);
         GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
         GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
         GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
         GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
         GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
         GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
         GL11.glEnd();
         return (f1 - f) / 2.0F + 1.0F;
      }
   }

   public int drawStringWithShadow(String par1Str, int par2, int par3, int par4) {
      return this.drawString(par1Str, par2, par3, par4, true);
   }

   public int drawString(String par1Str, int par2, int par3, int par4) {
      return this.drawString(par1Str, par2, par3, par4, false);
   }

   public int drawString(String par1Str, int par2, int par3, int par4, boolean par5) {
      this.resetStyles();
      if (this.bidiFlag) {
         par1Str = this.bidiReorder(par1Str);
      }

      int l;
      if (par5) {
         l = this.renderString(par1Str, par2 + 1, par3 + 1, par4, true);
         l = Math.max(l, this.renderString(par1Str, par2, par3, par4, false));
      } else {
         l = this.renderString(par1Str, par2, par3, par4, false);
      }

      return l;
   }

   private String bidiReorder(String par1Str) {
      if (par1Str != null && Bidi.requiresBidi(par1Str.toCharArray(), 0, par1Str.length())) {
         Bidi bidi = new Bidi(par1Str, -2);
         byte[] abyte = new byte[bidi.getRunCount()];
         String[] astring = new String[abyte.length];

         for(int j = 0; j < abyte.length; ++j) {
            int k = bidi.getRunStart(j);
            int i = bidi.getRunLimit(j);
            int l = bidi.getRunLevel(j);
            String s1 = par1Str.substring(k, i);
            abyte[j] = (byte)l;
            astring[j] = s1;
         }

         String[] astring1 = astring.clone();
         Bidi.reorderVisually(abyte, 0, astring, 0, abyte.length);
         StringBuilder stringbuilder = new StringBuilder();

         for(int i = 0; i < astring.length; ++i) {
            byte b0 = abyte[i];

            for(int i1 = 0; i1 < astring1.length; ++i1) {
               if (astring1[i1].equals(astring[i])) {
                  b0 = abyte[i1];
                  break;
               }
            }

            if ((b0 & 1) == 0) {
               stringbuilder.append(astring[i]);
            } else {
               for(int var16 = astring[i].length() - 1; var16 >= 0; --var16) {
                  char c0 = astring[i].charAt(var16);
                  if (c0 == '(') {
                     c0 = ')';
                  } else if (c0 == ')') {
                     c0 = '(';
                  }

                  stringbuilder.append(c0);
               }
            }
         }

         return stringbuilder.toString();
      } else {
         return par1Str;
      }
   }

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   private void renderStringAtPos(String par1Str, boolean par2) {
      for(int i = 0; i < par1Str.length(); ++i) {
         char c0 = par1Str.charAt(i);
         if (c0 == 167 && i + 1 < par1Str.length()) {
            int j = "0123456789abcdefklmnor".indexOf(par1Str.toLowerCase().charAt(i + 1));
            if (j < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (j < 0 || j > 15) {
                  j = 15;
               }

               if (par2) {
                  j += 16;
               }

               int k = this.colorCode[j];
               this.textColor = k;
               GL11.glColor4f((float)(k >> 16) / 255.0F, (float)(k >> 8 & 255) / 255.0F, (float)(k & 255) / 255.0F, this.alpha);
            } else if (j == 16) {
               this.randomStyle = true;
            } else if (j == 17) {
               this.boldStyle = true;
            } else if (j == 18) {
               this.strikethroughStyle = true;
            } else if (j == 19) {
               this.underlineStyle = true;
            } else if (j == 20) {
               this.italicStyle = true;
            } else if (j == 21) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
            }

            ++i;
         } else {
            int j = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
            if (this.randomStyle && j != -1) {
               int k;
               do {
                  k = this.fontRandom.nextInt(this.charWidth.length);
               } while(this.charWidth[j] != this.charWidth[k]);

               j = k;
            }

            float f = this.unicodeFlag ? 0.5F : 1.0F;
            boolean flag1 = (j <= 0 || this.unicodeFlag) && par2;
            if (flag1) {
               this.posX -= f;
               this.posY -= f;
            }

            float f1 = this.renderCharAtPos(j, c0, this.italicStyle);
            if (flag1) {
               this.posX += f;
               this.posY += f;
            }

            if (this.boldStyle) {
               this.posX += f;
               if (flag1) {
                  this.posX -= f;
                  this.posY -= f;
               }

               this.renderCharAtPos(j, c0, this.italicStyle);
               this.posX -= f;
               if (flag1) {
                  this.posX += f;
                  this.posY += f;
               }

               ++f1;
            }

            if (this.strikethroughStyle) {
               Tessellator tessellator = Tessellator.instance;
               GL11.glDisable(3553);
               tessellator.startDrawingQuads();
               tessellator.addVertex(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0F);
               tessellator.addVertex(this.posX + f1, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0F);
               tessellator.addVertex(this.posX + f1, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F, 0.0F);
               tessellator.addVertex(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F, 0.0F);
               tessellator.draw();
               GL11.glEnable(3553);
            }

            if (this.underlineStyle) {
               Tessellator tessellator = Tessellator.instance;
               GL11.glDisable(3553);
               tessellator.startDrawingQuads();
               int l = this.underlineStyle ? -1 : 0;
               tessellator.addVertex(this.posX + (float)l, this.posY + (float)this.FONT_HEIGHT, 0.0F);
               tessellator.addVertex(this.posX + f1, this.posY + (float)this.FONT_HEIGHT, 0.0F);
               tessellator.addVertex(this.posX + f1, this.posY + (float)this.FONT_HEIGHT - 1.0F, 0.0F);
               tessellator.addVertex(this.posX + (float)l, this.posY + (float)this.FONT_HEIGHT - 1.0F, 0.0F);
               tessellator.draw();
               GL11.glEnable(3553);
            }

            this.posX += (float)((int)f1);
         }
      }

   }

   private int renderStringAligned(String par1Str, int par2, int par3, int par4, int par5, boolean par6) {
      if (this.bidiFlag) {
         par1Str = this.bidiReorder(par1Str);
         int i1 = this.getStringWidth(par1Str);
         par2 = par2 + par4 - i1;
      }

      return this.renderString(par1Str, par2, par3, par5, par6);
   }

   private int renderString(String par1Str, int par2, int par3, int par4, boolean par5) {
      if (par1Str == null) {
         return 0;
      } else {
         if ((par4 & -67108864) == 0) {
            par4 |= -16777216;
         }

         if (par5) {
            par4 = (par4 & 16579836) >> 2 | par4 & -16777216;
         }

         this.red = (float)(par4 >> 16 & 255) / 255.0F;
         this.blue = (float)(par4 >> 8 & 255) / 255.0F;
         this.green = (float)(par4 & 255) / 255.0F;
         this.alpha = (float)(par4 >> 24 & 255) / 255.0F;
         GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
         this.posX = (float)par2;
         this.posY = (float)par3;
         this.renderStringAtPos(par1Str, par5);
         return (int)this.posX;
      }
   }

   public int getStringWidth(String par1Str) {
      if (par1Str == null) {
         return 0;
      } else {
         int i = 0;
         boolean flag = false;

         for(int j = 0; j < par1Str.length(); ++j) {
            char c0 = par1Str.charAt(j);
            int k = this.getCharWidth(c0);
            if (k < 0 && j < par1Str.length() - 1) {
               ++j;
               c0 = par1Str.charAt(j);
               if (c0 != 'l' && c0 != 'L') {
                  if (c0 == 'r' || c0 == 'R') {
                     flag = false;
                  }
               } else {
                  flag = true;
               }

               k = 0;
            }

            i += k;
            if (flag) {
               ++i;
            }
         }

         return i;
      }
   }

   public int getCharWidth(char par1) {
      if (par1 == 167) {
         return -1;
      } else if (par1 == ' ') {
         return 4;
      } else {
         int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(par1);
         if (par1 > 0 && i != -1 && !this.unicodeFlag) {
            return this.charWidth[i];
         } else if (this.glyphWidth[par1] != 0) {
            int j = this.glyphWidth[par1] >>> 4;
            int k = this.glyphWidth[par1] & 15;
            if (k > 7) {
               k = 15;
               j = 0;
            }

            ++k;
            return (k - j) / 2 + 1;
         } else {
            return 0;
         }
      }
   }

   public String trimStringToWidth(String par1Str, int par2) {
      return this.trimStringToWidth(par1Str, par2, false);
   }

   public String trimStringToWidth(String par1Str, int par2, boolean par3) {
      StringBuilder stringbuilder = new StringBuilder();
      int j = 0;
      int k = par3 ? par1Str.length() - 1 : 0;
      int l = par3 ? -1 : 1;
      boolean flag1 = false;
      boolean flag2 = false;

      for(int i1 = k; i1 >= 0 && i1 < par1Str.length() && j < par2; i1 += l) {
         char c0 = par1Str.charAt(i1);
         int j1 = this.getCharWidth(c0);
         if (flag1) {
            flag1 = false;
            if (c0 != 'l' && c0 != 'L') {
               if (c0 == 'r' || c0 == 'R') {
                  flag2 = false;
               }
            } else {
               flag2 = true;
            }
         } else if (j1 < 0) {
            flag1 = true;
         } else {
            j += j1;
            if (flag2) {
               ++j;
            }
         }

         if (j > par2) {
            break;
         }

         if (par3) {
            stringbuilder.insert(0, c0);
         } else {
            stringbuilder.append(c0);
         }
      }

      return stringbuilder.toString();
   }

   private String trimStringNewline(String par1Str) {
      while(par1Str != null && par1Str.endsWith("\n")) {
         par1Str = par1Str.substring(0, par1Str.length() - 1);
      }

      return par1Str;
   }

   public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5) {
      this.resetStyles();
      this.textColor = par5;
      par1Str = this.trimStringNewline(par1Str);
      this.renderSplitString(par1Str, par2, par3, par4, false);
   }

   public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5, Gui gui) {
      this.resetStyles();
      this.textColor = par5;
      par1Str = this.trimStringNewline(par1Str);
      this.renderSplitString(par1Str, par2, par3, par4, false, gui);
   }

   private void renderSplitString(String par1Str, int par2, int par3, int par4, boolean par5, Gui gui) {
      for(String s1 : this.listFormattedStringToWidth(par1Str, par4)) {
         if (s1.contains("@")) {
            int i1 = s1.indexOf("@");
            int i2 = s1.indexOf("@", i1 + 1);
            if (i1 >= 0 && i2 > i1) {
               int index = Integer.parseInt(s1.substring(i1 + 1, i2));
               s1 = s1.replaceAll(s1.substring(i1, i2 + 1), "").trim();
               String s2 = (String)this.inserts.get(index);
               if (s2 != null) {
                  if (!s2.contains("<LINE>") && !s2.contains("<LINE/>")) {
                     if (s2.contains("<IMG>")) {
                        String cont = s2.replace("<IMG>", "");
                        cont = cont.replace("</IMG>", "");
                        String[] scont = cont.split(":");
                        UtilsFX.bindTexture(scont[0], scont[1]);
                        float scale = Float.parseFloat(scont[6]);
                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glTranslatef((float)(par2 - 3 + par4 / 2) - (float)(Integer.parseInt(scont[4]) / 2) * scale, (float)par3, 0.0F);
                        GL11.glScalef(scale, scale, scale);
                        gui.drawTexturedModalRect(0, 0, Integer.parseInt(scont[2]), Integer.parseInt(scont[3]), Integer.parseInt(scont[4]), Integer.parseInt(scont[5]));
                        GL11.glPopMatrix();
                        par3 = (int)((float)par3 + ((float)Integer.parseInt(scont[5]) * scale - (float)this.FONT_HEIGHT));
                     }
                  } else {
                     UtilsFX.bindTexture("textures/gui/gui_researchbook.png");
                     gui.drawTexturedModalRect(par2 + par4 / 2 - 48, par3 + 2, 24, 184, 96, 4);
                  }
               }
            }
         } else {
            this.renderStringAligned(s1, par2, par3, par4, this.textColor, par5);
         }

         par3 += this.FONT_HEIGHT;
      }

   }

   private void renderSplitString(String par1Str, int par2, int par3, int par4, boolean par5) {
      for(String s1 : this.listFormattedStringToWidth(par1Str, par4)) {
         this.renderStringAligned(s1, par2, par3, par4, this.textColor, par5);
         par3 += this.FONT_HEIGHT;
      }

   }

   public int splitStringWidth(String par1Str, int par2) {
      return this.FONT_HEIGHT * this.listFormattedStringToWidth(par1Str, par2).size();
   }

   public void setUnicodeFlag(boolean par1) {
      this.unicodeFlag = par1;
   }

   public boolean getUnicodeFlag() {
      return this.unicodeFlag;
   }

   public void setBidiFlag(boolean par1) {
      this.bidiFlag = par1;
   }

   public List<String> listFormattedStringToWidth(String par1Str, int par2) {
      this.inserts.clear();
      int count = 0;
      boolean found = true;

      while(found) {
         found = false;
         par1Str = par1Str.replaceAll("<BR>", "\n");
         par1Str = par1Str.replaceAll("<BR/>", "\n");
         if (par1Str.contains("<LINE>") || par1Str.contains("<LINE/>")) {
            this.inserts.add("<LINE>");
            if (par1Str.contains("<LINE>")) {
               par1Str = par1Str.replaceFirst("<LINE>", "\n@" + count + "@\n");
            } else {
               par1Str = par1Str.replaceFirst("<LINE/>", "\n@" + count + "@\n");
            }

            ++count;
            found = true;
         }

         if (par1Str.contains("<IMG>")) {
            int i1 = par1Str.indexOf("<IMG>");
            int i2 = par1Str.indexOf("</IMG>");
            String s = par1Str.substring(i1, i2) + "</IMG>";
            this.inserts.add(s);
            par1Str = par1Str.replaceFirst(s, "\n@" + count + "@\n");
            ++count;
            found = true;
         }
      }

      return Arrays.asList(this.wrapFormattedStringToWidth(par1Str, par2).split("\n"));
   }

   String wrapFormattedStringToWidth(String par1Str, int par2) {
      int j = this.sizeStringToWidth(par1Str, par2);
      if (par1Str.length() <= j) {
         return par1Str;
      } else {
         String s1 = par1Str.substring(0, j);
         char c0 = par1Str.charAt(j);
         boolean flag = c0 == ' ' || c0 == '\n';
         String s2 = getFormatFromString(s1) + par1Str.substring(j + (flag ? 1 : 0));
         return s1 + "\n" + this.wrapFormattedStringToWidth(s2, par2);
      }
   }

   private int sizeStringToWidth(String par1Str, int par2) {
      int j = par1Str.length();
      int k = 0;
      int l = 0;
      int i1 = -1;

      for(boolean flag = false; l < j; ++l) {
         char c0 = par1Str.charAt(l);
         switch (c0) {
            case ' ':
               i1 = l;
            default:
               k += this.getCharWidth(c0);
               if (flag) {
                  ++k;
               }
               break;
            case '§':
               if (l < j - 1) {
                  ++l;
                  char c1 = par1Str.charAt(l);
                  if (c1 != 'l' && c1 != 'L') {
                     if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                        flag = false;
                     }
                  } else {
                     flag = true;
                  }
               }
         }

         if (c0 == '\n') {
            ++l;
            i1 = l;
            break;
         }

         if (k > par2) {
            break;
         }
      }

      return l != j && i1 != -1 && i1 < l ? i1 : l;
   }

   private static boolean isFormatColor(char par0) {
      return par0 >= '0' && par0 <= '9' || par0 >= 'a' && par0 <= 'f' || par0 >= 'A' && par0 <= 'F';
   }

   private static boolean isFormatSpecial(char par0) {
      return par0 >= 'k' && par0 <= 'o' || par0 >= 'K' && par0 <= 'O' || par0 == 'r' || par0 == 'R';
   }

   private static String getFormatFromString(String par0Str) {
      StringBuilder s1 = new StringBuilder();
      int i = -1;
      int j = par0Str.length();

      while((i = par0Str.indexOf(167, i + 1)) != -1) {
         if (i < j - 1) {
            char c0 = par0Str.charAt(i + 1);
            if (isFormatColor(c0)) {
               s1 = new StringBuilder("§" + c0);
            } else if (isFormatSpecial(c0)) {
               s1.append("§").append(c0);
            }
         }
      }

      return s1.toString();
   }

   public boolean getBidiFlag() {
      return this.bidiFlag;
   }
}
