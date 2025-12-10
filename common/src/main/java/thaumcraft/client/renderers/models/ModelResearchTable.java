package thaumcraft.client.renderers.models;

import java.awt.Color;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelResearchTable extends ModelBase {
   ModelRenderer Top;
   ModelRenderer Leg1;
   ModelRenderer Leg2;
   ModelRenderer Leg3;
   ModelRenderer Leg4;
   ModelRenderer Crossbar;
   ModelRenderer Inkwell;
   ModelRenderer ScrollTube;
   ModelRenderer ScrollRibbon;

   public ModelResearchTable() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.Top = new ModelRenderer(this, 0, 0);
      this.Top.addBox(0.0F, 0.0F, 0.0F, 32, 4, 16);
      this.Top.setRotationPoint(-8.0F, 0.0F, -8.0F);
      this.Top.setTextureSize(128, 64);
      this.Top.mirror = true;
      this.setRotation(this.Top, 0.0F, 0.0F, 0.0F);
      this.Leg1 = new ModelRenderer(this, 0, 24);
      this.Leg1.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4);
      this.Leg1.setRotationPoint(-6.0F, 4.0F, -6.0F);
      this.Leg1.setTextureSize(128, 64);
      this.Leg1.mirror = true;
      this.setRotation(this.Leg1, 0.0F, 0.0F, 0.0F);
      this.Leg2 = new ModelRenderer(this, 0, 24);
      this.Leg2.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4);
      this.Leg2.setRotationPoint(-6.0F, 4.0F, 2.0F);
      this.Leg2.setTextureSize(128, 64);
      this.Leg2.mirror = true;
      this.setRotation(this.Leg2, 0.0F, 0.0F, 0.0F);
      this.Leg3 = new ModelRenderer(this, 0, 24);
      this.Leg3.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4);
      this.Leg3.setRotationPoint(18.0F, 4.0F, -6.0F);
      this.Leg3.setTextureSize(128, 64);
      this.Leg3.mirror = true;
      this.setRotation(this.Leg3, 0.0F, 0.0F, 0.0F);
      this.Leg4 = new ModelRenderer(this, 0, 24);
      this.Leg4.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4);
      this.Leg4.setRotationPoint(18.0F, 4.0F, 2.0F);
      this.Leg4.setTextureSize(128, 64);
      this.Leg4.mirror = true;
      this.setRotation(this.Leg4, 0.0F, 0.0F, 0.0F);
      this.Crossbar = new ModelRenderer(this, 24, 24);
      this.Crossbar.addBox(0.0F, 0.0F, 0.0F, 24, 4, 4);
      this.Crossbar.setRotationPoint(-4.0F, 10.0F, -2.0F);
      this.Crossbar.setTextureSize(128, 64);
      this.Crossbar.mirror = true;
      this.setRotation(this.Crossbar, 0.0F, 0.0F, 0.0F);
      this.Inkwell = new ModelRenderer(this, 0, 44);
      this.Inkwell.addBox(0.0F, 0.0F, 0.0F, 3, 2, 3);
      this.Inkwell.setRotationPoint(-6.0F, -2.0F, 3.0F);
      this.Inkwell.setTextureSize(128, 64);
      this.Inkwell.mirror = true;
      this.setRotation(this.Inkwell, 0.0F, 0.0F, 0.0F);
      this.ScrollTube = new ModelRenderer(this, 0, 0);
      this.ScrollTube.addBox(-21.0F, -0.5F, -8.0F, 8, 2, 2);
      this.ScrollTube.setRotationPoint(-2.0F, -2.0F, 2.0F);
      this.ScrollTube.setTextureSize(128, 64);
      this.ScrollTube.mirror = true;
      this.setRotation(this.ScrollTube, 0.0F, 10.0F, 0.0F);
      this.ScrollRibbon = new ModelRenderer(this, 0, 4);
      this.ScrollRibbon.addBox(-15.1F, -0.275F, -6.75F, 1, 2, 2);
      this.ScrollRibbon.setRotationPoint(-2.0F, -2.0F, 2.0F);
      this.ScrollRibbon.setTextureSize(128, 64);
      this.ScrollRibbon.mirror = true;
      this.setRotation(this.ScrollRibbon, 0.0F, 10.0F, 0.0F);
   }

   public void renderAll() {
      this.Top.render(0.0625F);
      this.Leg1.render(0.0625F);
      this.Leg2.render(0.0625F);
      this.Leg3.render(0.0625F);
      this.Leg4.render(0.0625F);
      this.Crossbar.render(0.0625F);
   }

   public void renderInkwell() {
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      this.Inkwell.render(0.0625F);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
   }

   public void renderScroll(int color) {
      GL11.glPushMatrix();
      this.ScrollTube.render(0.0625F);
      Color c = new Color(color);
      GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
      GL11.glScalef(1.2F, 1.2F, 1.2F);
      this.ScrollRibbon.render(0.0625F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
