package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileVisRelay;

@SideOnly(Side.CLIENT)
public class TileVisRelayRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation RELAY = new ResourceLocation("thaumcraft", "textures/models/vis_relay.obj");

   public TileVisRelayRenderer() {
      this.model = AdvancedModelLoader.loadModel(RELAY);
   }

   public void renderTileEntityAt(TileVisRelay tile, double par2, double par4, double par6, float par8) {
      int facing = 1;
      if (tile.getWorldObj() != null) {
         facing = tile.orientation;
      }

      int ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted;
      GL11.glPushMatrix();
      this.translateFromOrientation(par2, par4, par6, facing);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(45.0F, 0.0F, 0.0F, 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/vis_relay.png");
      GL11.glPushMatrix();
      GL11.glScaled(0.75F, 0.75F, 0.75F);
      GL11.glTranslated(0.0F, 0.0F, -0.16);
      this.model.renderPart("RingBase");
      GL11.glPopMatrix();
      this.model.renderPart("RingFloat");
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      if (tile.color >= 0) {
         Color c = new Color(TileVisRelay.colors[tile.color]);
         GL11.glColor3f((float)c.getRed() / 200.0F, (float)c.getGreen() / 200.0F, (float)c.getBlue() / 200.0F);
      }

      float scale = MathHelper.sin(((float)ticks + par8) / 2.0F) * 0.05F + 0.95F;
      int j = (VisNetHandler.isNodeValid(tile.getParent()) ? 50 : 0) + (int)(150.0F * scale);
      int k = j % 65536;
      int l = j / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
      this.model.renderPart("Crystal");
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   private void translateFromOrientation(double x, double y, double z, int orientation) {
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      if (orientation == 0) {
         GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 1) {
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation != 2) {
         if (orientation == 3) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         } else if (orientation == 4) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         } else if (orientation == 5) {
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         }
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileVisRelay)tileEntity, par2, par4, par6, par8);
   }
}
