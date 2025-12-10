package thaumcraft.common.entities.golems;

public class Marker {
   public int x;
   public int y;
   public int z;
   public int dim;
   public byte side;
   public byte color;

   public Marker(int x, int y, int z, int dim, byte side, byte color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dim = dim;
      this.side = side;
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Marker) {
         Marker marker = (Marker)obj;
         return this.x == marker.x && this.y == marker.y && this.z == marker.z && this.dim == marker.dim && this.side == marker.side && this.color == marker.color;
      } else {
         return false;
      }
   }

   public boolean equalsFuzzy(Object obj) {
      if (!(obj instanceof Marker)) {
         return false;
      } else {
         Marker marker = (Marker)obj;
         return this.x == marker.x && this.y == marker.y && this.z == marker.z && this.dim == marker.dim && this.side == marker.side && (this.color == marker.color || this.color == -1);
      }
   }
}
