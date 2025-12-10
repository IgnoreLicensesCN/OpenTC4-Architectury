package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileEssentiaCrystalizer;

@SideOnly(Side.CLIENT)
public class TileEssentiaCrystalizerRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private IModelCustom model2;
   private static final ResourceLocation RELAY = new ResourceLocation("thaumcraft", "textures/models/crystalizer.obj");
   private static final ResourceLocation CRYSTAL = new ResourceLocation("thaumcraft", "textures/models/vis_relay.obj");

   public TileEssentiaCrystalizerRenderer() {
      this.model = AdvancedModelLoader.loadModel(RELAY);
      this.model2 = AdvancedModelLoader.loadModel(CRYSTAL);
   }

   public void renderTileEntityAt(TileEssentiaCrystalizer tile, double par2, double par4, double par6, float par8) {
      int facing = tile.facing.ordinal();
      int ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted;
      GL11.glPushMatrix();
      this.translateFromOrientation(par2, par4, par6, facing);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/crystalizer.png");
      this.model.renderAll();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      UtilsFX.bindTexture("textures/models/vis_relay.png");
      GL11.glColor3f(tile.cr, tile.cg, tile.cb);

      for(int q = 0; q < 4; ++q) {
         GL11.glPushMatrix();
         GL11.glScaled(0.75F, 0.75F, 0.75F);
         float glow = MathHelper.sin(((float)ticks + par8 + (float)(q * 10)) / 2.0F) * 0.05F + 0.95F;
         int j = 50 + (int)(150.0F * glow);
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         GL11.glRotatef((float)(90 * q), 0.0F, 0.0F, 1.0F);
         GL11.glTranslated(0.34, 0.0F, 1.2125);
         GL11.glRotatef(tile.spin + tile.spinInc * par8, 0.0F, 0.0F, 1.0F);
         this.model2.renderPart("Crystal");
         GL11.glPopMatrix();
      }

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

      GL11.glTranslated(0.0F, 0.0F, -0.5F);
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileEssentiaCrystalizer)tileEntity, par2, par4, par6, par8);
   }
}
