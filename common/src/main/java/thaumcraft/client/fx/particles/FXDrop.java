package thaumcraft.client.fx.particles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;

@SideOnly(Side.CLIENT)
public class FXDrop extends EntityFX {
   int bobTimer;

   public FXDrop(World par1World, double par2, double par4, double par6, float r, float g, float b) {
      super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
      this.motionX = this.motionY = this.motionZ = 0.0F;
      this.particleRed = r;
      this.particleGreen = g;
      this.particleBlue = b;
      this.setParticleTextureIndex(113);
      this.particleGravity = 0.06F;
      this.bobTimer = 40;
      this.particleMaxAge = (int)((double)64.0F / (Math.random() * 0.8 + 0.2));
      this.motionX = this.motionY = this.motionZ = 0.0F;
   }

   public int getBrightnessForRender(float par1) {
      return 257;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= this.particleGravity;
      if (this.bobTimer-- > 0) {
         this.motionX *= 0.02;
         this.motionY *= 0.02;
         this.motionZ *= 0.02;
         this.setParticleTextureIndex(113);
      } else {
         this.setParticleTextureIndex(112);
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.particleMaxAge-- <= 0) {
         this.setDead();
      }

      if (this.onGround) {
         this.setParticleTextureIndex(114);
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }

      Material material = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial();
      if (material != Material.glass && (material.isLiquid() || material.isSolid())) {
         double d0 = (float)(MathHelper.floor_double(this.posY) + 1) - BlockLiquid.getLiquidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
         if (this.posY < d0) {
            this.setDead();
         }
      }

   }

