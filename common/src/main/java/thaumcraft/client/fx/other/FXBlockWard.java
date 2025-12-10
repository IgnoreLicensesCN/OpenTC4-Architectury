package thaumcraft.client.fx.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

public class FXBlockWard extends EntityFX {
   ForgeDirection side;
   int rotation = 0;
   float sx = 0.0F;
   float sy = 0.0F;
   float sz = 0.0F;

   public FXBlockWard(World world, double d, double d1, double d2, ForgeDirection side, float f, float f1, float f2) {
      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
      this.side = side;
      this.particleGravity = 0.0F;
      this.motionX = this.motionY = this.motionZ = 0.0F;
      this.particleMaxAge = 12 + this.rand.nextInt(5);
      this.noClip = false;
      this.setSize(0.01F, 0.01F);
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.noClip = true;
      this.particleScale = (float)(1.4 + this.rand.nextGaussian() * (double)0.3F);
      this.rotation = this.rand.nextInt(360);
      this.sx = MathHelper.clamp_float(f - 0.6F + this.rand.nextFloat() * 0.2F, -0.4F, 0.4F);
      this.sy = MathHelper.clamp_float(f1 - 0.6F + this.rand.nextFloat() * 0.2F, -0.4F, 0.4F);
      this.sz = MathHelper.clamp_float(f2 - 0.6F + this.rand.nextFloat() * 0.2F, -0.4F, 0.4F);
      if (side.offsetX != 0) {
         this.sx = 0.0F;
      }

      if (side.offsetY != 0) {
         this.sy = 0.0F;
      }

      if (side.offsetZ != 0) {
         this.sz = 0.0F;
      }

   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      tessellator.draw();
      GL11.glPushMatrix();
      float fade = ((float)this.particleAge + f) / (float)this.particleMaxAge;
      int frame = Math.min(15, (int)(15.0F * fade));
      UtilsFX.bindTexture("textures/models/hemis" + frame + ".png");
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.particleAlpha / 2.0F);
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      GL11.glTranslated(var13 + this.sx, var14 + this.sy, var15 + this.sz);
      GL11.glRotatef(90.0F, (float)this.side.offsetY, (float)(-this.side.offsetX), (float)this.side.offsetZ);
      GL11.glRotatef((float)this.rotation, 0.0F, 0.0F, 1.0F);
      if (this.side.offsetZ > 0) {
         GL11.glTranslated(0.0F, 0.0F, 0.505F);
         GL11.glRotatef(180.0F, 0.0F, -1.0F, 0.0F);
      } else {
         GL11.glTranslated(0.0F, 0.0F, -0.505F);
      }

      float var12 = this.particleScale;
      float var16 = 1.0F;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, this.particleAlpha / 2.0F);
      tessellator.addVertexWithUV((double)-0.5F * (double)var12, (double)0.5F * (double)var12, 0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV((double)0.5F * (double)var12, (double)0.5F * (double)var12, 0.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV((double)0.5F * (double)var12, (double)-0.5F * (double)var12, 0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV((double)-0.5F * (double)var12, (double)-0.5F * (double)var12, 0.0F, 0.0F, 0.0F);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
      tessellator.startDrawingQuads();
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      float threshold = (float)this.particleMaxAge / 5.0F;
      if ((float)this.particleAge <= threshold) {
         this.particleAlpha = (float)this.particleAge / threshold;
      } else {
         this.particleAlpha = (float)(this.particleMaxAge - this.particleAge) / (float)this.particleMaxAge;
      }

      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      this.motionY -= 0.04 * (double)this.particleGravity;
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
   }

   public void setGravity(float value) {
      this.particleGravity = value;
   }
}
