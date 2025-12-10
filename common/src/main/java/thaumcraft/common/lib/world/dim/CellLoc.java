package thaumcraft.common.lib.world.dim;

public class CellLoc implements Comparable<CellLoc> {
   public int x;
   public int z;

   public CellLoc() {
   }

   public CellLoc(int x, int z) {
      this.x = x;
      this.z = z;
   }

   public CellLoc(CellLoc c) {
      this.x = c.x;
      this.z = c.z;
   }

   public boolean equals(Object o) {
      if (!(o instanceof CellLoc)) {
         return false;
      } else {
         CellLoc chunkcoordinates = (CellLoc)o;
         return this.x == chunkcoordinates.x && this.z == chunkcoordinates.z;
      }
   }

   public int hashCode() {
      return ((1664525 * this.x) + 1013904223) ^ ((1664525 * (this.z ^ -559038737)) + 1013904223);
   }

   @Override
   public int compareTo(CellLoc c) {
      return this.z == c.z ? this.x - c.x : this.z - c.z;
   }

   public void set(int x, int z) {
      this.x = x;
      this.z = z;
   }

   public float getDistanceSquared(int x, int z) {
      float f = (float)(this.x - x);
      float f2 = (float)(this.z - z);
      return f * f + f2 * f2;
   }

   public float getDistanceSquaredToChunkCoordinates(CellLoc c) {
      return this.getDistanceSquared(c.x, c.z);
   }

   public String toString() {
      return "Pos{x=" + this.x + ", z=" + this.z + '}';
   }
}
