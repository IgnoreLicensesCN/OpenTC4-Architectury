package thaumcraft.client.renderers.entity;

import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

public class RenderExplosiveOrb extends Render {
   private Random random = new Random();

   public RenderExplosiveOrb() {
      this.shadowSize = 0.0F;
   }

   public void renderEntityAt(Entity entity, double x, double y, double z, float fq, float pticks) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      UtilsFX.bindTexture(ParticleEngine.particleTexture2);
      float f2 = (float)(entity.ticksExisted % 4) / 16.0F;
      float f3 = f2 + 0.0625F;
      float f4 = 0.8125F;
      float f5 = f4 + 0.0625F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(0.7F, 0.7F, 0.7F);
      tessellator.startDrawingQuads();
      tessellator.setBrightness(220);
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(-f7, -f8, 0.0F, f2, f5);
      tessellator.addVertexWithUV(f6 - f7, -f8, 0.0F, f3, f5);
      tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0F, f3, f4);
      tessellator.addVertexWithUV(-f7, 1.0F - f8, 0.0F, f2, f4);
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
