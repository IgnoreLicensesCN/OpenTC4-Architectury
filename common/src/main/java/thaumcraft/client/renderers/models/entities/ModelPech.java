package thaumcraft.client.renderers.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thaumcraft.common.entities.monster.EntityPech;

public class ModelPech extends ModelBase {
   ModelRenderer Body;
   ModelRenderer RightLeg;
   ModelRenderer LeftLeg;
   ModelRenderer Head;
   ModelRenderer Jowls;
   ModelRenderer LowerPack;
   ModelRenderer UpperPack;
   public ModelRenderer RightArm;
   ModelRenderer LeftArm;

   public ModelPech() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Body = new ModelRenderer(this, 34, 12);
      this.Body.addBox(-3.0F, 0.0F, 0.0F, 6, 10, 6);
      this.Body.setRotationPoint(0.0F, 9.0F, -3.0F);
      this.Body.setTextureSize(128, 64);
      this.Body.mirror = true;
      this.setRotation(this.Body, 0.3129957F, 0.0F, 0.0F);
      this.RightLeg = new ModelRenderer(this, 35, 1);
      this.RightLeg.mirror = true;
      this.RightLeg.addBox(-2.9F, 0.0F, 0.0F, 3, 6, 3);
      this.RightLeg.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.RightLeg.setTextureSize(128, 64);
      this.RightLeg.mirror = true;
      this.setRotation(this.RightLeg, 0.0F, 0.0F, 0.0F);
      this.RightLeg.mirror = false;
      this.LeftLeg = new ModelRenderer(this, 35, 1);
      this.LeftLeg.addBox(-0.1F, 0.0F, 0.0F, 3, 6, 3);
      this.LeftLeg.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.LeftLeg.setTextureSize(128, 64);
      this.LeftLeg.mirror = true;
      this.setRotation(this.LeftLeg, 0.0F, 0.0F, 0.0F);
      this.Head = new ModelRenderer(this, 2, 11);
      this.Head.addBox(-3.5F, -5.0F, -5.0F, 7, 5, 5);
      this.Head.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.Head.setTextureSize(128, 64);
      this.Head.mirror = true;
      this.setRotation(this.Head, 0.0F, 0.0F, 0.0F);
      this.Jowls = new ModelRenderer(this, 1, 21);
      this.Jowls.addBox(-4.0F, -1.0F, -6.0F, 8, 3, 5);
      this.Jowls.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.Jowls.setTextureSize(128, 64);
      this.Jowls.mirror = true;
      this.setRotation(this.Jowls, 0.0F, 0.0F, 0.0F);
      this.LowerPack = new ModelRenderer(this, 0, 0);
      this.LowerPack.addBox(-5.0F, 0.0F, 0.0F, 10, 5, 5);
      this.LowerPack.setRotationPoint(0.0F, 10.0F, 3.5F);
      this.LowerPack.setTextureSize(128, 64);
      this.LowerPack.mirror = true;
      this.setRotation(this.LowerPack, 0.3013602F, 0.0F, 0.0F);
      this.UpperPack = new ModelRenderer(this, 64, 1);
      this.UpperPack.addBox(-7.5F, -14.0F, 0.0F, 15, 14, 11);
      this.UpperPack.setRotationPoint(0.0F, 10.0F, 3.0F);
      this.UpperPack.setTextureSize(128, 64);
      this.UpperPack.mirror = true;
      this.setRotation(this.UpperPack, 0.4537856F, 0.0F, 0.0F);
      this.RightArm = new ModelRenderer(this, 52, 2);
      this.RightArm.mirror = true;
      this.RightArm.addBox(-2.0F, 0.0F, -1.0F, 2, 6, 2);
      this.RightArm.setRotationPoint(-3.0F, 10.0F, -1.0F);
      this.RightArm.setTextureSize(128, 64);
      this.RightArm.mirror = true;
      this.setRotation(this.RightArm, 0.0F, 0.0F, 0.0F);
      this.RightArm.mirror = false;
      this.LeftArm = new ModelRenderer(this, 52, 2);
      this.LeftArm.addBox(0.0F, 0.0F, -1.0F, 2, 6, 2);
      this.LeftArm.setRotationPoint(3.0F, 10.0F, -1.0F);
      this.LeftArm.setTextureSize(128, 64);
      this.LeftArm.mirror = true;
      this.setRotation(this.LeftArm, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
      this.Body.render(par7);
      this.RightLeg.render(par7);
      this.LeftLeg.render(par7);
      this.Head.render(par7);
      this.Jowls.render(par7);
      this.LowerPack.render(par7);
      this.UpperPack.render(par7);
      this.RightArm.render(par7);
      this.LeftArm.render(par7);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.Head.rotateAngleY = par4 / (180F / (float)Math.PI);
      this.Head.rotateAngleX = par5 / (180F / (float)Math.PI);
      float mumble = 0.0F;
      if (entity instanceof EntityPech) {
         mumble = ((EntityPech)entity).mumble;
      }

      this.Jowls.rotateAngleY = this.Head.rotateAngleY;
      this.Jowls.rotateAngleX = this.Head.rotateAngleX + 0.2617994F + MathHelper.cos(par1 * 0.6662F) * par2 * 0.25F + 0.34906587F * Math.abs(MathHelper.sin(mumble));
      this.RightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
      this.LeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.RightArm.rotateAngleZ = 0.0F;
      this.LeftArm.rotateAngleZ = 0.0F;
      this.RightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
      this.LeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
      this.RightLeg.rotateAngleY = 0.0F;
      this.LeftLeg.rotateAngleY = 0.0F;
      this.LowerPack.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.125F;
      this.LowerPack.rotateAngleZ = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.125F;
      if (this.isRiding) {
         ModelRenderer var10000 = this.RightArm;
         var10000.rotateAngleX += (-(float)Math.PI / 5F);
         var10000 = this.LeftArm;
         var10000.rotateAngleX += (-(float)Math.PI / 5F);
         this.RightLeg.rotateAngleX = -1.2566371F;
         this.LeftLeg.rotateAngleX = -1.2566371F;
         this.RightLeg.rotateAngleY = ((float)Math.PI / 10F);
         this.LeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
      }

      this.RightArm.rotateAngleY = 0.0F;
      this.LeftArm.rotateAngleY = 0.0F;
      if (this.onGround > -9990.0F) {
         float f6 = this.onGround;
         ModelRenderer var17 = this.RightArm;
         var17.rotateAngleY += this.Body.rotateAngleY;
         var17 = this.LeftArm;
         var17.rotateAngleY += this.Body.rotateAngleY;
         var17 = this.LeftArm;
         var17.rotateAngleX += this.Body.rotateAngleY;
         f6 = 1.0F - this.onGround;
         f6 *= f6;
         f6 *= f6;
         f6 = 1.0F - f6;
         float f7 = MathHelper.sin(f6 * (float)Math.PI);
         float f8 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.Head.rotateAngleX - 0.7F) * 0.75F;
         this.RightArm.rotateAngleX = (float)((double)this.RightArm.rotateAngleX - ((double)f7 * 1.2 + (double)f8));
         var17 = this.RightArm;
         var17.rotateAngleY += this.Body.rotateAngleY * 2.0F;
         this.RightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
      }

      if (entity.isSneaking()) {
         ModelRenderer var21 = this.RightArm;
         var21.rotateAngleX += 0.4F;
         var21 = this.LeftArm;
         var21.rotateAngleX += 0.4F;
      }

      ModelRenderer var23 = this.RightArm;
      var23.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      var23 = this.LeftArm;
      var23.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      var23 = this.RightArm;
      var23.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      var23 = this.LeftArm;
      var23.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
   }
}
