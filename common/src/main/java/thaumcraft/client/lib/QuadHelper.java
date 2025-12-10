package thaumcraft.client.lib;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class QuadHelper {
   public double x;
   public double y;
   public double z;
   public double angle;

   public QuadHelper(double ang, double xx, double yy, double zz) {
      this.x = xx;
      this.y = yy;
      this.z = zz;
      this.angle = ang;
   }

   public static QuadHelper setAxis(Vec3 vec, double angle) {
      angle *= 0.5F;
      double d4 = MathHelper.sin((float)angle);
      return new QuadHelper(MathHelper.cos((float)angle), vec.xCoord * d4, vec.yCoord * d4, vec.zCoord * d4);
   }

   public void rotate(Vec3 vec) {
      double d = -this.x * vec.xCoord - this.y * vec.yCoord - this.z * vec.zCoord;
      double d1 = this.angle * vec.xCoord + this.y * vec.zCoord - this.z * vec.yCoord;
      double d2 = this.angle * vec.yCoord - this.x * vec.zCoord + this.z * vec.xCoord;
      double d3 = this.angle * vec.zCoord + this.x * vec.yCoord - this.y * vec.xCoord;
      vec.xCoord = d1 * this.angle - d * this.x - d2 * this.z + d3 * this.y;
      vec.yCoord = d2 * this.angle - d * this.y + d1 * this.z - d3 * this.x;
      vec.zCoord = d3 * this.angle - d * this.z - d1 * this.y + d2 * this.x;
   }
}
