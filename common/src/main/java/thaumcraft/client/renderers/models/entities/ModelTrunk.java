package thaumcraft.client.renderers.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;

public class ModelTrunk extends ModelBase {
   public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
   public ModelRenderer chestBelow;
   public ModelRenderer chestKnob;

   public ModelTrunk() {
      this.chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
      this.chestLid.rotationPointX = 1.0F;
      this.chestLid.rotationPointY = 7.0F;
      this.chestLid.rotationPointZ = 15.0F;
      this.chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
      this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
      this.chestKnob.rotationPointX = 8.0F;
      this.chestKnob.rotationPointY = 7.0F;
      this.chestKnob.rotationPointZ = 15.0F;
      this.chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
      this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
      this.chestBelow.rotationPointX = 1.0F;
      this.chestBelow.rotationPointY = 6.0F;
      this.chestBelow.rotationPointZ = 1.0F;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
      this.chestLid.render(0.0625F);
      this.chestBelow.render(0.0625F);
      this.chestKnob.render(0.0625F);
      GL11.glPushMatrix();
      GL11.glTranslatef(this.chestKnob.offsetX, this.chestKnob.offsetY, this.chestKnob.offsetZ);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      GL11.glPushMatrix();
      GL11.glTranslatef(this.chestKnob.rotationPointX * 0.0625F, this.chestKnob.rotationPointY * 0.0625F, this.chestKnob.rotationPointZ * 0.0625F);
      if (this.chestKnob.rotateAngleZ != 0.0F) {
         GL11.glRotatef(this.chestKnob.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
      }

      if (this.chestKnob.rotateAngleY != 0.0F) {
         GL11.glRotatef(this.chestKnob.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
      }

      if (this.chestKnob.rotateAngleX != 0.0F) {
         GL11.glRotatef(this.chestKnob.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
      }

      GL11.glTranslatef(-0.075F, -0.115F, -0.94301F);
      GL11.glScaled(0.15, 0.15, 0.15);
      Tessellator tessellator = Tessellator.instance;
      IIcon icon = ConfigItems.itemGolemUpgrade.getIconFromDamage(((EntityTravelingTrunk)entity).getUpgrade());
      float ff1 = icon.getMaxU();
      float ff2 = icon.getMinV();
      float ff3 = icon.getMinU();
      float ff4 = icon.getMaxV();
      RenderManager.instance.renderEngine.bindTexture(TextureMap.locationItemsTexture);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      tessellator.addVertexWithUV(0.0F, 0.0F, 0.0F, ff1, ff4);
      tessellator.addVertexWithUV(1.0F, 0.0F, 0.0F, ff3, ff4);
      tessellator.addVertexWithUV(1.0F, 1.0F, 0.0F, ff3, ff2);
      tessellator.addVertexWithUV(0.0F, 1.0F, 0.0F, ff1, ff2);
      tessellator.draw();
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glTranslatef(-this.chestKnob.offsetX, -this.chestKnob.offsetY, -this.chestKnob.offsetZ);
      GL11.glPopMatrix();
   }
}
