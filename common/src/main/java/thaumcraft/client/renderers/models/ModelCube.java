package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCube extends ModelBase {
   ModelRenderer cube;

   public ModelCube() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.cube = new ModelRenderer(this, 0, 0);
      this.cube.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
      this.cube.setRotationPoint(8.0F, 8.0F, 8.0F);
      this.cube.setTextureSize(64, 32);
      this.cube.mirror = true;
   }

   public ModelCube(int shift) {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.cube = new ModelRenderer(this, 0, shift);
      this.cube.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
      this.cube.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.cube.setTextureSize(64, 64);
      this.cube.mirror = true;
   }

   public void render() {
      this.cube.render(0.0625F);
   }

   public void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
