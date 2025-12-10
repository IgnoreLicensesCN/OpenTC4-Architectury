package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXBubbleAlt extends EntityFX {
   public int particle = 25;
   public double bubblespeed = 1.0E-4;

   public FXBubbleAlt(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, int age) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      this.particleRed = 1.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 0.5F;
      this.setSize(0.02F, 0.02F);
      this.noClip = true;
      this.particleScale *= this.rand.nextFloat() * 0.3F + 0.2F;
      this.motionX = par8 * (double)0.2F + (double)((float)(Math.random() * (double)2.0F - (double)1.0F) * 0.02F);
      this.motionY = par10 * (double)0.2F + (double)((float)Math.random() * 0.02F);
      this.motionZ = par12 * (double)0.2F + (double)((float)(Math.random() * (double)2.0F - (double)1.0F) * 0.02F);
      this.particleMaxAge = (int)((double)(age + 2) + (double)8.0F / (Math.random() * 0.8 + 0.2));
      this.particleAge = 0;
      EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
      int visibleDistance = 50;
      if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
         visibleDistance = 25;
      }

      if (renderentity.getDistance(this.posX, this.posY, this.posZ) > (double)visibleDistance) {
         this.particleMaxAge = 0;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionX += (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.001F;
      this.motionZ += (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.001F;
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      this.motionX *= 0.85F;
      this.motionY *= 0.85F;
      this.motionZ *= 0.85F;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      if (this.particleAge == this.particleMaxAge - 2) {
         this.particle = 17;
      } else if (this.particleAge == this.particleMaxAge - 1) {
         this.particle = 18;
      }

   }

   public void setRGB(float r, float g, float b) {
      this.particleRed = r;
      this.particleGreen = g;
      this.particleBlue = b;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.particleAlpha);
      float var8 = (float)(this.particle % 16) / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = (float)(this.particle / 16) / 16.0F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.2F * this.particleScale * ((float)this.particleAge / (float)this.particleMaxAge);
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, this.particleAlpha);
      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
   }
}
