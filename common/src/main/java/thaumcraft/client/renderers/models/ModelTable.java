package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTable extends ModelBase {
   ModelRenderer Top;
   ModelRenderer Leg1;
   ModelRenderer Leg2;
   ModelRenderer Crossbar;

   public ModelTable() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.Top = new ModelRenderer(this, 0, 0);
      this.Top.addBox(0.0F, 0.0F, 0.0F, 16, 4, 16);
      this.Top.setRotationPoint(-8.0F, 0.0F, -8.0F);
      this.Top.setTextureSize(64, 32);
      this.Top.mirror = true;
      this.setRotation(this.Top, 0.0F, 0.0F, 0.0F);
      this.Leg1 = new ModelRenderer(this, 0, 20);
      this.Leg1.addBox(0.0F, 0.0F, 0.0F, 4, 8, 4);
      this.Leg1.setRotationPoint(2.0F, 4.0F, -2.0F);
      this.Leg1.setTextureSize(64, 32);
      this.Leg1.mirror = true;
      this.setRotation(this.Leg1, 0.0F, 0.0F, 0.0F);
      this.Leg2 = new ModelRenderer(this, 0, 20);
      this.Leg2.addBox(0.0F, 0.0F, 0.0F, 4, 8, 4);
      this.Leg2.setRotationPoint(-6.0F, 4.0F, -2.0F);
      this.Leg2.setTextureSize(64, 32);
      this.Leg2.mirror = true;
      this.setRotation(this.Leg2, 0.0F, 0.0F, 0.0F);
      this.Crossbar = new ModelRenderer(this, 16, 20);
      this.Crossbar.addBox(0.0F, 0.0F, 0.0F, 16, 4, 8);
      this.Crossbar.setRotationPoint(-8.0F, 12.0F, -4.0F);
      this.Crossbar.setTextureSize(64, 32);
      this.Crossbar.mirror = true;
      this.setRotation(this.Crossbar, 0.0F, 0.0F, 0.0F);
   }

   public void renderAll() {
      this.Top.render(0.0625F);
      this.Leg1.render(0.0625F);
      this.Leg2.render(0.0625F);
      this.Crossbar.render(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
