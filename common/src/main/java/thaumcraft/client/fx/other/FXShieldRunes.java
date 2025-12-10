package thaumcraft.client.fx.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.monster.EntityCultist;

public class FXShieldRunes extends EntityFX {
   Entity target = null;
   float yaw = 0.0F;
   float pitch = 0.0F;
   private IModelCustom model;
   private static final ResourceLocation MODEL = new ResourceLocation("thaumcraft", "textures/models/hemis.obj");

   public FXShieldRunes(World world, double d, double d1, double d2, Entity target, int age, float yaw, float pitch) {
      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.particleGravity = 0.0F;
      this.motionX = this.motionY = this.motionZ = 0.0F;
      this.particleMaxAge = age + this.rand.nextInt(age / 2);
      this.noClip = false;
      this.setSize(0.01F, 0.01F);
      this.noClip = true;
      this.particleScale = 1.0F;
      this.target = target;
      this.yaw = yaw;
      this.pitch = pitch;
      this.prevPosX = this.posX = target.posX;
      this.prevPosY = this.posY = (target.boundingBox.minY + target.boundingBox.maxY) / (double)2.0F;
      this.prevPosZ = this.posZ = target.posZ;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      tessellator.draw();
      GL11.glPushMatrix();
      GL11.glDisable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      if (this.model == null) {
         this.model = AdvancedModelLoader.loadModel(MODEL);
      }

      float fade = ((float)this.particleAge + f) / (float)this.particleMaxAge;
      float xx = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float yy = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float zz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      GL11.glTranslated(xx, yy, zz);
      float b = 1.0F;
      int frame = Math.min(15, (int)(14.0F * fade) + 1);
      if (this.target instanceof EntityMob && !(this.target instanceof EntityCultist)) {
         UtilsFX.bindTexture("textures/models/ripple" + frame + ".png");
         b = 0.5F;
      } else {
         UtilsFX.bindTexture("textures/models/hemis" + frame + ".png");
      }

      int i = 220;
      int j = i % 65536;
      int k = i / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
      GL11.glRotatef(180.0F - this.yaw, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.pitch, 1.0F, 0.0F, 0.0F);
      GL11.glScaled(0.4 * (double)this.target.height, 0.4 * (double)this.target.height, 0.4 * (double)this.target.height);
      GL11.glColor4f(b, b, b, Math.min(1.0F, (1.0F - fade) * 3.0F));
      this.model.renderAll();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(2884);
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

      this.posX = this.target.posX;
      this.posY = (this.target.boundingBox.minY + this.target.boundingBox.maxY) / (double)2.0F;
      this.posZ = this.target.posZ;
   }
}
