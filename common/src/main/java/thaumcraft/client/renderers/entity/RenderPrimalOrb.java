package thaumcraft.client.renderers.entity;

import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.blocks.BlockCustomOreItem;

public class RenderPrimalOrb extends Render {
   public RenderPrimalOrb() {
      this.shadowSize = 0.0F;
   }

   public void renderEntityAt(Entity entity, double x, double y, double z, float fq, float pticks) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glPushMatrix();
      RenderHelper.disableStandardItemLighting();
      float f1 = (float)entity.ticksExisted / 80.0F;
      float f3 = 0.9F;
      float f2 = 0.0F;
      Random random = new Random(entity.getEntityId());
      GL11.glTranslatef((float)x, (float)y, (float)z);
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();

      for(int i = 0; i < 12; ++i) {
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
         tessellator.startDrawing(6);
         float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
         float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
         fa /= 30.0F / ((float)Math.min(entity.ticksExisted, 10) / 10.0F);
         f4 /= 30.0F / ((float)Math.min(entity.ticksExisted, 10) / 10.0F);
         tessellator.setColorRGBA_I(16777215, (int)(255.0F * (1.0F - f2)));
         tessellator.addVertex(0.0F, 0.0F, 0.0F);
         tessellator.setColorRGBA_I(BlockCustomOreItem.colors[i / 2 + 1], 0);
         tessellator.addVertex(-0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.addVertex(0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.addVertex(0.0F, fa, f4);
         tessellator.addVertex(-0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.draw();
      }

      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.enableStandardItemLighting();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      UtilsFX.bindTexture(ParticleEngine.particleTexture);
      f2 = (float)(entity.ticksExisted % 13) / 16.0F;
      f3 = f2 + 0.0624375F;
      float f4 = 0.125F;
      float f5 = f4 + 0.0624375F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0F, f2, f5);
      tessellator.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0F, f3, f5);
      tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0F, f3, f4);
      tessellator.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0F, f2, f4);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt(entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
