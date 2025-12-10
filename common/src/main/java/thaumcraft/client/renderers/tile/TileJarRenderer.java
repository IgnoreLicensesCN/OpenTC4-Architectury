package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelBrain;
import thaumcraft.client.renderers.models.ModelJar;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileJar;
import thaumcraft.common.tiles.TileJarBrain;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileJarNode;

@SideOnly(Side.CLIENT)
public class TileJarRenderer extends TileEntitySpecialRenderer {
   private ModelJar model = new ModelJar();
   private ModelBrain brain = new ModelBrain();
   private TileNodeRenderer tnr = new TileNodeRenderer();

   public void renderTileEntityAt(TileJar tile, double x, double y, double z, float f) {
      if (tile instanceof TileJarNode) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, -0.1F, 0.0F);
         this.tnr.renderTileEntityAt(tile, x, y, z, f);
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      GL11.glDisable(2884);
      GL11.glTranslatef((float)x + 0.5F, (float)y + 0.01F, (float)z + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (tile instanceof TileJarBrain) {
         this.renderBrain((TileJarBrain)tile, x, y, z, f);
      } else if (tile instanceof TileJarFillable) {
         if (((TileJarFillable)tile).amount > 0) {
            this.renderLiquid((TileJarFillable)tile, x, y, z, f);
         }

         if (((TileJarFillable)tile).aspectFilter != null) {
            GL11.glPushMatrix();
            switch (((TileJarFillable)tile).facing) {
               case 3:
                  GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                  break;
               case 4:
                  GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                  break;
               case 5:
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            float rot = (float)((((TileJarFillable)tile).aspectFilter.getTag().hashCode() + tile.xCoord + ((TileJarFillable)tile).facing) % 4 - 2);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -0.4F, 0.315F);
            if (Config.crooked) {
               GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
            }

            UtilsFX.renderQuadCenteredFromTexture("textures/models/label.png", 0.5F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -0.4F, 0.316F);
            if (Config.crooked) {
               GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
            }

            GL11.glScaled(0.021, 0.021, 0.021);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            UtilsFX.drawTag(-8, -8, ((TileJarFillable)tile).aspectFilter);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
         }
      }

      this.bindTexture(tile.getTexture());
      if (tile instanceof TileJarNode && ((TileJarNode)tile).animate > 0L) {
         long time = System.currentTimeMillis();
         if (((TileJarNode)tile).animate > time) {
            float size = 1.0F + 2.0F * ((float)(((TileJarNode)tile).animate - time) / 1000.0F);
            GL11.glScalef(size, size, size);
         } else {
            ((TileJarNode)tile).animate = 0L;
         }
      }

      GL11.glEnable(2884);
      GL11.glPopMatrix();
   }

   public void renderLiquid(TileJarFillable te, double x, double y, double z, float f) {
      if (this.field_147501_a.field_147553_e != null) {
         GL11.glPushMatrix();
         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         World world = te.getWorldObj();
         RenderBlocks renderBlocks = new RenderBlocks();
         GL11.glDisable(2896);
         float level = (float)te.amount / (float)te.maxAmount * 0.625F;
         Tessellator t = Tessellator.instance;
         renderBlocks.setRenderBounds(0.25F, 0.0625F, 0.25F, 0.75F, (double)0.0625F + (double)level, 0.75F);
         t.startDrawingQuads();
         if (te.aspect != null) {
            t.setColorOpaque_I(te.aspect.getColor());
         }

         int bright = 200;
         if (te.getWorldObj() != null) {
            bright = Math.max(200, ConfigBlocks.blockJar.getMixedBrightnessForBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord));
         }

         t.setBrightness(bright);
         IIcon icon = ((BlockJar)ConfigBlocks.blockJar).iconLiquid;
         this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
         renderBlocks.renderFaceYNeg(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, icon);
         renderBlocks.renderFaceYPos(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, icon);
         renderBlocks.renderFaceZNeg(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, icon);
         renderBlocks.renderFaceZPos(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, icon);
         renderBlocks.renderFaceXNeg(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, icon);
         renderBlocks.renderFaceXPos(ConfigBlocks.blockJar, -0.5F, 0.0F, -0.5F, icon);
         t.draw();
         GL11.glEnable(2896);
         GL11.glPopMatrix();
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }
   }

   public void renderBrain(TileJarBrain te, double x, double y, double z, float f) {
      float bob = MathHelper.sin((float)Minecraft.getMinecraft().thePlayer.ticksExisted / 14.0F) * 0.03F + 0.03F;
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, -0.8F + bob, 0.0F);

      float f2;
      for(f2 = te.rota - te.rotb; f2 >= 3.141593F; f2 -= 6.283185F) {
      }

      while(f2 < -3.141593F) {
         f2 += 6.283185F;
      }

      float f3 = te.rotb + f2 * f;
      GL11.glRotatef(f3 * 180.0F / 3.141593F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      UtilsFX.bindTexture("textures/models/brain2.png");
      GL11.glScalef(0.4F, 0.4F, 0.4F);
      this.brain.render();
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      UtilsFX.bindTexture("textures/models/jarbrine.png");
      this.model.renderBrine();
   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileJar)tileEntity, par2, par4, par6, par8);
   }
}
