package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEssentiaReservoir;

@SideOnly(Side.CLIENT)
public class TileEssentiaReservoirRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation RELAY = new ResourceLocation("thaumcraft", "textures/models/reservoir.obj");

   public TileEssentiaReservoirRenderer() {
      this.model = AdvancedModelLoader.loadModel(RELAY);
   }

   public void renderTileEntityAt(TileEssentiaReservoir tile, double par2, double par4, double par6, float par8) {
      int facing = tile.facing.ordinal();
      GL11.glPushMatrix();
      this.translateFromOrientation(par2, par4, par6, facing);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/models/reservoir.png");
      this.model.renderAll();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(par2, par4 - (double)0.5F, par6);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.renderLiquid(tile, par2, par4, par6, par8);
      GL11.glPopMatrix();
   }

   public void renderLiquid(TileEssentiaReservoir te, double x, double y, double z, float f) {
      if (this.field_147501_a.field_147553_e != null && te.displayAspect != null && te.essentia.visSize() != 0) {
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         World world = te.getWorldObj();
         RenderBlocks renderBlocks = new RenderBlocks();
         GL11.glDisable(2896);
         float level = (float)te.essentia.visSize() / (float)te.maxAmount;
         Tessellator t = Tessellator.instance;
         renderBlocks.setRenderBounds(BlockRenderer.W3, BlockRenderer.W3, BlockRenderer.W3, BlockRenderer.W13, BlockRenderer.W3 + BlockRenderer.W10 * level, BlockRenderer.W13);
         t.startDrawingQuads();
         t.setColorRGBA_F(te.cr, te.cg, te.cb, 0.9F);
         int bright = 200;
         t.setBrightness(200);
         IIcon icon = ((BlockJar)ConfigBlocks.blockJar).iconLiquid;
         this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
         renderBlocks.renderFaceYNeg(ConfigBlocks.blockEssentiaReservoir, 0.0F, 0.5F, 0.0F, icon);
         renderBlocks.renderFaceYPos(ConfigBlocks.blockEssentiaReservoir, 0.0F, 0.5F, 0.0F, icon);
         renderBlocks.renderFaceZNeg(ConfigBlocks.blockEssentiaReservoir, 0.0F, 0.5F, 0.0F, icon);
         renderBlocks.renderFaceZPos(ConfigBlocks.blockEssentiaReservoir, 0.0F, 0.5F, 0.0F, icon);
         renderBlocks.renderFaceXNeg(ConfigBlocks.blockEssentiaReservoir, 0.0F, 0.5F, 0.0F, icon);
         renderBlocks.renderFaceXPos(ConfigBlocks.blockEssentiaReservoir, 0.0F, 0.5F, 0.0F, icon);
         t.draw();
         GL11.glEnable(2896);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
      }
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
      this.renderTileEntityAt((TileEssentiaReservoir)tileEntity, par2, par4, par6, par8);
   }
}
