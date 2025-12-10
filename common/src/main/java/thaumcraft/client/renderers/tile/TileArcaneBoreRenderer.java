package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBore;
import thaumcraft.client.renderers.models.ModelBoreEmit;
import thaumcraft.client.renderers.models.ModelJar;
import thaumcraft.common.tiles.TileArcaneBore;

public class TileArcaneBoreRenderer extends TileEntitySpecialRenderer {
   private ModelBoreEmit modelEmit = new ModelBoreEmit();
   private ModelBore modelBore = new ModelBore();
   private ModelJar modelJar = new ModelJar();

   public void renderEntityAt(TileArcaneBore bore, double x, double y, double z, float fq) {
      if (bore == null){return;}
      Minecraft mc = FMLClientHandler.instance().getClient();
      UtilsFX.bindTexture("textures/models/Bore.png");
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
      GL11.glRotatef((float)bore.rotX - bore.vRadX + fq * (float)bore.speedX, 0.0F, 1.0F, 0.0F);
      GL11.glPushMatrix();
      if (bore.baseOrientation.ordinal() == 0) {
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      }

      GL11.glTranslatef(0.0F, -0.5F, 0.0F);
      this.modelBore.renderBase();
      GL11.glPopMatrix();
      GL11.glRotatef((float)bore.rotZ - bore.vRadZ + fq * (float)bore.speedZ, 0.0F, 0.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -0.5F, 0.0F);
      this.modelBore.renderNozzle();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glRotatef((float)bore.topRotation, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.5F, 0.0F);
      this.modelEmit.render(bore.hasFocus);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      float rotation = (float)(Minecraft.getMinecraft().renderViewEntity.ticksExisted % 45) + fq;
      GL11.glTranslatef(0.0F, -0.17F, 0.0F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-(rotation * 8.0F), 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
      UtilsFX.renderQuadCenteredFromTexture("textures/misc/vortex.png", 0.4F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      rotation = (float)(Minecraft.getMinecraft().renderViewEntity.ticksExisted % 45) + fq;
      GL11.glTranslatef(0.0F, -0.21F, 0.0F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(rotation * 8.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
      UtilsFX.renderQuadCenteredFromTexture("textures/misc/vortex.png", 0.3F, 1.0F, 1.0F, 1.0F, 200, 771, 0.8F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      rotation = (float)(Minecraft.getMinecraft().renderViewEntity.ticksExisted % 45) + fq;
      GL11.glTranslatef(0.0F, -0.25F, 0.0F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-(rotation * 8.0F), 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(-10.0F, 0.0F, 1.0F, 0.0F);
      UtilsFX.renderQuadCenteredFromTexture("textures/misc/vortex.png", 0.2F, 1.0F, 1.0F, 1.0F, 200, 771, 0.8F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/jar.png");
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, 0.3F, 0.0F);
      GL11.glScalef(0.6F, 0.6F, 0.6F);
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      this.modelJar.Core.render(0.0625F);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
      if (! (tileentity instanceof TileArcaneBore)) {return;}
      this.renderEntityAt((TileArcaneBore)tileentity, d, d1, d2, f);
   }
}
