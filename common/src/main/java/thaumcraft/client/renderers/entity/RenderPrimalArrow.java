package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.blocks.BlockCustomOreItem;
import thaumcraft.common.entities.projectile.EntityPrimalArrow;

@SideOnly(Side.CLIENT)
public class RenderPrimalArrow extends Render {
   private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
   int size1 = 0;
   int size2 = 0;

   public void renderArrow(EntityPrimalArrow arrow, double x, double y, double z, float ns, float prt) {
      Color color = new Color(BlockCustomOreItem.colors[arrow.type + 1]);
      this.bindEntityTexture(arrow);
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glTranslatef((float)x, (float)y, (float)z);
      GL11.glRotatef(arrow.prevRotationYaw + (arrow.rotationYaw - arrow.prevRotationYaw) * prt - 90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(arrow.prevRotationPitch + (arrow.rotationPitch - arrow.prevRotationPitch) * prt, 0.0F, 0.0F, 1.0F);
      Tessellator tessellator = Tessellator.instance;
      byte b0 = 0;
      float f2 = 0.0F;
      float f3 = 0.5F;
      float f4 = (float)(b0 * 10) / 32.0F;
      float f5 = (float)(5 + b0 * 10) / 32.0F;
      float f6 = 0.0F;
      float f7 = 0.15625F;
      float f8 = (float)(5 + b0 * 10) / 32.0F;
      float f9 = (float)(10 + b0 * 10) / 32.0F;
      float f10 = 0.05625F;
      GL11.glEnable(32826);
      float f11 = (float)arrow.arrowShake - prt;
      if (f11 > 0.0F) {
         float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
         GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, (100.0F - (float)arrow.ticksInGround) / 100.0F);
      GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(f10, f10, f10);
      GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
      GL11.glNormal3f(f10, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(-7.0F, -2.0F, -2.0F, f6, f8);
      tessellator.addVertexWithUV(-7.0F, -2.0F, 2.0F, f7, f8);
      tessellator.addVertexWithUV(-7.0F, 2.0F, 2.0F, f7, f9);
      tessellator.addVertexWithUV(-7.0F, 2.0F, -2.0F, f6, f9);
      tessellator.draw();
      GL11.glNormal3f(-f10, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(-7.0F, 2.0F, -2.0F, f6, f8);
      tessellator.addVertexWithUV(-7.0F, 2.0F, 2.0F, f7, f8);
      tessellator.addVertexWithUV(-7.0F, -2.0F, 2.0F, f7, f9);
      tessellator.addVertexWithUV(-7.0F, -2.0F, -2.0F, f6, f9);
      tessellator.draw();

      for(int i = 0; i < 4; ++i) {
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glNormal3f(0.0F, 0.0F, f10);
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV(-8.0F, -2.0F, 0.0F, f2, f4);
         tessellator.addVertexWithUV(8.0F, -2.0F, 0.0F, f3, f4);
         tessellator.addVertexWithUV(8.0F, 2.0F, 0.0F, f3, f5);
         tessellator.addVertexWithUV(-8.0F, 2.0F, 0.0F, f2, f5);
         tessellator.draw();
      }

      GL11.glDisable(32826);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
      float f1 = ActiveRenderInfo.rotationX;
      f2 = ActiveRenderInfo.rotationXZ;
      f3 = ActiveRenderInfo.rotationZ;
      f4 = ActiveRenderInfo.rotationYZ;
      f5 = ActiveRenderInfo.rotationXY;
      f10 = 0.5F;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x, (float)y, (float)z);
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      if (arrow.type < 5) {
         GL11.glBlendFunc(770, 1);
      } else {
         GL11.glBlendFunc(770, 771);
      }

      UtilsFX.bindTexture("textures/misc/wisp.png");
      int i = arrow.ticksExisted % 16;
      float size4 = (float)(this.size1 * 4);
      float float_sizeMinus0_01 = (float)this.size1 - 0.01F;
      float float_texNudge = 1.0F / ((float)this.size1 * (float)this.size1 * 2.0F);
      float float_reciprocal = 1.0F / (float)this.size1;
      float x0 = ((float)(i % 4 * this.size1) + 0.0F) / size4;
      float x1 = ((float)(i % 4 * this.size1) + float_sizeMinus0_01) / size4;
      float x2 = ((float)(i / 4 * this.size1) + 0.0F) / size4;
      float x3 = ((float)(i / 4 * this.size1) + float_sizeMinus0_01) / size4;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (100.0F - (float)arrow.ticksInGround) / 100.0F);
      tessellator.addVertexWithUV(-f1 * f10 - f4 * f10, -f2 * f10, -f3 * f10 - f5 * f10, x1, x3);
      tessellator.addVertexWithUV(-f1 * f10 + f4 * f10, f2 * f10, -f3 * f10 + f5 * f10, x1, x2);
      tessellator.addVertexWithUV(f1 * f10 + f4 * f10, f2 * f10, f3 * f10 + f5 * f10, x0, x2);
      tessellator.addVertexWithUV(f1 * f10 - f4 * f10, -f2 * f10, f3 * f10 - f5 * f10, x0, x3);
      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   protected ResourceLocation getArrowTextures(EntityPrimalArrow par1EntityArrow) {
      return arrowTextures;
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getArrowTextures((EntityPrimalArrow)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      if (this.size1 == 0) {
         this.size1 = UtilsFX.getTextureSize("textures/misc/wisp.png", 64);
      }

      this.renderArrow((EntityPrimalArrow)par1Entity, par2, par4, par6, par8, par9);
   }
}
