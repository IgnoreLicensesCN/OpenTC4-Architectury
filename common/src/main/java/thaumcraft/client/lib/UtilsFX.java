package thaumcraft.client.lib;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXScorch;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

import static tc4tweak.ClientUtils.fieldParticleTexture;

public class UtilsFX {
   public static final String[] colorNames = new String[]{"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
   public static final String[] colorCodes = new String[]{"§f", "§6", "§d", "§9", "§e", "§a", "§d", "§8", "§7", "§b", "§5", "§9", "§4", "§2", "§c", "§8"};
   public static final int[] colors = new int[]{15790320, 15435844, 12801229, 6719955, 14602026, 4312372, 14188952, 4408131, 10526880, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019};
   public static int[] connectedTextureRefByID = new int[]{0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5, 4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 16, 16, 20, 20, 16, 16, 28, 28, 21, 21, 46, 42, 21, 21, 43, 38, 4, 4, 5, 5, 4, 4, 5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 16, 16, 20, 20, 16, 16, 28, 28, 25, 25, 45, 37, 25, 25, 40, 32, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5, 4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 7, 7, 24, 24, 7, 7, 10, 10, 29, 29, 44, 41, 29, 29, 39, 33, 4, 4, 5, 5, 4, 4, 5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 7, 7, 24, 24, 7, 7, 10, 10, 8, 8, 36, 35, 8, 8, 34, 11};
   public static float[] lightBrightnessTable = null;
   private static Map textureSizeCache = new HashMap<>();
   static Map boundTextures = new HashMap<>();
   static DecimalFormat myFormatter = new DecimalFormat("#######.##");

   public static float getBrightnessFromLight(int light) {
      if (lightBrightnessTable == null) {
         lightBrightnessTable = new float[16];
         float f = 0.0F;

         for(int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float)i / 15.0F;
            lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
         }
      }

      return lightBrightnessTable[light];
   }

   public static void infusedStoneSparkle(World world, int x, int y, int z, int md) {
      if (world.isRemote) {
         int color = 0;
         switch (md) {
            case 1:
               color = 1;
               break;
            case 2:
               color = 4;
               break;
            case 3:
               color = 2;
               break;
            case 4:
               color = 3;
               break;
            case 5:
               color = 6;
               break;
            case 6:
               color = 5;
         }

         for(int a = 0; a < Thaumcraft.proxy.particleCount(3); ++a) {
            FXSparkle fx = new FXSparkle(world, (float)x + world.rand.nextFloat(), (float)y + world.rand.nextFloat(), (float)z + world.rand.nextFloat(), 1.75F, color == -1 ? world.rand.nextInt(5) : color, 3 + world.rand.nextInt(3));
            fx.setGravity(0.1F);
            ParticleEngine.instance.addEffect(world, fx);
         }

      }
   }

   public static void shootFire(World world, EntityPlayer p, boolean offset, int range, boolean lance) {
      Vec3 vec3d = p.getLook((float)range);
      double px = p.posX - (double)(MathHelper.cos(p.rotationYaw / 180.0F * 3.141593F) * 0.1F);
      double py = p.posY - (double)0.08F;
      double pz = p.posZ - (double)(MathHelper.sin(p.rotationYaw / 180.0F * 3.141593F) * 0.1F);
      if (p.getEntityId() != FMLClientHandler.instance().getClient().thePlayer.getEntityId()) {
         py = p.boundingBox.minY + (double)(p.height / 2.0F) + (double)0.25F;
      }

      for(int q = 0; q < 3; ++q) {
         FXScorch ef = new FXScorch(p.worldObj, px, py, pz, vec3d, (float)range, lance);
         ef.posX += vec3d.xCoord * (double)0.3F;
         ef.posY += vec3d.yCoord * (double)0.3F;
         ef.posZ += vec3d.zCoord * (double)0.3F;
         ef.prevPosX = ef.posX;
         ef.prevPosY = ef.posY;
         ef.prevPosZ = ef.posZ;
         ef.posX += vec3d.xCoord * (double)0.3F;
         ef.posY += vec3d.yCoord * (double)0.3F;
         ef.posZ += vec3d.zCoord * (double)0.3F;
         ParticleEngine.instance.addEffect(world, ef);
      }

   }

