package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;

public class ModelTaintSporeSwarmer extends ModelBase {
   ModelRenderer cube;
   ModelRenderer cube2;

   public ModelTaintSporeSwarmer() {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.cube = new ModelRenderer(this, 0, 0);
      this.cube.addBox(-8.0F, 0.0F, -8.0F, 16, 16, 16);
      this.cube.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.cube2 = new ModelRenderer(this, 0, 32);
      this.cube2.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
      this.cube2.setRotationPoint(0.0F, 16.0F, 0.0F);
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
      EntityTaintSporeSwarmer spore = (EntityTaintSporeSwarmer)par1Entity;
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      GL11.glPushMatrix();
      float f1 = spore.displaySize;
      float f3 = -0.07F;
      float pulse = 0.025F * MathHelper.sin((float)spore.ticksExisted * 0.075F);
      GL11.glTranslatef(0.0F, 1.6F, 0.0F);
      GL11.glScalef(f3 * f1 - pulse, f3 * f1 + pulse, f3 * f1 - pulse);
      GL11.glTranslatef(0.0F, -(f3 * f1 + pulse) / 2.0F, 0.0F);
      int j = 15728880;
      int k = j % 65536;
      int l = j / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
      this.cube.render(par7);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      j = spore.getBrightnessForRender(par7);
      k = j % 65536;
      l = j / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
      this.cube2.render(par7);
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      float intensity = 0.02F;
      if (((EntityLivingBase)entity).hurtTime > 0) {
         intensity = 0.04F;
      }

      this.cube.rotateAngleX = intensity * MathHelper.sin(par3 * 0.05F);
      this.cube.rotateAngleZ = intensity * MathHelper.sin(par3 * 0.1F);
   }
}
