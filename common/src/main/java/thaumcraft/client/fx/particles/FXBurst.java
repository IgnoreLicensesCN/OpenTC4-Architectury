package thaumcraft.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

public class FXBurst extends EntityFX {
   public FXBurst(World world, double d, double d1, double d2, float f) {
      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.particleGravity = 0.0F;
      this.motionX = this.motionY = this.motionZ = 0.0F;
      this.particleScale *= f;
      this.particleMaxAge = 31;
      this.noClip = false;
      this.setSize(0.01F, 0.01F);
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      tessellator.draw();
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      UtilsFX.bindTexture(TileNodeRenderer.nodetex);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      float var8 = (float)(this.particleAge % 32) / 32.0F;
      float var9 = var8 + 0.03125F;
      float var10 = 0.96875F;
      float var11 = 1.0F;
      float var12 = this.particleScale;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 1.0F);
      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
      tessellator.startDrawingQuads();
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

   }

   public void setGravity(float value) {
      this.particleGravity = value;
   }
}
