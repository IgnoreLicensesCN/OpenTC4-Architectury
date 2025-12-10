package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelCentrifuge;
import thaumcraft.common.tiles.TileCentrifuge;

public class TileCentrifugeRenderer extends TileEntitySpecialRenderer {
   private ModelCentrifuge model = new ModelCentrifuge();

   public void renderEntityAt(TileCentrifuge cf, double x, double y, double z, float fq) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      UtilsFX.bindTexture("textures/models/centrifuge.png");
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      this.model.renderBoxes();
      GL11.glRotated(cf.rotation, 0.0F, 1.0F, 0.0F);
      this.model.renderSpinnyBit();
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      this.renderEntityAt((TileCentrifuge)tileentity, d, d1, d2, f);
   }
}
