package thaumcraft.client.fx;

public class WRMat4 {
   float[] mat;

   public WRMat4() {
      this.loadIdentity();
   }

   public WRMat4 loadIdentity() {
      this.mat = new float[16];
      this.mat[0] = this.mat[5] = this.mat[10] = this.mat[15] = 1.0F;
      return this;
   }

   public WRVector3 translate(WRVector3 vec) {
      float x = vec.x * this.mat[0] + vec.y * this.mat[1] + vec.z * this.mat[2] + this.mat[3];
      float y = vec.x * this.mat[4] + vec.y * this.mat[5] + vec.z * this.mat[6] + this.mat[7];
      float z = vec.x * this.mat[8] + vec.y * this.mat[9] + vec.z * this.mat[10] + this.mat[11];
      vec.x = x;
      vec.y = y;
      vec.z = z;
      return vec;
   }

   public static WRMat4 rotationMat(double angle, WRVector3 axis) {
      axis = axis.copy().normalize();
      float x = axis.x;
      float y = axis.y;
      float z = axis.z;
      angle *= 0.0174532925;
      float cos = (float)Math.cos(angle);
      float ocos = 1.0F - cos;
      float sin = (float)Math.sin(angle);
      WRMat4 rotmat = new WRMat4();
      rotmat.mat[0] = x * x * ocos + cos;
      rotmat.mat[1] = y * x * ocos + z * sin;
      rotmat.mat[2] = x * z * ocos - y * sin;
      rotmat.mat[4] = x * y * ocos - z * sin;
      rotmat.mat[5] = y * y * ocos + cos;
      rotmat.mat[6] = y * z * ocos + x * sin;
      rotmat.mat[8] = x * z * ocos + y * sin;
      rotmat.mat[9] = y * z * ocos - x * sin;
      rotmat.mat[10] = z * z * ocos + cos;
      rotmat.mat[15] = 1.0F;
      return rotmat;
   }
}
