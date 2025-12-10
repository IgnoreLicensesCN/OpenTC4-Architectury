package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.EntityAspectOrb;

@SideOnly(Side.CLIENT)
public class RenderAspectOrb extends Render {
   public RenderAspectOrb() {
      this.shadowSize = 0.1F;
      this.shadowOpaque = 0.5F;
   }

   public void renderAspectOrb(EntityAspectOrb orb, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      GL11.glEnable(GL11.GL_BLEND);
      if (orb.getAspect() != null) {
         GL11.glBlendFunc(770, orb.getAspect().getBlend());
      } else {
         GL11.glBlendFunc(770, 1);
      }

      UtilsFX.bindTexture(ParticleEngine.particleTexture);
      int i = (int)(System.nanoTime() / 25000000L % 16L);
      Tessellator tessellator = Tessellator.instance;
      float f2 = (float)i / 16.0F;
      float f3 = (float)(i + 1) / 16.0F;
      float f4 = 0.5F;
      float f5 = 0.5625F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.25F;
      int j = orb.getBrightnessForRender(par9);
      int k = j % 65536;
      int l = j / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      float f11 = 0.1F + 0.3F * ((float)(orb.orbMaxAge - orb.orbAge) / (float)orb.orbMaxAge);
      GL11.glScalef(f11, f11, f11);
      tessellator.startDrawingQuads();
      if (orb.getAspect() != null) {
         tessellator.setColorRGBA_I(orb.getAspect().getColor(), 128);
      }

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

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderAspectOrb((EntityAspectOrb)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
