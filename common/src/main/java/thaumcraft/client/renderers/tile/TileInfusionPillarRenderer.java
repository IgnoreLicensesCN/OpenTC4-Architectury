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
import thaumcraft.common.tiles.TileInfusionPillar;

@SideOnly(Side.CLIENT)
public class TileInfusionPillarRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation PILLAR = new ResourceLocation("thaumcraft", "textures/models/pillar.obj");

   public TileInfusionPillarRenderer() {
      this.model = AdvancedModelLoader.loadModel(PILLAR);
   }

   public void renderTileEntityAt(TileInfusionPillar tile, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/models/pillar.png");
      if (tile.orientation == 3) {
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      } else if (tile.orientation == 4) {
         GL11.glRotatef(270.0F, 0.0F, 0.0F, 1.0F);
      } else if (tile.orientation == 5) {
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      }

      this.model.renderAll();
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileInfusionPillar)tileEntity, par2, par4, par6, par8);
   }
}
