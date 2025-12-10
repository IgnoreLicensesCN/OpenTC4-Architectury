package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBoreBase;
import thaumcraft.common.tiles.TileArcaneBoreBase;

public class TileArcaneBoreBaseRenderer extends TileEntitySpecialRenderer {
   private ModelBoreBase model = new ModelBoreBase();

   public void renderEntityAt(TileArcaneBoreBase bore, double x, double y, double z, float fq) {
      if (bore == null){return;}
      Minecraft mc = FMLClientHandler.instance().getClient();
      UtilsFX.bindTexture("textures/models/Bore.png");
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
      GL11.glPushMatrix();
      this.model.render();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      switch (bore.orientation.ordinal()) {
         case 2:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 3:
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 4:
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 5:
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
      }

      this.model.renderNozzle();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      if (! (tileentity instanceof TileArcaneBoreBase)) {return;}
      this.renderEntityAt((TileArcaneBoreBase)tileentity, d, d1, d2, f);
   }
}
