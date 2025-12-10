package thaumcraft.client.fx.migrated.beams;


import com.linearity.opentc4.OpenTC4CommonProxy;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;
import thaumcraft.client.lib.UtilsFX;

public class FXBeamPower extends ThaumcraftParticle {
   public int particle = 16;
   private double offset = 0.0F;
   private double tX = 0.0F;
   private double tY = 0.0F;
   private double tZ = 0.0F;
   private double ptX = 0.0F;
   private double ptY = 0.0F;
   private double ptZ = 0.0F;
   private float length = 0.0F;
   private float rotYaw = 0.0F;
   private float rotPitch = 0.0F;
   private float prevYaw = 0.0F;
   private float prevPitch = 0.0F;
   private Entity targetEntity = null;
   private float opacity = 0.3F;
   private float prevSize = 0.0F;

   public FXBeamPower(ClientLevel par1World, double px, double py, double pz, double tx, double ty, double tz, float red, float green, float blue, int age) {
      super(par1World, px, py, pz, 0.0F, 0.0F, 0.0F);
      this.rCol = 0.5F;
      this.gCol = 0.5F;
      this.bCol = 0.5F;
      this.setSize(0.02F, 0.02F);
      this.hasPhysics = false;
      this.xd = 0.0F;
      this.yd = 0.0F;
      this.zd = 0.0F;
      this.tX = tx;
      this.tY = ty;
      this.tZ = tz;
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      this.lifetime = age;
      removeIfTooFar();

   }

   public void updateBeam(double xx, double yy, double zz, double x, double y, double z) {
      this.setPosition(xx, yy, zz);
      this.tX = x;
      this.tY = y;

      for(this.tZ = z; this.lifetime - this.age < 4; ++this.lifetime) {
      }

   }

   public void onUpdate() {
      this.xo = this.x;
      this.yo = this.y + this.offset;
      this.zo = this.z;
      this.ptX = this.tX;
      this.ptY = this.tY;
      this.ptZ = this.tZ;
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      float xd = (float)(this.x - this.tX);
      float yd = (float)(this.y - this.tY);
      float zd = (float)(this.z - this.tZ);
      this.length = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
      double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
      this.rotYaw = (float) (Math.atan2(xd, zd) * (double) 180.0F / Math.PI);
      this.rotPitch = (float) (Math.atan2(yd, var7) * (double) 180.0F / Math.PI);
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      if (this.opacity > 0.3F) {
         this.opacity -= 0.025F;
      }

      if (this.opacity < 0.3F) {
         this.opacity = 0.3F;
      }

      if (this.age++ >= this.lifetime) {
         this.setDead();
      }

   }

