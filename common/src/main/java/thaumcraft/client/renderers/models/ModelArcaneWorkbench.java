package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelArcaneWorkbench extends ModelBase {
   ModelRenderer Top;
   ModelRenderer Base;
   ModelRenderer Leg1;
   ModelRenderer Leg2;
   ModelRenderer Leg3;
   ModelRenderer Leg4;

   public ModelArcaneWorkbench() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Top = new ModelRenderer(this, 0, 0);
      this.Top.addBox(0.0F, 0.0F, 0.0F, 16, 8, 16);
      this.Top.setRotationPoint(-8.0F, 0.0F, -8.0F);
      this.Top.setTextureSize(128, 64);
      this.Top.mirror = true;
      this.setRotation(this.Top, 0.0F, 0.0F, 0.0F);
      this.Base = new ModelRenderer(this, 0, 32);
      this.Base.addBox(0.0F, 0.0F, 0.0F, 16, 4, 16);
      this.Base.setRotationPoint(-8.0F, 12.0F, -8.0F);
      this.Base.setTextureSize(128, 64);
      this.Base.mirror = true;
      this.setRotation(this.Base, 0.0F, 0.0F, 0.0F);
      this.Leg1 = new ModelRenderer(this, 72, 0);
      this.Leg1.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      this.Leg1.setRotationPoint(3.0F, 8.0F, -7.0F);
      this.Leg1.setTextureSize(128, 64);
      this.Leg1.mirror = true;
      this.setRotation(this.Leg1, 0.0F, 0.0F, 0.0F);
      this.Leg2 = new ModelRenderer(this, 72, 0);
      this.Leg2.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      this.Leg2.setRotationPoint(-7.0F, 8.0F, 3.0F);
      this.Leg2.setTextureSize(128, 64);
      this.Leg2.mirror = true;
      this.setRotation(this.Leg2, 0.0F, 0.0F, 0.0F);
      this.Leg3 = new ModelRenderer(this, 72, 0);
      this.Leg3.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      this.Leg3.setRotationPoint(3.0F, 8.0F, 3.0F);
      this.Leg3.setTextureSize(128, 64);
      this.Leg3.mirror = true;
      this.setRotation(this.Leg3, 0.0F, 0.0F, 0.0F);
      this.Leg4 = new ModelRenderer(this, 72, 0);
      this.Leg4.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      this.Leg4.setRotationPoint(-7.0F, 8.0F, -7.0F);
      this.Leg4.setTextureSize(128, 64);
      this.Leg4.mirror = true;
      this.setRotation(this.Leg4, 0.0F, 0.0F, 0.0F);
   }

   public void renderAll() {
      this.Top.render(0.0625F);
      this.Base.render(0.0625F);
      this.Leg1.render(0.0625F);
      this.Leg2.render(0.0625F);
      this.Leg3.render(0.0625F);
      this.Leg4.render(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
