package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBellows;
import thaumcraft.common.tiles.TileBellows;

public class TileBellowsRenderer extends TileEntitySpecialRenderer {
   private ModelBellows model = new ModelBellows();

   private void translateFromOrientation(double x, double y, double z, int orientation) {
      GL11.glTranslatef((float)x + 0.5F, (float)y - 0.5F, (float)z + 0.5F);
      if (orientation == 0) {
         GL11.glTranslatef(0.0F, 1.0F, -1.0F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 1) {
         GL11.glTranslatef(0.0F, 1.0F, 1.0F);
         GL11.glRotatef(270.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 2) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 4) {
         GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 5) {
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      }

   }

   public void renderEntityAt(TileBellows bellows, double x, double y, double z, float fq) {
      float scale = 0.0F;
      if (bellows.getWorldObj() == null) {
         EntityPlayer p = Minecraft.getMinecraft().thePlayer;
         scale = MathHelper.sin((float)p.ticksExisted / 8.0F) * 0.3F + 0.7F;
         bellows.orientation = 2;
      } else {
         scale = bellows.inflation;
      }

      float tscale = 0.125F + scale * 0.875F;
      Minecraft mc = FMLClientHandler.instance().getClient();
      UtilsFX.bindTexture("textures/models/bellows.png");
      GL11.glPushMatrix();
      GL11.glEnable(2977);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glEnable(32826);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.translateFromOrientation((float)x, (float)y, (float)z, bellows.orientation);
      GL11.glTranslatef(0.0F, 1.0F, 0.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, (scale + 0.1F) / 2.0F, 0.5F);
      this.model.Bag.setRotationPoint(0.0F, 0.5F, 0.0F);
      this.model.Bag.render(0.0625F);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, -tscale / 2.0F + 0.5F, 0.0F);
      this.model.TopPlank.render(0.0625F);
      GL11.glTranslatef(0.0F, tscale / 2.0F - 0.5F, 0.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, tscale / 2.0F - 0.5F, 0.0F);
      this.model.BottomPlank.render(0.0625F);
      GL11.glTranslatef(0.0F, -tscale / 2.0F + 0.5F, 0.0F);
      GL11.glPopMatrix();
      this.model.render();
      GL11.glDisable(32826);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      this.renderEntityAt((TileBellows)tileentity, d, d1, d2, f);
   }
}
