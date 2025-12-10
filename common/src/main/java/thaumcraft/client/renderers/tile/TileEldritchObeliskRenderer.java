package thaumcraft.client.renderers.tile;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.Config;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockTypeSafely;

public class TileEldritchObeliskRenderer extends TileEntitySpecialRenderer {
   FloatBuffer fBuffer = GLAllocation.createDirectFloatBuffer(16);
   private boolean inrange;
   private IModelCustom model;
   private static final ResourceLocation CAP = new ResourceLocation("thaumcraft", "textures/models/obelisk_cap.obj");
   private String t1 = "textures/misc/tunnel.png";
   private String t2 = "textures/misc/particlefield.png";
   private String t3 = "textures/misc/particlefield32.png";
   private String t4 = "textures/models/obelisk_side.png";
   private String t5 = "textures/models/obelisk_cap.png";
   private String t6 = "textures/models/obelisk_side_2.png";
   private String t7 = "textures/models/obelisk_cap_2.png";

   public TileEldritchObeliskRenderer() {
      this.model = AdvancedModelLoader.loadModel(CAP);
   }

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      double var10002 = (double)te.xCoord + (double)0.5F;
      double var10003 = (double)te.yCoord + (double)0.5F;
      double var10004 = te.zCoord;
      this.inrange = Minecraft.getMinecraft().renderViewEntity.getDistanceSq(var10002, var10003, var10004 + (double)0.5F) < (double)512.0F;
      float bob = 0.0F;
      float count = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + f;
      bob = MathHelper.sin(count / 10.0F) * 0.1F + 0.1F;
      GL11.glPushMatrix();
      GL11.glDisable(2912);
      this.drawPlaneZNeg(x, y + (double)1.0F + (double)bob, z, f, 3);
      this.drawPlaneZPos(x, y + (double)1.0F + (double)bob, z, f, 3);
      this.drawPlaneXNeg(x, y + (double)1.0F + (double)bob, z, f, 3);
      this.drawPlaneXPos(x, y + (double)1.0F + (double)bob, z, f, 3);
      GL11.glEnable(2912);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glEnable(2896);
      GL11.glEnable(32826);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      String tempTex1 = this.t4;
      String tempTex2 = this.t5;
      Block blockType = getBlockTypeSafely(te);
      if (blockType != null) {
         int j = blockType.getMixedBrightnessForBlock(te.getWorldObj(), te.xCoord, te.yCoord + 5, te.zCoord);
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         if (te.hasWorldObj() && te.getWorldObj().provider.dimensionId == Config.dimensionOuterId) {
            tempTex1 = this.t6;
            tempTex2 = this.t7;
         }
      }

      GL11.glPushMatrix();
      UtilsFX.bindTexture(tempTex1);
      GL11.glTranslated(x + (double)0.5F, y + (double)1.0F + (double)bob, z + (double)0.5F);

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         GL11.glRotatef((float)(a * 90), 0.0F, 1.0F, 0.0F);
         GL11.glTranslated(0.0F, 0.0F, -0.5F);
         this.renderSide(3);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)1.0F + (double)bob, z + (double)0.5F);
      GL11.glRotated(90.0F, 1.0F, 0.0F, 0.0F);
      UtilsFX.bindTexture(tempTex2);
      this.model.renderPart("Cap");
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)4.0F + (double)bob, z + (double)0.5F);
      GL11.glRotated(90.0F, -1.0F, 0.0F, 0.0F);
      this.model.renderPart("Cap");
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   public void drawPlaneZPos(double x, double y, double z, float f, int height) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      if (this.inrange) {
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
            tessellator.addVertex(x, y + (double)height, z + (double)offset);
            tessellator.addVertex(x, y, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y + (double)height, z + (double)offset);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x, y + (double)height, z + (double)offset, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x, y, z + (double)offset, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y, z + (double)offset, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)height, z + (double)offset, 0.0F, 1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZNeg(double x, double y, double z, float f, int height) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      if (this.inrange) {
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
            tessellator.addVertex(x, y, z + (double)offset);
            tessellator.addVertex(x, y + (double)height, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y + (double)height, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y, z + (double)offset);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x, y, z + (double)offset, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x, y + (double)height, z + (double)offset, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)height, z + (double)offset, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y, z + (double)offset, 0.0F, 1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXPos(double x, double y, double z, float f, int height) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      if (this.inrange) {
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
            tessellator.addVertex(x + (double)offset, y + (double)height, z);
            tessellator.addVertex(x + (double)offset, y + (double)height, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)height, z, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)height, z + (double)1.0F, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z + (double)1.0F, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z, 0.0F, 1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXNeg(double x, double y, double z, float f, int height) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      if (this.inrange) {
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
            tessellator.addVertex(x + (double)offset, y, z);
            tessellator.addVertex(x + (double)offset, y, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y + (double)height, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y + (double)height, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z + (double)1.0F, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)height, z + (double)1.0F, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)height, z, 0.0F, 1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
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

   public void renderSide(int h) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glEnable(32826);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV(-0.5F, h, 0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV(0.5F, h, 0.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV(0.5F, 0.0F, 0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
   }
}
