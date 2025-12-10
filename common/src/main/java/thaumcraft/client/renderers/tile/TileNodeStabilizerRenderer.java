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
import thaumcraft.common.tiles.TileNodeStabilizer;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockTypeSafely;

@SideOnly(Side.CLIENT)
public class TileNodeStabilizerRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation MODEL = new ResourceLocation("thaumcraft", "textures/models/node_stabilizer.obj");

   public TileNodeStabilizerRenderer() {
      this.model = AdvancedModelLoader.loadModel(MODEL);
   }

   public void renderTileEntityAt(TileNodeStabilizer tile, double par2, double par4, double par6, float par8) {
      int lock = 1;
      int bright = 20;
      Block blockType = getBlockTypeSafely(tile);
      if (blockType != null) {
         if (tile.hasWorldObj() && tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord) == 10) {
            lock = 2;
         }

         bright = blockType.getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
      } else {
         lock = tile.lock;
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/models/node_stabilizer.png");
      this.model.renderPart("lock");

      for(int a = 0; a < 4; ++a) {
         GL11.glPushMatrix();
         if (tile.getWorldObj() != null) {
            int k = bright % 65536;
            int l = bright / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         }

         GL11.glRotatef((float)(90 * a), 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(0.0F, 0.0F, (float)tile.count / 100.0F);
         UtilsFX.bindTexture("textures/models/node_stabilizer.png");
         this.model.renderPart("piston");
         if (lock == 2) {
            GL11.glColor4f(1.0F, 0.2F, 0.2F, 1.0F);
         }

         if (tile.getWorldObj() != null) {
            float scale = MathHelper.sin((float)(Minecraft.getMinecraft().renderViewEntity.ticksExisted + a * 5) / 3.0F) * 0.1F + 0.9F;
            int j = 50 + (int)(170.0F * (float)tile.count / 37.0F * scale);
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         }

         UtilsFX.bindTexture("textures/models/node_stabilizer_over.png");
         this.model.renderPart("piston");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      if (tile.count > 0) {
         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);
         GL11.glDepthMask(false);
         float alpha = MathHelper.sin((float)Minecraft.getMinecraft().renderViewEntity.ticksExisted / 8.0F) * 0.1F + 0.5F;
         UtilsFX.bindTexture("textures/misc/node_bubble.png");
         UtilsFX.renderFacingQuad((double)tile.xCoord + (double)0.5F, (double)tile.yCoord + (double)1.5F, (double)tile.zCoord + (double)0.5F, 0.0F, 0.9F, (float)tile.count / 37.0F * alpha, 1, 0, par8, lock == 1 ? 16777215 : 16729156);
         GL11.glDepthMask(true);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (!(tileEntity instanceof TileNodeStabilizer)){return;}
      this.renderTileEntityAt((TileNodeStabilizer)tileEntity, par2, par4, par6, par8);
   }
}
