package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileCrystal;

@SideOnly(Side.CLIENT)
public class TileEldritchCrystalRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation CRYSTAL = new ResourceLocation("thaumcraft", "textures/models/vcrystal.obj");

   public TileEldritchCrystalRenderer() {
      this.model = AdvancedModelLoader.loadModel(CRYSTAL);
   }

   public void renderTileEntityAt(TileCrystal tile, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      this.translateFromOrientation(par2, par4, par6, tile.orientation, (tile.getWorldObj() == null ? 0 : tile.hashCode()) % 4);
      EntityPlayer p = Minecraft.getMinecraft().thePlayer;
      UtilsFX.bindTexture("textures/blocks/crust.png");
      this.model.renderPart("Base");
      float shade = MathHelper.sin((float)p.ticksExisted / 6.0F) * 0.075F + 0.925F;
      int var19 = (int)(210.0F * shade);
      int var20 = var19 % 65536;
      int var21 = var19 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var20, (float) var21);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
      UtilsFX.bindTexture("textures/models/vcrystal.png");
      this.model.renderPart("Crystal");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   private void translateFromOrientation(double x, double y, double z, int orientation, int r) {
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      if (orientation == 0) {
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 1) {
         GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 2) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation != 3) {
         if (orientation == 4) {
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         } else if (orientation == 5) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      GL11.glTranslated(0.0F, 0.0F, -0.5F);
      GL11.glRotatef(90.0F * (float)r, 0.0F, 0.0F, 1.0F);
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileCrystal)tileEntity, par2, par4, par6, par8);
   }
}
