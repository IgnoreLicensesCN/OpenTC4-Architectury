package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelWatcher;
import thaumcraft.common.entities.monster.EntityWatcher;

@SideOnly(Side.CLIENT)
public class RenderWatcher extends RenderLiving {
   private static final ResourceLocation field_177114_e = new ResourceLocation("thaumcraft", "textures/models/watcher.png");
   private static final ResourceLocation field_177117_k = new ResourceLocation("thaumcraft", "textures/models/watcher_beam.png");
   int field_177115_a;

   public RenderWatcher() {
      super(new ModelWatcher(), 0.5F);
      this.field_177115_a = ((ModelWatcher)this.mainModel).func_178706_a();
   }

   private Vec3 func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_) {
      double d1 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * (double)p_177110_4_;
      double d2 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * (double)p_177110_4_;
      double d3 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * (double)p_177110_4_;
      return Vec3.createVectorHelper(d1, d2, d3);
   }

   public void func_177109_a(EntityWatcher p_177109_1_, double p_177109_2_, double p_177109_4_, double p_177109_6_, float p_177109_8_, float p_177109_9_) {
      if (this.field_177115_a != ((ModelWatcher)this.mainModel).func_178706_a()) {
         this.mainModel = new ModelWatcher();
         this.field_177115_a = ((ModelWatcher)this.mainModel).func_178706_a();
      }

      super.doRender(p_177109_1_, p_177109_2_, p_177109_4_, p_177109_6_, p_177109_8_, p_177109_9_);
      EntityLivingBase entitylivingbase = p_177109_1_.getTargetedEntity();
      if (entitylivingbase != null) {
         float f2 = p_177109_1_.func_175477_p(p_177109_9_);
         Tessellator tessellator = Tessellator.instance;
         this.bindTexture(field_177117_k);
         GL11.glTexParameterf(3553, 10242, 10497.0F);
         GL11.glTexParameterf(3553, 10243, 10497.0F);
         GL11.glDisable(2896);
         GL11.glDisable(2884);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glDepthMask(true);
         float f3 = 240.0F;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f3, f3);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);
         float f4 = (float)p_177109_1_.worldObj.getTotalWorldTime() + p_177109_9_;
         float f5 = f4 * 0.5F % 1.0F;
         float f6 = p_177109_1_.getEyeHeight();
         GL11.glPushMatrix();
         GL11.glTranslatef((float)p_177109_2_, (float)p_177109_4_ + f6, (float)p_177109_6_);
         Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * (double)0.5F, p_177109_9_);
         Vec3 vec31 = this.func_177110_a(p_177109_1_, f6, p_177109_9_);
         Vec3 vec32 = vec3.subtract(vec31);
         double d3 = vec32.lengthVector() + (double)1.0F;
         vec32 = vec32.normalize();
         float f7 = (float)Math.acos(vec32.yCoord);
         float f8 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
         GL11.glRotatef((((float) Math.PI / 2F) - f8) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(f7 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
         byte b0 = 1;
         double d4 = (double)f4 * 0.05 * ((double)1.0F - (double)(b0 & 1) * (double)2.5F);
         tessellator.startDrawingQuads();
         float f9 = f2 * f2;
         tessellator.setColorRGBA(64 + (int)(f9 * 240.0F), 32 + (int)(f9 * 192.0F), 128 - (int)(f9 * 64.0F), 255);
         double d5 = (double)b0 * 0.2;
         double d6 = d5 * 1.41;
         double d7 = (double)0.0F + Math.cos(d4 + 2.356194490192345) * d6;
         double d8 = (double)0.0F + Math.sin(d4 + 2.356194490192345) * d6;
         double d9 = (double)0.0F + Math.cos(d4 + (Math.PI / 4D)) * d6;
         double d10 = (double)0.0F + Math.sin(d4 + (Math.PI / 4D)) * d6;
         double d11 = (double)0.0F + Math.cos(d4 + 3.9269908169872414) * d6;
         double d12 = (double)0.0F + Math.sin(d4 + 3.9269908169872414) * d6;
         double d13 = (double)0.0F + Math.cos(d4 + 5.497787143782138) * d6;
         double d14 = (double)0.0F + Math.sin(d4 + 5.497787143782138) * d6;
         double d15 = (double)0.0F + Math.cos(d4 + Math.PI) * d5;
         double d16 = (double)0.0F + Math.sin(d4 + Math.PI) * d5;
         double d17 = (double)0.0F + Math.cos(d4 + (double)0.0F) * d5;
         double d18 = (double)0.0F + Math.sin(d4 + (double)0.0F) * d5;
         double d19 = (double)0.0F + Math.cos(d4 + (Math.PI / 2D)) * d5;
         double d20 = (double)0.0F + Math.sin(d4 + (Math.PI / 2D)) * d5;
         double d21 = (double)0.0F + Math.cos(d4 + (Math.PI * 1.5D)) * d5;
         double d22 = (double)0.0F + Math.sin(d4 + (Math.PI * 1.5D)) * d5;
         double d23 = 0.0F;
         double d24 = 0.4999;
         double d25 = -1.0F + f5;
         double d26 = d3 * ((double)0.5F / d5) + d25;
         tessellator.addVertexWithUV(d15, d3, d16, d24, d26);
         tessellator.addVertexWithUV(d15, 0.0F, d16, d24, d25);
         tessellator.addVertexWithUV(d17, 0.0F, d18, d23, d25);
         tessellator.addVertexWithUV(d17, d3, d18, d23, d26);
         tessellator.addVertexWithUV(d19, d3, d20, d24, d26);
         tessellator.addVertexWithUV(d19, 0.0F, d20, d24, d25);
         tessellator.addVertexWithUV(d21, 0.0F, d22, d23, d25);
         tessellator.addVertexWithUV(d21, d3, d22, d23, d26);
         double d27 = 0.0F;
         if (p_177109_1_.ticksExisted % 2 == 0) {
            d27 = 0.5F;
         }

         tessellator.addVertexWithUV(d7, d3, d8, 0.5F, d27 + (double)0.5F);
         tessellator.addVertexWithUV(d9, d3, d10, 1.0F, d27 + (double)0.5F);
         tessellator.addVertexWithUV(d13, d3, d14, 1.0F, d27);
         tessellator.addVertexWithUV(d11, d3, d12, 0.5F, d27);
         tessellator.draw();
         GL11.glPopMatrix();
      }

   }

   public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
      this.func_177109_a((EntityWatcher)entity, x, y, z, p_76986_8_, partialTicks);
   }

   public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
      this.func_177109_a((EntityWatcher)entity, x, y, z, p_76986_8_, partialTicks);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return field_177114_e;
   }

   public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
      this.func_177109_a((EntityWatcher)entity, x, y, z, p_76986_8_, partialTicks);
   }
}
