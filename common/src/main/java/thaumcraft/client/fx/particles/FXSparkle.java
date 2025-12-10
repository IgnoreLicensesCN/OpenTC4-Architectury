package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXSparkle extends EntityFX {
   public boolean leyLineEffect;
   public int multiplier;
   public boolean shrink;
   public int particle;
   public boolean tinkle;
   public int blendmode;
   public boolean slowdown;
   public int currentColor;

   public FXSparkle(World world, double d, double d1, double d2, float f, float f1, float f2, float f3, int m) {
      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
      this.leyLineEffect = false;
      this.multiplier = 2;
      this.shrink = true;
      this.particle = 16;
      this.tinkle = false;
      this.blendmode = 1;
      this.slowdown = true;
      this.currentColor = 0;
      if (f1 == 0.0F) {
         f1 = 1.0F;
      }

      this.particleRed = f1;
      this.particleGreen = f2;
      this.particleBlue = f3;
      this.particleGravity = 0.0F;
      this.motionX = this.motionY = this.motionZ = 0.0F;
      this.particleScale *= f;
      this.particleMaxAge = 3 * m;
      this.multiplier = m;
      this.noClip = false;
      this.setSize(0.01F, 0.01F);
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
   }

   public FXSparkle(World world, double d, double d1, double d2, float f, int type, int m) {
      this(world, d, d1, d2, f, 0.0F, 0.0F, 0.0F, m);
      this.currentColor = type;
      switch (type) {
         case 0:
            this.particleRed = 0.75F + world.rand.nextFloat() * 0.25F;
            this.particleGreen = 0.25F + world.rand.nextFloat() * 0.25F;
            this.particleBlue = 0.75F + world.rand.nextFloat() * 0.25F;
            break;
         case 1:
            this.particleRed = 0.5F + world.rand.nextFloat() * 0.3F;
            this.particleGreen = 0.5F + world.rand.nextFloat() * 0.3F;
            this.particleBlue = 0.2F;
            break;
         case 2:
            this.particleRed = 0.2F;
            this.particleGreen = 0.2F;
            this.particleBlue = 0.7F + world.rand.nextFloat() * 0.3F;
            break;
         case 3:
            this.particleRed = 0.2F;
            this.particleGreen = 0.7F + world.rand.nextFloat() * 0.3F;
            this.particleBlue = 0.2F;
            break;
         case 4:
            this.particleRed = 0.7F + world.rand.nextFloat() * 0.3F;
            this.particleGreen = 0.2F;
            this.particleBlue = 0.2F;
            break;
         case 5:
            this.blendmode = 771;
            this.particleRed = world.rand.nextFloat() * 0.1F;
            this.particleGreen = world.rand.nextFloat() * 0.1F;
            this.particleBlue = world.rand.nextFloat() * 0.1F;
            break;
         case 6:
            this.particleRed = 0.8F + world.rand.nextFloat() * 0.2F;
            this.particleGreen = 0.8F + world.rand.nextFloat() * 0.2F;
            this.particleBlue = 0.8F + world.rand.nextFloat() * 0.2F;
            break;
         case 7:
            this.particleRed = 0.2F;
            this.particleGreen = 0.5F + world.rand.nextFloat() * 0.3F;
            this.particleBlue = 0.6F + world.rand.nextFloat() * 0.3F;
      }

   }

   public FXSparkle(World world, double d, double d1, double d2, double x, double y, double z, float f, int type, int m) {
      this(world, d, d1, d2, f, type, m);
      double dx = x - this.posX;
      double dy = y - this.posY;
      double dz = z - this.posZ;
      this.motionX = dx / (double)this.particleMaxAge;
      this.motionY = dy / (double)this.particleMaxAge;
      this.motionZ = dz / (double)this.particleMaxAge;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      int part = this.particle + this.particleAge / this.multiplier;
      float var8 = (float)(part % 4) / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.25F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.1F * this.particleScale;
      if (this.shrink) {
         var12 *= (float)(this.particleMaxAge - this.particleAge + 1) / (float)this.particleMaxAge;
      }

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

   public int getFXLayer() {
      return this.blendmode == 1 ? 0 : 1;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge == 0 && this.tinkle && this.worldObj.rand.nextInt(10) == 0) {
         this.worldObj.playSoundAtEntity(this, "random.orb", 0.02F, 0.7F * ((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.6F + 2.0F));
      }

      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      this.motionY -= 0.04 * (double)this.particleGravity;
      if (!this.noClip) {
         this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / (double)2.0F, this.posZ);
      }

      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      if (this.slowdown) {
         this.motionX *= 0.9080000019073486;
         this.motionY *= 0.9080000019073486;
         this.motionZ *= 0.9080000019073486;
         if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
         }
      }

      if (this.leyLineEffect) {
         FXSparkle fx = new FXSparkle(this.worldObj, this.prevPosX + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.1F), this.prevPosY + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.1F), this.prevPosZ + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.1F), 1.0F, this.currentColor, 3 + this.worldObj.rand.nextInt(3));
         fx.noClip = true;
         FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
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
      if (!this.worldObj.isAirBlock(var7, var8, var9)) {
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
