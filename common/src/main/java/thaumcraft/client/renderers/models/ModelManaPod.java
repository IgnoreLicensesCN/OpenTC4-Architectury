package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelManaPod extends ModelBase {
   public ModelRenderer pod0;
   public ModelRenderer pod1;
   public ModelRenderer pod2;

   public ModelManaPod() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.pod0 = new ModelRenderer(this, 0, 0);
      this.pod0.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4);
      this.pod0.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.pod0.setTextureSize(32, 32);
      this.pod0.mirror = true;
      this.setRotation(this.pod0, 0.0F, 0.0F, 0.0F);
      this.pod1 = new ModelRenderer(this, 0, 0);
      this.pod1.addBox(-3.0F, 0.0F, -3.0F, 6, 7, 6);
      this.pod1.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.pod1.setTextureSize(32, 32);
      this.pod1.mirror = true;
      this.setRotation(this.pod1, 0.0F, 0.0F, 0.0F);
      this.pod2 = new ModelRenderer(this, 0, 0);
      this.pod2.addBox(-3.5F, 0.0F, -3.5F, 7, 9, 7);
      this.pod2.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.pod2.setTextureSize(32, 32);
      this.pod2.mirror = true;
      this.setRotation(this.pod2, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5);
      this.pod0.render(f5);
      this.pod1.render(f5);
      this.pod2.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
   }
}
