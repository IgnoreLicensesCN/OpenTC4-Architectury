package thaumcraft.client.renderers.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileEldritchAltar;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockTypeSafely;

public class TileEldritchCapRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation CAP = new ResourceLocation("thaumcraft", "textures/models/obelisk_cap.obj");
   private String tex = "textures/models/obelisk_cap.png";
   private String tex2 = "textures/models/obelisk_cap_2.png";
   private ItemStack eye = null;
   EntityItem entityitem = null;

   public TileEldritchCapRenderer(String texture) {
      this.tex = texture;
      this.model = AdvancedModelLoader.loadModel(CAP);
   }

   public TileEldritchCapRenderer() {
      this.model = AdvancedModelLoader.loadModel(CAP);
   }

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      String tempTex = this.tex;
      GL11.glPushMatrix();
      Block blockType = getBlockTypeSafely(te);
      if (blockType != null) {
         int j = blockType.getMixedBrightnessForBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         if (te.getWorldObj().provider.dimensionId == Config.dimensionOuterId) {
            tempTex = this.tex2;
         }
      }

      GL11.glPushMatrix();
      UtilsFX.bindTexture(tempTex);
      GL11.glTranslated(x + (double)0.5F, y, z + (double)0.5F);
      GL11.glRotated(90.0F, -1.0F, 0.0F, 0.0F);
      this.model.renderPart("Cap");
      GL11.glPopMatrix();
      if (te.getWorldObj() != null && te instanceof TileEldritchAltar && ((TileEldritchAltar)te).getEyes() > 0) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)x + 0.5F, (float)y + 0.0F, (float)z + 0.5F);
         if (this.entityitem == null || this.eye == null) {
            this.eye = new ItemStack(ConfigItems.itemEldritchObject, 1, 0);
            this.entityitem = new EntityItem(te.getWorldObj(), 0.0F, 0.0F, 0.0F, this.eye);
            this.entityitem.hoverStart = 0.0F;
         }

         if (this.eye != null) {
            for(int a = 0; a < ((TileEldritchAltar)te).getEyes(); ++a) {
               GL11.glPushMatrix();
               GL11.glRotated(a * 90, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(0.46F, 0.2F, 0.0F);
               GL11.glRotated(90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotated(18.0F, -1.0F, 0.0F, 0.0F);
               RenderItem.renderInFrame = true;
               RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
               RenderItem.renderInFrame = false;
               GL11.glPopMatrix();
            }
         }

         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
   }
}
