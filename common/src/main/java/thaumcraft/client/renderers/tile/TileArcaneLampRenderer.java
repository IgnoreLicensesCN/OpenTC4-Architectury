package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBoreBase;
import thaumcraft.common.tiles.TileArcaneBoreBase;
import thaumcraft.common.tiles.TileArcaneLamp;
import thaumcraft.common.tiles.TileArcaneLampFertility;
import thaumcraft.common.tiles.TileArcaneLampGrowth;

public class TileArcaneLampRenderer extends TileEntitySpecialRenderer {
   private ModelBoreBase model = new ModelBoreBase();

   public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
      if (tileentity == null){return;}
      Minecraft mc = FMLClientHandler.instance().getClient();
      if (tileentity.getWorldObj() != null) {
         ForgeDirection dir = ForgeDirection.DOWN;
         if (tileentity instanceof TileArcaneLamp) {
            dir = ((TileArcaneLamp)tileentity).facing;
         } else if (tileentity instanceof TileArcaneLampGrowth) {
            dir = ((TileArcaneLampGrowth)tileentity).facing;
         } else if (tileentity instanceof TileArcaneLampFertility) {
            dir = ((TileArcaneLampFertility)tileentity).facing;
         }

         GL11.glPushMatrix();
         UtilsFX.bindTexture("textures/models/Bore.png");
         if (tileentity.getWorldObj().getTileEntity(tileentity.xCoord + dir.offsetX, tileentity.yCoord + dir.offsetY, tileentity.zCoord + dir.offsetZ) instanceof TileArcaneBoreBase) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.5F + (float)dir.offsetX, (float)y + (float)dir.offsetY, (float)z + 0.5F + (float)dir.offsetZ);
            switch (dir.getOpposite().ordinal()) {
               case 0:
                  GL11.glTranslatef(-0.5F, 0.5F, 0.0F);
                  GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
                  break;
               case 1:
                  GL11.glTranslatef(0.5F, 0.5F, 0.0F);
                  GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                  break;
               case 2:
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                  break;
               case 3:
                  GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                  break;
               case 4:
                  GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                  break;
               case 5:
                  GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
            }

            this.model.renderNozzle();
            GL11.glPopMatrix();
         }

         GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
         GL11.glPushMatrix();
         switch (dir.ordinal()) {
            case 0:
               GL11.glTranslatef(-0.5F, 0.5F, 0.0F);
               GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
               break;
            case 1:
               GL11.glTranslatef(0.5F, 0.5F, 0.0F);
               GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
               break;
            case 2:
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 3:
               GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 4:
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 5:
               GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
         }

         this.model.renderNozzle();
         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }

   }
}
