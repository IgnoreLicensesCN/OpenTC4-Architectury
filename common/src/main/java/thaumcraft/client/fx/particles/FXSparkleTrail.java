package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXSparkleTrail extends EntityFX {
   private Entity target;
   public int particle = 24;

   public FXSparkleTrail(World par1World, double x, double y, double z, Entity target, float r, float g, float b) {
      super(par1World, x, y, z, 0.0F, 0.0F, 0.0F);
      this.particleRed = r;
      this.particleGreen = g;
      this.particleBlue = b;
      this.particleScale = this.rand.nextFloat() * 0.5F + 0.5F;
      this.target = target;
      double dx = target.posX - this.posX;
      double dy = target.posY - this.posY;
      double dz = target.posZ - this.posZ;
      int base = (int)(MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz) * 3.0F);
      if (base < 1) {
         base = 1;
      }

      this.particleMaxAge = base / 2 + this.rand.nextInt(base);
      float f3 = 0.1F;
      this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * f3;
      this.motionY = (this.rand.nextFloat() - this.rand.nextFloat()) * f3;
      this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * f3;
      this.particleGravity = 0.2F;
      this.noClip = false;
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
      float bob = MathHelper.sin((float)this.particleAge / 3.0F) * 0.5F + 1.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      int part = this.particle + this.particleAge % 16;
      float var8 = (float)(part % 8) / 8.0F;
      float var9 = var8 + 0.124875F;
      float var10 = (float)(part / 8) / 8.0F;
      float var11 = var10 + 0.124875F;
      float var12 = 0.1F * this.particleScale * bob;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 1.0F);
      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ < this.particleMaxAge && !(this.getDistanceSqToEntity(this.target) < (double)1.0F)) {
         if (!this.noClip) {
            this.pushOutOfBlocks(this.posX, this.posY, this.posZ);
         }

         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.985;
         this.motionY *= 0.985;
         this.motionZ *= 0.985;
         double dx = this.target.posX - this.posX;
         double dy = this.target.posY - this.posY;
         double dz = this.target.posZ - this.posZ;
         double d13 = 0.3;
         double d11 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
         if (d11 < (double)4.0F) {
            this.particleScale *= 0.9F;
            d13 = 0.6;
         }

         dx /= d11;
         dy /= d11;
         dz /= d11;
         this.motionX += dx * d13;
         this.motionY += dy * d13;
         this.motionZ += dz * d13;
         this.motionX = MathHelper.clamp_float((float)this.motionX, -0.35F, 0.35F);
         this.motionY = MathHelper.clamp_float((float)this.motionY, -0.35F, 0.35F);
         this.motionZ = MathHelper.clamp_float((float)this.motionZ, -0.35F, 0.35F);
      } else {
         this.setDead();
      }
   }

   public void setGravity(float value) {
      this.particleGravity = value;
   }

   protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      double var10 = par1 - (double)var7;
      double var12 = par3 - (double)var8;
      double var14 = par5 - (double)var9;
      if (!this.worldObj.isAirBlock(var7, var8, var9) && !this.worldObj.isAnyLiquid(this.boundingBox)) {
         boolean var16 = !this.worldObj.isBlockNormalCubeDefault(var7 - 1, var8, var9, true);
         boolean var17 = !this.worldObj.isBlockNormalCubeDefault(var7 + 1, var8, var9, true);
         boolean var18 = !this.worldObj.isBlockNormalCubeDefault(var7, var8 - 1, var9, true);
         boolean var19 = !this.worldObj.isBlockNormalCubeDefault(var7, var8 + 1, var9, true);
         boolean var20 = !this.worldObj.isBlockNormalCubeDefault(var7, var8, var9 - 1, true);
         boolean var21 = !this.worldObj.isBlockNormalCubeDefault(var7, var8, var9 + 1, true);
         byte var22 = -1;
         double var23 = 9999.0F;
         if (var16 && var10 < var23) {
            var23 = var10;
            var22 = 0;
         }

         if (var17 && (double)1.0F - var10 < var23) {
            var23 = (double)1.0F - var10;
            var22 = 1;
         }

         if (var18 && var12 < var23) {
            var23 = var12;
            var22 = 2;
         }

         if (var19 && (double)1.0F - var12 < var23) {
            var23 = (double)1.0F - var12;
            var22 = 3;
         }

         if (var20 && var14 < var23) {
            var23 = var14;
            var22 = 4;
         }

         if (var21 && (double)1.0F - var14 < var23) {
            var23 = (double)1.0F - var14;
            var22 = 5;
         }

         float var25 = this.rand.nextFloat() * 0.05F + 0.025F;
         float var26 = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
         if (var22 == 0) {
            this.motionX = -var25;
            this.motionY = this.motionZ = var26;
         }

         if (var22 == 1) {
            this.motionX = var25;
            this.motionY = this.motionZ = var26;
         }

         if (var22 == 2) {
            this.motionY = -var25;
            this.motionX = this.motionZ = var26;
         }

         if (var22 == 3) {
            this.motionY = var25;
            this.motionX = this.motionZ = var26;
         }

         if (var22 == 4) {
            this.motionZ = -var25;
            this.motionY = this.motionX = var26;
         }

         if (var22 == 5) {
            this.motionZ = var25;
            this.motionY = this.motionX = var26;
         }

         return true;
      } else {
         return false;
      }
   }
}
