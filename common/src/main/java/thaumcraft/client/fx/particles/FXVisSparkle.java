package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXVisSparkle extends EntityFX {
   private double targetX;
   private double targetY;
   private double targetZ;
   float sizeMod = 0.0F;

   public FXVisSparkle(World par1World, double par2, double par4, double par6, double tx, double ty, double tz) {
      super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
      this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
      this.particleScale = 0.0F;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      this.particleMaxAge = 1000;
      float f3 = 0.01F;
      this.motionX = (float)this.rand.nextGaussian() * f3;
      this.motionY = (float)this.rand.nextGaussian() * f3;
      this.motionZ = (float)this.rand.nextGaussian() * f3;
      this.sizeMod = (float)(45 + this.rand.nextInt(15));
      this.particleRed = 0.2F;
      this.particleGreen = 0.6F + this.rand.nextFloat() * 0.3F;
      this.particleBlue = 0.2F;
      this.particleGravity = 0.2F;
      this.noClip = true;
      EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
      int visibleDistance = 64;
      if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
         visibleDistance = 32;
      }

      if (renderentity.getDistance(this.posX, this.posY, this.posZ) > (double)visibleDistance) {
         this.particleMaxAge = 0;
      }

   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      float bob = MathHelper.sin((float)this.particleAge / 3.0F) * 0.3F + 6.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      int part = this.particleAge % 16;
      float var8 = (float)part / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.5F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.1F * this.particleScale * bob;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 0.5F);
      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      } else {
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.985;
         this.motionY *= 0.985;
         this.motionZ *= 0.985;
         double dx = this.targetX - this.posX;
         double dy = this.targetY - this.posY;
         double dz = this.targetZ - this.posZ;
         double d13 = 0.1F;
         double d11 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
         if (d11 < (double)2.0F) {
            this.particleScale *= 0.95F;
         }

         if (d11 < 0.2) {
            this.particleMaxAge = this.particleAge;
         }

         if (this.particleAge < 10) {
            this.particleScale = (float)this.particleAge / this.sizeMod;
         }

         dx /= d11;
         dy /= d11;
         dz /= d11;
         this.motionX += dx * d13;
         this.motionY += dy * d13;
         this.motionZ += dz * d13;
         this.motionX = MathHelper.clamp_float((float)this.motionX, -0.1F, 0.1F);
         this.motionY = MathHelper.clamp_float((float)this.motionY, -0.1F, 0.1F);
         this.motionZ = MathHelper.clamp_float((float)this.motionZ, -0.1F, 0.1F);
      }
   }

   public void setGravity(float value) {
      this.particleGravity = value;
   }
}
