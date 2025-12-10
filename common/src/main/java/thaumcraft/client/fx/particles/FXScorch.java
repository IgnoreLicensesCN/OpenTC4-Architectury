package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class FXScorch extends EntityFX {
   public boolean pvp = true;
   public boolean mobs = true;
   public boolean animals = true;
   private double px;
   private double py;
   private double pz;
   private float transferParticleScale;
   Entity partDestEnt;
   public boolean lance = false;

   public FXScorch(World world, double x, double y, double z, Vec3 v, float spread, boolean lance) {
      super(world, x, y, z, 0.0F, 0.0F, 0.0F);
      this.posX = x;
      this.posY = y;
      this.posZ = z;
      this.lance = lance;
      this.px = x + v.xCoord * (double)100.0F;
      this.py = y + v.yCoord * (double)100.0F;
      this.pz = z + v.zCoord * (double)100.0F;
      if (!lance) {
         this.px += (this.rand.nextFloat() - this.rand.nextFloat()) * spread;
         this.py += (this.rand.nextFloat() - this.rand.nextFloat()) * spread;
         this.pz += (this.rand.nextFloat() - this.rand.nextFloat()) * spread;
      } else {
         this.px += (double)(this.rand.nextFloat() - this.rand.nextFloat()) * (double)0.5F;
         this.py += (double)(this.rand.nextFloat() - this.rand.nextFloat()) * (double)0.5F;
         this.pz += (double)(this.rand.nextFloat() - this.rand.nextFloat()) * (double)0.5F;
      }

      this.transferParticleScale = this.particleScale = this.rand.nextFloat() * 0.5F + 2.0F;
      if (!lance) {
         this.transferParticleScale = this.particleScale = this.rand.nextFloat() + 3.0F;
      }

      this.particleMaxAge = 50;
      this.setSize(0.1F, 0.1F);
      this.setParticleTextureIndex(151);
      this.noClip = false;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.particleAlpha = 0.66F;
   }

   public int getBrightnessForRender(float par1) {
      return 210;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   public void onUpdate() {
      double dx = this.px - this.posX;
      double dy = this.py - this.posY;
      double dz = this.pz - this.posZ;
      double distance = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
      this.motionX = dx / (distance * (double)1.25F);
      this.motionY = dy / (distance * (double)1.25F);
      this.motionZ = dz / (distance * (double)1.25F);
      this.motionX *= (float)(this.particleMaxAge - this.particleAge) / (float)this.particleMaxAge;
      this.motionY *= (float)(this.particleMaxAge - this.particleAge) / (float)this.particleMaxAge;
      this.motionZ *= (float)(this.particleMaxAge - this.particleAge) / (float)this.particleMaxAge;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionX += this.rand.nextFloat() * 0.07F - 0.035F;
      this.motionY += this.rand.nextFloat() * 0.07F - 0.035F;
      this.motionZ += this.rand.nextFloat() * 0.07F - 0.035F;
      int var7 = MathHelper.floor_double(this.posX);
      int var8 = MathHelper.floor_double(this.posY);
      int var9 = MathHelper.floor_double(this.posZ);
      if (this.particleAge > 1 && this.worldObj.getBlock(var7, var8, var9).isOpaqueCube()) {
         this.motionX = 0.0F;
         this.motionY = 0.0F;
         this.motionZ = 0.0F;
         this.particleAge += 10;
      }

      this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / (double)2.0F, this.posZ);
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      ++this.particleAge;
      if (this.particleAge >= this.particleMaxAge) {
         this.setDead();
      }

      float fs = (float)this.particleAge / (float)(this.particleMaxAge - 9);
      if (fs <= 1.0F) {
         this.setParticleTextureIndex((int)(151.0F + fs * 6.0F));
      } else {
         this.setParticleTextureIndex(159 - (this.particleMaxAge - this.particleAge) / 3);
      }

   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      float fs = (float)this.particleAge / (float)this.particleMaxAge;
      this.particleScale = this.transferParticleScale * (fs + 0.25F) * 2.0F;
      float fc = (float)this.particleAge * 9.0F / (float)this.particleMaxAge;
      if (fc > 1.0F) {
         fc = 1.0F;
      }

      this.particleRed = this.particleGreen = fc;
      this.particleBlue = 1.0F;
      super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
   }
}
