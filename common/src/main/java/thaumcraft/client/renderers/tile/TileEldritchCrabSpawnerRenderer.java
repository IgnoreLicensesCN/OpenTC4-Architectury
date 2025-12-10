package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;

@SideOnly(Side.CLIENT)
public class TileEldritchCrabSpawnerRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation VENT = new ResourceLocation("thaumcraft", "textures/models/crabvent.obj");

   public TileEldritchCrabSpawnerRenderer() {
      this.model = AdvancedModelLoader.loadModel(VENT);
   }

   public void renderTileEntityAt(TileEldritchCrabSpawner tile, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      this.translateFromOrientation(par2, par4, par6, tile.getFacing());
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/models/crabvent.png");
      this.model.renderAll();
      GL11.glPopMatrix();
   }

   private void translateFromOrientation(double x, double y, double z, int orientation) {
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

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileEldritchCrabSpawner)tileEntity, par2, par4, par6, par8);
   }
}
