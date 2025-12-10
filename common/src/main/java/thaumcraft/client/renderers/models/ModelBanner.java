package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBanner extends ModelBase {
   ModelRenderer B1;
   ModelRenderer B2;
   ModelRenderer Beam;
   public ModelRenderer Banner;
   ModelRenderer Pole;

   public ModelBanner() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.B1 = new ModelRenderer(this, 0, 29);
      this.B1.addBox(-5.0F, -7.5F, -1.5F, 2, 3, 3);
      this.B1.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.B1.setTextureSize(128, 64);
      this.B1.mirror = true;
      this.setRotation(this.B1, 0.0F, 0.0F, 0.0F);
      this.B2 = new ModelRenderer(this, 0, 29);
      this.B2.addBox(3.0F, -7.5F, -1.5F, 2, 3, 3);
      this.B2.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.B2.setTextureSize(128, 64);
      this.B2.mirror = true;
      this.setRotation(this.B2, 0.0F, 0.0F, 0.0F);
      this.Beam = new ModelRenderer(this, 30, 0);
      this.Beam.addBox(-7.0F, -7.0F, -1.0F, 14, 2, 2);
      this.Beam.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Beam.setTextureSize(128, 64);
      this.Beam.mirror = true;
      this.setRotation(this.Beam, 0.0F, 0.0F, 0.0F);
      this.Banner = new ModelRenderer(this, 0, 0);
      this.Banner.addBox(-7.0F, 0.0F, -0.5F, 14, 28, 1);
      this.Banner.setRotationPoint(0.0F, -5.0F, 0.0F);
      this.Banner.setTextureSize(128, 64);
      this.Banner.mirror = true;
      this.setRotation(this.Banner, 0.0F, 0.0F, 0.0F);
      this.Pole = new ModelRenderer(this, 62, 0);
      this.Pole.addBox(0.0F, 0.0F, -1.0F, 2, 31, 2);
      this.Pole.setRotationPoint(-1.0F, -7.0F, -2.0F);
      this.Pole.setTextureSize(128, 64);
      this.Pole.mirror = true;
      this.setRotation(this.Pole, 0.0F, 0.0F, 0.0F);
   }

   public void renderPole() {
      this.Pole.render(0.0625F);
   }

   public void renderBeam() {
      this.Beam.render(0.0625F);
   }

   public void renderTabs() {
      this.B1.render(0.0625F);
      this.B2.render(0.0625F);
   }

   public void renderBanner() {
      this.Banner.render(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
