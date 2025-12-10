package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelTaintSpore extends ModelBase {
   ModelRenderer cube;

   public ModelTaintSpore() {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.cube = new ModelRenderer(this, 0, 0);
      this.cube.addBox(-6.0F, 2.0F, -6.0F, 12, 12, 12);
      this.cube.addBox(-8.0F, 0.0F, -8.0F, 16, 16, 16);
      this.cube.setRotationPoint(0.0F, 24.0F, 0.0F);
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      this.cube.render(par7);
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
