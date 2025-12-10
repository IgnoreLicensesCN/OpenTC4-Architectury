package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCentrifuge extends ModelBase {
   ModelRenderer Crossbar;
   ModelRenderer Dingus1;
   ModelRenderer Dingus2;
   ModelRenderer Core;
   ModelRenderer Top;
   ModelRenderer Bottom;

   public ModelCentrifuge() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.Crossbar = new ModelRenderer(this, 16, 0);
      this.Crossbar.addBox(-4.0F, -1.0F, -1.0F, 8, 2, 2);
      this.Crossbar.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Crossbar.setTextureSize(64, 32);
      this.Crossbar.mirror = true;
      this.setRotation(this.Crossbar, 0.0F, 0.0F, 0.0F);
      this.Dingus1 = new ModelRenderer(this, 0, 16);
      this.Dingus1.addBox(4.0F, -3.0F, -2.0F, 4, 6, 4);
      this.Dingus1.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Dingus1.setTextureSize(64, 32);
      this.Dingus1.mirror = true;
      this.setRotation(this.Dingus1, 0.0F, 0.0F, 0.0F);
      this.Dingus2 = new ModelRenderer(this, 0, 16);
      this.Dingus2.addBox(-8.0F, -3.0F, -2.0F, 4, 6, 4);
      this.Dingus2.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Dingus2.setTextureSize(64, 32);
      this.Dingus2.mirror = true;
      this.setRotation(this.Dingus2, 0.0F, 0.0F, 0.0F);
      this.Core = new ModelRenderer(this, 0, 0);
      this.Core.addBox(-1.5F, -4.0F, -1.5F, 3, 8, 3);
      this.Core.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Core.setTextureSize(64, 32);
      this.Core.mirror = true;
      this.setRotation(this.Core, 0.0F, 0.0F, 0.0F);
      this.Top = new ModelRenderer(this, 20, 16);
      this.Top.addBox(-4.0F, -8.0F, -4.0F, 8, 4, 8);
      this.Top.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Top.setTextureSize(64, 32);
      this.Top.mirror = true;
      this.setRotation(this.Top, 0.0F, 0.0F, 0.0F);
      this.Bottom = new ModelRenderer(this, 20, 16);
      this.Bottom.addBox(-4.0F, 4.0F, -4.0F, 8, 4, 8);
      this.Bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Bottom.setTextureSize(64, 32);
      this.Bottom.mirror = true;
      this.setRotation(this.Bottom, 0.0F, 0.0F, 0.0F);
   }

   public void renderBoxes() {
      this.Top.render(0.0625F);
      this.Bottom.render(0.0625F);
   }

   public void renderSpinnyBit() {
      this.Crossbar.render(0.0625F);
      this.Dingus1.render(0.0625F);
      this.Dingus2.render(0.0625F);
      this.Core.render(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
