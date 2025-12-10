package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class FXGeneric extends EntityFX {
   boolean loop = false;
   int delay = 0;
   int startParticle = 0;
   int numParticles = 1;
   int particleInc = 1;

   public FXGeneric(World world, double x, double y, double z, double xx, double yy, double zz) {
      super(world, x, y, z, xx, yy, zz);
      this.setSize(0.1F, 0.1F);
      this.noClip = false;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionX = xx;
      this.motionY = yy;
      this.motionZ = zz;
      this.noClip = true;
   }

   public void setLoop(boolean loop) {
      this.loop = loop;
   }

   public void setScale(float scale) {
      this.particleScale = scale;
   }

   public void setMaxAge(int max, int delay) {
      this.particleMaxAge = max;
      this.particleMaxAge += delay;
      this.delay = delay;
   }

   public void setParticles(int startParticle, int numParticles, int particleInc) {
      this.startParticle = startParticle;
      this.numParticles = numParticles;
      this.particleInc = particleInc;
      this.setParticleTextureIndex(startParticle);
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.loop) {
         this.setParticleTextureIndex(this.startParticle + this.particleAge / this.particleInc % this.numParticles);
      } else {
         float fs = (float)this.particleAge / (float)this.particleMaxAge;
         this.setParticleTextureIndex((int)((float)this.startParticle + Math.min((float)this.numParticles * fs, (float)(this.numParticles - 1))));
      }

   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      if (this.particleAge >= this.delay) {
         float t = this.particleAlpha;
         if (this.particleAge <= 1 || this.particleAge >= this.particleMaxAge - 1) {
            this.particleAlpha = t / 2.0F;
         }

         super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
         this.particleAlpha = t;
      }
   }
}
