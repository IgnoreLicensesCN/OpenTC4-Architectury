package thaumcraft.client.renderers.tile;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelCube;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileEldritchLock;

public class TileEldritchLockRenderer extends TileEntitySpecialRenderer {
   FloatBuffer fBuffer = GLAllocation.createDirectFloatBuffer(16);
   private boolean inrange;
   private ModelCube model = new ModelCube(0);
   private String t1 = "textures/misc/tunnel.png";
   private String t2 = "textures/misc/particlefield.png";
   private String t3 = "textures/misc/particlefield32.png";
   ItemStack is = null;
   EntityItem entityitem = null;

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      double var10002 = (double)te.xCoord + (double)0.5F;
      double var10003 = (double)te.yCoord + (double)0.5F;
      double var10004 = te.zCoord;
      this.inrange = Minecraft.getMinecraft().renderViewEntity.getDistanceSq(var10002, var10003, var10004 + (double)0.5F) < (double)512.0F;
      if (this.is == null) {
         this.is = new ItemStack(ConfigItems.itemEldritchObject, 1, 2);
      }

      float bob = 0.0F;
      float count = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + f;
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/eldritch_cube.png");
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      ForgeDirection dir = ForgeDirection.getOrientation(((TileEldritchLock)te).getFacing());

      for(int u = 0; u < 4; ++u) {
         GL11.glPushMatrix();
         GL11.glRotated(90 * u, dir.offsetX, dir.offsetY, dir.offsetZ);

         for(int a = 1; a < 5 - (((TileEldritchLock)te).count + u * 5) / 20; ++a) {
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.25F + 0.5F * (float)a, 0.0F);
            float w = MathHelper.sin((count + (float)(a * 10) + (float)(u * 20)) / 20.0F) * 0.1F;
            if (a == 1 || a == 4) {
               w = w / 2.0F + 0.2F;
            }

            GL11.glScaled((double)0.5F + (double)w, 0.5F, (double)0.5F + (double)w);
            this.model.render();
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      if (((TileEldritchLock)te).count >= 0) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)x + 0.5F + (float)dir.offsetX * 0.525F, (float)y + 0.285F, (float)z + 0.5F + (float)dir.offsetZ * 0.525F);
         switch (dir.ordinal()) {
            case 2:
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            case 3:
            default:
               break;
            case 4:
               GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 5:
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         }

         GL11.glScaled(1.0F, 1.0F, 1.0F);
         if (this.entityitem == null) {
            this.entityitem = new EntityItem(te.getWorldObj(), 0.0F, 0.0F, 0.0F, this.is);
         }

         this.entityitem.hoverStart = 0.0F;
         RenderItem.renderInFrame = true;
         RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         RenderItem.renderInFrame = false;
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      GL11.glDisable(2912);
      switch (((TileEldritchLock)te).getFacing()) {
         case 2:
            this.drawPlaneZNeg(x, y, z, f, 3);
            break;
         case 3:
            this.drawPlaneZPos(x, y, z, f, 3);
            break;
         case 4:
            this.drawPlaneXNeg(x, y, z, f, 3);
            break;
         case 5:
            this.drawPlaneXPos(x, y, z, f, 3);
      }

      GL11.glEnable(2912);
      GL11.glPopMatrix();
   }

   public void drawPlaneZPos(double x, double y, double z, float f, int height) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.5F;
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
            tessellator.addVertex(x - (double)2.0F, y + (double)3.0F, z + (double)offset);
            tessellator.addVertex(x - (double)2.0F, y - (double)2.0F, z + (double)offset);
            tessellator.addVertex(x + (double)3.0F, y - (double)2.0F, z + (double)offset);
            tessellator.addVertex(x + (double)3.0F, y + (double)3.0F, z + (double)offset);
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
         tessellator.addVertexWithUV(x - (double)2.0F, y + (double)3.0F, z + (double)offset, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x - (double)2.0F, y - (double)2.0F, z + (double)offset, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)3.0F, y - (double)2.0F, z + (double)offset, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)3.0F, y + (double)3.0F, z + (double)offset, 0.0F, 1.0F);
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
      float offset = 0.5F;
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
            tessellator.addVertex(x - (double)2.0F, y - (double)2.0F, z + (double)offset);
            tessellator.addVertex(x - (double)2.0F, y + (double)3.0F, z + (double)offset);
            tessellator.addVertex(x + (double)3.0F, y + (double)3.0F, z + (double)offset);
            tessellator.addVertex(x + (double)3.0F, y - (double)2.0F, z + (double)offset);
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
         tessellator.addVertexWithUV(x - (double)2.0F, y - (double)2.0F, z + (double)offset, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x - (double)2.0F, y + (double)3.0F, z + (double)offset, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)3.0F, y + (double)3.0F, z + (double)offset, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)3.0F, y - (double)2.0F, z + (double)offset, 0.0F, 1.0F);
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
      float offset = 0.5F;
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
            tessellator.addVertex(x + (double)offset, y + (double)3.0F, z - (double)2.0F);
            tessellator.addVertex(x + (double)offset, y + (double)3.0F, z + (double)3.0F);
            tessellator.addVertex(x + (double)offset, y - (double)2.0F, z + (double)3.0F);
            tessellator.addVertex(x + (double)offset, y - (double)2.0F, z - (double)2.0F);
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
         tessellator.addVertexWithUV(x + (double)offset, y + (double)3.0F, z - (double)2.0F, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)3.0F, z + (double)3.0F, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y - (double)2.0F, z + (double)3.0F, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y - (double)2.0F, z - (double)2.0F, 0.0F, 1.0F);
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
      float offset = 0.5F;
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
            tessellator.addVertex(x + (double)offset, y - (double)2.0F, z - (double)2.0F);
            tessellator.addVertex(x + (double)offset, y - (double)2.0F, z + (double)3.0F);
            tessellator.addVertex(x + (double)offset, y + (double)3.0F, z + (double)3.0F);
            tessellator.addVertex(x + (double)offset, y + (double)3.0F, z - (double)2.0F);
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
         tessellator.addVertexWithUV(x + (double)offset, y - (double)2.0F, z - (double)2.0F, 1.0F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y - (double)2.0F, z + (double)3.0F, 1.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)3.0F, z + (double)3.0F, 0.0F, 0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)3.0F, z - (double)2.0F, 0.0F, 1.0F);
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
}
