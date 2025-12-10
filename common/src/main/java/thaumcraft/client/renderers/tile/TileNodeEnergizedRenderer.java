package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileNodeEnergized;

@SideOnly(Side.CLIENT)
public class TileNodeEnergizedRenderer extends TileEntitySpecialRenderer {
   static String tx1 = "textures/items/lightningringv.png";

   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
      EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
      TileNodeRenderer.renderNode(viewer, 64.0F, true, false, 1.0F, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((TileNodeEnergized)tile).getAuraBase(), ((TileNodeEnergized)tile).getNodeType(), ((TileNodeEnergized)tile).getNodeModifier());
      GL11.glPushMatrix();
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      long nt = System.nanoTime();
      UtilsFX.bindTexture(tx1);
      int frames = UtilsFX.getTextureAnimationSize(tx1);
      int i = (int)(((double)(nt / 40000000L) + x) % (double)frames);
      UtilsFX.renderFacingQuad((double)tile.xCoord + (double)0.5F, (double)tile.yCoord + (double)0.5F, (double)tile.zCoord + (double)0.5F, 0.0F, 0.33F, 0.9F, frames, i, partialTicks, 16777215);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
   }
}
