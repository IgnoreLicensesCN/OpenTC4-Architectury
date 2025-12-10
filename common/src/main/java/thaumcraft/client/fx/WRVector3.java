package thaumcraft.client.fx;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

public class WRVector3 {
   public float x;
   public float y;
   public float z;

   public WRVector3(double x, double y, double z) {
      this.x = (float)x;
      this.y = (float)y;
      this.z = (float)z;
   }

   public WRVector3(TileEntity tile) {
      this.x = (float)tile.xCoord + 0.5F;
      this.y = (float)tile.yCoord + 0.5F;
      this.z = (float)tile.zCoord + 0.5F;
   }

   public WRVector3(Entity entity) {
      this(entity.posX, entity.posY, entity.posZ);
   }

   public WRVector3 add(WRVector3 vec) {
      this.x += vec.x;
      this.y += vec.y;
      this.z += vec.z;
      return this;
   }

   public WRVector3 sub(WRVector3 vec) {
      this.x -= vec.x;
      this.y -= vec.y;
      this.z -= vec.z;
      return this;
   }

   public WRVector3 scale(float scale) {
      this.x *= scale;
      this.y *= scale;
      this.z *= scale;
      return this;
   }

   public WRVector3 scale(float scalex, float scaley, float scalez) {
      this.x *= scalex;
      this.y *= scaley;
      this.z *= scalez;
      return this;
   }

   public WRVector3 normalize() {
      float length = this.length();
      this.x /= length;
      this.y /= length;
      this.z /= length;
      return this;
   }

   public float length() {
      return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public float lengthPow2() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public WRVector3 copy() {
      return new WRVector3(this.x, this.y, this.z);
   }

   public static WRVector3 crossProduct(WRVector3 vec1, WRVector3 vec2) {
      return new WRVector3(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
   }

   public static WRVector3 xCrossProduct(WRVector3 vec) {
      return new WRVector3(0.0F, vec.z, -vec.y);
   }

   public static WRVector3 zCrossProduct(WRVector3 vec) {
      return new WRVector3(-vec.y, vec.x, 0.0F);
   }

   public static float dotProduct(WRVector3 vec1, WRVector3 vec2) {
      return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
   }

   public static float angle(WRVector3 vec1, WRVector3 vec2) {
      return anglePreNorm(vec1.copy().normalize(), vec2.copy().normalize());
   }

   public static float anglePreNorm(WRVector3 vec1, WRVector3 vec2) {
      return (float)Math.acos(dotProduct(vec1, vec2));
   }

   public WRVector3 rotate(float angle, WRVector3 axis) {
      return WRMat4.rotationMat(angle, axis).translate(this);
   }

   public String toString() {
      return "[" + this.x + "," + this.y + "," + this.z + "]";
   }

   public Vec3 toVec3D() {
      return Vec3.createVectorHelper(this.x, this.y, this.z);
   }

   public static WRVector3 getPerpendicular(WRVector3 vec) {
      return vec.z == 0.0F ? zCrossProduct(vec) : xCrossProduct(vec);
   }

   public boolean isZero() {
      return this.x == 0.0F && this.y == 0.0F && this.z == 0.0F;
   }
}
