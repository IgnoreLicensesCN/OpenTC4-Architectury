package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.golems.EntityGolemBobber;

@SideOnly(Side.CLIENT)
public class RenderGolemBobber extends Render {
   private static final ResourceLocation tex = new ResourceLocation("textures/particle/particles.png");

   public void doRender(EntityGolemBobber bobber, double xx, double yy, double zz, float p_147922_8_, float p_147922_9_) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)xx, (float)yy, (float)zz);
      GL11.glEnable(32826);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.bindEntityTexture(bobber);
      Tessellator tessellator = Tessellator.instance;
      byte b0 = 1;
      byte b1 = 2;
      float f2 = (float)(b0 * 8) / 128.0F;
      float f3 = (float)(b0 * 8 + 8) / 128.0F;
      float f4 = (float)(b1 * 8) / 128.0F;
      float f5 = (float)(b1 * 8 + 8) / 128.0F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0F, f2, f5);
      tessellator.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0F, f3, f5);
      tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0F, f3, f4);
      tessellator.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0F, f2, f4);
      tessellator.draw();
      GL11.glDisable(32826);
      GL11.glPopMatrix();
      if (bobber.fisher != null) {
         float f9 = (float)bobber.fisher.rightArm / 3.0F;
         float f10 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
         Vec3 vec3 = Vec3.createVectorHelper(-0.5F, 0.03, 0.8);
         vec3.rotateAroundX(-(bobber.fisher.prevRotationPitch + (bobber.fisher.rotationPitch - bobber.fisher.prevRotationPitch) * p_147922_9_) * (float)Math.PI / 180.0F);
         vec3.rotateAroundY(-(bobber.fisher.prevRotationYaw + (bobber.fisher.rotationYaw - bobber.fisher.prevRotationYaw) * p_147922_9_) * (float)Math.PI / 180.0F);
         vec3.rotateAroundY(f10 * 0.5F);
         vec3.rotateAroundX(-f10 * 0.7F);
         double d3 = bobber.fisher.prevPosX + (bobber.fisher.posX - bobber.fisher.prevPosX) * (double)p_147922_9_ + vec3.xCoord;
         double d4 = bobber.fisher.prevPosY + (bobber.fisher.posY - bobber.fisher.prevPosY) * (double)p_147922_9_ + vec3.yCoord;
         double d5 = bobber.fisher.prevPosZ + (bobber.fisher.posZ - bobber.fisher.prevPosZ) * (double)p_147922_9_ + vec3.zCoord;
         double d6 = bobber.fisher.getEyeHeight();
         float f11 = (bobber.fisher.prevRenderYawOffset + (bobber.fisher.renderYawOffset - bobber.fisher.prevRenderYawOffset) * p_147922_9_) * (float)Math.PI / 180.0F;
         double d7 = MathHelper.sin(f11);
         double d9 = MathHelper.cos(f11);
         d3 = bobber.fisher.prevPosX + (bobber.fisher.posX - bobber.fisher.prevPosX) * (double)p_147922_9_ - d9 * (double)0.25F - d7 * 0.7;
         d4 = bobber.fisher.prevPosY + d6 + (bobber.fisher.posY - bobber.fisher.prevPosY) * (double)p_147922_9_ - 0.4;
         d5 = bobber.fisher.prevPosZ + (bobber.fisher.posZ - bobber.fisher.prevPosZ) * (double)p_147922_9_ - d7 * (double)0.25F + d9 * 0.7;
         double d14 = bobber.prevPosX + (bobber.posX - bobber.prevPosX) * (double)p_147922_9_;
         double d8 = bobber.prevPosY + (bobber.posY - bobber.prevPosY) * (double)p_147922_9_ + (double)0.25F;
         double d10 = bobber.prevPosZ + (bobber.posZ - bobber.prevPosZ) * (double)p_147922_9_;
         double d11 = (float)(d3 - d14);
         double d12 = (float)(d4 - d8);
         double d13 = (float)(d5 - d10);
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         tessellator.startDrawing(3);
         tessellator.setColorOpaque_I(0);
         byte b2 = 16;

         for(int i = 0; i <= b2; ++i) {
            float f12 = (float)i / (float)b2;
            tessellator.addVertex(xx + d11 * (double)f12, yy + d12 * (double)(f12 * f12 + f12) * (double)0.5F + (double)0.25F, zz + d13 * (double)f12);
         }

         tessellator.draw();
         GL11.glEnable(2896);
         GL11.glEnable(3553);
      }

   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return tex;
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRender((EntityGolemBobber)par1Entity, par2, par4, par6, par8, par9);
   }
}
