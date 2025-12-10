package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class ModelGolemAccessories extends ModelBase {
   public ModelRenderer golemHeadFez;
   public ModelRenderer golemHeadGlasses;
   public ModelRenderer golemHeadHat;
   public ModelRenderer golemHeadHatRim;
   public ModelRenderer golemBowtie;
   public ModelRenderer golemDartgun;
   public ModelRenderer golemMace;
   public ModelRenderer golemVisor;
   public ModelRenderer golemPlate;
   public ModelRenderer golemPlateLeft;
   public ModelRenderer golemPlateRight;
   public ModelRenderer golemHeadJar;
   public ModelRenderer golemHeadBrain;
   public ModelRenderer golemEvilHead;

   public ModelGolemAccessories() {
      this(0.0F);
   }

   public ModelGolemAccessories(float par1) {
      this(par1, -7.0F);
   }

   public ModelGolemAccessories(float par1, float par2) {
      short var3 = 128;
      short var4 = 128;
      this.golemHeadFez = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemHeadFez.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemHeadFez.setTextureOffset(0, 94).addBox(-4.5F, -15.0F, -6.0F, 9, 7, 9, par1);
      this.golemPlate = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemPlate.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
      this.golemPlate.setTextureOffset(32, 40).addBox(-6.5F, -1.0F, -7.0F, 13, 12, 13, par1);
      this.golemPlateLeft = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemPlateLeft.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
      this.golemPlateLeft.setTextureOffset(0, 44).addBox(-8.5F, -4.0F, -6.5F, 3, 6, 12, par1);
      this.golemPlateRight = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemPlateRight.mirror = true;
      this.golemPlateRight.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
      this.golemPlateRight.setTextureOffset(0, 44).addBox(5.5F, -4.0F, -6.5F, 3, 6, 12, par1);
      this.golemHeadHat = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemHeadHat.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemHeadHat.setTextureOffset(0, 110).addBox(-4.5F, -17.0F, -6.0F, 9, 9, 9, par1);
      this.golemHeadGlasses = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemHeadGlasses.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemHeadGlasses.setTextureOffset(0, 80).addBox(-4.5F, -8.0F, -6.0F, 9, 4, 9, par1);
      this.golemVisor = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemVisor.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemVisor.setTextureOffset(0, 70).addBox(-5.0F, -8.0F, -6.0F, 10, 5, 5, par1);
      this.golemHeadHatRim = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemHeadHatRim.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemHeadHatRim.setTextureOffset(36, 114).addBox(-6.5F, -9.0F, -8.0F, 13, 1, 13, 0.0F);
      this.golemDartgun = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemDartgun.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
      this.golemDartgun.setTextureOffset(80, 80).addBox(7.9F, 7.5F, -3.5F, 6, 16, 7, par1);
      this.golemMace = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemMace.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
      this.golemMace.setTextureOffset(80, 26).addBox(-13.0F, 15.0F, -5.0F, 6, 8, 10, par1);
      this.golemBowtie = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemBowtie.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
      this.golemBowtie.setTextureOffset(0, 0).addBox(-8.5F, -2.0F, -6.5F, 17, 4, 12, par1);
      this.golemHeadJar = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemHeadJar.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemHeadJar.setTextureOffset(96, 56).addBox(-4.0F, -15.0F, -5.5F, 8, 4, 8, par1);
      this.golemHeadBrain = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemHeadBrain.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemHeadBrain.setTextureOffset(96, 70).addBox(-3.5F, -14.0F, -5.0F, 7, 3, 7, par1);
      this.golemEvilHead = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.golemEvilHead.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
      this.golemEvilHead.setTextureOffset(64, 65).addBox(-4.0F, -9.0F, -5.5F, 8, 7, 8, par1);
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      EntityGolemBase en = (EntityGolemBase)par1Entity;
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, en);
      GL11.glPushMatrix();
      GL11.glScaled(0.4, 0.4, 0.4);
      String deco = en.getGolemDecoration();
      if (deco != null && deco.contains("R")) {
         this.golemDartgun.render(par7);
      }

      GL11.glPushMatrix();
      if (deco != null && deco.contains("F")) {
         if (en.advanced) {
            GL11.glTranslatef(0.0F, -0.01F, 0.0F);
         }

         this.golemHeadFez.render(par7);
      }

      if (deco != null && deco.contains("H")) {
         if (en.advanced) {
            GL11.glTranslatef(0.0F, -0.01F, 0.0F);
         }

         this.golemHeadHat.render(par7);
         this.golemHeadHatRim.render(par7);
      }

      GL11.glPopMatrix();
      if (deco != null && deco.contains("B")) {
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         this.golemBowtie.render(par7);
         GL11.glDisable(GL11.GL_BLEND);
      }

      if (deco != null && deco.contains("P")) {
         this.golemPlate.render(par7);
         this.golemPlateLeft.render(par7);
         this.golemPlateRight.render(par7);
      }

      if (deco != null && deco.contains("G")) {
         this.golemHeadGlasses.render(par7);
      }

      if (deco != null && deco.contains("V")) {
         this.golemVisor.render(par7);
      }

      if (deco != null && deco.contains("M")) {
         this.golemMace.render(par7);
      }

      if (en.advanced) {
         this.golemHeadBrain.render(par7);
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         this.golemHeadJar.render(par7);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
         if (en.getCore() >= 0) {
            GL11.glPushMatrix();
            GL11.glScaled(1.01, 1.0F, 1.01);
            this.golemEvilHead.render(par7);
            GL11.glPopMatrix();
         }
      }

      GL11.glPopMatrix();
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, EntityGolemBase en) {
      if (en.getCore() != -1 && !(en.bootup < 0.0F)) {
         if (en.inactive) {
            this.golemHeadFez.rotateAngleY = 0.0F;
            this.golemHeadFez.rotateAngleX = 0.57595867F;
         } else if (en.bootup > 0.0F) {
            this.golemHeadFez.rotateAngleY = 0.0F;
            this.golemHeadFez.rotateAngleX = en.bootup / (180F / (float)Math.PI);
         } else {
            this.golemHeadFez.rotateAngleY = par4 / (180F / (float)Math.PI);
            this.golemHeadFez.rotateAngleX = par5 / (180F / (float)Math.PI);
         }
      } else {
         this.golemHeadFez.rotateAngleY = 0.0F;
         this.golemHeadFez.rotateAngleX = 0.57595867F;
      }

      this.golemHeadGlasses.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemHeadGlasses.rotateAngleX = this.golemHeadFez.rotateAngleX;
      this.golemHeadJar.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemHeadJar.rotateAngleX = this.golemHeadFez.rotateAngleX;
      this.golemHeadBrain.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemHeadBrain.rotateAngleX = this.golemHeadFez.rotateAngleX;
      this.golemEvilHead.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemEvilHead.rotateAngleX = this.golemHeadFez.rotateAngleX;
      this.golemVisor.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemVisor.rotateAngleX = this.golemHeadFez.rotateAngleX;
      this.golemHeadHat.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemHeadHat.rotateAngleX = this.golemHeadFez.rotateAngleX;
      this.golemHeadHatRim.rotateAngleY = this.golemHeadFez.rotateAngleY;
      this.golemHeadHatRim.rotateAngleX = this.golemHeadFez.rotateAngleX;
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      EntityGolemBase var5 = (EntityGolemBase)par1EntityLiving;
      int var6 = var5.getActionTimer();
      if (var6 > 0) {
         this.golemDartgun.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)var6 - par4, 10.0F);
         this.golemMace.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)var6 - par4, 10.0F);
      } else if (var5.leftArm <= 0 && var5.rightArm <= 0) {
         if (var5.getCarriedForDisplay() != null) {
            this.golemDartgun.rotateAngleX = -1.0F;
            this.golemMace.rotateAngleX = -1.0F;
         } else {
            this.golemDartgun.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
            this.golemMace.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
         }
      } else {
         if (var5.leftArm > 0) {
            this.golemDartgun.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)var5.leftArm - par4, 10.0F);
         }

         if (var5.rightArm > 0) {
            this.golemMace.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)var5.rightArm - par4, 10.0F);
         }
      }

   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }
}
