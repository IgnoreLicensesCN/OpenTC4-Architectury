package thaumcraft.codechicken.lib.vec;

import thaumcraft.codechicken.lib.util.Copyable;

public class BlockCoord implements Comparable<BlockCoord>, Copyable<BlockCoord> {
   public int x;
   public int y;
   public int z;
   public BlockCoord(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }
   public boolean equals(Object obj) {
      if (!(obj instanceof BlockCoord)) {
         return false;
      } else {
         BlockCoord o2 = (BlockCoord)obj;
         return this.x == o2.x && this.y == o2.y && this.z == o2.z;
      }
   }

   public int hashCode() {
      return (this.x ^ this.z) * 31 + this.y;
   }

   public int compareTo(BlockCoord o) {
      if (this.x != o.x) {
         return this.x < o.x ? 1 : -1;
      } else if (this.y != o.y) {
         return this.y < o.y ? 1 : -1;
      } else if (this.z != o.z) {
         return this.z < o.z ? 1 : -1;
      } else {
         return 0;
      }
   }
   public BlockCoord copy() {
      return new BlockCoord(this.x, this.y, this.z);
   }

}
