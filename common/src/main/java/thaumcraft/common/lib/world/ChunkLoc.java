package thaumcraft.common.lib.world;

import net.minecraft.world.ChunkCoordIntPair;

import java.io.Serializable;

public class ChunkLoc implements Serializable {
   public final int chunkXPos;
   public final int chunkZPos;

   public ChunkLoc(int par1, int par2) {
      this.chunkXPos = par1;
      this.chunkZPos = par2;
   }

   public boolean equals(ChunkLoc par1Obj) {
      return par1Obj.chunkXPos == this.chunkXPos && par1Obj.chunkZPos == this.chunkZPos;
   }

   public boolean equals(ChunkCoordIntPair par1Obj) {
      return par1Obj.chunkXPos == this.chunkXPos && par1Obj.chunkZPos == this.chunkZPos;
   }

   public int getCenterXPos() {
      return (this.chunkXPos << 4) + 8;
   }

   public int getCenterZPosition() {
      return (this.chunkZPos << 4) + 8;
   }

   public String toString() {
      return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
   }
}
