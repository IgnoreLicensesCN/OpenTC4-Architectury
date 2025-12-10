package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelTubeValve;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileTubeValve;

public class TileTubeValveRenderer extends TileEntitySpecialRenderer {
   private ModelTubeValve model = new ModelTubeValve();

   public void renderEntityAt(TileTubeValve valve, double x, double y, double z, float fq) {
      UtilsFX.bindTexture("textures/models/valve.png");
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      if (valve.facing.offsetY == 0) {
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      } else {
         GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
         GL11.glRotatef(90.0F, (float)valve.facing.offsetY, 0.0F, 0.0F);
      }

      GL11.glRotatef(90.0F, (float)valve.facing.offsetX, (float)valve.facing.offsetY, (float)valve.facing.offsetZ);
      GL11.glRotated((double)(-valve.rotation) * (double)1.5F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslated(0.0F, -(valve.rotation / 360.0F) * 0.12F, 0.0F);
      GL11.glPushMatrix();
      this.model.render();
      GL11.glPopMatrix();
      this.renderValve();
      GL11.glPopMatrix();
   }

   void renderValve() {
      GL11.glPushMatrix();
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(-0.25F, -0.25F, -0.25F);
      GL11.glScaled(0.5F, 0.5F, 0.5F);
      Tessellator tessellator = Tessellator.instance;
      IIcon icon = ((BlockTube)ConfigBlocks.blockTube).iconValve;
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMinU();
      float f4 = icon.getMaxV();
      this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
      ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.1F);
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      this.renderEntityAt((TileTubeValve)tileentity, d, d1, d2, f);
   }
}
