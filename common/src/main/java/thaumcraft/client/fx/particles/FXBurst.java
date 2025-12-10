package thaumcraft.client.fx.particles;

@Deprecated(forRemoval = true,since = "see particles.migrated")
public class FXBurst /*extends EntityFX*/ {
//   public FXBurst(World world, double d, double d1, double d2, float f) {
//      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
//      this.particleRed = 1.0F;
//      this.particleGreen = 1.0F;
//      this.particleBlue = 1.0F;
//      this.particleGravity = 0.0F;
//      this.motionX = this.motionY = this.motionZ = 0.0F;
//      this.particleScale *= f;
//      this.particleMaxAge = 31;
//      this.noClip = false;
//      this.setSize(0.01F, 0.01F);
//   }
//
//   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
//      tessellator.draw();
//      GL11.glPushMatrix();
//      GL11.glDepthMask(false);
//      GL11.glEnable(GL11.GL_BLEND);
//      GL11.glBlendFunc(770, 1);
//      UtilsFX.bindTexture(TileNodeRenderer.nodetex);
//      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
//      float u0 = (float)(this.particleAge % 32) / 32.0F;
//      float u1 = u0 + 0.03125F;
//      float v0 = 0.96875F;
//      float v1 = 1.0F;
//
//      float scale = this.particleScale;
//      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
//      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
//      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
//
//      float var16 = 1.0F;
//      tessellator.startDrawingQuads();
//      tessellator.setBrightness(240);
//      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 1.0F);
//
//      //   //Right = ( f1,  0, f3 )
////   //Up    = ( f4, f2, f5 )
//      tessellator.addVertexWithUV(
//              var13 - f1 * scale - f4 * scale,
//              var14 - f2 * scale,
//              var15 - f3 * scale - f5 * scale,
//              u1,
//              v1
//      );
//      tessellator.addVertexWithUV(
//              var13 - f1 * scale + f4 * scale,
//              var14 + f2 * scale,
//              var15 - f3 * scale + f5 * scale,
//              u1,
//              v0);
//      tessellator.addVertexWithUV(var13 + f1 * scale + f4 * scale, var14 + f2 * scale, var15 + f3 * scale + f5 * scale, u0, v0);
//      tessellator.addVertexWithUV(var13 + f1 * scale - f4 * scale, var14 - f2 * scale, var15 + f3 * scale - f5 * scale, u0, v1);
//      tessellator.draw();
//      GL11.glDisable(GL11.GL_BLEND);
//      GL11.glDepthMask(true);
//      GL11.glPopMatrix();
//      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
//      tessellator.startDrawingQuads();
//   }
//
//   public void onUpdate() {
//      this.prevPosX = this.posX;
//      this.prevPosY = this.posY;
//      this.prevPosZ = this.posZ;
//      if (this.particleAge++ >= this.particleMaxAge) {
//         this.setDead();
//      }
//
//   }
//
//   public void setGravity(float value) {
//      this.particleGravity = value;
//   }
}
