package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBoreEmit extends ModelBase {
   ModelRenderer Knob;
   ModelRenderer Cross1;
   ModelRenderer Cross3;
   ModelRenderer Cross2;
   ModelRenderer Rod;

   public ModelBoreEmit() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Knob = new ModelRenderer(this, 66, 0);
      this.Knob.addBox(-2.0F, 12.0F, -2.0F, 4, 4, 4);
      this.Knob.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Knob.setTextureSize(128, 64);
      this.Knob.mirror = true;
      this.setRotation(this.Knob, 0.0F, 0.0F, 0.0F);
      this.Cross1 = new ModelRenderer(this, 56, 16);
      this.Cross1.addBox(-2.0F, 0.0F, -2.0F, 4, 1, 4);
      this.Cross1.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.Cross1.setTextureSize(128, 64);
      this.Cross1.mirror = true;
      this.setRotation(this.Cross1, 0.0F, 0.0F, 0.0F);
      this.Cross3 = new ModelRenderer(this, 56, 16);
      this.Cross3.addBox(-2.0F, 0.0F, -2.0F, 4, 1, 4);
      this.Cross3.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Cross3.setTextureSize(128, 64);
      this.Cross3.mirror = true;
      this.setRotation(this.Cross3, 0.0F, 0.0F, 0.0F);
      this.Cross2 = new ModelRenderer(this, 56, 24);
      this.Cross2.addBox(-3.0F, 4.0F, -3.0F, 6, 1, 6);
      this.Cross2.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Cross2.setTextureSize(128, 64);
      this.Cross2.mirror = true;
      this.setRotation(this.Cross2, 0.0F, 0.0F, 0.0F);
      this.Rod = new ModelRenderer(this, 56, 0);
      this.Rod.addBox(-1.0F, 1.0F, -1.0F, 2, 11, 2);
      this.Rod.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Rod.setTextureSize(128, 64);
      this.Rod.mirror = true;
      this.setRotation(this.Rod, 0.0F, 0.0F, 0.0F);
   }

   public void render(boolean focus) {
      float f5 = 0.0625F;
      if (focus) {
         this.Knob.render(f5);
      }

      this.Cross1.render(f5);
      this.Cross3.render(f5);
      this.Cross2.render(f5);
      this.Rod.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
