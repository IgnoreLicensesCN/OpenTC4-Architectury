package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDart extends Render {
   private static final ResourceLocation rl = new ResourceLocation("textures/entity/arrow.png");

   public void renderArrow(EntityArrow par1EntityArrow, double par2, double par4, double par6, float par8, float par9) {
      this.bindEntityTexture(par1EntityArrow);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      GL11.glRotatef(par1EntityArrow.prevRotationYaw + (par1EntityArrow.rotationYaw - par1EntityArrow.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(par1EntityArrow.prevRotationPitch + (par1EntityArrow.rotationPitch - par1EntityArrow.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
      Tessellator var10 = Tessellator.instance;
      byte var11 = 0;
      float var12 = 0.0F;
      float var13 = 0.5F;
      float var14 = (float)(var11 * 10) / 32.0F;
      float var15 = (float)(5 + var11 * 10) / 32.0F;
      float var16 = 0.0F;
      float var17 = 0.15625F;
      float var18 = (float)(5 + var11 * 10) / 32.0F;
      float var19 = (float)(10 + var11 * 10) / 32.0F;
      float var20 = 0.025625F;
      GL11.glEnable(32826);
      float var21 = (float)par1EntityArrow.arrowShake - par9;
      if (var21 > 0.0F) {
         float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
         GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
      }

      GL11.glColor3f(0.5F, 0.5F, 0.6F);
      GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(var20 * 0.75F, var20, var20);
      GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
      GL11.glNormal3f(var20, 0.0F, 0.0F);
      var10.startDrawingQuads();
      var10.addVertexWithUV(-7.0F, -2.0F, -2.0F, var16, var18);
      var10.addVertexWithUV(-7.0F, -2.0F, 2.0F, var17, var18);
      var10.addVertexWithUV(-7.0F, 2.0F, 2.0F, var17, var19);
      var10.addVertexWithUV(-7.0F, 2.0F, -2.0F, var16, var19);
      var10.draw();
      GL11.glNormal3f(-var20, 0.0F, 0.0F);
      var10.startDrawingQuads();
      var10.addVertexWithUV(-7.0F, 2.0F, -2.0F, var16, var18);
      var10.addVertexWithUV(-7.0F, 2.0F, 2.0F, var17, var18);
      var10.addVertexWithUV(-7.0F, -2.0F, 2.0F, var17, var19);
      var10.addVertexWithUV(-7.0F, -2.0F, -2.0F, var16, var19);
      var10.draw();

      for(int var23 = 0; var23 < 4; ++var23) {
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glNormal3f(0.0F, 0.0F, var20);
         var10.startDrawingQuads();
         var10.addVertexWithUV(-8.0F, -2.0F, 0.0F, var12, var14);
         var10.addVertexWithUV(8.0F, -2.0F, 0.0F, var13, var14);
         var10.addVertexWithUV(8.0F, 2.0F, 0.0F, var13, var15);
         var10.addVertexWithUV(-8.0F, 2.0F, 0.0F, var12, var15);
         var10.draw();
      }

      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderArrow((EntityArrow)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }
}
