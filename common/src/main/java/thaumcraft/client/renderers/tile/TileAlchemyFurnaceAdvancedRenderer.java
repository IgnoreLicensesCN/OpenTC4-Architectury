package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileAlchemyFurnaceAdvanced;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockTypeSafely;

@SideOnly(Side.CLIENT)
public class TileAlchemyFurnaceAdvancedRenderer extends TileEntitySpecialRenderer {
   public static IModelCustom model = null;
   public static final ResourceLocation FURNACE = new ResourceLocation("thaumcraft", "textures/models/adv_alch_furnace.obj");

   public TileAlchemyFurnaceAdvancedRenderer() {
      if (model == null){
         model = AdvancedModelLoader.loadModel(FURNACE);
      }
   }

   public void renderTileEntityAt(TileAlchemyFurnaceAdvanced tile, double par2, double par4, double par6, float par8) {
      if (tile == null){return;}
      Block blockType = getBlockTypeSafely(tile);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (tile.heat <= 100) {
         UtilsFX.bindTexture("textures/models/alch_furnace.png");
      } else {
         UtilsFX.bindTexture("textures/models/alch_furnace_on.png");
      }

      model.renderPart("Base");
      if (tile.vis <= 0) {
         UtilsFX.bindTexture("textures/models/alch_furnace_tank.png");
      } else {
         UtilsFX.bindTexture("textures/models/alch_furnace_tank_on.png");
      }

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         GL11.glRotatef((float)(90 * a), 0.0F, 0.0F, 1.0F);
         model.renderPart("Tank");
         GL11.glPopMatrix();
      }

      if (tile.vis > 0) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.5F, -0.5F, 1.1F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         this.renderQuadCenteredFromIcon(ConfigBlocks.FLUXGOO.getIcon(), 190, 0.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         float f = 1.0F - (float)tile.vis / (float)tile.maxVis;

         for(int a = 0; a < 4; ++a) {
            GL11.glPushMatrix();
            GL11.glPushMatrix();
            GL11.glRotatef((float)(90 * a), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.85F, -1.8F, -1.4F);
            GL11.glScaled(0.3, 0.6, 1.0F);
            if (blockType != null){
               this.renderQuadCenteredFromIcon(blockType.getIcon(0, 0), 150, 0.0F);
            }
            GL11.glTranslatef(0.0F, 0.0F, -0.01F);
            this.renderQuadCenteredFromIcon(ConfigBlocks.FLUXGOO.getIcon(), 190, f);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glRotatef((float)(90 * a), 0.0F, 0.0F, -1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(1.15F, 1.8F, -1.4F);
            GL11.glScaled(-0.3, -0.6, -1.0F);
            if (blockType != null){
               this.renderQuadCenteredFromIcon(blockType.getIcon(0, 0), 150, 0.0F);
            }
            GL11.glTranslatef(0.0F, 0.0F, 0.01F);
            this.renderQuadCenteredFromIcon(ConfigBlocks.FLUXGOO.getIcon(), 190, f);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
      }

      if (tile.heat > 100) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 1.0F);

         for(int a = 0; a < 4; ++a) {
            GL11.glPushMatrix();
            GL11.glRotatef((float)(90 * a), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(135.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, 0.0F, -1.0F);
            GL11.glPushMatrix();
            this.renderQuadCenteredFromIcon(Blocks.fire.getIcon(0, 0), 220, 1.0F - Math.min(1.0F, (float)tile.heat / (float)tile.maxPower));
            GL11.glPopMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.05F);
            if (blockType != null){
               this.renderQuadCenteredFromIcon(blockType.getIcon(0, 0), 150, 0.0F);
            }
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public void renderQuadCenteredFromIcon(IIcon icon, int brightness, float width) {
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

      GL11.glDisable(GL11.GL_LIGHTING);
//      RenderHelper.disableStandardItemLighting();

      Tessellator tessellator = Tessellator.instance;
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMinU();
      float f4 = icon.getMaxV();
      GL11.glEnable(32826);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      tessellator.startDrawingQuads();
      tessellator.setBrightness(brightness);
      tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV(0.0F, 1.0F, 0.0F, f1, f4);
      tessellator.addVertexWithUV(1.0F, 1.0F, 0.0F, f3, f4);
      tessellator.addVertexWithUV(1.0F, width, 0.0F, f3, f2);
      tessellator.addVertexWithUV(0.0F, width, 0.0F, f1, f2);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
      GL11.glEnable(GL11.GL_LIGHTING);
//      RenderHelper.enableStandardItemLighting();
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (!(tileEntity instanceof TileAlchemyFurnaceAdvanced)){return;}
      this.renderTileEntityAt((TileAlchemyFurnaceAdvanced)tileEntity, par2, par4, par6, par8);
   }
}
