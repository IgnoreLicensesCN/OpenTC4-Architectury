package thaumcraft.common.lib.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MathHelper;

public class TCVec3 {
   public static final TCVec3Pool vec3dPool = new TCVec3Pool(-1, -1);
   public final TCVec3Pool myVec3LocalPool;
   public double xCoord;
   public double yCoord;
   public double zCoord;

   public static TCVec3 createVectorHelper(double par0, double par2, double par4) {
      return new TCVec3(vec3dPool, par0, par2, par4);
   }

   protected TCVec3(TCVec3Pool par1Vec3Pool, double par2, double par4, double par6) {
      if (par2 == (double)-0.0F) {
         par2 = 0.0F;
      }

      if (par4 == (double)-0.0F) {
         par4 = 0.0F;
      }

      if (par6 == (double)-0.0F) {
         par6 = 0.0F;
      }

      this.xCoord = par2;
      this.yCoord = par4;
      this.zCoord = par6;
      this.myVec3LocalPool = par1Vec3Pool;
   }

   protected TCVec3 setComponents(double par1, double par3, double par5) {
      this.xCoord = par1;
      this.yCoord = par3;
      this.zCoord = par5;
      return this;
   }

   @SideOnly(Side.CLIENT)
   public TCVec3 subtract(TCVec3 par1Vec3) {
      return this.myVec3LocalPool.getVecFromPool(par1Vec3.xCoord - this.xCoord, par1Vec3.yCoord - this.yCoord, par1Vec3.zCoord - this.zCoord);
   }

   public TCVec3 normalize() {
      double var1 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
      return var1 < 1.0E-4 ? this.myVec3LocalPool.getVecFromPool(0.0F, 0.0F, 0.0F) : this.myVec3LocalPool.getVecFromPool(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
   }

   public double dotProduct(TCVec3 par1Vec3) {
      return this.xCoord * par1Vec3.xCoord + this.yCoord * par1Vec3.yCoord + this.zCoord * par1Vec3.zCoord;
   }

   @SideOnly(Side.CLIENT)
   public TCVec3 crossProduct(TCVec3 par1Vec3) {
      return this.myVec3LocalPool.getVecFromPool(this.yCoord * par1Vec3.zCoord - this.zCoord * par1Vec3.yCoord, this.zCoord * par1Vec3.xCoord - this.xCoord * par1Vec3.zCoord, this.xCoord * par1Vec3.yCoord - this.yCoord * par1Vec3.xCoord);
   }

   public TCVec3 addVector(double par1, double par3, double par5) {
      return this.myVec3LocalPool.getVecFromPool(this.xCoord + par1, this.yCoord + par3, this.zCoord + par5);
   }

   public double distanceTo(TCVec3 par1Vec3) {
      double var2 = par1Vec3.xCoord - this.xCoord;
      double var4 = par1Vec3.yCoord - this.yCoord;
      double var6 = par1Vec3.zCoord - this.zCoord;
      return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
   }

   public double squareDistanceTo(TCVec3 par1Vec3) {
      double var2 = par1Vec3.xCoord - this.xCoord;
      double var4 = par1Vec3.yCoord - this.yCoord;
      double var6 = par1Vec3.zCoord - this.zCoord;
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public double squareDistanceTo(double par1, double par3, double par5) {
      double var7 = par1 - this.xCoord;
      double var9 = par3 - this.yCoord;
      double var11 = par5 - this.zCoord;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public double lengthVector() {
      return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
   }

   public TCVec3 getIntermediateWithXValue(TCVec3 par1Vec3, double par2) {
      double var4 = par1Vec3.xCoord - this.xCoord;
      double var6 = par1Vec3.yCoord - this.yCoord;
      double var8 = par1Vec3.zCoord - this.zCoord;
      if (var4 * var4 < (double)1.0E-7F) {
         return null;
      } else {
         double var10 = (par2 - this.xCoord) / var4;
         return var10 >= (double)0.0F && var10 <= (double)1.0F ? this.myVec3LocalPool.getVecFromPool(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
      }
   }

   public TCVec3 getIntermediateWithYValue(TCVec3 par1Vec3, double par2) {
      double var4 = par1Vec3.xCoord - this.xCoord;
      double var6 = par1Vec3.yCoord - this.yCoord;
      double var8 = par1Vec3.zCoord - this.zCoord;
      if (var6 * var6 < (double)1.0E-7F) {
         return null;
      } else {
         double var10 = (par2 - this.yCoord) / var6;
         return var10 >= (double)0.0F && var10 <= (double)1.0F ? this.myVec3LocalPool.getVecFromPool(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
      }
   }

   public TCVec3 getIntermediateWithZValue(TCVec3 par1Vec3, double par2) {
      double var4 = par1Vec3.xCoord - this.xCoord;
      double var6 = par1Vec3.yCoord - this.yCoord;
      double var8 = par1Vec3.zCoord - this.zCoord;
      if (var8 * var8 < (double)1.0E-7F) {
         return null;
      } else {
         double var10 = (par2 - this.zCoord) / var8;
         return var10 >= (double)0.0F && var10 <= (double)1.0F ? this.myVec3LocalPool.getVecFromPool(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
      }
   }

   public String toString() {
      return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
   }

   public void rotateAroundX(float par1) {
      float var2 = MathHelper.cos(par1);
      float var3 = MathHelper.sin(par1);
      double var4 = this.xCoord;
      double var6 = this.yCoord * (double)var2 + this.zCoord * (double)var3;
      double var8 = this.zCoord * (double)var2 - this.yCoord * (double)var3;
      this.xCoord = var4;
      this.yCoord = var6;
      this.zCoord = var8;
   }

   public void rotateAroundY(float par1) {
      float var2 = MathHelper.cos(par1);
      float var3 = MathHelper.sin(par1);
      double var4 = this.xCoord * (double)var2 + this.zCoord * (double)var3;
      double var6 = this.yCoord;
      double var8 = this.zCoord * (double)var2 - this.xCoord * (double)var3;
      this.xCoord = var4;
      this.yCoord = var6;
      this.zCoord = var8;
   }

   public void rotateAroundZ(float par1) {
      float var2 = MathHelper.cos(par1);
      float var3 = MathHelper.sin(par1);
      double var4 = this.xCoord * (double)var2 + this.yCoord * (double)var3;
      double var6 = this.yCoord * (double)var2 - this.xCoord * (double)var3;
      double var8 = this.zCoord;
      this.xCoord = var4;
      this.yCoord = var6;
      this.zCoord = var8;
   }
}
