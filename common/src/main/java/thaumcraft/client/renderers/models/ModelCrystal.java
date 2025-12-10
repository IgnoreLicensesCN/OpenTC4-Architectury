package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCrystal extends ModelBase {
   ModelRenderer Crystal;

   public ModelCrystal() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.Crystal = new ModelRenderer(this, 0, 0);
      this.Crystal.addBox(-16.0F, -16.0F, 0.0F, 16, 16, 16);
      this.Crystal.setRotationPoint(0.0F, 32.0F, 0.0F);
      this.Crystal.setTextureSize(64, 32);
      this.Crystal.mirror = true;
      this.setRotation(this.Crystal, 0.7071F, 0.0F, 0.7071F);
   }

   public void render() {
      this.Crystal.render(0.0625F);
   }

   public void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
