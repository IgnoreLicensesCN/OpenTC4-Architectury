package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelTubeValve;
import thaumcraft.common.tiles.TileTubeOneway;

public class TileTubeOnewayRenderer extends TileEntitySpecialRenderer {
   private ModelTubeValve model = new ModelTubeValve();
   ForgeDirection fd = null;

   public void renderEntityAt(TileTubeOneway valve, double x, double y, double z, float fq) {
      UtilsFX.bindTexture("textures/models/valve.png");
      if (valve.getWorldObj() == null || ThaumcraftApiHelper.getConnectableTile(valve.getWorldObj(), valve.xCoord, valve.yCoord, valve.zCoord, valve.facing.getOpposite()) != null) {
         GL11.glPushMatrix();
         this.fd = valve.facing;
         GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
         if (this.fd.offsetY == 0) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
            GL11.glRotatef(90.0F, (float)this.fd.offsetY, 0.0F, 0.0F);
         }

         GL11.glRotatef(90.0F, (float)this.fd.offsetX, (float)this.fd.offsetY, (float)this.fd.offsetZ);
         GL11.glPushMatrix();
         GL11.glColor3f(0.45F, 0.5F, 1.0F);
         GL11.glScaled(1.1, 0.5F, 1.1);
         GL11.glTranslated(0.0F, -0.5F, 0.0F);
         this.model.render();
         GL11.glTranslated(0.0F, -0.25F, 0.0F);
         this.model.render();
         GL11.glTranslated(0.0F, -0.25F, 0.0F);
         this.model.render();
         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }
   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      this.renderEntityAt((TileTubeOneway)tileentity, d, d1, d2, f);
   }
}
