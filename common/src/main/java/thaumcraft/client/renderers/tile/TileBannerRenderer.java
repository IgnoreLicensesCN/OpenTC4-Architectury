package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBanner;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.TileBanner;

@SideOnly(Side.CLIENT)
public class TileBannerRenderer extends TileEntitySpecialRenderer {
   private ModelBanner model = new ModelBanner();

   public void renderTileEntityAt(TileBanner banner, double par2, double par4, double par6, float par8) {
      if (banner == null){return;}
      boolean flag = banner.getWorldObj() != null;
      long k = flag ? banner.getWorldObj().getTotalWorldTime() : 0L;
      GL11.glPushMatrix();
      if (banner.getAspect() == null && banner.getColor() == -1) {
         UtilsFX.bindTexture("textures/models/banner_cultist.png");
      } else {
         UtilsFX.bindTexture("textures/models/banner_blank.png");
      }

      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (banner.getWorldObj() != null) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         float f2 = (float)(banner.getFacing() * 360) / 16.0F;
         GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
      }

      if (!banner.getWall()) {
         this.model.renderPole();
      } else {
         GL11.glTranslated(0.0F, 0.0F, -0.4125);
      }

      this.model.renderBeam();
      if (banner.getColor() != -1) {
         Color c = new Color(Utils.colors[banner.getColor()]);
         GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
      }

      this.model.renderTabs();
      float f3 = (float)(banner.xCoord * 7 + banner.yCoord * 9 + banner.zCoord * 13) + (float)k + par8;
      float rx = (0.005F + 0.005F * MathHelper.cos(f3 * (float)Math.PI * 0.02F)) * (float)Math.PI;
      this.model.Banner.rotateAngleX = rx;
      this.model.renderBanner();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (banner.getAspect() != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 0.05001F);
         GL11.glScaled(0.0375, 0.0375, 0.0375);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-rx * (180F / (float)Math.PI) * 2.0F, 1.0F, 0.0F, 0.0F);
         UtilsFX.drawTag(-8, 4, banner.getAspect(), 0.0F, 0, 0.0F, 771, 0.75F, false);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (! (tileEntity instanceof TileBanner)) {return;}
      this.renderTileEntityAt((TileBanner)tileEntity, par2, par4, par6, par8);
   }
}
