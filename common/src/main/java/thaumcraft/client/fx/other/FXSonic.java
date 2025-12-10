package thaumcraft.client.fx.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

public class FXSonic extends EntityFX {
   Entity target = null;
   float yaw = 0.0F;
   float pitch = 0.0F;
   public static IModelCustom model = null;
   public static final ResourceLocation MODEL = new ResourceLocation("thaumcraft", "textures/models/hemis.obj");

   public FXSonic(World world, double d, double d1, double d2, Entity target, int age) {
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
      this.yaw = target.getRotationYawHead();
      this.pitch = target.rotationPitch;
      this.prevPosX = this.posX = target.posX;
      this.prevPosY = this.posY = target.posY + (double)target.getEyeHeight();
      this.prevPosZ = this.posZ = target.posZ;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      tessellator.draw();
      GL11.glPushMatrix();
      GL11.glDisable(2884);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      if (model == null){
         model = AdvancedModelLoader.loadModel(MODEL);
      }

      float fade = ((float)this.particleAge + f) / (float)this.particleMaxAge;
      float xx = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float yy = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float zz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      GL11.glTranslated(xx, yy, zz);
      float b = 1.0F;
      int frame = Math.min(15, (int)(14.0F * fade) + 1);
      UtilsFX.bindTexture("textures/models/ripple" + frame + ".png");
      b = 0.5F;
      int i = 220;
      int j = i % 65536;
      int k = i / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
      GL11.glRotatef(-this.yaw, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.pitch, 1.0F, 0.0F, 0.0F);
      GL11.glTranslated(0.0F, 0.0F, 2.0F * this.target.height + this.target.width / 2.0F);
      GL11.glScaled((double)0.25F * (double)this.target.height, (double)0.25F * (double)this.target.height, -1.0F * this.target.height);
      GL11.glColor4f(b, b, b, 1.0F);
      model.renderAll();
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
      this.posY = this.target.posY + (double)this.target.getEyeHeight();
      this.posZ = this.target.posZ;
   }
}
