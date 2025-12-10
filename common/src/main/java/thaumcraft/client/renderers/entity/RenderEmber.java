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
import thaumcraft.common.entities.projectile.EntityEmber;

public class RenderEmber extends Render {
   private Random random = new Random();

   public RenderEmber() {
      this.shadowSize = 0.0F;
   }

   public void renderEntityAt(EntityEmber entity, double x, double y, double z, float fq, float pticks) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      UtilsFX.bindTexture(ParticleEngine.particleTexture);
      int p = (int)(8.0F * ((float)entity.ticksExisted / (float)entity.duration));
      float f2 = (float)(7 + p) / 16.0F;
      float f3 = f2 + 0.0625F;
      float f4 = 0.5625F;
      float f5 = f4 + 0.0625F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      float fc = (float)entity.ticksExisted / (float)entity.duration;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
      float particleScale = 0.25F + fc;
      GL11.glScalef(particleScale, particleScale, particleScale);
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.setBrightness(220);
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 0.9F);
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
      this.renderEntityAt((EntityEmber)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
