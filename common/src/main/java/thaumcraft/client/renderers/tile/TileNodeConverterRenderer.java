package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
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
import thaumcraft.common.tiles.TileNodeConverter;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockTypeSafely;

@SideOnly(Side.CLIENT)
public class TileNodeConverterRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation MODEL = new ResourceLocation("thaumcraft", "textures/models/node_stabilizer.obj");

   public TileNodeConverterRenderer() {
      this.model = AdvancedModelLoader.loadModel(MODEL);
   }

   public void renderTileEntityAt(TileNodeConverter tile, double par2, double par4, double par6, float par8) {
      if (tile == null){return;}
      int bright = 20;
      Block blockType = getBlockTypeSafely(tile);
      if (blockType != null
      ) {
         bright = blockType.getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
      UtilsFX.bindTexture("textures/models/node_converter.png");
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float v = (float)Math.min(50, tile.count) / 137.0F;
      this.model.renderPart("lock");
      if (tile.status == 2) {
         GL11.glColor4f(1.0F, 0.0F, 0.3F, 1.0F);
      } else if (tile.status == 1) {
         GL11.glColor4f(1.0F, 0.6F, 0.1F, 1.0F);
      } else {
         GL11.glColor4f(0.5F, 1.0F, 0.5F, 1.0F);
      }

      if (tile.getWorldObj() != null) {
         float scale = MathHelper.sin((float)Minecraft.getMinecraft().renderViewEntity.ticksExisted / 3.0F) * 0.1F + 0.9F;
         int j = 50 + (int)(170.0F * v * 2.5F * scale);
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
      }

      UtilsFX.bindTexture("textures/models/node_converter_over.png");
      this.model.renderPart("lock");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         if (tile.getWorldObj() != null) {
            int k = bright % 65536;
            int l = bright / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         }

         GL11.glRotatef((float)(90 * a), 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(0.0F, 0.0F, v);
         UtilsFX.bindTexture("textures/models/node_converter.png");
         this.model.renderPart("piston");
         if (tile.status == 2) {
            GL11.glColor4f(1.0F, 0.0F, 0.3F, 1.0F);
         } else if (tile.status == 1) {
            GL11.glColor4f(1.0F, 0.6F, 0.1F, 1.0F);
         } else {
            GL11.glColor4f(0.5F, 1.0F, 0.5F, 1.0F);
         }

         if (tile.getWorldObj() != null) {
            float scale = MathHelper.sin((float)(Minecraft.getMinecraft().renderViewEntity.ticksExisted + a * 5) / 3.0F) * 0.1F + 0.9F;
            int j = 50 + (int)(170.0F * v * 2.5F * scale);
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         }

         UtilsFX.bindTexture("textures/models/node_converter_over.png");
         this.model.renderPart("piston");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (!(tileEntity instanceof TileNodeConverter)){return;}
      this.renderTileEntityAt((TileNodeConverter)tileEntity, par2, par4, par6, par8);
   }
}
