package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXWispArcing extends EntityFX {
   private double field_70568_aq;
   private double field_70567_ar;
   private double field_70566_as;
   float moteParticleScale;
   int moteHalfLife;
   public boolean tinkle;
   public int blendmode;

   public FXWispArcing(World world, double d, double d1, double d2, float f, float red, float green, float blue) {
      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
      this.tinkle = false;
      this.blendmode = 1;
      if (red == 0.0F) {
         red = 1.0F;
      }

      this.field_70568_aq = this.posX = d;
      this.field_70567_ar = this.posY = d1;
      this.field_70566_as = this.posZ = d2;
      this.particleRed = red;
      this.particleGreen = green;
      this.particleBlue = blue;
      this.particleGravity = 0.0F;
      this.particleScale *= f;
      this.moteParticleScale = this.particleScale;
      this.particleMaxAge = (int)((double)36.0F / (Math.random() * 0.3 + 0.7));
      this.moteHalfLife = this.particleMaxAge / 2;
      this.noClip = false;
      this.setSize(0.01F, 0.01F);
      EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
      int visibleDistance = 50;
      if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
         visibleDistance = 25;
      }

      if (renderentity.getDistance(this.posX, this.posY, this.posZ) > (double)visibleDistance) {
         this.particleMaxAge = 0;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
   }

   public FXWispArcing(World world, double d, double d1, double d2, double x, double y, double z, float f, float red, float green, float blue) {
      this(world, d, d1, d2, f, red, green, blue);
      this.motionX = x - d;
      this.motionY = y - d1;
      this.motionZ = z - d2;
      this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      float agescale = 0.0F;
      agescale = (float)this.particleAge / (float)this.moteHalfLife;
      if (agescale > 1.0F) {
         agescale = 2.0F - agescale;
      }

      this.particleScale = this.moteParticleScale * agescale;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      float f10 = 0.5F * this.particleScale;
      float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var8 = 0.0F;
      float var9 = 0.0F;
      float var10 = 0.875F;
      float var11 = 1.0F;
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 0.5F);
      tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, var9, var10);
      tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, var9, var11);
      tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, var8, var10);
      tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, var8, var11);
   }

   public int getFXLayer() {
      return this.blendmode == 1 ? 0 : 1;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      float var1 = (float)this.particleAge / (float)this.particleMaxAge;
      float var2 = (float)this.particleAge / ((float)this.particleMaxAge / 2.0F);
      var1 = 1.0F - var1;
      var2 = 1.0F - var2;
      var2 *= var2;
      this.posX = this.field_70568_aq + this.motionX * (double)var1;
      this.posY = this.field_70567_ar + this.motionY * (double)var1 - (double)var2 + (double)1.0F;
      this.posZ = this.field_70566_as + this.motionZ * (double)var1;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

   }

   public void setGravity(float value) {
      this.particleGravity = value;
   }
}
