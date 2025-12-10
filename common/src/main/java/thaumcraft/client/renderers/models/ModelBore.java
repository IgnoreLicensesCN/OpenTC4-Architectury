package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBore extends ModelBase {
   ModelRenderer Base;
   ModelRenderer Side1;
   ModelRenderer Side2;
   ModelRenderer NozCrossbar;
   ModelRenderer NozFront;
   ModelRenderer NozMid;

   public ModelBore() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Base = new ModelRenderer(this, 0, 32);
      this.Base.addBox(-6.0F, 0.0F, -6.0F, 12, 2, 12);
      this.Base.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Base.setTextureSize(64, 32);
      this.Base.mirror = true;
      this.setRotation(this.Base, 0.0F, 0.0F, 0.0F);
      this.Side1 = new ModelRenderer(this, 0, 0);
      this.Side1.addBox(-2.0F, 2.0F, -5.5F, 4, 8, 1);
      this.Side1.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Side1.setTextureSize(64, 32);
      this.Side1.mirror = true;
      this.setRotation(this.Side1, 0.0F, 0.0F, 0.0F);
      this.Side2 = new ModelRenderer(this, 0, 0);
      this.Side2.addBox(-2.0F, 2.0F, 4.5F, 4, 8, 1);
      this.Side2.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Side2.setTextureSize(64, 32);
      this.Side2.mirror = true;
      this.setRotation(this.Side2, 0.0F, 0.0F, 0.0F);
      this.NozCrossbar = new ModelRenderer(this, 0, 48);
      this.NozCrossbar.addBox(-1.0F, -1.0F, -6.0F, 2, 2, 12);
      this.NozCrossbar.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.NozCrossbar.setTextureSize(64, 32);
      this.NozCrossbar.mirror = true;
      this.setRotation(this.NozCrossbar, 0.0F, 0.0F, 0.0F);
      this.NozFront = new ModelRenderer(this, 30, 14);
      this.NozFront.addBox(4.0F, -2.5F, -2.5F, 4, 5, 5);
      this.NozFront.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.NozFront.setTextureSize(64, 32);
      this.NozFront.mirror = true;
      this.setRotation(this.NozFront, 0.0F, 0.0F, 0.0F);
      this.NozMid = new ModelRenderer(this, 0, 14);
      this.NozMid.addBox(-2.0F, -4.0F, -4.0F, 6, 8, 8);
      this.NozMid.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.NozMid.setTextureSize(64, 32);
      this.NozMid.mirror = true;
      this.setRotation(this.NozMid, 0.0F, 0.0F, 0.0F);
   }

   public void renderBase() {
      float f5 = 0.0625F;
      this.Base.render(f5);
      this.Side1.render(f5);
      this.Side2.render(f5);
      this.NozCrossbar.render(f5);
   }

   public void renderNozzle() {
      float f5 = 0.0625F;
      this.NozFront.render(f5);
      this.NozMid.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
