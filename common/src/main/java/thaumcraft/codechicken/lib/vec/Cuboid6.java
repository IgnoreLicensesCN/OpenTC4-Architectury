package thaumcraft.codechicken.lib.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import net.minecraft.block.Block;
import thaumcraft.codechicken.lib.util.Copyable;

public class Cuboid6 implements Copyable<Cuboid6> {
   public static Cuboid6 full = new Cuboid6(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   public Vector3 min;
   public Vector3 max;

   public Cuboid6(Cuboid6 cuboid) {
      this.min = cuboid.min.copy();
      this.max = cuboid.max.copy();
   }

   public Cuboid6(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
      this.min = new Vector3(minx, miny, minz);
      this.max = new Vector3(maxx, maxy, maxz);
   }

   public Cuboid6 copy() {
      return new Cuboid6(this);
   }

   public Cuboid6 add(Vector3 vec) {
      this.min.add(vec);
      this.max.add(vec);
      return this;
   }


   public void setBlockBounds(Block block) {
      block.setBlockBounds((float)this.min.x, (float)this.min.y, (float)this.min.z, (float)this.max.x, (float)this.max.y, (float)this.max.z);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "Cuboid: (" + new BigDecimal(this.min.x, cont) + ", " + new BigDecimal(this.min.y, cont) + ", " + new BigDecimal(this.min.z, cont) + ") -> (" + new BigDecimal(this.max.x, cont) + ", " + new BigDecimal(this.max.y, cont) + ", " + new BigDecimal(this.max.z, cont) + ")";
   }
}