   public static void renderFacingQuad(double px, double py, double pz, float angle, float scale, float alpha, int frames, int cframe, float partialTicks, int color) {
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         Tessellator tessellator = Tessellator.instance;
         float arX = ActiveRenderInfo.rotationX;
         float arZ = ActiveRenderInfo.rotationZ;
         float arYZ = ActiveRenderInfo.rotationYZ;
         float arXY = ActiveRenderInfo.rotationXY;
         float arXZ = ActiveRenderInfo.rotationXZ;
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
         double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
         double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
         GL11.glTranslated(-iPX, -iPY, -iPZ);
         tessellator.startDrawingQuads();
         tessellator.setBrightness(220);
         tessellator.setColorRGBA_I(color, (int)(alpha * 255.0F));
         Vec3 v1 = Vec3.createVectorHelper(-arX * scale - arYZ * scale, -arXZ * scale, -arZ * scale - arXY * scale);
         Vec3 v2 = Vec3.createVectorHelper(-arX * scale + arYZ * scale, arXZ * scale, -arZ * scale + arXY * scale);
         Vec3 v3 = Vec3.createVectorHelper(arX * scale + arYZ * scale, arXZ * scale, arZ * scale + arXY * scale);
         Vec3 v4 = Vec3.createVectorHelper(arX * scale - arYZ * scale, -arXZ * scale, arZ * scale - arXY * scale);
         if (angle != 0.0F) {
            Vec3 pvec = Vec3.createVectorHelper(iPX, iPY, iPZ);
            Vec3 tvec = Vec3.createVectorHelper(px, py, pz);
            Vec3 qvec = pvec.subtract(tvec).normalize();
            QuadHelper.setAxis(qvec, angle).rotate(v1);
            QuadHelper.setAxis(qvec, angle).rotate(v2);
            QuadHelper.setAxis(qvec, angle).rotate(v3);
            QuadHelper.setAxis(qvec, angle).rotate(v4);
         }

         float f2 = (float)cframe / (float)frames;
         float f3 = (float)(cframe + 1) / (float)frames;
         float f4 = 0.0F;
         float f5 = 1.0F;
         tessellator.setNormal(0.0F, 0.0F, -1.0F);
         tessellator.addVertexWithUV(px + v1.xCoord, py + v1.yCoord, pz + v1.zCoord, f2, f5);
         tessellator.addVertexWithUV(px + v2.xCoord, py + v2.yCoord, pz + v2.zCoord, f3, f5);
         tessellator.addVertexWithUV(px + v3.xCoord, py + v3.yCoord, pz + v3.zCoord, f3, f4);
         tessellator.addVertexWithUV(px + v4.xCoord, py + v4.yCoord, pz + v4.zCoord, f2, f4);
         tessellator.draw();
      }

   }

   public static void renderFacingStrip(double px, double py, double pz, float angle, float scale, float alpha, int frames, int strip, int frame, float partialTicks, int color) {
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         Tessellator tessellator = Tessellator.instance;
         float arX = ActiveRenderInfo.rotationX;
         float arZ = ActiveRenderInfo.rotationZ;
         float arYZ = ActiveRenderInfo.rotationYZ;
         float arXY = ActiveRenderInfo.rotationXY;
         float arXZ = ActiveRenderInfo.rotationXZ;
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
         double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
         double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
         GL11.glTranslated(-iPX, -iPY, -iPZ);
         tessellator.startDrawingQuads();
         tessellator.setBrightness(220);
         tessellator.setColorRGBA_I(color, (int)(alpha * 255.0F));
         Vec3 v1 = Vec3.createVectorHelper(-arX * scale - arYZ * scale, -arXZ * scale, -arZ * scale - arXY * scale);
         Vec3 v2 = Vec3.createVectorHelper(-arX * scale + arYZ * scale, arXZ * scale, -arZ * scale + arXY * scale);
         Vec3 v3 = Vec3.createVectorHelper(arX * scale + arYZ * scale, arXZ * scale, arZ * scale + arXY * scale);
         Vec3 v4 = Vec3.createVectorHelper(arX * scale - arYZ * scale, -arXZ * scale, arZ * scale - arXY * scale);
         if (angle != 0.0F) {
            Vec3 pvec = Vec3.createVectorHelper(iPX, iPY, iPZ);
            Vec3 tvec = Vec3.createVectorHelper(px, py, pz);
            Vec3 qvec = pvec.subtract(tvec).normalize();
            QuadHelper.setAxis(qvec, angle).rotate(v1);
            QuadHelper.setAxis(qvec, angle).rotate(v2);
            QuadHelper.setAxis(qvec, angle).rotate(v3);
            QuadHelper.setAxis(qvec, angle).rotate(v4);
         }

         float f2 = (float)frame / (float)frames;
         float f3 = (float)(frame + 1) / (float)frames;
         float f4 = (float)strip / (float)frames;
         float f5 = ((float)strip + 1.0F) / (float)frames;
         tessellator.setNormal(0.0F, 0.0F, -1.0F);
         tessellator.addVertexWithUV(px + v1.xCoord, py + v1.yCoord, pz + v1.zCoord, f3, f5);
         tessellator.addVertexWithUV(px + v2.xCoord, py + v2.yCoord, pz + v2.zCoord, f3, f4);
         tessellator.addVertexWithUV(px + v3.xCoord, py + v3.yCoord, pz + v3.zCoord, f2, f4);
         tessellator.addVertexWithUV(px + v4.xCoord, py + v4.yCoord, pz + v4.zCoord, f2, f5);
         tessellator.draw();
      }

   }

   public static void renderAnimatedQuad(float scale, float alpha, int frames, int cframe, float partialTicks, int color) {
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(220);
         tessellator.setColorRGBA_I(color, (int)(alpha * 255.0F));
         float f2 = (float)cframe / (float)frames;
         float f3 = (float)(cframe + 1) / (float)frames;
         float f4 = 0.0F;
         float f5 = 1.0F;
         tessellator.setNormal(0.0F, 0.0F, -1.0F);
         tessellator.addVertexWithUV((double)-0.5F * (double)scale, (double)0.5F * (double)scale, 0.0F, f2, f5);
         tessellator.addVertexWithUV((double)0.5F * (double)scale, (double)0.5F * (double)scale, 0.0F, f3, f5);
         tessellator.addVertexWithUV((double)0.5F * (double)scale, (double)-0.5F * (double)scale, 0.0F, f3, f4);
         tessellator.addVertexWithUV((double)-0.5F * (double)scale, (double)-0.5F * (double)scale, 0.0F, f2, f4);
         tessellator.draw();
      }

   }

   public static void renderAnimatedQuadStrip(float scale, float alpha, int frames, int strip, int cframe, float partialTicks, int color) {
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(220);
         tessellator.setColorRGBA_I(color, (int)(alpha * 255.0F));
         float f2 = (float)cframe / (float)frames;
         float f3 = (float)(cframe + 1) / (float)frames;
         float f4 = (float)strip / (float)frames;
         float f5 = (float)(strip + 1) / (float)frames;
         tessellator.setNormal(0.0F, 0.0F, -1.0F);
         tessellator.addVertexWithUV((double)-0.5F * (double)scale, (double)0.5F * (double)scale, 0.0F, f2, f5);
         tessellator.addVertexWithUV((double)0.5F * (double)scale, (double)0.5F * (double)scale, 0.0F, f3, f5);
         tessellator.addVertexWithUV((double)0.5F * (double)scale, (double)-0.5F * (double)scale, 0.0F, f3, f4);
         tessellator.addVertexWithUV((double)-0.5F * (double)scale, (double)-0.5F * (double)scale, 0.0F, f2, f4);
         tessellator.draw();
      }

   }

   public static Vec3 perpendicular(Vec3 v) {
      return v.zCoord == (double)0.0F ? zCrossProduct(v) : xCrossProduct(v);
   }

   public static Vec3 xCrossProduct(Vec3 v) {
      double d = v.zCoord;
      double d1 = -v.yCoord;
      v.xCoord = 0.0F;
      v.yCoord = d;
      v.zCoord = d1;
      return v;
   }

   public static Vec3 zCrossProduct(Vec3 v) {
      double d = v.yCoord;
      double d1 = -v.xCoord;
      v.xCoord = d;
      v.yCoord = d1;
      v.zCoord = 0.0F;
      return v;
   }

   public static void drawTexturedQuad(int par1, int par2, int par3, int par4, int par5, int par6, double zLevel) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(par1, par2 + par6, zLevel, (float)(par3) * var7, (float)(par4 + par6) * var8);
      var9.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (float)(par3 + par5) * var7, (float)(par4 + par6) * var8);
      var9.addVertexWithUV(par1 + par5, par2, zLevel, (float)(par3 + par5) * var7, (float)(par4) * var8);
      var9.addVertexWithUV(par1, par2, zLevel, (float)(par3) * var7, (float)(par4) * var8);
      var9.draw();
   }

   public static void drawTexturedQuadFull(int par1, int par2, double zLevel) {
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(par1, par2 + 16, zLevel, 0.0F, 1.0F);
      var9.addVertexWithUV(par1 + 16, par2 + 16, zLevel, 1.0F, 1.0F);
      var9.addVertexWithUV(par1 + 16, par2, zLevel, 1.0F, 0.0F);
      var9.addVertexWithUV(par1, par2, zLevel, 0.0F, 0.0F);
      var9.draw();
   }

   public static void renderQuad(String texture) {
      renderQuad(texture, 1, 0.66F);
   }

   public static void renderQuad(String texture, int blend, float trans) {
      renderQuad(texture, blend, trans, 1.0F, 1.0F, 1.0F);
   }

   public static void renderQuad(String texture, int blend, float trans, float r, float g, float b) {
      bindTexture(texture);
      Tessellator tessellator = Tessellator.instance;
      GL11.glEnable(32826);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, blend);
      GL11.glColor4f(r, g, b, trans);
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA_F(r, g, b, trans);
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      tessellator.addVertexWithUV(0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV(1.0F, 1.0F, 0.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV(1.0F, 0.0F, 0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
   }

   public static void renderQuadCenteredFromTexture(String texture, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      bindTexture(texture);
      renderQuadCenteredFromTexture(scale, red, green, blue, brightness, blend, opacity);
   }

   public static void renderQuadCenteredFromTexture(ResourceLocation texture, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      Minecraft.getMinecraft().renderEngine.bindTexture(texture);
      renderQuadCenteredFromTexture(scale, red, green, blue, brightness, blend, opacity);
   }

   public static void renderQuadCenteredFromTexture(float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glScalef(scale, scale, scale);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, blend);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
      tessellator.startDrawingQuads();
      if (brightness > 0) {
         tessellator.setBrightness(brightness);
      }

      tessellator.setColorRGBA_F(red, green, blue, opacity);
      tessellator.addVertexWithUV(-0.5F, 0.5F, 0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV(0.5F, 0.5F, 0.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV(0.5F, -0.5F, 0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(-0.5F, -0.5F, 0.0F, 0.0F, 0.0F);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
   }

   public static void renderQuadFromTexture(String texture, int tileSize, int icon, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      bindTexture(texture);
      int size = getTextureSize(texture, tileSize);
      float size16 = (float)(size * tileSize);
      float float_sizeMinus0_01 = (float)size - 0.01F;
      float float_texNudge = 1.0F / ((float)size * (float)size * 2.0F);
      float float_reciprocal = 1.0F / (float)size;
      Tessellator tessellator = Tessellator.instance;
      float f = ((float)(icon % tileSize * size) + 0.0F) / size16;
      float f1 = ((float)(icon % tileSize * size) + float_sizeMinus0_01) / size16;
      float f2 = ((float)(icon / tileSize * size) + 0.0F) / size16;
      float f3 = ((float)(icon / tileSize * size) + float_sizeMinus0_01) / size16;
      float f5 = 0.0F;
      float f6 = 0.3F;
      GL11.glEnable(32826);
      GL11.glScalef(scale, scale, scale);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, blend);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
      tessellator.startDrawingQuads();
      tessellator.setBrightness(brightness);
      tessellator.setColorRGBA_F(red, green, blue, opacity);
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      tessellator.addVertexWithUV(0.0F, 1.0F, 0.0F, f1, f2);
      tessellator.addVertexWithUV(1.0F, 1.0F, 0.0F, f, f2);
      tessellator.addVertexWithUV(1.0F, 0.0F, 0.0F, f, f3);
      tessellator.addVertexWithUV(0.0F, 0.0F, 0.0F, f1, f3);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
   }

   public static void renderQuadFromIcon(boolean isBlock, IIcon icon, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      if (isBlock) {
         Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      } else {
         Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
      }

      Tessellator tessellator = Tessellator.instance;
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMinU();
      float f4 = icon.getMaxV();
      GL11.glScalef(scale, scale, scale);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, blend);
      GL11.glColor4f(red, green, blue, opacity);
      tessellator.startDrawingQuads();
      if (brightness > -1) {
         tessellator.setBrightness(brightness);
      }

      tessellator.setColorRGBA_F(red, green, blue, opacity);
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV(0.0F, 0.0F, 0.0F, f1, f4);
      tessellator.addVertexWithUV(1.0F, 0.0F, 0.0F, f3, f4);
      tessellator.addVertexWithUV(1.0F, 1.0F, 0.0F, f3, f2);
      tessellator.addVertexWithUV(0.0F, 1.0F, 0.0F, f1, f2);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
   }

   public static void renderQuadCenteredFromIcon(boolean isBlock, IIcon icon, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      if (isBlock) {
         Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      } else {
         Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
      }

      Tessellator tessellator = Tessellator.instance;
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMinU();
      float f4 = icon.getMaxV();
      GL11.glEnable(32826);
      GL11.glScalef(scale, scale, scale);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, blend);
      GL11.glColor4f(red, green, blue, opacity);
      tessellator.startDrawingQuads();
      tessellator.setBrightness(brightness);
      tessellator.setColorRGBA_F(red, green, blue, opacity);
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV(-0.5F, 0.5F, 0.0F, f1, f4);
      tessellator.addVertexWithUV(0.5F, 0.5F, 0.0F, f3, f4);
      tessellator.addVertexWithUV(0.5F, -0.5F, 0.0F, f3, f2);
      tessellator.addVertexWithUV(-0.5F, -0.5F, 0.0F, f1, f2);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
   }

   public static int getTextureAnimationSize(String s) {
      if (textureSizeCache.get(s) != null) {
         return (Integer)textureSizeCache.get(s);
      } else {
         try {
            InputStream inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("thaumcraft", s)).getInputStream();
            if (inputstream == null) {
               throw new Exception("Image not found: " + s);
            } else {
               BufferedImage bi = ImageIO.read(inputstream);
               int size = bi.getWidth() / bi.getHeight();
               textureSizeCache.put(s, size);
               return size;
            }
         } catch (Exception e) {
            e.printStackTrace();
            return 16;
         }
      }
   }

   public static int getTextureSize(String s, int dv) {
      if (textureSizeCache.get(Arrays.asList(s, dv)) != null) {
         return (Integer)textureSizeCache.get(Arrays.asList(s, dv));
      } else {
         try {
            InputStream inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("thaumcraft", s)).getInputStream();
            if (inputstream == null) {
               throw new Exception("Image not found: " + s);
            } else {
               BufferedImage bi = ImageIO.read(inputstream);
               int size = bi.getWidth() / dv;
               textureSizeCache.put(Arrays.asList(s, dv), size);
               return size;
            }
         } catch (Exception e) {
            e.printStackTrace();
            return 16;
         }
      }
   }

   public static int getBrightnessForRender(Entity entity, double x, double z) {
      int var2 = MathHelper.floor_double(x);
      int var3 = MathHelper.floor_double(z);
      if (entity.worldObj.blockExists(var2, 0, var3)) {
         double var4 = (entity.boundingBox.maxY - entity.boundingBox.minY) * 0.66;
         int var6 = MathHelper.floor_double(entity.posY - (double)entity.yOffset + var4);
         return entity.worldObj.getLightBrightnessForSkyBlocks(var2, var6, var3, 2);
      } else {
         return 0;
      }
   }

   public static void bindTexture(String texture) {
      ResourceLocation rl = null;
      if (boundTextures.containsKey(texture)) {
         rl = (ResourceLocation)boundTextures.get(texture);
      } else {
         rl = new ResourceLocation("thaumcraft", texture);
      }

      Minecraft.getMinecraft().renderEngine.bindTexture(rl);
   }

   public static void bindTexture(String mod, String texture) {
      ResourceLocation rl = null;
      if (boundTextures.containsKey(mod + ":" + texture)) {
         rl = (ResourceLocation)boundTextures.get(mod + ":" + texture);
      } else {
         rl = new ResourceLocation(mod, texture);
      }

      Minecraft.getMinecraft().renderEngine.bindTexture(rl);
   }

   public static void bindTexture(ResourceLocation resource) {
      Minecraft.getMinecraft().renderEngine.bindTexture(resource);
   }

   public static void drawTag(int x, int y, Aspect aspect, float amount, int bonus, double z, int blend, float alpha) {
      drawTag(x, y, aspect, amount, bonus, z, blend, alpha, false);
   }

   public static void drawTag(int x, int y, Aspect aspect, float amt, int bonus, double z) {
      drawTag(x, y, aspect, amt, bonus, z, 771, 1.0F, false);
   }

   public static void drawTag(int x, int y, Aspect aspect) {
      drawTag(x, y, aspect, 0.0F, 0, 0.0F, 771, 1.0F, true);
   }

   public static void drawTag(int x, int y, Aspect aspect, float amount, int bonus, double z, int blend, float alpha, boolean bw) {
      drawTag(x, (double)y, aspect, amount, bonus, z, blend, alpha, bw);
   }

   public static void drawTag(double x, double y, Aspect aspect, float amount, int bonus, double z, int blend, float alpha, boolean bw) {
      if (aspect != null) {
         Minecraft mc = Minecraft.getMinecraft();
         Color color = new Color(aspect.getColor());
         GL11.glPushMatrix();
         GL11.glDisable(2896);
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, blend);
         GL11.glPushMatrix();
         mc.renderEngine.bindTexture(aspect.getImage());
         if (!bw) {
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha);
         } else {
            GL11.glColor4f(0.1F, 0.1F, 0.1F, alpha * 0.8F);
         }

         Tessellator var9 = Tessellator.instance;
         var9.startDrawingQuads();
         if (!bw) {
            var9.setColorRGBA_F((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha);
         } else {
            var9.setColorRGBA_F(0.1F, 0.1F, 0.1F, alpha * 0.8F);
         }

         var9.addVertexWithUV(x + (double)0.0F, y + (double)16.0F, z, 0.0F, 1.0F);
         var9.addVertexWithUV(x + (double)16.0F, y + (double)16.0F, z, 1.0F, 1.0F);
         var9.addVertexWithUV(x + (double)16.0F, y + (double)0.0F, z, 1.0F, 0.0F);
         var9.addVertexWithUV(x + (double)0.0F, y + (double)0.0F, z, 0.0F, 0.0F);
         var9.draw();
         GL11.glPopMatrix();
         if (amount > 0.0F) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            String am = myFormatter.format(amount);
            int sw = mc.fontRenderer.getStringWidth(am);
            if (blend > 1) {
               for(int a = -1; a <= 1; ++a) {
                  for(int b = -1; b <= 1; ++b) {
                     if ((a == 0 || b == 0) && (a != 0 || b != 0)) {
                        mc.fontRenderer.drawString(am, a + 32 - sw + (int)x * 2, b + 32 - mc.fontRenderer.FONT_HEIGHT + (int)y * 2, 0);
                     }
                  }
               }
            }

            mc.fontRenderer.drawString(am, 32 - sw + (int)x * 2, 32 - mc.fontRenderer.FONT_HEIGHT + (int)y * 2, 16777215);
            GL11.glPopMatrix();
         }

         if (bonus > 0) {
            GL11.glPushMatrix();
            bindTexture(ParticleEngine.particleTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int px = 16 * (mc.thePlayer.ticksExisted % 16);
            drawTexturedQuad((int)x - 4, (int)y - 4, px, 80, 16, 16, z);
            if (bonus > 1) {
               GL11.glScalef(0.5F, 0.5F, 0.5F);
               String am = "" + bonus;
               int sw = mc.fontRenderer.getStringWidth(am) / 2;
               if (blend > 1) {
                  for(int a = -1; a <= 1; ++a) {
                     for(int b = -1; b <= 1; ++b) {
                        if ((a == 0 || b == 0) && (a != 0 || b != 0)) {
                           mc.fontRenderer.drawString(am, 8 - sw + a + (int)x * 2, 15 + b - mc.fontRenderer.FONT_HEIGHT + (int)y * 2, 0);
                        }
                     }
                  }
               }

               mc.fontRenderer.drawString(am, 8 - sw + (int)x * 2, 15 - mc.fontRenderer.FONT_HEIGHT + (int)y * 2, 16777215);
            }

            GL11.glPopMatrix();
         }

         GL11.glDisable(GL11.GL_BLEND);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glEnable(2896);
         GL11.glPopMatrix();
      }
   }

   public static boolean isVisibleTo(float fov, Entity ent, double x, double y, double z) {
      double dist = ent.getDistance(x, y, z);
      if (dist < (double)2.0F) {
         return true;
      } else {
         Minecraft mc = FMLClientHandler.instance().getClient();
         double vT = fov + mc.gameSettings.fovSetting / 2.0F;
         int j = 512;
         if (j > 400) {
            j = 400;
         }

         double rD = j;
         float f1 = MathHelper.cos(-ent.rotationYaw * 0.01745329F - 3.141593F);
         float f3 = MathHelper.sin(-ent.rotationYaw * 0.01745329F - 3.141593F);
         float f5 = -MathHelper.cos(-ent.rotationPitch * 0.01745329F);
         float f7 = MathHelper.sin(-ent.rotationPitch * 0.01745329F);
         double lx = f3 * f5;
         double ly = f7;
         double lz = f1 * f5;
         double dx = x + (double)0.5F - ent.posX;
         double dy = y + (double)0.5F - ent.posY - (double)ent.getEyeHeight();
         double dz = z + (double)0.5F - ent.posZ;
         double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
         double dot = dx / len * lx + dy / len * ly + dz / len * lz;
         double angle = Math.acos(dot);
         return angle < vT && mc.gameSettings.thirdPersonView == 0 && dist < rD || mc.gameSettings.thirdPersonView > 0 && dist < rD;
      }
   }

   public static void drawCustomTooltip(GuiScreen gui, RenderItem itemRenderer, FontRenderer fr, List<String> var4, int par2, int par3, int subTipColor) {
      GL11.glDisable(32826);
      GL11.glDisable(2929);
      if (!var4.isEmpty()) {
         int var5 = 0;

         for(String var7 : var4) {
            int var8 = fr.getStringWidth(var7);
            if (var8 > var5) {
               var5 = var8;
            }
         }

         int var15 = par2 + 12;
         int var16 = par3 - 12;
         int var9 = 8;
         if (var4.size() > 1) {
            var9 += 2 + (var4.size() - 1) * 10;
         }

         itemRenderer.zLevel = 300.0F;
         int var10 = -267386864;
         drawGradientRect(var15 - 3, var16 - 4, var15 + var5 + 3, var16 - 3, var10, var10);
         drawGradientRect(var15 - 3, var16 + var9 + 3, var15 + var5 + 3, var16 + var9 + 4, var10, var10);
         drawGradientRect(var15 - 3, var16 - 3, var15 + var5 + 3, var16 + var9 + 3, var10, var10);
         drawGradientRect(var15 - 4, var16 - 3, var15 - 3, var16 + var9 + 3, var10, var10);
         drawGradientRect(var15 + var5 + 3, var16 - 3, var15 + var5 + 4, var16 + var9 + 3, var10, var10);
         int var11 = 1347420415;
         int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
         drawGradientRect(var15 - 3, var16 - 3 + 1, var15 - 3 + 1, var16 + var9 + 3 - 1, var11, var12);
         drawGradientRect(var15 + var5 + 2, var16 - 3 + 1, var15 + var5 + 3, var16 + var9 + 3 - 1, var11, var12);
         drawGradientRect(var15 - 3, var16 - 3, var15 + var5 + 3, var16 - 3 + 1, var11, var11);
         drawGradientRect(var15 - 3, var16 + var9 + 2, var15 + var5 + 3, var16 + var9 + 3, var12, var12);

         for(int var13 = 0; var13 < var4.size(); ++var13) {
            String var14 = var4.get(var13);
            if (var13 == 0) {
               var14 = "§" + Integer.toHexString(subTipColor) + var14;
            } else {
               var14 = "§7" + var14;
            }

            fr.drawStringWithShadow(var14, var15, var16, -1);
            if (var13 == 0) {
               var16 += 2;
            }

            var16 += 10;
         }
      }

      itemRenderer.zLevel = 0.0F;
      GL11.glEnable(2929);
   }

   public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
      float var7 = (float)(par5 >> 24 & 255) / 255.0F;
      float var8 = (float)(par5 >> 16 & 255) / 255.0F;
      float var9 = (float)(par5 >> 8 & 255) / 255.0F;
      float var10 = (float)(par5 & 255) / 255.0F;
      float var11 = (float)(par6 >> 24 & 255) / 255.0F;
      float var12 = (float)(par6 >> 16 & 255) / 255.0F;
      float var13 = (float)(par6 >> 8 & 255) / 255.0F;
      float var14 = (float)(par6 & 255) / 255.0F;
      GL11.glDisable(3553);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glDisable(3008);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      Tessellator var15 = Tessellator.instance;
      var15.startDrawingQuads();
      var15.setColorRGBA_F(var8, var9, var10, var7);
      var15.addVertex(par3, par2, 300.0F);
      var15.addVertex(par1, par2, 300.0F);
      var15.setColorRGBA_F(var12, var13, var14, var11);
      var15.addVertex(par1, par4, 300.0F);
      var15.addVertex(par3, par4, 300.0F);
      var15.draw();
      GL11.glShadeModel(7424);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(3008);
      GL11.glEnable(3553);
   }

   public static void drawFloatyLine(double x, double y, double z, double x2, double y2, double z2, float partialTicks, int color, String texture, float speed, float distance) {
      drawFloatyLine(x, y, z, x2, y2, z2, partialTicks, color, texture, speed, distance, 0.15F);
   }

   public static void drawFloatyLine(double x, double y, double z, double x2, double y2, double z2, float partialTicks, int color, String texture, float speed, float distance, float width) {
      EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      GL11.glTranslated(-iPX + x2, -iPY + y2, -iPZ + z2);
      float time = (float)(System.nanoTime() / 30000000L);
      Color co = new Color(color);
      float r = (float)co.getRed() / 255.0F;
      float g = (float)co.getGreen() / 255.0F;
      float b = (float)co.getBlue() / 255.0F;
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      Tessellator tessellator = Tessellator.instance;
      double dc1x = (float)(x - x2);
      double dc1y = (float)(y - y2);
      double dc1z = (float)(z - z2);
      bindTexture(texture);
      GL11.glDisable(2884);
      tessellator.startDrawing(5);
      double dx2 = 0.0F;
      double dy2 = 0.0F;
      double dz2 = 0.0F;
      double d3 = x - x2;
      double d4 = y - y2;
      double d5 = z - z2;
      float dist = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
      float blocks = (float)Math.round(dist);
      float length = blocks * ((float)Config.golemLinkQuality / 2.0F);
      float f9 = 0.0F;
      float f10 = 1.0F;

      for(int i = 0; (float)i <= length * distance; ++i) {
         float f2 = (float)i / length;
         float f2a = (float)i * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         float f3 = 1.0F - Math.abs((float)i - length / 2.0F) / (length / 2.0F);
         double dx = dc1x + (double)(MathHelper.sin((float)((z % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)4.0F)) * 0.5F * f3);
         double dy = dc1y + (double)(MathHelper.sin((float)((x % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)3.0F)) * 0.5F * f3);
         double dz = dc1z + (double)(MathHelper.sin((float)((y % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)2.0F)) * 0.5F * f3);
         tessellator.setColorRGBA_F(r, g, b, f3);
         float f13 = (1.0F - f2) * dist - time * speed;
         tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 - (double)width, dz * (double)f2, f13, f10);
         tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 + (double)width, dz * (double)f2, f13, f9);
      }

      tessellator.draw();
      tessellator.startDrawing(5);

      for(int var84 = 0; (float)var84 <= length * distance; ++var84) {
         float f2 = (float)var84 / length;
         float f2a = (float)var84 * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         float f3 = 1.0F - Math.abs((float)var84 - length / 2.0F) / (length / 2.0F);
         double dx = dc1x + (double)(MathHelper.sin((float)((z % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)4.0F)) * 0.5F * f3);
         double dy = dc1y + (double)(MathHelper.sin((float)((x % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)3.0F)) * 0.5F * f3);
         double dz = dc1z + (double)(MathHelper.sin((float)((y % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)2.0F)) * 0.5F * f3);
         tessellator.setColorRGBA_F(r, g, b, f3);
         float f13 = (1.0F - f2) * dist - time * speed;
         tessellator.addVertexWithUV(dx * (double)f2 - (double)width, dy * (double)f2, dz * (double)f2, f13, f10);
         tessellator.addVertexWithUV(dx * (double)f2 + (double)width, dy * (double)f2, dz * (double)f2, f13, f9);
      }

      tessellator.draw();
      GL11.glEnable(2884);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
   }

   public static void drawFloatyGUILine(double x, double y, double x2, double y2, float partialTicks, int color, String texture, float speed, float distance) {
      GL11.glPushMatrix();
      GL11.glTranslated(x2, y2, 0.0F);
      float time = (float)(System.nanoTime() / 30000000L);
      Color co = new Color(color);
      float r = (float)co.getRed() / 255.0F;
      float g = (float)co.getGreen() / 255.0F;
      float b = (float)co.getBlue() / 255.0F;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      Tessellator tessellator = Tessellator.instance;
      double dc1x = (float)(x - x2);
      double dc1y = (float)(y - y2);
      bindTexture(texture);
      tessellator.startDrawing(5);
      double d3 = x - x2;
      double d4 = y - y2;
      float dist = MathHelper.sqrt_double(d3 * d3 + d4 * d4);
      double dx = d3 / (double)dist;
      double var10000 = d4 / (double)dist;
      GL11.glRotated((float)(-(Math.atan2(d3, d4) * (double)180.0F / Math.PI)) + 90.0F, 0.0F, 0.0F, 1.0F);
      float blocks = (float)Math.round(dist);
      float length = blocks * distance;
      float f9 = 0.0F;
      float f10 = 1.0F;
      float sec = 1.0F / length;

      for(int i = 0; (float)i <= length; ++i) {
         float f2 = (float)i / length;
         tessellator.setColorRGBA_F(r, g, b, 1.0F);
         float f13 = (1.0F - f2) * length;
         float f14 = (1.0F - f2) * length + sec;
         float width = 1.0F;
         tessellator.addVertexWithUV(dx * (double)i, 0.0F - width, 0.0F, f13 / width, f10);
         tessellator.addVertexWithUV(dx * (double)i, 0.0F + width, 0.0F, f14 / width, f9);
      }

      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
   }

   public static float getEquippedProgress(ItemRenderer ir) {
      try {
         return ReflectionHelper.getPrivateValue(ItemRenderer.class, ir, new String[]{"equippedProgress", "f", "field_78454_c"});
      } catch (Exception var2) {
         return 0.0F;
      }
   }

   public static float getPrevEquippedProgress(ItemRenderer ir) {
      try {
         return ReflectionHelper.getPrivateValue(ItemRenderer.class, ir, new String[]{"prevEquippedProgress", "g", "field_78451_d"});
      } catch (Exception var2) {
         return 0.0F;
      }
   }

   public static Timer getTimer(Minecraft mc) {
      try {
         return ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{"timer", "Q", "field_71428_T"});
      } catch (Exception var2) {
         return new Timer(20.0F);
      }
   }

   public static int getGuiXSize(GuiContainer gui) {
      try {
         return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, new String[]{"xSize", "f", "field_146999_f"});
      } catch (Exception var2) {
         return 0;
      }
   }

   public static int getGuiYSize(GuiContainer gui) {
      try {
         return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, new String[]{"ySize", "g", "field_147000_g"});
      } catch (Exception var2) {
         return 0;
      }
   }

   public static float getGuiZLevel(Gui gui) {
      try {
         return ReflectionHelper.getPrivateValue(Gui.class, gui, new String[]{"zLevel", "e", "field_77023_b"});
      } catch (Exception var2) {
         return 0.0F;
      }
   }

   public static ResourceLocation getParticleTexture() {
      try {
         if (fieldParticleTexture == null)
            fieldParticleTexture = ReflectionHelper.findField(
                    EffectRenderer.class,
                    "particleTextures", "b", "field_110737_b");
         return (ResourceLocation) fieldParticleTexture.get(null);
      } catch (Exception ignored) {
         return null;
      }
//      try {
//         return ReflectionHelper.getPrivateValue(
//
//                 EffectRenderer.class,
//                 null,
//                 new String[]{"particleTextures", "b", "field_110737_b"}
//         );
//      } catch (Exception var1) {
//         return null;
//      }
   }
}
