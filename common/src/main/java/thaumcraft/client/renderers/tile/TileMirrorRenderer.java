package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileMirror;
import thaumcraft.common.tiles.TileMirrorEssentia;

public class TileMirrorRenderer extends TileEntitySpecialRenderer {
   FloatBuffer fBuffer = GLAllocation.createDirectFloatBuffer(16);
   private String t1 = "textures/misc/tunnel.png";
   private String t2 = "textures/misc/particlefield.png";

   public void drawPlaneYPos(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            UtilsFX.bindTexture(this.t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            UtilsFX.bindTexture(this.t2);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(y + (double)offset);
         float f9 = f8 - ActiveRenderInfo.objectY;
         float f10 = f8 + f5 - ActiveRenderInfo.objectY;
         float f11 = f9 / f10;
         f11 = (float)(y + (double)offset) + f11;
         GL11.glTranslatef(px, f11, pz);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-px, -pz, -py);
         GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -py);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
         tessellator.addVertex(x + (double)p, y + (double)offset, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)p, y + (double)offset, z + (double)p);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)offset, z + (double)p);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)offset, z + (double)1.0F - (double)p);
         tessellator.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneYNeg(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float f1 = (float)TileEntityRendererDispatcher.staticPlayerX;
      float f2 = (float)TileEntityRendererDispatcher.staticPlayerY;
      float f3 = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            UtilsFX.bindTexture(this.t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            UtilsFX.bindTexture(this.t2);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(-(y + (double)offset));
         float f9 = f8 + ActiveRenderInfo.objectY;
         float f10 = f8 + f5 + ActiveRenderInfo.objectY;
         float f11 = f9 / f10;
         f11 = (float)(y + (double)offset) + f11;
         GL11.glTranslatef(f1, f11, f3);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-f1, -f3, -f2);
         GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
         tessellator.addVertex(x + (double)p, y + (double)offset, z + (double)p);
         tessellator.addVertex(x + (double)p, y + (double)offset, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)offset, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)offset, z + (double)p);
         tessellator.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZNeg(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            UtilsFX.bindTexture(this.t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            UtilsFX.bindTexture(this.t2);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(-(z + (double)offset));
         float f9 = f8 + ActiveRenderInfo.objectZ;
         float f10 = f8 + f5 + ActiveRenderInfo.objectZ;
         float f11 = f9 / f10;
         f11 = (float)(z + (double)offset) + f11;
         GL11.glTranslatef(px, py, f11);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-px, -py, -pz);
         GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -pz);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
         tessellator.addVertex(x + (double)p, y + (double)1.0F - (double)p, z + (double)offset);
         tessellator.addVertex(x + (double)p, y + (double)p, z + (double)offset);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)p, z + (double)offset);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)1.0F - (double)p, z + (double)offset);
         tessellator.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZPos(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            UtilsFX.bindTexture(this.t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            UtilsFX.bindTexture(this.t2);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(z + (double)offset);
         float f9 = f8 - ActiveRenderInfo.objectZ;
         float f10 = f8 + f5 - ActiveRenderInfo.objectZ;
         float f11 = f9 / f10;
         f11 = (float)(z + (double)offset) + f11;
         GL11.glTranslatef(px, py, f11);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-px, -py, -pz);
         GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -pz);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
         tessellator.addVertex(x + (double)p, y + (double)p, z + (double)offset);
         tessellator.addVertex(x + (double)p, y + (double)1.0F - (double)p, z + (double)offset);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)1.0F - (double)p, z + (double)offset);
         tessellator.addVertex(x + (double)1.0F - (double)p, y + (double)p, z + (double)offset);
         tessellator.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXNeg(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            UtilsFX.bindTexture(this.t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            UtilsFX.bindTexture(this.t2);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(-(x + (double)offset));
         float f9 = f8 + ActiveRenderInfo.objectX;
         float f10 = f8 + f5 + ActiveRenderInfo.objectX;
         float f11 = f9 / f10;
         f11 = (float)(x + (double)offset) + f11;
         GL11.glTranslatef(f11, py, pz);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-pz, -py, -px);
         GL11.glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -px);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
         tessellator.addVertex(x + (double)offset, y + (double)1.0F - (double)p, z + (double)p);
         tessellator.addVertex(x + (double)offset, y + (double)1.0F - (double)p, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)offset, y + (double)p, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)offset, y + (double)p, z + (double)p);
         tessellator.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXPos(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            UtilsFX.bindTexture(this.t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            UtilsFX.bindTexture(this.t2);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(x + (double)offset);
         float f9 = f8 - ActiveRenderInfo.objectX;
         float f10 = f8 + f5 - ActiveRenderInfo.objectX;
         float f11 = f9 / f10;
         f11 = (float)(x + (double)offset) + f11;
         GL11.glTranslatef(f11, py, pz);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-pz, -py, -px);
         GL11.glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -px);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
         tessellator.addVertex(x + (double)offset, y + (double)p, z + (double)p);
         tessellator.addVertex(x + (double)offset, y + (double)p, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)offset, y + (double)1.0F - (double)p, z + (double)1.0F - (double)p);
         tessellator.addVertex(x + (double)offset, y + (double)1.0F - (double)p, z + (double)p);
         tessellator.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
      this.fBuffer.clear();
      this.fBuffer.put(f).put(f1).put(f2).put(f3);
      this.fBuffer.flip();
      return this.fBuffer;
   }

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      ForgeDirection dir = ForgeDirection.getOrientation(te.getBlockMetadata() % 6);
      boolean linked = false;
      float instability = 0.0F;
      if (te instanceof TileMirror) {
         linked = ((TileMirror)te).linked;
         if (((TileMirror)te).instability > 0) {
            instability = Minecraft.getMinecraft().theWorld.rand.nextFloat() * ((float)((TileMirror)te).instability / 10000.0F);
         }
      }

      if (te instanceof TileMirrorEssentia) {
         linked = ((TileMirrorEssentia)te).linked;
      }

      label30: {
         int b = ConfigBlocks.blockMirror.getMixedBrightnessForBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
         if (linked) {
            double var10002 = (double)te.xCoord + (double)0.5F;
            double var10003 = (double)te.yCoord + (double)0.5F;
            double var10004 = te.zCoord;
            if (UtilsFX.isVisibleTo(1.5F, FMLClientHandler.instance().getClient().thePlayer, var10002, var10003, var10004 + (double)0.5F)) {
               GL11.glPushMatrix();
               switch (dir) {
                  case DOWN:
                     this.drawPlaneYPos(te, x, y, z, f);
                     break;
                  case UP:
                     this.drawPlaneYNeg(te, x, y, z, f);
                     break;
                  case WEST:
                     this.drawPlaneXPos(te, x, y, z, f);
                     break;
                  case EAST:
                     this.drawPlaneXNeg(te, x, y, z, f);
                     break;
                  case NORTH:
                     this.drawPlaneZPos(te, x, y, z, f);
                     break;
                  case SOUTH:
                     this.drawPlaneZNeg(te, x, y, z, f);
               }

               GL11.glPopMatrix();
               GL11.glPushMatrix();
               this.translateFromOrientation((float)x, (float)y, (float)z, dir.ordinal(), 0.02F + instability);
               UtilsFX.renderQuadFromTexture("textures/blocks/mirrorpanetrans.png", 1, 0, 1.0F, 1.0F, 1.0F, 1.0F, b, 771, 1.0F);
               GL11.glPopMatrix();
               break label30;
            }
         }

         GL11.glPushMatrix();
         this.translateFromOrientation((float)x, (float)y, (float)z, dir.ordinal(), 0.02F + instability);
         UtilsFX.renderQuadFromTexture("textures/blocks/mirrorpane.png", 1, 0, 1.0F, 1.0F, 1.0F, 1.0F, b, 771, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      this.translateFromOrientation((float)x, (float)y, (float)z, dir.ordinal(), 0.0F);
      IIcon icon = ConfigBlocks.blockMirror.getIcon(0, te.getBlockMetadata());
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMinU();
      float f4 = icon.getMaxV();
      Tessellator tessellator = Tessellator.instance;
      this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
      ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
      GL11.glPopMatrix();
   }

   private void translateFromOrientation(float x, float y, float z, int orientation, float off) {
      if (orientation == 0) {
         GL11.glTranslatef(x, y + 1.0F, z + 1.0F);
         GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 1) {
         GL11.glTranslatef(x, y, z);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 2) {
         GL11.glTranslatef(x, y, z + 1.0F);
      } else if (orientation == 3) {
         GL11.glTranslatef(x + 1.0F, y, z);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 4) {
         GL11.glTranslatef(x + 1.0F, y, z + 1.0F);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 5) {
         GL11.glTranslatef(x, y, z);
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, 0.0F, -off);
   }
}
