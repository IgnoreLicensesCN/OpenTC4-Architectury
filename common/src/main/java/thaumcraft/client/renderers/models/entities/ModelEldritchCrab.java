package thaumcraft.client.renderers.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thaumcraft.common.entities.monster.EntityEldritchCrab;

public class ModelEldritchCrab extends ModelBase {
   ModelRenderer TailHelm;
   ModelRenderer TailBare;
   ModelRenderer RFLeg1;
   ModelRenderer RClaw1;
   ModelRenderer Head1;
   ModelRenderer RClaw0;
   ModelRenderer RClaw2;
   ModelRenderer LClaw2;
   ModelRenderer LClaw1;
   ModelRenderer RArm;
   ModelRenderer Torso;
   ModelRenderer RRLeg1;
   ModelRenderer Head0;
   ModelRenderer LRLeg1;
   ModelRenderer LFLeg1;
   ModelRenderer RRLeg0;
   ModelRenderer RFLeg0;
   ModelRenderer LFLeg0;
   ModelRenderer LRLeg0;
   ModelRenderer LClaw0;
   ModelRenderer LArm;

   public ModelEldritchCrab() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.TailHelm = new ModelRenderer(this, 0, 0);
      this.TailHelm.addBox(-4.5F, -4.5F, -0.4F, 9, 9, 9);
      this.TailHelm.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.setRotation(this.TailHelm, 0.1047198F, 0.0F, 0.0F);
      this.TailBare = new ModelRenderer(this, 64, 0);
      this.TailBare.addBox(-4.0F, -4.0F, -0.4F, 8, 8, 8);
      this.TailBare.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.setRotation(this.TailBare, 0.1047198F, 0.0F, 0.0F);
      this.RClaw1 = new ModelRenderer(this, 0, 47);
      this.RClaw1.addBox(-2.0F, -1.0F, -5.066667F, 4, 3, 5);
      this.RClaw1.setRotationPoint(-6.0F, 15.5F, -10.0F);
      this.Head1 = new ModelRenderer(this, 0, 38);
      this.Head1.addBox(-2.0F, -1.5F, -9.066667F, 4, 4, 1);
      this.Head1.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.RClaw0 = new ModelRenderer(this, 0, 55);
      this.RClaw0.addBox(-2.0F, -2.5F, -3.066667F, 4, 5, 3);
      this.RClaw0.setRotationPoint(-6.0F, 17.0F, -7.0F);
      this.RClaw2 = new ModelRenderer(this, 14, 54);
      this.RClaw2.addBox(-1.5F, -1.0F, -4.066667F, 3, 2, 5);
      this.RClaw2.setRotationPoint(-6.0F, 18.5F, -10.0F);
      this.setRotation(this.RClaw2, 0.3141593F, 0.0F, 0.0F);
      this.RArm = new ModelRenderer(this, 44, 4);
      this.RArm.addBox(-1.0F, -1.0F, -5.066667F, 2, 2, 6);
      this.RArm.setRotationPoint(-3.0F, 17.0F, -4.0F);
      this.setRotation(this.RArm, 0.0F, 0.7504916F, 0.0F);
      this.LClaw2 = new ModelRenderer(this, 14, 54);
      this.LClaw2.addBox(-1.5F, -1.0F, -4.066667F, 3, 2, 5);
      this.LClaw2.setRotationPoint(6.0F, 18.5F, -10.0F);
      this.setRotation(this.LClaw2, 0.3141593F, 0.0F, 0.0F);
      this.LClaw1 = new ModelRenderer(this, 0, 47);
      this.LClaw1.mirror = true;
      this.LClaw1.addBox(-2.0F, -1.0F, -5.066667F, 4, 3, 5);
      this.LClaw1.setRotationPoint(6.0F, 15.5F, -10.0F);
      this.LClaw0 = new ModelRenderer(this, 0, 55);
      this.LClaw0.mirror = true;
      this.LClaw0.addBox(-2.0F, -2.5F, -3.066667F, 4, 5, 3);
      this.LClaw0.setRotationPoint(6.0F, 17.0F, -7.0F);
      this.LArm = new ModelRenderer(this, 44, 4);
      this.LArm.addBox(-1.0F, -1.0F, -4.066667F, 2, 2, 6);
      this.LArm.setRotationPoint(4.0F, 17.0F, -5.0F);
      this.setRotation(this.LArm, 0.0F, -0.7504916F, 0.0F);
      this.Torso = new ModelRenderer(this, 0, 18);
      this.Torso.addBox(-3.5F, -3.5F, -6.066667F, 7, 7, 6);
      this.Torso.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.setRotation(this.Torso, 0.0523599F, 0.0F, 0.0F);
      this.Head0 = new ModelRenderer(this, 0, 31);
      this.Head0.addBox(-2.5F, -2.0F, -8.066667F, 5, 5, 2);
      this.Head0.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.RRLeg1 = new ModelRenderer(this, 36, 4);
      this.RRLeg1.addBox(-4.5F, 1.0F, -0.9F, 2, 5, 2);
      this.RRLeg1.setRotationPoint(-4.0F, 20.0F, -1.5F);
      this.RFLeg1 = new ModelRenderer(this, 36, 4);
      this.RFLeg1.addBox(-5.0F, 1.0F, -1.066667F, 2, 5, 2);
      this.RFLeg1.setRotationPoint(-4.0F, 20.0F, -3.5F);
      this.LRLeg1 = new ModelRenderer(this, 36, 4);
      this.LRLeg1.addBox(2.5F, 1.0F, -0.9F, 2, 5, 2);
      this.LRLeg1.setRotationPoint(4.0F, 20.0F, -1.5F);
      this.LFLeg1 = new ModelRenderer(this, 36, 4);
      this.LFLeg1.addBox(3.0F, 1.0F, -1.066667F, 2, 5, 2);
      this.LFLeg1.setRotationPoint(4.0F, 20.0F, -3.5F);
      this.RRLeg0 = new ModelRenderer(this, 36, 0);
      this.RRLeg0.addBox(-4.5F, -1.0F, -0.9F, 6, 2, 2);
      this.RRLeg0.setRotationPoint(-4.0F, 20.0F, -1.5F);
      this.RFLeg0 = new ModelRenderer(this, 36, 0);
      this.RFLeg0.addBox(-5.0F, -1.0F, -1.066667F, 6, 2, 2);
      this.RFLeg0.setRotationPoint(-4.0F, 20.0F, -3.5F);
      this.LFLeg0 = new ModelRenderer(this, 36, 0);
      this.LFLeg0.addBox(-1.0F, -1.0F, -1.066667F, 6, 2, 2);
      this.LFLeg0.setRotationPoint(4.0F, 20.0F, -3.5F);
      this.LRLeg0 = new ModelRenderer(this, 36, 0);
      this.LRLeg0.addBox(-1.5F, -1.0F, -0.9F, 6, 2, 2);
      this.LRLeg0.setRotationPoint(4.0F, 20.0F, -1.5F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      if (entity instanceof EntityEldritchCrab && ((EntityEldritchCrab)entity).hasHelm()) {
         this.TailHelm.render(f5);
      } else {
         this.TailBare.render(f5);
      }

      this.RFLeg1.render(f5);
      this.RClaw1.render(f5);
      this.Head1.render(f5);
      this.RClaw0.render(f5);
      this.RClaw2.render(f5);
      this.LClaw2.render(f5);
      this.LClaw1.render(f5);
      this.RArm.render(f5);
      this.Torso.render(f5);
      this.RRLeg1.render(f5);
      this.Head0.render(f5);
      this.LRLeg1.render(f5);
      this.LFLeg1.render(f5);
      this.RRLeg0.render(f5);
      this.RFLeg0.render(f5);
      this.LFLeg0.render(f5);
      this.LRLeg0.render(f5);
      this.LClaw0.render(f5);
      this.LArm.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.setRotation(this.RRLeg1, 0.0F, 0.2094395F, 0.4363323F);
      this.setRotation(this.RFLeg1, 0.0F, -0.2094395F, 0.4363323F);
      this.setRotation(this.LRLeg1, 0.0F, -0.2094395F, -0.4363323F);
      this.setRotation(this.LFLeg1, 0.0F, 0.2094395F, -0.4363323F);
      this.setRotation(this.RRLeg0, 0.0F, 0.2094395F, 0.4363323F);
      this.setRotation(this.RFLeg0, 0.0F, -0.2094395F, 0.4363323F);
      this.setRotation(this.LFLeg0, 0.0F, 0.2094395F, -0.4363323F);
      this.setRotation(this.LRLeg0, 0.0F, -0.2094395F, -0.4363323F);
      float f9 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * par2;
      float f10 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * par2;
      ModelRenderer var10000 = this.RRLeg1;
      var10000.rotateAngleY += f9;
      var10000 = this.RRLeg0;
      var10000.rotateAngleY += f9;
      var10000 = this.LRLeg1;
       var10000.rotateAngleY -= f9;
      var10000 = this.LRLeg0;
       var10000.rotateAngleY -= f9;
      var10000 = this.RFLeg1;
      var10000.rotateAngleY += f10;
      var10000 = this.RFLeg0;
      var10000.rotateAngleY += f10;
      var10000 = this.LFLeg1;
       var10000.rotateAngleY -= f10;
      var10000 = this.LFLeg0;
       var10000.rotateAngleY -= f10;
      var10000 = this.RRLeg1;
      var10000.rotateAngleZ += f9;
      var10000 = this.RRLeg0;
      var10000.rotateAngleZ += f9;
      var10000 = this.LRLeg1;
       var10000.rotateAngleZ -= f9;
      var10000 = this.LRLeg0;
       var10000.rotateAngleZ -= f9;
      var10000 = this.RFLeg1;
      var10000.rotateAngleZ += f10;
      var10000 = this.RFLeg0;
      var10000.rotateAngleZ += f10;
      var10000 = this.LFLeg1;
       var10000.rotateAngleZ -= f10;
      var10000 = this.LFLeg0;
       var10000.rotateAngleZ -= f10;
      this.TailBare.rotateAngleY = this.TailHelm.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.125F;
      this.TailBare.rotateAngleZ = this.TailHelm.rotateAngleZ = MathHelper.cos(par1 * 0.6662F) * par2 * 0.125F;
      this.RClaw2.rotateAngleX = 0.3141593F - MathHelper.sin((float)entity.ticksExisted / 4.0F) * 0.25F;
      this.LClaw2.rotateAngleX = 0.3141593F + MathHelper.sin((float)entity.ticksExisted / 4.1F) * 0.25F;
      this.RClaw1.rotateAngleX = MathHelper.sin((float)entity.ticksExisted / 4.0F) * 0.125F;
      this.LClaw1.rotateAngleX = -MathHelper.sin((float)entity.ticksExisted / 4.1F) * 0.125F;
   }
}
