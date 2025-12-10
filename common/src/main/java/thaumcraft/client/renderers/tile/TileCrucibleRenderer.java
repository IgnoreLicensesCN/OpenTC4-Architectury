package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileCrucible;

public class TileCrucibleRenderer extends TileEntitySpecialRenderer {
   public void renderEntityAt(TileCrucible cr, double x, double y, double z, float fq) {
      if (cr.tank.getFluidAmount() > 0) {
         this.renderFluid(cr, x, y, z);
      }

   }

   public void renderFluid(TileCrucible cr, double x, double y, double z) {
      IIcon icon = Blocks.water.getIcon(0, 0);
      GL11.glPushMatrix();
      GL11.glTranslated(x, y + (double)cr.getFluidHeight(), z + (double)1.0F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      if (cr.tank.getFluidAmount() > 0) {
         float var10000 = (float)cr.tagAmount();
         cr.getClass();
         float recolor = var10000 / 100.0F;
         if (recolor > 0.0F) {
            recolor = 0.5F + recolor / 2.0F;
         }

         UtilsFX.renderQuadFromIcon(true, icon, 1.0F, 1.0F - recolor / 3.0F, 1.0F - recolor, 1.0F - recolor / 2.0F, ConfigBlocks.blockMetalDevice.getMixedBrightnessForBlock(cr.getWorldObj(), cr.xCoord, cr.yCoord, cr.zCoord), 771, 1.0F);
      }

      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity te, double d, double d1, double d2, float f) {
      this.renderEntityAt((TileCrucible)te, d, d1, d2, f);
   }
}
