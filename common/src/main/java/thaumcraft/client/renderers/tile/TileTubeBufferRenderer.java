package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelTubeValve;
import thaumcraft.common.tiles.TileTubeBuffer;

public class TileTubeBufferRenderer extends TileEntitySpecialRenderer {
   private ModelTubeValve model = new ModelTubeValve();

   public void renderEntityAt(TileTubeBuffer buffer, double x, double y, double z, float fq) {
      UtilsFX.bindTexture("textures/models/valve.png");
      if (buffer.getWorldObj() != null) {
         for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (buffer.chokedSides[dir.ordinal()] != 0 && buffer.openSides[dir.ordinal()] && ThaumcraftApiHelper.getConnectableTile(buffer.getWorldObj(), buffer.xCoord, buffer.yCoord, buffer.zCoord, dir) != null) {
               GL11.glPushMatrix();
               GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
               if (dir.getOpposite().offsetY == 0) {
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               } else {
                  GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
                  GL11.glRotatef(90.0F, (float)dir.getOpposite().offsetY, 0.0F, 0.0F);
               }

               GL11.glRotatef(90.0F, (float)dir.getOpposite().offsetX, (float)dir.getOpposite().offsetY, (float)dir.getOpposite().offsetZ);
               GL11.glPushMatrix();
               if (buffer.chokedSides[dir.ordinal()] == 2) {
                  GL11.glColor3f(1.0F, 0.3F, 0.3F);
               } else {
                  GL11.glColor3f(0.3F, 0.3F, 1.0F);
               }

               GL11.glScaled(1.2, 1.0F, 1.2);
               GL11.glTranslated(0.0F, -0.5F, 0.0F);
               this.model.render();
               GL11.glPopMatrix();
               GL11.glPopMatrix();
            }
         }
      }

   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      this.renderEntityAt((TileTubeBuffer)tileentity, d, d1, d2, f);
   }
}
