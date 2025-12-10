package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.config.ConfigBlocks;

public class FXSwarm extends EntityFX {
   private Entity target;
   private float turnSpeed;
   private float speed;
   int deathtimer;
   private static ArrayList<Long> buzzcount = new ArrayList<>();
   public int particle;

   public FXSwarm(World par1World, double x, double y, double z, Entity target, float r, float g, float b) {
      super(par1World, x, y, z, 0.0F, 0.0F, 0.0F);
      this.turnSpeed = 10.0F;
      this.speed = 0.2F;
      this.deathtimer = 0;
      this.particle = 40;
      this.particleRed = r;
      this.particleGreen = g;
      this.particleBlue = b;
      this.particleScale = this.rand.nextFloat() * 0.5F + 1.0F;
      this.target = target;
      float f3 = 0.2F;
      this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * f3;
      this.motionY = (this.rand.nextFloat() - this.rand.nextFloat()) * f3;
      this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * f3;
      this.particleGravity = 0.1F;
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

   public FXSwarm(World par1World, double x, double y, double z, Entity target, float r, float g, float b, float sp, float ts, float pg) {
      this(par1World, x, y, z, target, r, g, b);
      this.speed = sp;
      this.turnSpeed = ts;
      this.particleGravity = pg;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      float bob = MathHelper.sin((float)this.particleAge / 3.0F) * 0.25F + 1.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      int part = 7 + this.particleAge % 8;
      float var8 = (float)part / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.25F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.1F * this.particleScale * bob;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      float trans = (50.0F - (float)this.deathtimer) / 50.0F;
      tessellator.setBrightness(240);
      if (this.target instanceof EntityLivingBase && ((EntityLivingBase)this.target).hurtTime <= 0) {
         tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, trans);
      } else {
         tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16 / 2.0F, this.particleBlue * var16 / 2.0F, trans);
      }

      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
   }

   public int getFXLayer() {
      return 1;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      ++this.particleAge;
      if (this.target == null || this.target.isDead || this.target instanceof EntityLivingBase && ((EntityLivingBase)this.target).deathTime > 0) {
         ++this.deathtimer;
         this.motionY -= this.particleGravity / 2.0F;
         if (this.deathtimer > 50) {
            this.setDead();
         }
      } else {
         this.motionY += this.particleGravity;
      }

      this.pushOutOfBlocks(this.posX, this.posY, this.posZ);
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.985;
      this.motionY *= 0.985;
      this.motionZ *= 0.985;
      if (this.target != null && !this.target.isDead && (!(this.target instanceof EntityLivingBase) || ((EntityLivingBase)this.target).deathTime <= 0)) {
         boolean hurt = false;
         if (this.target instanceof EntityLivingBase) {
            hurt = ((EntityLivingBase)this.target).hurtTime > 0;
         }

         if (this.getDistanceSqToEntity(this.target) > (double)this.target.width && !hurt) {
            this.faceEntity(this.target, this.turnSpeed / 2.0F + (float)this.rand.nextInt((int)(this.turnSpeed / 2.0F)), this.turnSpeed / 2.0F + (float)this.rand.nextInt((int)(this.turnSpeed / 2.0F)));
         } else {
            this.faceEntity(this.target, -(this.turnSpeed / 2.0F + (float)this.rand.nextInt((int)(this.turnSpeed / 2.0F))), -(this.turnSpeed / 2.0F + (float)this.rand.nextInt((int)(this.turnSpeed / 2.0F))));
         }

         this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
         this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
         this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI);
         this.setHeading(this.motionX, this.motionY, this.motionZ, this.speed, 15.0F);
      }

      if (buzzcount.size() < 3 && this.rand.nextInt(50) == 0 && this.worldObj.getClosestPlayerToEntity(this, 8.0F) != null) {
         this.worldObj.playSound(this.posX, this.posY, this.posZ, "thaumcraft:fly", 0.03F, 0.5F + this.rand.nextFloat() * 0.4F, false);
         buzzcount.add(System.nanoTime() + 1500000L);
      }

      if (buzzcount.size() >= 3 && buzzcount.get(0) < System.nanoTime()) {
         buzzcount.remove(0);
      }

   }

   public void faceEntity(Entity par1Entity, float par2, float par3) {
      double d0 = par1Entity.posX - this.posX;
      double d1 = par1Entity.posZ - this.posZ;
      double d2 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / (double)2.0F - (this.boundingBox.minY + this.boundingBox.maxY) / (double)2.0F;
      double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
      float f2 = (float)(Math.atan2(d1, d0) * (double)180.0F / Math.PI) - 90.0F;
      float f3 = (float)(-(Math.atan2(d2, d3) * (double)180.0F / Math.PI));
      this.rotationPitch = this.updateRotation(this.rotationPitch, f3, par3);
      this.rotationYaw = this.updateRotation(this.rotationYaw, f2, par2);
   }

   private float updateRotation(float par1, float par2, float par3) {
      float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);
      if (f3 > par3) {
         f3 = par3;
      }

      if (f3 < -par3) {
         f3 = -par3;
      }

      return par1 + f3;
   }

   public void setHeading(double par1, double par3, double par5, float par7, float par8) {
      float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= f2;
      par3 /= f2;
      par5 /= f2;
      par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
      par3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
      par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
      par1 *= par7;
      par3 *= par7;
      par5 *= par7;
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
   }

   protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      double var10 = par1 - (double)var7;
      double var12 = par3 - (double)var8;
      double var14 = par5 - (double)var9;
      if (this.worldObj.getBlock(var7, var8, var9) != ConfigBlocks.blockTaintFibres && !this.worldObj.isAirBlock(var7, var8, var9) && !this.worldObj.isAnyLiquid(this.boundingBox)) {
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
