package thaumcraft.client.fx.migrated.beams;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FXArc extends ThaumcraftParticle {
   public int particle = 16;
   List<Vec3> points = new ArrayList<>();
   private Entity targetEntity = null;
   private double tX = 0.0F;
   private double tY = 0.0F;
   private double tZ = 0.0F;
   public int blendmode = 1;
   public float length = 1.0F;

   public FXArc(ClientLevel par1World, double x, double y, double z, double tx, double ty, double tz, float red, float green, float blue, double hg) {
      super(par1World, x, y, z, 0.0F, 0.0F, 0.0F);
      this.rCol = red;
      this.gCol = green;
      this.bCol = blue;
      this.setSize(0.02F, 0.02F);
      this.hasPhysics = false;
      this.xd = 0.0F;
      this.yd = 0.0F;
      this.zd = 0.0F;
      this.tX = tx - x;
      this.tY = ty - y;
      this.tZ = tz - z;
      this.lifetime = 1;
      double xx = 0.0F;
      double yy = 0.0F;
      double zz = 0.0F;
      double gravity = 0.115;
      double noise = 0.25F;
      Vec3 vs = new Vec3(xx, yy, zz);
      Vec3 ve = new Vec3(this.tX, this.tY, this.tZ);
      Vec3 vc = new Vec3(xx, yy, zz);
      this.length = (float) ve.length();
      Vec3 vv = Utils.calculateVelocity(vs, ve, hg, gravity);
      double l = Utils.distanceSquared3d(new Vec3(0.0F, 0.0F, 0.0F), vv);
      this.points.add(vs);

      for(int c = 0; Utils.distanceSquared3d(ve, vc) > l && c < 50; ++c) {
         Vec3 vt = vc.add(vv.x, vv.y, vv.z).add((this.random.nextDouble() - this.random.nextDouble()) * noise,(this.random.nextDouble() - this.random.nextDouble()) * noise,(this.random.nextDouble() - this.random.nextDouble()) * noise);
         vc = new Vec3(vt.x, vt.y, vt.z);
         this.points.add(vt);
         vv = vv.add(0,-gravity / 1.9,0);
      }

      this.points.add(ve);
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.setDead();
      }

   }

   @Override
   public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

      var rightVec = camera.getLeftVector().negate();
      var f1 = rightVec.x;
      var f3 = rightVec.z;
      var upVec = camera.getUpVector();
      var f4 = upVec.x;
      var f2 = upVec.y;
      var f5 = upVec.z;
      var f = partialTicks;
      var cameraPos = camera.getPosition();
      var interpPosX = cameraPos.x;
      var interpPosY = cameraPos.y;
      var interpPosZ = cameraPos.z;

      PoseStack pose = new PoseStack();
      pose.pushPose();

//      tessellator.draw();
//      GL11.glPushMatrix();
      double ePX = this.xo + (this.x - this.xo) * (double) f - interpPosX;
      double ePY = this.yo + (this.y - this.yo) * (double) f - interpPosY;
      double ePZ = this.zo + (this.z - this.zo) * (double) f - interpPosZ;
//      GL11.glTranslated(ePX, ePY, ePZ);

      pose.translate(ePX, ePY, ePZ);
      float size = 0.25F;
      UtilsFX.bindTexture("textures/misc/beamh.png");
//      GL11.glDepthMask(false);
//      GL11.glEnable(GL11.GL_BLEND);
//      GL11.glBlendFunc(770, 1);
//      GL11.glDisable(2884);
//      tessellator.startDrawing(5);
//      tessellator.setBrightness(200);
//      tessellator.setColorRGBA_F(this.rCol, this.gCol, this.bCol, 0.8F);
      float f9 = 0.0F;
      float f10 = 1.0F;

      for(int c = 0; c < this.points.size(); ++c) {
         Vec3 v = (Vec3) this.points.get(c);
         float f13 = (float) c / this.length;
         double dx = v.x;
         double dy = v.y;
         double dz = v.z;
//         tessellator.addVertexWithUV(dx, dy - (double)size, dz, f13, f10);
//         tessellator.addVertexWithUV(dx, dy + (double)size, dz, f13, f9);
         consumer.vertex(dx, dy - (double)size, dz).uv(f13, f10).uv2(200 & 0xFFFF,(200 >> 16) & 0xFFFF).color(this.rCol, this.gCol, this.bCol, 0.8F).endVertex();
         consumer.vertex(dx, dy + (double)size, dz).uv(f13, f9).uv2(200 & 0xFFFF,(200 >> 16) & 0xFFFF).color(this.rCol, this.gCol, this.bCol, 0.8F).endVertex();
      }

//      tessellator.draw();
//      tessellator.startDrawing(5);
//      tessellator.setBrightness(200);
//      tessellator.setColorRGBA_F(this.rCol, this.gCol, this.bCol, 0.8F);

      for(int c = 0; c < this.points.size(); ++c) {
         Vec3 v = this.points.get(c);
         float f13 = (float) c / this.length;
         double dx = v.x;
         double dy = v.y;
         double dz = v.z;
         consumer.vertex(dx - (double)size, dy, dz - (double)size).uv(f13, f10).uv2(200 & 0xFFFF,(200 >> 16) & 0xFFFF).color(this.rCol, this.gCol, this.bCol, 0.8F).endVertex();
         consumer.vertex(dx - (double)size, dy, dz - (double)size).uv(f13, f9).uv2(200 & 0xFFFF,(200 >> 16) & 0xFFFF).color(this.rCol, this.gCol, this.bCol, 0.8F).endVertex();
      }

//      tessellator.draw();
//      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//      GL11.glEnable(2884);
//      GL11.glDisable(GL11.GL_BLEND);
//      GL11.glDepthMask(true);
//      GL11.glPopMatrix();
      pose.popPose();
//      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
//      tessellator.startDrawingQuads();
   }
}
