package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelManaPod;
import thaumcraft.common.tiles.TileManaPod;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockMetaSafely;

public class TileManaPodRenderer extends TileEntitySpecialRenderer {
   private ModelManaPod model = new ModelManaPod();
   private static final ResourceLocation pod0tex = new ResourceLocation("thaumcraft", "textures/models/manapod_0.png");
   private static final ResourceLocation pod2tex = new ResourceLocation("thaumcraft", "textures/models/manapod_2.png");

   public void renderEntityAt(TileManaPod pod, double x, double y, double z, float fq) {
      int meta = 0;
      int bright = 20;
      Aspect aspect = Aspect.PLANT;

      if (!pod.hasWorldObj()) {
         meta = 5;
      } else {
         meta = getBlockMetaSafely(pod);
         if (meta == -1){
            meta = 5;
         }
         if (pod.aspect != null) {
            aspect = pod.aspect;
         }

//         bright = pod.getBlockType().getMixedBrightnessForBlock(pod.getWorldObj(), pod.xCoord, pod.yCoord, pod.zCoord);
      }

      if (meta > 1) {
         float br = 0.14509805F;
         float bg = 0.6156863F;
         float bb = 0.45882353F;
         float fr = br;
         float fg = bg;
         float fb = bb;
         if (pod.aspect != null) {
            Color color = new Color(aspect.getColor());
            float ar = (float)color.getRed() / 255.0F;
            float ag = (float)color.getGreen() / 255.0F;
            float ab = (float)color.getBlue() / 255.0F;
            if (meta == 7) {
               fr = ar;
               fg = ag;
               fb = ab;
            } else {
               float m = (float)(meta - 2);
               fr = (br + ar * m) / (m + 1.0F);
               fg = (bg + ag * m) / (m + 1.0F);
               fb = (bb + ab * m) / (m + 1.0F);
            }
         }

         Minecraft mc = FMLClientHandler.instance().getClient();
         GL11.glPushMatrix();
         GL11.glEnable(2977);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glEnable(32826);
         GL11.glBlendFunc(770, 771);
         GL11.glTranslated(x + (double)0.5F, y + (double)0.75F, z + (double)0.5F);
         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         if (meta > 2) {
            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
            float scale = MathHelper.sin((float)(p.ticksExisted + pod.hashCode() % 100) / 8.0F) * 0.1F + 0.9F;
            GL11.glPushMatrix();
            float bs = MathHelper.sin((float)(p.ticksExisted + pod.hashCode() % 100) / 8.0F) * 0.3F + 0.7F;
            int j = meta * 10 + (int)(150.0F * scale);
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
            GL11.glTranslated(0.0F, 0.1, 0.0F);
            GL11.glScaled((double)0.125F * (double)meta * (double)scale, (double)0.125F * (double)meta * (double)scale, (double)0.125F * (double)meta * (double)scale);
            UtilsFX.bindTexture(pod0tex);
            this.model.pod0.render(0.0625F);
            GL11.glPopMatrix();
         }

         GL11.glScaled(0.15 * (double)meta, 0.15 * (double)meta, 0.15 * (double)meta);
         GL11.glColor4f(fr, fg, fb, 0.9F);
         UtilsFX.bindTexture(pod2tex);
         this.model.pod2.render(0.0625F);
         GL11.glDisable(32826);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity te, double d, double d1, double d2, float f) {

      if (!(te instanceof TileManaPod)) {return;}
      this.renderEntityAt((TileManaPod) te, d, d1, d2, f);
   }
}
