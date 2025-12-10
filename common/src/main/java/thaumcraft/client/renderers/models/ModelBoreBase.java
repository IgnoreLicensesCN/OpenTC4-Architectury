package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBoreBase extends ModelBase {
   ModelRenderer Base1;
   ModelRenderer Base2;
   ModelRenderer PillarMid;
   ModelRenderer Pillar2;
   ModelRenderer Pillar3;
   ModelRenderer Pillar4;
   ModelRenderer Pillar1;
   ModelRenderer Nozzle1;
   ModelRenderer Nozzle2;

   public ModelBoreBase() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Base1 = new ModelRenderer(this, 64, 24);
      this.Base1.addBox(-8.0F, 0.0F, -8.0F, 16, 2, 16);
      this.Base1.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Base1.setTextureSize(128, 64);
      this.Base1.mirror = true;
      this.setRotation(this.Base1, 0.0F, 0.0F, 0.0F);
      this.Base2 = new ModelRenderer(this, 64, 24);
      this.Base2.addBox(-8.0F, 0.0F, -8.0F, 16, 2, 16);
      this.Base2.setRotationPoint(0.0F, 14.0F, 0.0F);
      this.Base2.setTextureSize(128, 64);
      this.Base2.mirror = true;
      this.setRotation(this.Base2, 0.0F, 0.0F, 0.0F);
      this.PillarMid = new ModelRenderer(this, 84, 42);
      this.PillarMid.addBox(-2.5F, 0.0F, -2.5F, 5, 12, 5);
      this.PillarMid.setRotationPoint(0.0F, 2.0F, 0.0F);
      this.PillarMid.setTextureSize(128, 64);
      this.PillarMid.mirror = true;
      this.setRotation(this.PillarMid, 0.0F, 0.0F, 0.0F);
      this.Pillar2 = new ModelRenderer(this, 64, 42);
      this.Pillar2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.Pillar2.setRotationPoint(-5.0F, 2.0F, -5.0F);
      this.Pillar2.setTextureSize(128, 64);
      this.Pillar2.mirror = true;
      this.setRotation(this.Pillar2, 0.0F, 0.0F, 0.0F);
      this.Pillar3 = new ModelRenderer(this, 64, 42);
      this.Pillar3.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.Pillar3.setRotationPoint(-5.0F, 2.0F, 5.0F);
      this.Pillar3.setTextureSize(128, 64);
      this.Pillar3.mirror = true;
      this.setRotation(this.Pillar3, 0.0F, 0.0F, 0.0F);
      this.Pillar4 = new ModelRenderer(this, 64, 42);
      this.Pillar4.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.Pillar4.setRotationPoint(5.0F, 2.0F, 5.0F);
      this.Pillar4.setTextureSize(128, 64);
      this.Pillar4.mirror = true;
      this.setRotation(this.Pillar4, 0.0F, 0.0F, 0.0F);
      this.Pillar1 = new ModelRenderer(this, 64, 42);
      this.Pillar1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.Pillar1.setRotationPoint(5.0F, 2.0F, -5.0F);
      this.Pillar1.setTextureSize(128, 64);
      this.Pillar1.mirror = true;
      this.setRotation(this.Pillar1, 0.0F, 0.0F, 0.0F);
      this.Nozzle1 = new ModelRenderer(this, 106, 42);
      this.Nozzle1.addBox(2.5F, -2.0F, -2.0F, 5, 4, 4);
      this.Nozzle1.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.Nozzle1.setTextureSize(128, 64);
      this.Nozzle1.mirror = true;
      this.setRotation(this.Nozzle1, 0.0F, 0.0F, 0.0F);
      this.Nozzle2 = new ModelRenderer(this, 106, 51);
      this.Nozzle2.addBox(7.0F, -2.5F, -2.5F, 1, 5, 5);
      this.Nozzle2.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.Nozzle2.setTextureSize(128, 64);
      this.Nozzle2.mirror = true;
      this.setRotation(this.Nozzle2, 0.0F, 0.0F, 0.0F);
   }

   public void render() {
      float f5 = 0.0625F;
      this.Base1.render(f5);
      this.Base2.render(f5);
      this.PillarMid.render(f5);
      this.Pillar2.render(f5);
      this.Pillar3.render(f5);
      this.Pillar4.render(f5);
      this.Pillar1.render(f5);
   }

   public void renderNozzle() {
      float f5 = 0.0625F;
      this.Nozzle1.render(f5);
      this.Nozzle2.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
