package thaumcraft.common.lib.world.dim;

import thaumcraft.common.lib.utils.Utils;

public class Cell {
   public boolean north;
   public boolean south;
   public boolean east;
   public boolean west;
   public boolean above;
   public boolean below = false;
   public byte feature = 0;

   public Cell() {
   }

   public Cell(short data) {
      this.unpack(data);
   }

   private void unpack(short data) {
      this.north = Utils.getBit(data, 0);
      this.south = Utils.getBit(data, 1);
      this.east = Utils.getBit(data, 2);
      this.west = Utils.getBit(data, 3);
      this.above = Utils.getBit(data, 4);
      this.below = Utils.getBit(data, 5);
      this.feature = (byte)(data >> 8);
   }

   public short pack() {
      int out = 0;
      if (this.north) {
         out = Utils.setBit(out, 0);
      }

      if (this.south) {
         out = Utils.setBit(out, 1);
      }

      if (this.east) {
         out = Utils.setBit(out, 2);
      }

      if (this.west) {
         out = Utils.setBit(out, 3);
      }

      if (this.above) {
         out = Utils.setBit(out, 4);
      }

      if (this.below) {
         out = Utils.setBit(out, 5);
      }

      out |= this.feature << 8;
      return (short)out;
   }
}
