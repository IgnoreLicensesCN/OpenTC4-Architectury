package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTubeValve extends ModelBase {
   ModelRenderer ValveRod;
   ModelRenderer ValveRing;

   public ModelTubeValve() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.ValveRod = new ModelRenderer(this, 0, 10);
      this.ValveRod.addBox(-1.0F, 2.0F, -1.0F, 2, 2, 2);
      this.ValveRod.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.ValveRod.setTextureSize(64, 32);
      this.ValveRod.mirror = true;
      this.setRotation(this.ValveRod, 0.0F, 0.0F, 0.0F);
   }

   public void render() {
      this.ValveRod.render(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
