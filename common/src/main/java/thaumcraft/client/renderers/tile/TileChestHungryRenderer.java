package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileChestHungry;

import static thaumcraft.client.renderers.tile.TileBlockInfoGetter.getBlockMetaSafely;

@SideOnly(Side.CLIENT)
public class TileChestHungryRenderer extends TileEntitySpecialRenderer {
   private static final ModelChest chestModel = new ModelChest();

   public void renderTileEntityChestAt(TileChestHungry chest, double par2, double par4, double par6, float par8) {
      int direction = getBlockMetaSafely(chest);
      if (direction == -1){
         direction = 0;
      }


      ModelChest var14 = chestModel;
      UtilsFX.bindTexture("textures/models/chesthungry.png");
      GL11.glPushMatrix();
      GL11.glEnable(32826);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
      GL11.glScalef(1.0F, -1.0F, -1.0F);
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      short rotateDegree = 0;
      if (direction == 4) {
         rotateDegree = 90;
      }
      if (direction == 5) {
         rotateDegree = -90;
      }
      if (direction == 2) {
         rotateDegree = 180;
      }

      GL11.glRotatef(rotateDegree, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      float var12 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * par8;
      var12 = 1.0F - var12;
      var12 = 1.0F - var12 * var12 * var12;
      var14.chestLid.rotateAngleX = -(var12 * (float)Math.PI / 2.0F);
      var14.renderAll();
      GL11.glDisable(32826);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (!(tileEntity instanceof TileChestHungry)){
         System.out.println("TileChestHungryRenderer.renderTileEntityAt:" + tileEntity.getClass().getName());
         return;
      }
      this.renderTileEntityChestAt((TileChestHungry)tileEntity, par2, par4, par6, par8);
   }
}