   public void moveEntity(double par1, double par3, double par5) {
      int x = MathHelper.floor_double(this.posX);
      int y = MathHelper.floor_double(this.posY);
      int z = MathHelper.floor_double(this.posZ);
      if (!this.noClip && this.worldObj.getBlock(x, y, z) != ConfigBlocks.blockJar) {
         this.worldObj.theProfiler.startSection("move");
         this.ySize *= 0.4F;
         double d3 = this.posX;
         double d4 = this.posY;
         double d5 = this.posZ;
         if (this.isInWeb) {
            this.isInWeb = false;
            par1 *= 0.25F;
            par3 *= 0.05F;
            par5 *= 0.25F;
            this.motionX = 0.0F;
            this.motionY = 0.0F;
            this.motionZ = 0.0F;
         }

         double d6 = par1;
         double d7 = par3;
         double d8 = par5;
         AxisAlignedBB axisalignedbb = this.boundingBox.copy();
         boolean flag = this.onGround && this.isSneaking();
         if (flag) {
            double d9;
            for(d9 = 0.05; par1 != (double)0.0F && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, -1.0F, 0.0F)).isEmpty(); d6 = par1) {
               if (par1 < d9 && par1 >= -d9) {
                  par1 = 0.0F;
               } else if (par1 > (double)0.0F) {
                  par1 -= d9;
               } else {
                  par1 += d9;
               }
            }

            for(; par5 != (double)0.0F && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0F, -1.0F, par5)).isEmpty(); d8 = par5) {
               if (par5 < d9 && par5 >= -d9) {
                  par5 = 0.0F;
               } else if (par5 > (double)0.0F) {
                  par5 -= d9;
               } else {
                  par5 += d9;
               }
            }

            while(par1 != (double)0.0F && par5 != (double)0.0F && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, -1.0F, par5)).isEmpty()) {
               if (par1 < d9 && par1 >= -d9) {
                  par1 = 0.0F;
               } else if (par1 > (double)0.0F) {
                  par1 -= d9;
               } else {
                  par1 += d9;
               }

               if (par5 < d9 && par5 >= -d9) {
                  par5 = 0.0F;
               } else if (par5 > (double)0.0F) {
                  par5 -= d9;
               } else {
                  par5 += d9;
               }

               d6 = par1;
               d8 = par5;
            }
         }

         List list = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(par1, par3, par5));

          for (Object o2 : list) {
              par3 = ((AxisAlignedBB) o2).calculateYOffset(this.boundingBox, par3);
          }

         this.boundingBox.offset(0.0F, par3, 0.0F);
         if (!this.field_70135_K && d7 != par3) {
            par5 = 0.0F;
            par3 = 0.0F;
            par1 = 0.0F;
         }

         boolean flag1 = this.onGround || d7 != par3 && d7 < (double)0.0F;

          for (Object o1 : list) {
              par1 = ((AxisAlignedBB) o1).calculateXOffset(this.boundingBox, par1);
          }

         this.boundingBox.offset(par1, 0.0F, 0.0F);
         if (!this.field_70135_K && d6 != par1) {
            par5 = 0.0F;
            par3 = 0.0F;
            par1 = 0.0F;
         }

          for (Object element : list) {
              par5 = ((AxisAlignedBB) element).calculateZOffset(this.boundingBox, par5);
          }

         this.boundingBox.offset(0.0F, 0.0F, par5);
         if (!this.field_70135_K && d8 != par5) {
            par5 = 0.0F;
            par3 = 0.0F;
            par1 = 0.0F;
         }

         if (this.stepHeight > 0.0F && flag1 && (flag || this.ySize < 0.05F) && (d6 != par1 || d8 != par5)) {
            double d12 = par1;
            double d10 = par3;
            double d11 = par5;
            par1 = d6;
            par3 = this.stepHeight;
            par5 = d8;
            AxisAlignedBB axisalignedbb1 = this.boundingBox.copy();
            this.boundingBox.setBB(axisalignedbb);
            list = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d6, par3, d8));

             for (Object item : list) {
                 par3 = ((AxisAlignedBB) item).calculateYOffset(this.boundingBox, par3);
             }

            this.boundingBox.offset(0.0F, par3, 0.0F);
            if (!this.field_70135_K && d7 != par3) {
               par5 = 0.0F;
               par3 = 0.0F;
               par1 = 0.0F;
            }

             for (Object value : list) {
                 par1 = ((AxisAlignedBB) value).calculateXOffset(this.boundingBox, par1);
             }

            this.boundingBox.offset(par1, 0.0F, 0.0F);
            if (!this.field_70135_K && d6 != par1) {
               par5 = 0.0F;
               par3 = 0.0F;
               par1 = 0.0F;
            }

             for (Object object : list) {
                 par5 = ((AxisAlignedBB) object).calculateZOffset(this.boundingBox, par5);
             }

            this.boundingBox.offset(0.0F, 0.0F, par5);
            if (!this.field_70135_K && d8 != par5) {
               par5 = 0.0F;
               par3 = 0.0F;
               par1 = 0.0F;
            }

            if (!this.field_70135_K && d7 != par3) {
               par5 = 0.0F;
               par3 = 0.0F;
               par1 = 0.0F;
            } else {
               par3 = -this.stepHeight;

                for (Object o : list) {
                    par3 = ((AxisAlignedBB) o).calculateYOffset(this.boundingBox, par3);
                }

               this.boundingBox.offset(0.0F, par3, 0.0F);
            }

            if (d12 * d12 + d11 * d11 >= par1 * par1 + par5 * par5) {
               par1 = d12;
               par3 = d10;
               par5 = d11;
               this.boundingBox.setBB(axisalignedbb1);
            }
         }

         this.worldObj.theProfiler.endSection();
         this.worldObj.theProfiler.startSection("rest");
         this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / (double)2.0F;
         this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
         this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / (double)2.0F;
         this.isCollidedHorizontally = d6 != par1 || d8 != par5;
         this.isCollidedVertically = d7 != par3;
         this.onGround = d7 != par3 && d7 < (double)0.0F;
         this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
         this.updateFallState(par3, this.onGround);
         if (d6 != par1) {
            this.motionX = 0.0F;
         }

         if (d7 != par3) {
            this.motionY = 0.0F;
         }

         if (d8 != par5) {
            this.motionZ = 0.0F;
         }

         double d12 = this.posX - d3;
         double d10 = this.posY - d4;
         double d11 = this.posZ - d5;
         if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
            int l = MathHelper.floor_double(this.posX);
            int k = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
            int i1 = MathHelper.floor_double(this.posZ);
            Block j1 = this.worldObj.getBlock(l, k, i1);
            if (j1.isAir(this.worldObj, l, k, i1)) {
               int k1 = this.worldObj.getBlock(l, k - 1, i1).getRenderType();
               if (k1 == 11 || k1 == 32 || k1 == 21) {
                  j1 = this.worldObj.getBlock(l, k - 1, i1);
               }
            }

            if (j1 != Blocks.ladder) {
               d10 = 0.0F;
            }

            this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d12 * d12 + d11 * d11) * 0.6);
            this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d12 * d12 + d10 * d10 + d11 * d11) * 0.6);
         }

         try {
            this.func_145775_I();
         } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity tile collision");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
         }

         this.worldObj.theProfiler.endSection();
      } else {
         this.boundingBox.offset(par1, par3, par5);
         this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / (double)2.0F;
         this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
         this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / (double)2.0F;
         x = MathHelper.floor_double(this.posX);
         y = MathHelper.floor_double(this.posY);
         y = MathHelper.floor_double(this.posY);
         if (this.worldObj.getBlock(x, y + 1, z) == ConfigBlocks.blockJar) {
            this.posX = this.prevPosX;
            this.posY = this.prevPosY;
            this.posZ = this.prevPosZ;
            this.motionY = 0.0F;
            this.onGround = true;
         }
      }

   }
}
