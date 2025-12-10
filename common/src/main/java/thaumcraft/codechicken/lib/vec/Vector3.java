package thaumcraft.codechicken.lib.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import net.minecraft.util.Vec3;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.codechicken.lib.util.Copyable;

public class Vector3 implements Copyable {
   public static Vector3 zero = new Vector3();
   public static Vector3 one = new Vector3(1.0F, 1.0F, 1.0F);
   public static Vector3 center = new Vector3(0.5F, 0.5F, 0.5F);
   public double x;
   public double y;
   public double z;

   public Vector3() {
   }

   public Vector3(double d, double d1, double d2) {
      this.x = d;
      this.y = d1;
      this.z = d2;
   }

   public Vector3(Vector3 vec) {
      this.x = vec.x;
      this.y = vec.y;
      this.z = vec.z;
   }
   public Vector3(Vec3 vec) {
      this.x = vec.xCoord;
      this.y = vec.yCoord;
      this.z = vec.zCoord;
   }


   public Vector3 copy() {
      return new Vector3(this);
   }
   public Vector3 set(double d, double d1, double d2) {
      this.x = d;
      this.y = d1;
      this.z = d2;
      return this;
   }

   public Vector3 set(Vector3 vec) {
      this.x = vec.x;
      this.y = vec.y;
      this.z = vec.z;
      return this;
   }
   public double dotProduct(Vector3 vec) {
      double d = vec.x * this.x + vec.y * this.y + vec.z * this.z;
      if (d > (double)1.0F && d < 1.00001) {
         d = 1.0F;
      } else if (d < (double)-1.0F && d > -1.00001) {
         d = -1.0F;
      }

      return d;
   }
   public Vector3 add(double d, double d1, double d2) {
      this.x += d;
      this.y += d1;
      this.z += d2;
      return this;
   }

   public Vector3 add(Vector3 vec) {
      this.x += vec.x;
      this.y += vec.y;
      this.z += vec.z;
      return this;
   }

   public Vector3 add(double d) {
      return this.add(d, d, d);
   }

   public Vector3 sub(Vector3 vec) {
      return this.subtract(vec);
   }

   public Vector3 subtract(Vector3 vec) {
      this.x -= vec.x;
      this.y -= vec.y;
      this.z -= vec.z;
      return this;
   }

   public Vector3 multiply(double d) {
      this.x *= d;
      this.y *= d;
      this.z *= d;
      return this;
   }

   public Vector3 multiply(Vector3 f) {
      this.x *= f.x;
      this.y *= f.y;
      this.z *= f.z;
      return this;
   }

   public Vector3 multiply(double fx, double fy, double fz) {
      this.x *= fx;
      this.y *= fy;
      this.z *= fz;
      return this;
   }

   public double mag() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public double magSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public Vector3 normalize() {
      double d = this.mag();
      if (d != (double)0.0F) {
         this.multiply((double)1.0F / d);
      }

      return this;
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "Vector3(" + new BigDecimal(this.x, cont) + ", " + new BigDecimal(this.y, cont) + ", " + new BigDecimal(this.z, cont) + ")";
   }

   public Vector3 xCrossProduct() {
      double d = this.z;
      double d1 = -this.y;
      this.x = 0.0F;
      this.y = d;
      this.z = d1;
      return this;
   }

   public Vector3 zCrossProduct() {
      double d = this.y;
      double d1 = -this.x;
      this.x = d;
      this.y = d1;
      this.z = 0.0F;
      return this;
   }

   public Vector3 yCrossProduct() {
      double d = -this.z;
      double d1 = this.x;
      this.x = d;
      this.y = 0.0F;
      this.z = d1;
      return this;
   }

   public Vec3 toVec3D() {
      return Vec3.createVectorHelper(this.x, this.y, this.z);
   }

   public double angle(Vector3 vec) {
      return Math.acos(this.copy().normalize().dotProduct(vec.copy().normalize()));
   }

   public boolean isZero() {
      return this.x == (double)0.0F && this.y == (double)0.0F && this.z == (double)0.0F;
   }

   public Vector3 YZintercept(Vector3 end, double px) {
      double dx = end.x - this.x;
      double dy = end.y - this.y;
      double dz = end.z - this.z;
      if (dx == (double)0.0F) {
         return null;
      } else {
         double d = (px - this.x) / dx;
         if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
            return this;
         } else if (!MathHelper.between(0.0F, d, 1.0F)) {
            return null;
         } else {
            this.x = px;
            this.y += d * dy;
            this.z += d * dz;
            return this;
         }
      }
   }

   public Vector3 XZintercept(Vector3 end, double py) {
      double dx = end.x - this.x;
      double dy = end.y - this.y;
      double dz = end.z - this.z;
      if (dy == (double)0.0F) {
         return null;
      } else {
         double d = (py - this.y) / dy;
         if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
            return this;
         } else if (!MathHelper.between(0.0F, d, 1.0F)) {
            return null;
         } else {
            this.x += d * dx;
            this.y = py;
            this.z += d * dz;
            return this;
         }
      }
   }

   public Vector3 XYintercept(Vector3 end, double pz) {
      double dx = end.x - this.x;
      double dy = end.y - this.y;
      double dz = end.z - this.z;
      if (dz == (double)0.0F) {
         return null;
      } else {
         double d = (pz - this.z) / dz;
         if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
            return this;
         } else if (!MathHelper.between(0.0F, d, 1.0F)) {
            return null;
         } else {
            this.x += d * dx;
            this.y += d * dy;
            this.z = pz;
            return this;
         }
      }
   }

   public Vector3 negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

//   public Translation translation() {
//      return new Translation(this);
//   }

   public double scalarProject(Vector3 b) {
      double l = b.mag();
      return l == (double)0.0F ? (double)0.0F : this.dotProduct(b) / l;
   }

   public Vector3 project(Vector3 b) {
      double l = b.magSquared();
      if (l == (double)0.0F) {
         this.set(0.0F, 0.0F, 0.0F);
      } else {
         double m = this.dotProduct(b) / l;
         this.set(b).multiply(m);
      }
       return this;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Vector3)) {
         return false;
      } else {
         Vector3 v = (Vector3)o;
         return this.x == v.x && this.y == v.y && this.z == v.z;
      }
   }
}
