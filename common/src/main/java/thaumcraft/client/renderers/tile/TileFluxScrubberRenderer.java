package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileFluxScrubber;

public class TileFluxScrubberRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation CAP = new ResourceLocation("thaumcraft", "textures/models/obelisk_cap.obj");

   public TileFluxScrubberRenderer() {
      this.model = AdvancedModelLoader.loadModel(CAP);
   }

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      GL11.glPushMatrix();
      int facing = ((TileFluxScrubber)te).facing.ordinal();
      this.translateFromOrientation(x, y, z, facing);
      UtilsFX.bindTexture("textures/models/fluxscrubber.png");
      this.model.renderPart("Cap");
      float q = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + f + (float)((TileFluxScrubber)te).count;
      float bob = MathHelper.sin(q / 8.0F) * 0.075F + 0.075F;
      GL11.glTranslated(0.0F, 0.0F, -bob);
      this.model.renderPart("Tip");
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
}
