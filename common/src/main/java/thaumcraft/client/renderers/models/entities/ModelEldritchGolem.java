package thaumcraft.client.renderers.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;

public class ModelEldritchGolem extends ModelBase {
   ModelRenderer Frontcloth1;
   ModelRenderer CollarL;
   ModelRenderer Cloak1;
   ModelRenderer CloakCL;
   ModelRenderer CloakCR;
   ModelRenderer Cloak3;
   ModelRenderer Cloak2;
   ModelRenderer Head;
   ModelRenderer Head2;
   ModelRenderer Frontcloth0;
   ModelRenderer CollarB;
   ModelRenderer Torso;
   ModelRenderer CollarR;
   ModelRenderer CollarF;
   ModelRenderer CollarBlack;
   ModelRenderer ShoulderR1;
   ModelRenderer ArmL;
   ModelRenderer ShoulderR;
   ModelRenderer ShoulderR2;
   ModelRenderer ShoulderR0;
   ModelRenderer ArmR;
   ModelRenderer ShoulderL1;
   ModelRenderer ShoulderL0;
   ModelRenderer ShoulderL;
   ModelRenderer ShoulderL2;
   ModelRenderer BackpanelR1;
   ModelRenderer WaistR1;
   ModelRenderer WaistR2;
   ModelRenderer WaistR3;
   ModelRenderer LegR;
   ModelRenderer WaistL1;
   ModelRenderer WaistL2;
   ModelRenderer WaistL3;
   ModelRenderer Frontcloth2;
   ModelRenderer BackpanelL1;
   ModelRenderer LegL;

