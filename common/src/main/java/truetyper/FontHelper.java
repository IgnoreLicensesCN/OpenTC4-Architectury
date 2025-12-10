package truetyper;

import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

public class FontHelper {
   private static final String formatEscape = "§";

   public static void drawString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, int format, float... rgba) {
      Minecraft mc = Minecraft.getMinecraft();
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), mc.displayWidth, mc.displayHeight);
      if (!mc.gameSettings.hideGUI) {
         int amt = 1;
         if (sr.getScaleFactor() == 1) {
            amt = 2;
         }

         FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
         GL11.glGetFloat(2982, matrixData);
         Matrix4f matrix = new Matrix4f();
         matrix.load(matrixData);
         set2DMode();
         y = (float)mc.displayHeight - y * (float)sr.getScaleFactor() - font.getLineHeight() / (float)amt;
         GL11.glEnable(GL11.GL_BLEND);
         if (s.contains(formatEscape)) {
            String[] pars = s.split(formatEscape);
            float totalOffset = 0.0F;

            for(int i = 0; i < pars.length; ++i) {
               String par = pars[i];
               float[] c = rgba;
               if (i > 0) {
                  c = Formatter.getFormatted(par.charAt(0));
                  par = par.substring(1);
               }

               font.drawString(x * (float)sr.getScaleFactor() + totalOffset, y - matrix.m31 * (float)sr.getScaleFactor(), par, scaleX / (float)amt, scaleY / (float)amt, format, c);
               totalOffset += font.getWidth(par);
            }
         } else {
            font.drawString(x * (float)sr.getScaleFactor(), y - matrix.m31 * (float)sr.getScaleFactor(), s, scaleX / (float)amt, scaleY / (float)amt, format, rgba);
         }

         GL11.glDisable(GL11.GL_BLEND);
         set3DMode();
      }
   }

   private static void set2DMode() {
      Minecraft mc = Minecraft.getMinecraft();
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0F, mc.displayWidth, 0.0F, mc.displayHeight, -1.0F, 1.0F);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
   }

   private static void set3DMode() {
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
   }
}
