package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class FXBoreParticles extends EntityFX {
   private Block blockInstance;
   private Item itemInstance;
   private int metadata;
   private int side;
   private double targetX;
   private double targetY;
   private double targetZ;

   public FXBoreParticles(World par1World, double par2, double par4, double par6, double tx, double ty, double tz, Block par14Block, int par15, int par16) {
      super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
      this.blockInstance = par14Block;
      this.setParticleIcon(par14Block.getIcon(par15, par16));
      this.particleGravity = par14Block.blockParticleGravity;
      this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
      this.particleScale = this.rand.nextFloat() * 0.3F + 0.4F;
      this.side = par15;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      double dx = tx - this.posX;
      double dy = ty - this.posY;
      double dz = tz - this.posZ;
      int base = (int)(MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz) * 3.0F);
      if (base < 1) {
         base = 1;
      }

      this.particleMaxAge = base / 2 + this.rand.nextInt(base);
      float f3 = 0.01F;
      this.motionX = (float)this.worldObj.rand.nextGaussian() * f3;
      this.motionY = (float)this.worldObj.rand.nextGaussian() * f3;
      this.motionZ = (float)this.worldObj.rand.nextGaussian() * f3;
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

   public FXBoreParticles(World par1World, double par2, double par4, double par6, double tx, double ty, double tz, Item item, int par15, int par16) {
      super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
      this.itemInstance = item;
      this.setParticleIcon(item.getIconFromDamage(par16));
      this.metadata = par16;
      this.particleGravity = Blocks.snow_layer.blockParticleGravity;
      this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
      this.particleScale = this.rand.nextFloat() * 0.3F + 0.4F;
      this.side = par15;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      double dx = tx - this.posX;
      double dy = ty - this.posY;
      double dz = tz - this.posZ;
      int base = (int)(MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz) * 3.0F);
      if (base < 1) {
         base = 1;
      }

      this.particleMaxAge = base / 2 + this.rand.nextInt(base);
      float f3 = 0.01F;
      this.motionX = (float)this.worldObj.rand.nextGaussian() * f3;
      this.motionY = (float)this.worldObj.rand.nextGaussian() * f3;
      this.motionZ = (float)this.worldObj.rand.nextGaussian() * f3;
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

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ < this.particleMaxAge && (MathHelper.floor_double(this.posX) != MathHelper.floor_double(this.targetX) || MathHelper.floor_double(this.posY) != MathHelper.floor_double(this.targetY) || MathHelper.floor_double(this.posZ) != MathHelper.floor_double(this.targetZ))) {
         if (!this.noClip) {
            this.pushOutOfBlocks(this.posX, this.posY, this.posZ);
         }

         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.985;
         this.motionY *= 0.985;
         this.motionZ *= 0.985;
         double dx = this.targetX - this.posX;
         double dy = this.targetY - this.posY;
         double dz = this.targetZ - this.posZ;
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

   public FXBoreParticles applyColourMultiplier(int par1, int par2, int par3) {
      if (this.blockInstance != null && this.worldObj.getBlock(par1, par2, par3) == this.blockInstance) {
          if (this.blockInstance != Blocks.grass || this.side == 1) {
              try {
                  int var4 = this.blockInstance.colorMultiplier(this.worldObj, par1, par2, par3);
                  this.particleRed *= (float) (var4 >> 16 & 255) / 255.0F;
                  this.particleGreen *= (float) (var4 >> 8 & 255) / 255.0F;
                  this.particleBlue *= (float) (var4 & 255) / 255.0F;
              } catch (Exception ignored) {
              }

          }
          return this;
      } else {
         try {
            int var4 = this.itemInstance.getColorFromItemStack(new ItemStack(this.itemInstance, 1, this.metadata), 0);
            this.particleRed *= (float)(var4 >> 16 & 255) / 255.0F;
            this.particleGreen *= (float)(var4 >> 8 & 255) / 255.0F;
            this.particleBlue *= (float)(var4 & 255) / 255.0F;
         } catch (Exception ignored) {
         }

         return this;
      }
   }

   public FXBoreParticles applyRenderColor(int par1) {
      if (this.blockInstance != null) {
          if (this.blockInstance != Blocks.grass) {
              int var2 = this.blockInstance.getRenderColor(par1);
              this.particleRed *= (float) (var2 >> 16 & 255) / 255.0F;
              this.particleGreen *= (float) (var2 >> 8 & 255) / 255.0F;
              this.particleBlue *= (float) (var2 & 255) / 255.0F;
          }
      } else {
         int var4 = this.itemInstance.getColorFromItemStack(new ItemStack(this.itemInstance, 1, this.metadata), this.metadata);
         this.particleRed *= (float)(var4 >> 16 & 255) / 255.0F;
         this.particleGreen *= (float)(var4 >> 8 & 255) / 255.0F;
         this.particleBlue *= (float)(var4 & 255) / 255.0F;
      }
      return this;
   }

   public int getFXLayer() {
      return this.blockInstance != null ? 1 : 2;
   }

   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float f6 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
      float f7 = f6 + 0.015609375F;
      float f8 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
      float f9 = f8 + 0.015609375F;
      float f10 = 0.1F * this.particleScale;
      if (this.particleIcon != null) {
         f6 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0F * 16.0F);
         f7 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
         f8 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0F * 16.0F);
         f9 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
      }

      float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
      float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
      float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
      float f14 = 1.0F;
      par1Tessellator.setColorOpaque_F(f14 * this.particleRed, f14 * this.particleGreen, f14 * this.particleBlue);
      par1Tessellator.addVertexWithUV(f11 - par3 * f10 - par6 * f10, f12 - par4 * f10, f13 - par5 * f10 - par7 * f10, f6, f9);
      par1Tessellator.addVertexWithUV(f11 - par3 * f10 + par6 * f10, f12 + par4 * f10, f13 - par5 * f10 + par7 * f10, f6, f8);
      par1Tessellator.addVertexWithUV(f11 + par3 * f10 + par6 * f10, f12 + par4 * f10, f13 + par5 * f10 + par7 * f10, f7, f8);
      par1Tessellator.addVertexWithUV(f11 + par3 * f10 - par6 * f10, f12 - par4 * f10, f13 + par5 * f10 - par7 * f10, f7, f9);
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