   public ModelEldritchGolem() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Cloak1 = new ModelRenderer(this, 0, 47);
      this.Cloak1.addBox(-5.0F, 1.5F, 4.0F, 10, 12, 1);
      this.Cloak1.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.Cloak1.setTextureSize(128, 64);
      this.setRotation(this.Cloak1, 0.1396263F, 0.0F, 0.0F);
      this.Cloak3 = new ModelRenderer(this, 0, 37);
      this.Cloak3.addBox(-5.0F, 17.5F, -0.8F, 10, 4, 1);
      this.Cloak3.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.Cloak3.setTextureSize(128, 64);
      this.setRotation(this.Cloak3, 0.4465716F, 0.0F, 0.0F);
      this.Cloak2 = new ModelRenderer(this, 0, 59);
      this.Cloak2.addBox(-5.0F, 13.5F, 1.7F, 10, 4, 1);
      this.Cloak2.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.Cloak2.setTextureSize(128, 64);
      this.setRotation(this.Cloak2, 0.3069452F, 0.0F, 0.0F);
      this.CloakCL = new ModelRenderer(this, 0, 43);
      this.CloakCL.addBox(3.0F, 0.5F, 2.0F, 2, 1, 3);
      this.CloakCL.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CloakCL.setTextureSize(128, 64);
      this.setRotation(this.CloakCL, 0.1396263F, 0.0F, 0.0F);
      this.CloakCR = new ModelRenderer(this, 0, 43);
      this.CloakCR.addBox(-5.0F, 0.5F, 2.0F, 2, 1, 3);
      this.CloakCR.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CloakCR.setTextureSize(128, 64);
      this.setRotation(this.CloakCR, 0.1396263F, 0.0F, 0.0F);
      this.Head = new ModelRenderer(this, 47, 12);
      this.Head.addBox(-3.5F, -6.0F, -2.5F, 7, 7, 5);
      this.Head.setRotationPoint(0.0F, 4.5F, -3.8F);
      this.Head.setTextureSize(128, 64);
      this.setRotation(this.Head, -0.1047198F, 0.0F, 0.0F);
      this.Head2 = new ModelRenderer(this, 26, 16);
      this.Head2.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);
      this.Head2.setRotationPoint(0.0F, 0.0F, -5.0F);
      this.Head2.setTextureSize(128, 64);
      this.setRotation(this.Head2, -0.1047198F, 0.0F, 0.0F);
      this.CollarL = new ModelRenderer(this, 75, 50);
      this.CollarL.addBox(3.5F, -0.5F, -7.0F, 1, 4, 10);
      this.CollarL.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CollarL.setTextureSize(128, 64);
      this.setRotation(this.CollarL, 0.837758F, 0.0F, 0.0F);
      this.CollarR = new ModelRenderer(this, 67, 50);
      this.CollarR.addBox(-4.5F, -0.5F, -7.0F, 1, 4, 10);
      this.CollarR.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CollarR.setTextureSize(128, 64);
      this.setRotation(this.CollarR, 0.837758F, 0.0F, 0.0F);
      this.CollarB = new ModelRenderer(this, 77, 59);
      this.CollarB.addBox(-3.5F, -0.5F, 2.0F, 7, 4, 1);
      this.CollarB.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CollarB.setTextureSize(128, 64);
      this.setRotation(this.CollarB, 0.837758F, 0.0F, 0.0F);
      this.CollarF = new ModelRenderer(this, 77, 59);
      this.CollarF.addBox(-3.5F, -0.5F, -7.0F, 7, 4, 1);
      this.CollarF.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CollarF.setTextureSize(128, 64);
      this.setRotation(this.CollarF, 0.837758F, 0.0F, 0.0F);
      this.CollarBlack = new ModelRenderer(this, 22, 0);
      this.CollarBlack.addBox(-3.5F, 0.0F, -6.0F, 7, 1, 8);
      this.CollarBlack.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.CollarBlack.setTextureSize(128, 64);
      this.setRotation(this.CollarBlack, 0.837758F, 0.0F, 0.0F);
      this.Frontcloth0 = new ModelRenderer(this, 114, 52);
      this.Frontcloth0.addBox(-3.0F, 3.2F, -3.5F, 6, 10, 1);
      this.Frontcloth0.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.Frontcloth0.setTextureSize(114, 64);
      this.setRotation(this.Frontcloth0, 0.1745329F, 0.0F, 0.0F);
      this.Frontcloth1 = new ModelRenderer(this, 114, 39);
      this.Frontcloth1.addBox(-1.0F, 1.5F, -3.5F, 6, 6, 1);
      this.Frontcloth1.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.Frontcloth1.setTextureSize(114, 64);
      this.setRotation(this.Frontcloth1, -0.1047198F, 0.0F, 0.0F);
      this.Frontcloth2 = new ModelRenderer(this, 114, 47);
      this.Frontcloth2.addBox(-1.0F, 8.5F, -1.5F, 6, 3, 1);
      this.Frontcloth2.setRotationPoint(-2.0F, 11.0F, 0.0F);
      this.Frontcloth2.setTextureSize(114, 64);
      this.setRotation(this.Frontcloth2, -0.3316126F, 0.0F, 0.0F);
      this.Torso = new ModelRenderer(this, 34, 45);
      this.Torso.addBox(-5.0F, 2.5F, -3.0F, 10, 10, 6);
      this.Torso.setRotationPoint(0.0F, 0.0F, -2.5F);
      this.Torso.setTextureSize(128, 64);
      this.Torso.mirror = true;
      this.setRotation(this.Torso, 0.1745329F, 0.0F, 0.0F);
      this.ArmR = new ModelRenderer(this, 78, 32);
      this.ArmR.addBox(-3.5F, 1.5F, -2.0F, 4, 13, 5);
      this.ArmR.setRotationPoint(-5.0F, 3.0F, -2.0F);
      this.ArmR.setTextureSize(128, 64);
      this.setRotation(this.ArmR, 0.0F, 0.0F, 0.1047198F);
      this.ShoulderR1 = new ModelRenderer(this, 0, 23);
      this.ShoulderR1.addBox(-3.3F, 4.0F, -2.5F, 1, 2, 6);
      this.ShoulderR1.setTextureSize(128, 64);
      this.setRotation(this.ShoulderR1, 0.0F, 0.0F, 1.186824F);
      this.ShoulderR = new ModelRenderer(this, 0, 0);
      this.ShoulderR.addBox(-4.3F, -1.0F, -3.0F, 4, 5, 7);
      this.ShoulderR.setTextureSize(128, 64);
      this.setRotation(this.ShoulderR, 0.0F, 0.0F, 1.186824F);
      this.ShoulderR2 = new ModelRenderer(this, 0, 12);
      this.ShoulderR2.addBox(-2.3F, 4.0F, -3.0F, 2, 3, 7);
      this.ShoulderR2.setTextureSize(128, 64);
      this.setRotation(this.ShoulderR2, 0.0F, 0.0F, 1.186824F);
      this.ShoulderR0 = new ModelRenderer(this, 56, 31);
      this.ShoulderR0.addBox(-4.5F, -1.5F, -2.5F, 5, 6, 6);
      this.ShoulderR0.setTextureSize(128, 64);
      this.setRotation(this.ShoulderR0, 0.0F, 0.0F, 0.0F);
      this.ArmL = new ModelRenderer(this, 78, 32);
      this.ArmL.mirror = true;
      this.ArmL.addBox(-0.5F, 1.5F, -2.0F, 4, 13, 5);
      this.ArmL.setRotationPoint(5.0F, 3.0F, -2.0F);
      this.ArmL.setTextureSize(128, 64);
      this.setRotation(this.ArmL, 0.0F, 0.0F, -0.1047198F);
      this.ShoulderL1 = new ModelRenderer(this, 0, 23);
      this.ShoulderL1.mirror = true;
      this.ShoulderL1.addBox(2.3F, 4.0F, -2.5F, 1, 2, 6);
      this.ShoulderL1.setTextureSize(128, 64);
      this.setRotation(this.ShoulderL1, 0.0F, 0.0F, -1.186824F);
      this.ShoulderL0 = new ModelRenderer(this, 56, 31);
      this.ShoulderL0.mirror = true;
      this.ShoulderL0.addBox(-0.5F, -1.5F, -2.5F, 5, 6, 6);
      this.ShoulderL0.setTextureSize(128, 64);
      this.setRotation(this.ShoulderL0, 0.0F, 0.0F, 0.0F);
      this.ShoulderL = new ModelRenderer(this, 0, 0);
      this.ShoulderL.mirror = true;
      this.ShoulderL.addBox(0.3F, -1.0F, -3.0F, 4, 5, 7);
      this.ShoulderL.setTextureSize(128, 64);
      this.setRotation(this.ShoulderL, 0.0F, 0.0F, -1.186824F);
      this.ShoulderL2 = new ModelRenderer(this, 0, 12);
      this.ShoulderL2.mirror = true;
      this.ShoulderL2.addBox(0.3F, 4.0F, -3.0F, 2, 3, 7);
      this.ShoulderL2.setTextureSize(128, 64);
      this.setRotation(this.ShoulderL2, 0.0F, 0.0F, -1.186824F);
      this.BackpanelR1 = new ModelRenderer(this, 96, 7);
      this.BackpanelR1.addBox(0.0F, 2.5F, -2.5F, 2, 2, 5);
      this.BackpanelR1.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.BackpanelR1.setTextureSize(128, 64);
      this.setRotation(this.BackpanelR1, 0.0F, 0.0F, 0.1396263F);
      this.WaistR1 = new ModelRenderer(this, 96, 14);
      this.WaistR1.addBox(-3.0F, -0.5F, -2.5F, 5, 3, 5);
      this.WaistR1.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.WaistR1.setTextureSize(128, 64);
      this.setRotation(this.WaistR1, 0.0F, 0.0F, 0.1396263F);
      this.WaistR2 = new ModelRenderer(this, 116, 13);
      this.WaistR2.addBox(-3.0F, 2.5F, -2.5F, 1, 4, 5);
      this.WaistR2.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.WaistR2.setTextureSize(128, 64);
      this.setRotation(this.WaistR2, 0.0F, 0.0F, 0.1396263F);
      this.WaistR3 = new ModelRenderer(this, 114, 5);
      this.WaistR3.mirror = true;
      this.WaistR3.addBox(-2.0F, 2.5F, -2.5F, 2, 3, 5);
      this.WaistR3.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.WaistR3.setTextureSize(128, 64);
      this.setRotation(this.WaistR3, 0.0F, 0.0F, 0.1396263F);
      this.LegR = new ModelRenderer(this, 79, 19);
      this.LegR.addBox(-2.5F, 2.5F, -2.0F, 4, 9, 4);
      this.LegR.setRotationPoint(-2.0F, 12.5F, 0.0F);
      this.LegR.setTextureSize(128, 64);
      this.setRotation(this.LegR, 0.0F, 0.0F, 0.0F);
      this.WaistL1 = new ModelRenderer(this, 96, 14);
      this.WaistL1.addBox(-2.0F, -0.5F, -2.5F, 5, 3, 5);
      this.WaistL1.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.WaistL1.setTextureSize(128, 64);
      this.WaistL1.mirror = true;
      this.setRotation(this.WaistL1, 0.0F, 0.0F, -0.1396263F);
      this.WaistL2 = new ModelRenderer(this, 116, 13);
      this.WaistL2.addBox(2.0F, 2.5F, -2.5F, 1, 4, 5);
      this.WaistL2.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.WaistL2.setTextureSize(128, 64);
      this.WaistL2.mirror = true;
      this.setRotation(this.WaistL2, 0.0F, 0.0F, -0.1396263F);
      this.WaistL3 = new ModelRenderer(this, 114, 5);
      this.WaistL3.addBox(0.0F, 2.5F, -2.5F, 2, 3, 5);
      this.WaistL3.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.WaistL3.setTextureSize(128, 64);
      this.WaistL3.mirror = true;
      this.setRotation(this.WaistL3, 0.0F, 0.0F, -0.1396263F);
      this.BackpanelL1 = new ModelRenderer(this, 96, 7);
      this.BackpanelL1.addBox(-2.0F, 2.5F, -2.5F, 2, 2, 5);
      this.BackpanelL1.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.BackpanelL1.setTextureSize(128, 64);
      this.BackpanelL1.mirror = true;
      this.setRotation(this.BackpanelL1, 0.0F, 0.0F, -0.1396263F);
      this.LegL = new ModelRenderer(this, 79, 19);
      this.LegL.addBox(-1.5F, 2.5F, -2.0F, 4, 9, 4);
      this.LegL.setRotationPoint(2.0F, 12.5F, 0.0F);
      this.LegL.setTextureSize(128, 64);
      this.LegL.mirror = true;
      this.setRotation(this.LegL, 0.0F, 0.0F, 0.0F);
      this.ArmL.addChild(this.ShoulderL);
      this.ArmL.addChild(this.ShoulderL0);
      this.ArmL.addChild(this.ShoulderL1);
      this.ArmL.addChild(this.ShoulderL2);
      this.ArmR.addChild(this.ShoulderR);
      this.ArmR.addChild(this.ShoulderR0);
      this.ArmR.addChild(this.ShoulderR1);
      this.ArmR.addChild(this.ShoulderR2);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      float a = MathHelper.cos(f * 0.44F) * 1.4F * f1;
      float b = MathHelper.cos(f * 0.44F + (float)Math.PI) * 1.4F * f1;
      float c = Math.min(a, b);
      this.Frontcloth1.rotateAngleX = c - 0.1047198F;
      this.Frontcloth2.rotateAngleX = c - 0.3316126F;
      this.Cloak1.rotateAngleX = -c / 3.0F + 0.1396263F;
      this.Cloak2.rotateAngleX = -c / 3.0F + 0.3069452F;
      this.Cloak3.rotateAngleX = -c / 3.0F + 0.4465716F;
      this.Frontcloth1.render(f5);
      this.CollarL.render(f5);
      this.CollarBlack.render(f5);
      this.Cloak1.render(f5);
      this.CloakCL.render(f5);
      this.CloakCR.render(f5);
      this.Cloak3.render(f5);
      this.Cloak2.render(f5);
      if (entity instanceof EntityEldritchGolem && !((EntityEldritchGolem)entity).isHeadless()) {
         this.Head.render(f5);
      } else {
         this.Head2.render(f5);
      }

      this.Frontcloth0.render(f5);
      this.CollarB.render(f5);
      this.Torso.render(f5);
      this.CollarR.render(f5);
      this.CollarF.render(f5);
      this.Frontcloth1.render(f5);
      this.ArmL.render(f5);
      this.ArmR.render(f5);
      this.BackpanelR1.render(f5);
      this.WaistR1.render(f5);
      this.WaistR2.render(f5);
      this.WaistR3.render(f5);
      this.LegR.render(f5);
      this.WaistL1.render(f5);
      this.WaistL2.render(f5);
      this.WaistL3.render(f5);
      this.Frontcloth2.render(f5);
      this.BackpanelL1.render(f5);
      this.LegL.render(f5);
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      if (entity instanceof EntityEldritchGolem && ((EntityEldritchGolem)entity).getSpawnTimer() > 0) {
         this.Head.rotateAngleY = 0.0F;
         this.Head.rotateAngleX = (float)(((EntityEldritchGolem)entity).getSpawnTimer() / 2) / (180F / (float)Math.PI);
      } else {
         this.Head.rotateAngleY = par4 / 4.0F / (180F / (float)Math.PI);
         this.Head.rotateAngleX = par5 / 2.0F / (180F / (float)Math.PI);
         this.Head2.rotateAngleY = par4 / (180F / (float)Math.PI);
         this.Head2.rotateAngleX = par5 / (180F / (float)Math.PI);
      }

      this.LegR.rotateAngleX = MathHelper.cos(par1 * 0.4662F) * 1.4F * par2;
      this.LegL.rotateAngleX = MathHelper.cos(par1 * 0.4662F + (float)Math.PI) * 1.4F * par2;
   }

   public void setLivingAnimations(EntityLivingBase p_78086_1_, float par1, float par2, float par3) {
      EntityEldritchGolem golem = (EntityEldritchGolem)p_78086_1_;
      int i = golem.getAttackTimer();
      if (i > 0) {
         this.ArmR.rotateAngleX = -2.0F + 1.5F * this.doAbs((float)i - par3, 10.0F);
         this.ArmL.rotateAngleX = -2.0F + 1.5F * this.doAbs((float)i - par3, 10.0F);
      } else {
         this.ArmR.rotateAngleX = MathHelper.cos(par1 * 0.4F + (float)Math.PI) * 2.0F * par2 * 0.5F;
         this.ArmL.rotateAngleX = MathHelper.cos(par1 * 0.4F) * 2.0F * par2 * 0.5F;
      }

   }

   private float doAbs(float p_78172_1_, float p_78172_2_) {
      return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
