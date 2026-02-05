package thaumcraft.common.lib.utils;

import net.minecraft.util.RandomSource;
import thaumcraft.common.lib.research.HexEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//not 0xffffff hex,it's for research
public class HexCoordUtils {
   static final int[][] NEIGHBOURS = new int[][]{{1, 0}, {1, -1}, {0, -1}, {-1, 0}, {-1, 1}, {0, 1}};

   public static int getDistance(HexCoord a1, HexCoord a2) {
      return (Math.abs(a1.q() - a2.q()) + Math.abs(a1.r() - a2.r()) + Math.abs(a1.q() + a1.r() - a2.q() - a2.r())) / 2;
   }

   public static HexCoord getRoundedHex(double qq, double rr) {
      return getRoundedCubicHex(qq, rr, -qq - rr).toHex();
   }

   public static CubicHex getRoundedCubicHex(double xx, double yy, double zz) {
      int rx = (int)Math.round(xx);
      int ry = (int)Math.round(yy);
      int rz = (int)Math.round(zz);
      double x_diff = Math.abs((double)rx - xx);
      double y_diff = Math.abs((double)ry - yy);
      double z_diff = Math.abs((double)rz - zz);
      if (x_diff > y_diff && x_diff > z_diff) {
         rx = -ry - rz;
      } else if (y_diff > z_diff) {
         ry = -rx - rz;
      } else {
         rz = -rx - ry;
      }

      return new CubicHex(rx, ry, rz);
   }

   public static List<HexCoord> getRing(int radius) {
      HexCoord h = new HexCoord(0, 0);

      for(int k = 0; k < radius; ++k) {
         h = h.getNeighbour(4);
      }

      ArrayList<HexCoord> ring = new ArrayList<>();

      for(int i = 0; i < 6; ++i) {
         for(int j = 0; j < radius; ++j) {
            ring.add(h);
            h = h.getNeighbour(i);
         }
      }

      return ring;
   }

   public static List<HexCoord> distributeRingRandomly(int radius, int entries, RandomSource random) {
      List<HexCoord> ring = getRing(radius);
      List<HexCoord> results = new ArrayList<>();
      float spacing = (float)ring.size() / (float)entries;
      random.nextInt(ring.size());
      float pos = 0.0F;

      for(int i = 0; i < entries; ++i) {
         results.add(ring.get(Math.round(pos)));
         pos += spacing;
      }

      return results;
   }

   public static Map<HexCoord, HexEntry> generateHexGridWithRadius(int radius) {
      HashMap<HexCoord, HexEntry> results = new HashMap<>();
      HexCoord h = new HexCoord(0, 0);

      results.put(h,HexEntry.EMPTY);

      for(int k = 0; k < radius; ++k) {
         h = h.getNeighbour(4);
         HexCoord hd = new HexCoord(h.q(), h.r());

         for(int i = 0; i < 6; ++i) {
            for(int j = 0; j <= k; ++j) {
               results.put(hd,HexEntry.EMPTY);
               hd = hd.getNeighbour(i);
            }
         }
      }

      return results;
   }

   public record CubicHex(int x, int y, int z) {

      public HexCoord toHex() {
            return new HexCoord(this.x, this.z);
         }
      }

   public record Pixel(double x, double y) {

      public HexCoord toHex(int size) {
            double qq = 0.6666666666666666 * this.x / (double) size;
            double rr = (0.3333333333333333 * Math.sqrt(3.0F) * -this.y - 0.3333333333333333 * this.x) / (double) size;
            return HexCoordUtils.getRoundedHex(qq, rr);
         }
      }
}
