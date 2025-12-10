package thaumcraft.codechicken.lib.math;

public class MathHelper {
   public static final double phi = 1.618033988749894;
   public static final double pi = Math.PI;
   public static final double todeg = (180D / Math.PI);
   public static final double torad = 0.017453292519943;
   public static final double sqrt2 = 1.414213562373095;
   public static double[] SIN_TABLE = new double[65536];

   public static double sin(double d) {
      return SIN_TABLE[(int)((float)d * 10430.378F) & '\uffff'];
   }

   public static double cos(double d) {
      return SIN_TABLE[(int)((float)d * 10430.378F + 16384.0F) & '\uffff'];
   }

   public static float approachLinear(float a, float b, float max) {
      return a > b ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
   }

   public static double approachLinear(double a, double b, double max) {
      return a > b ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
   }

   public static float interpolate(float a, float b, float d) {
      return a + (b - a) * d;
   }

   public static double interpolate(double a, double b, double d) {
      return a + (b - a) * d;
   }

   public static double approachExp(double a, double b, double ratio) {
      return a + (b - a) * ratio;
   }

   public static double approachExp(double a, double b, double ratio, double cap) {
      double d = (b - a) * ratio;
      if (Math.abs(d) > cap) {
         d = Math.signum(d) * cap;
      }

      return a + d;
   }

   public static double retreatExp(double a, double b, double c, double ratio, double kick) {
      double d = (Math.abs(c - a) + kick) * ratio;
      return d > Math.abs(b - a) ? b : a + Math.signum(b - a) * d;
   }

   public static double clip(double value, double min, double max) {
      if (value > max) {
         value = max;
      }

      if (value < min) {
         value = min;
      }

      return value;
   }

   public static boolean between(double a, double x, double b) {
      return a <= x && x <= b;
   }

   public static int approachExpI(int a, int b, double ratio) {
      int r = (int)Math.round(approachExp(a, b, ratio));
      return r == a ? b : r;
   }

   public static int retreatExpI(int a, int b, int c, double ratio, int kick) {
      int r = (int)Math.round(retreatExp(a, b, c, ratio, kick));
      return r == a ? b : r;
   }

   public static int floor_double(double d) {
      return net.minecraft.util.MathHelper.floor_double(d);
   }

   public static int roundAway(double d) {
      return (int)(d < (double)0.0F ? Math.floor(d) : Math.ceil(d));
   }

   public static int compare(int a, int b) {
      return Integer.compare(a, b);
   }

   public static int compare(double a, double b) {
      return Double.compare(a, b);
   }

   static {
      for(int i = 0; i < 65536; ++i) {
         SIN_TABLE[i] = Math.sin((double)i / (double)65536.0F * (double)2.0F * Math.PI);
      }

      SIN_TABLE[0] = 0.0F;
      SIN_TABLE[16384] = 1.0F;
      SIN_TABLE['耀'] = 0.0F;
      SIN_TABLE['쀀'] = 1.0F;
   }
}
