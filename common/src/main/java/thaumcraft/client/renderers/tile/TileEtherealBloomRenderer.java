package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelCube;
import thaumcraft.common.blocks.BlockCustomPlant;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEtherealBloom;

import static thaumcraft.client.renderers.tile.TileNodeRenderer.renderFacingStrip_tweaked;

@SideOnly(Side.CLIENT)
public class TileEtherealBloomRenderer extends TileEntitySpecialRenderer {
   String tx2 = "textures/models/crystalcapacitor.png";
   BlockCustomPlant block;
   private ModelCube model = new ModelCube();

   public TileEtherealBloomRenderer() {
      this.block = (BlockCustomPlant)ConfigBlocks.blockCustomPlant;
   }

   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float par8) {
      float rc1 = (float)((TileEtherealBloom)tile).growthCounter + par8;
      float rc2 = rc1;
      float rc3 = rc1 - 33.0F;
      float rc4 = rc1 - 66.0F;
      if (rc1 > 100.0F) {
         rc1 = 100.0F;
      }

      if (rc2 > 50.0F) {
         rc2 = 50.0F;
      }

      if (rc3 < 0.0F) {
         rc3 = 0.0F;
      }

      if (rc3 > 33.0F) {
         rc3 = 33.0F;
      }

      if (rc4 < 0.0F) {
         rc4 = 0.0F;
      }

      if (rc4 > 33.0F) {
         rc4 = 33.0F;
      }

      float scale1 = rc1 / 100.0F;
      float scale2 = rc2 / 60.0F + 0.1666666F;
      float scale3 = rc3 / 33.0F;
      float scale4 = rc4 / 33.0F * 0.7F;
      Tessellator tessellator = Tessellator.instance;
      GL11.glPushMatrix();
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glDisable(2884);
      int i = ((TileEtherealBloom)tile).counter % 32;
      UtilsFX.bindTexture(TileNodeRenderer.nodetex);
      //            UtilsFX.renderFacingStrip
      renderFacingStrip_tweaked
              ((double)tile.xCoord + (double)0.5F, (float)tile.yCoord + scale1, (double)tile.zCoord + (double)0.5F, 0.0F, scale1, 1.0F, 32, 6, i, par8, 11197951);
      GL11.glEnable(2884);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F - (double)(scale4 / 8.0F), y + (double)scale1 - (double)(scale4 / 6.0F), z + (double)0.5F - (double)(scale4 / 8.0F));
      GL11.glScaled(scale4 / 4.0F, scale4 / 3.0F, scale4 / 4.0F);
      UtilsFX.bindTexture(this.tx2);
      this.model.render();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)0.25F, z + (double)0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         GL11.glScaled(scale3, scale1, scale3);
         GL11.glRotatef((float)(90 * a), 0.0F, 1.0F, 0.0F);
         UtilsFX.renderQuadCenteredFromIcon(true, this.block.iconLeaves, 1.0F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + 0.6, z + (double)0.5F);
      GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         GL11.glScaled(scale4, scale1 * 0.7F, scale4);
         GL11.glRotatef((float)(90 * a), 0.0F, 1.0F, 0.0F);
         UtilsFX.renderQuadCenteredFromIcon(true, this.block.iconLeaves, 1.0F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         GL11.glTranslated(0.0F, (1.0F - scale1) / 2.0F, 0.0F);
         GL11.glScaled(scale2, scale1, scale2);
         GL11.glRotatef((float)(90 * a), 0.0F, 1.0F, 0.0F);
         UtilsFX.renderQuadCenteredFromIcon(true, this.block.iconStalk, 1.0F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
   }
}