   public void setPulse(boolean pulse, float r, float g, float b) {
      this.setRGB(r, g, b);
      if (pulse) {
         this.opacity = 0.8F;
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
      PoseStack stack = new PoseStack();
//      GL11.glPushMatrix();
      stack.pushPose();
      float var9 = 1.0F;
      float slide = OpenTC4CommonProxy.INSTANCE.getLocalPlayerTicks();
      float size = 0.7F;
      UtilsFX.bindTexture("textures/misc/beam1.png");
      RenderSystem.texParameter(3553, 10242, 10497);
      RenderSystem.texParameter(3553, 10243, 10497);
      RenderSystem.disableCull();
//      GL11.glTexParameterf(3553, 10242, 10497.0F);
//      GL11.glTexParameterf(3553, 10243, 10497.0F);
//      GL11.glDisable(2884);
      float var11 = slide + f;
      float var12 = -var11 * 0.2F - (float)MathHelper.floor_float(-var11 * 0.1F);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
      RenderSystem.depthMask(false);
//      GL11.glEnable(GL11.GL_BLEND);
//      GL11.glBlendFunc(770, 1);
//      GL11.glDepthMask(false);
      float xx = (float)(this.xo + (this.x - this.xo) * (double) f - interpPosX);
      float yy = (float)(this.yo + (this.y - this.yo) * (double) f - interpPosY);
      float zz = (float)(this.zo + (this.z - this.zo) * (double) f - interpPosZ);
      GL11.glTranslated(xx, yy, zz);
      float ry = (float)((double) this.prevYaw + (double)(this.rotYaw - this.prevYaw) * (double)f);
      float rp = (float)((double) this.prevPitch + (double)(this.rotPitch - this.prevPitch) * (double)f);

      stack.mulPose(Axis.XP.rotationDegrees(90.0F));
      stack.mulPose(Axis.ZP.rotationDegrees(180.0F + ry));
      stack.mulPose(Axis.XP.rotationDegrees(rp));
//      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
//      GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
//      GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);
      double var44 = -0.15 * (double)size;
      double var17 = 0.15 * (double)size;
      float opmod = getOpModViaPlayer(0.1f);


      for(int t = 0; t < 2; ++t) {
         double var29 = this.length * var9;
         double var31 = 0.0F;
         double var33 = 1.0F;
         double var35 = -1.0F + var12 + (float)t / 3.0F;
         double var37 = (double)(this.length * var9) + var35;

         stack.mulPose(Axis.YP.rotationDegrees(90.0F));
         consumer.vertex(var44, var29, 0.0F)
                 .color(this.rCol, this.gCol, this.bCol, this.opacity * opmod)
                 .uv((float) var33, (float) var37)
                 .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                 .endVertex();
         consumer.vertex(var44, 0.0F, 0.0F)
                 .color(this.rCol, this.gCol, this.bCol, this.opacity * opmod)
                 .uv((float) var33, (float) var35)
                 .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                 .endVertex();
         consumer.vertex(var17, 0.0F, 0.0F)
                 .color(this.rCol, this.gCol, this.bCol, this.opacity * opmod)
                 .uv((float) var31, (float) var35)
                 .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                 .endVertex();
         consumer.vertex(var17, var29, 0.0F)
                 .color(this.rCol, this.gCol, this.bCol, this.opacity * opmod)
                 .uv((float) var31, (float) var37)
                 .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                 .endVertex();
//         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
//         tessellator.startDrawingQuads();
//         tessellator.setBrightness(200);
//         tessellator.setColorRGBA_F(this.rCol, this.gCol, this.bCol, this.opacity * opmod);
//         tessellator.addVertexWithUV(var44, var29, 0.0F, var33, var37);
//         tessellator.addVertexWithUV(var44, 0.0F, 0.0F, var33, var35);
//         tessellator.addVertexWithUV(var17, 0.0F, 0.0F, var31, var35);
//         tessellator.addVertexWithUV(var17, var29, 0.0F, var31, var37);
//         tessellator.draw();
      }

      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      stack.popPose();
//      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//      GL11.glDepthMask(true);
//      GL11.glDisable(GL11.GL_BLEND);
//      GL11.glEnable(2884);
//      GL11.glPopMatrix();
      this.renderFlare(consumer, f, f1, f2, f3, f4, f5,camera);
//      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
//      tessellator.startDrawingQuads();
      this.prevSize = size;
   }

   public void renderFlare(VertexConsumer consumer, float f, float f1, float f2, float f3, float f4, float f5,Camera camera) {
      var interpPosX = camera.getPosition().x;
      var interpPosY = camera.getPosition().y;
      var interpPosZ = camera.getPosition().z;

      float opmod = getOpModViaPlayer(0.2f);

      PoseStack stack = new PoseStack();
      stack.pushPose();
//      GL11.glPushMatrix();
      RenderSystem.depthMask(false);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
//      GL11.glDepthMask(false);
//      GL11.glEnable(GL11.GL_BLEND);
//      GL11.glBlendFunc(770, 1);
      UtilsFX.bindTexture(UtilsFX.getParticleTexture());
//      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F);
      int part = this.age % 16;
      float var8 = (float)part / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.3125F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.66F * this.opacity;
      float var13 = (float)(this.ptX + (this.tX - this.ptX) * (double) f - interpPosX);
      float var14 = (float)(this.ptY + (this.tY - this.ptY) * (double) f - interpPosY);
      float var15 = (float)(this.ptZ + (this.tZ - this.ptZ) * (double) f - interpPosZ);

      consumer.vertex(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12)
              .color(this.rCol, this.gCol, this.bCol, 0.66F)
              .uv( var9,  var11)
              .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
              .endVertex();
      consumer.vertex(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12)
              .color(this.rCol, this.gCol, this.bCol, 0.66F)
              .uv(var9, var10)
              .uv2(LightTexture.FULL_BRIGHT)
              .endVertex();
      consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12)
              .color(this.rCol, this.gCol, this.bCol, 0.66F)
              .uv(var8, var10)
              .uv2(LightTexture.FULL_BRIGHT)
              .endVertex();
      consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
              .color(this.rCol, this.gCol, this.bCol, 0.66F)
              .uv(var8, var11)
              .uv2(LightTexture.FULL_BRIGHT)
              .endVertex();

      RenderSystem.blendFunc(770, 771);
      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      stack.popPose();
//      GL11.glBlendFunc(770, 771);
//      GL11.glDisable(GL11.GL_BLEND);
//      GL11.glDepthMask(true);
//      GL11.glPopMatrix();
   }
}
