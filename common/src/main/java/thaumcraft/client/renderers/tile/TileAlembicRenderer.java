package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBoreBase;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileTube;

@SideOnly(Side.CLIENT)
public class TileAlembicRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation ALEMBIC = new ResourceLocation("thaumcraft", "textures/models/alembic.obj");
   private ModelBoreBase modelBore;

   public TileAlembicRenderer() {
      this.model = AdvancedModelLoader.loadModel(ALEMBIC);
      this.modelBore = new ModelBoreBase();
   }

   public void renderTileEntityAt(TileAlembic tile, double par2, double par4, double par6, float par8) {
      if (tile == null){return;}
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
      GL11.glPushMatrix();
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/models/alembic.png");
      int md = 0;
      if (tile.hasWorldObj()) {
         switch (tile.facing) {
            case 2:
               GL11.glRotatef(270.0F, 0.0F, 0.0F, 1.0F);
               break;
            case 3:
               GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            case 4:
            default:
               break;
            case 5:
               GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         }

         if (tile.aboveFurnace) {
            this.model.renderPart("TubeMain");
            this.model.renderPart("Legs");
         } else if (tile.aboveAlembic) {
            this.model.renderPart("TubeMain");
            this.model.renderPart("TubeSmall");
         } else {
            this.model.renderPart("Legs");
         }
      } else {
         GL11.glTranslatef(0.0F, 0.0F, -0.4F);
         this.model.renderPart("Legs");
      }

      this.model.renderPart("Pot");
      this.model.renderPart("Panel");
      GL11.glPopMatrix();
      if (tile.aspectFilter != null) {
         GL11.glPushMatrix();
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         switch (tile.facing) {
            case 2:
               GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 3:
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            case 4:
            default:
               break;
            case 5:
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.468F, -0.409F);
         UtilsFX.renderQuadCenteredFromTexture("textures/models/label.png", 0.27F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.468F, -0.41F);
         GL11.glScaled(0.013, 0.013, 0.013);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         UtilsFX.drawTag(-8, -8, tile.aspectFilter);
         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      if (tile.getWorldObj() != null) {
         UtilsFX.bindTexture("textures/models/Bore.png");

         for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (tile.canOutputTo(dir)) {
               TileEntity te = ThaumcraftApiHelper.getConnectableTile(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, dir);
               if (te instanceof IEssentiaTransport && !(te instanceof TileTube)) {
                  GL11.glPushMatrix();
                  GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
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

                  this.modelBore.renderNozzle();
                  GL11.glPopMatrix();
               }
            }
         }
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (! (tileEntity instanceof TileAlembic)) {
         return;
      }
      this.renderTileEntityAt((TileAlembic)tileEntity, par2, par4, par6, par8);
   }
}
