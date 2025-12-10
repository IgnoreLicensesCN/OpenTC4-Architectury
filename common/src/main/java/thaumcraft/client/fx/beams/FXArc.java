package thaumcraft.client.fx.beams;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.utils.Utils;

public class FXArc extends EntityFX {
   public int particle = 16;
   ArrayList points = new ArrayList<>();
   private Entity targetEntity = null;
   private double tX = 0.0F;
   private double tY = 0.0F;
   private double tZ = 0.0F;
   public int blendmode = 1;
   public float length = 1.0F;

   public FXArc(World par1World, double x, double y, double z, double tx, double ty, double tz, float red, float green, float blue, double hg) {
      super(par1World, x, y, z, 0.0F, 0.0F, 0.0F);
      this.particleRed = red;
      this.particleGreen = green;
      this.particleBlue = blue;
      this.setSize(0.02F, 0.02F);
      this.noClip = true;
      this.motionX = 0.0F;
      this.motionY = 0.0F;
      this.motionZ = 0.0F;
      this.tX = tx - x;
      this.tY = ty - y;
      this.tZ = tz - z;
      this.particleMaxAge = 1;
      double xx = 0.0F;
      double yy = 0.0F;
      double zz = 0.0F;
      double gravity = 0.115;
      double noise = 0.25F;
      Vec3 vs = Vec3.createVectorHelper(xx, yy, zz);
      Vec3 ve = Vec3.createVectorHelper(this.tX, this.tY, this.tZ);
      Vec3 vc = Vec3.createVectorHelper(xx, yy, zz);
      this.length = (float)ve.lengthVector();
      Vec3 vv = Utils.calculateVelocity(vs, ve, hg, gravity);
      double l = Utils.distanceSquared3d(Vec3.createVectorHelper(0.0F, 0.0F, 0.0F), vv);
      this.points.add(vs);

      for(int c = 0; Utils.distanceSquared3d(ve, vc) > l && c < 50; ++c) {
         Vec3 vt = vc.addVector(vv.xCoord, vv.yCoord, vv.zCoord);
         vc = Vec3.createVectorHelper(vt.xCoord, vt.yCoord, vt.zCoord);
         vt.xCoord += (this.rand.nextDouble() - this.rand.nextDouble()) * noise;
         vt.yCoord += (this.rand.nextDouble() - this.rand.nextDouble()) * noise;
         vt.zCoord += (this.rand.nextDouble() - this.rand.nextDouble()) * noise;
         this.points.add(vt);
         vv.yCoord -= gravity / 1.9;
      }

      this.points.add(ve);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

   }

   public void setRGB(float r, float g, float b) {
      this.particleRed = r;
      this.particleGreen = g;
      this.particleBlue = b;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      tessellator.draw();
      GL11.glPushMatrix();
      double ePX = this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX;
      double ePY = this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY;
      double ePZ = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ;
      GL11.glTranslated(ePX, ePY, ePZ);
      float size = 0.25F;
      UtilsFX.bindTexture("textures/misc/beamh.png");
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(2884);
      tessellator.startDrawing(5);
      tessellator.setBrightness(200);
      tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 0.8F);
      float f9 = 0.0F;
      float f10 = 1.0F;

      for(int c = 0; c < this.points.size(); ++c) {
         Vec3 v = (Vec3)this.points.get(c);
         float f13 = (float)c / this.length;
         double dx = v.xCoord;
         double dy = v.yCoord;
         double dz = v.zCoord;
         tessellator.addVertexWithUV(dx, dy - (double)size, dz, f13, f10);
         tessellator.addVertexWithUV(dx, dy + (double)size, dz, f13, f9);
      }

      tessellator.draw();
      tessellator.startDrawing(5);
      tessellator.setBrightness(200);
      tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 0.8F);

      for(int c = 0; c < this.points.size(); ++c) {
         Vec3 v = (Vec3)this.points.get(c);
         float f13 = (float)c / this.length;
         double dx = v.xCoord;
         double dy = v.yCoord;
         double dz = v.zCoord;
         tessellator.addVertexWithUV(dx - (double)size, dy, dz - (double)size, f13, f10);
         tessellator.addVertexWithUV(dx + (double)size, dy, dz + (double)size, f13, f9);
      }

      tessellator.draw();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(2884);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
      tessellator.startDrawingQuads();
   }
}
