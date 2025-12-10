package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.monster.EntityMindSpider;

@SideOnly(Side.CLIENT)
public class RenderMindSpider extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/taint_spider.png");

   public RenderMindSpider() {
      super(new ModelSpider(), 0.0F);
      this.setRenderPassModel(new ModelSpider());
   }

   protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }

   public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if (((EntityMindSpider) p_76986_1_).getViewer().isEmpty() || ((EntityMindSpider)p_76986_1_).getViewer().equals(this.renderManager.livingPlayer.getCommandSenderName())) {
         super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if (((EntityMindSpider) p_76986_1_).getViewer().isEmpty() || ((EntityMindSpider)p_76986_1_).getViewer().equals(this.renderManager.livingPlayer.getCommandSenderName())) {
         super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   public void doRender(EntityLivingBase entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if (((EntityMindSpider) entity).getViewer().isEmpty() || ((EntityMindSpider)entity).getViewer().equals(this.renderManager.livingPlayer.getCommandSenderName())) {
         super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   protected void renderModel(EntityLivingBase entity, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
      this.bindEntityTexture(entity);
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, Math.min(0.1F, (float)entity.ticksExisted / 100.0F));
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      GL11.glAlphaFunc(516, 0.003921569F);
      this.mainModel.render(entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
      GL11.glDepthMask(true);
   }

   protected void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
      super.passSpecialRender(p_77033_1_, p_77033_2_, p_77033_4_, p_77033_6_);
   }

   protected int setSpiderEyeBrightness(EntitySpider par1EntitySpider, int par2, float par3) {
      if (par2 != 0) {
         return -1;
      } else {
         UtilsFX.bindTexture("textures/models/taint_spider_eyes.png");
         float f1 = 1.0F;
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glDisable(3008);
         GL11.glBlendFunc(1, 1);
          GL11.glDepthMask(!par1EntitySpider.isInvisible());

         char c0 = '\uf0f0';
         int j = c0 % 65536;
         int k = c0 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
         return 1;
      }
   }

   protected void scaleSpider(EntityMindSpider par1EntitySpider, float par2) {
      float f1 = par1EntitySpider.spiderScaleAmount();
      GL11.glScalef(f1, f1, f1);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      this.scaleSpider((EntityMindSpider)par1EntityLiving, par2);
   }

   protected float getDeathMaxRotation(EntityLivingBase par1EntityLiving) {
      return this.setSpiderDeathMaxRotation((EntitySpider)par1EntityLiving);
   }

   protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return this.setSpiderEyeBrightness((EntitySpider)par1EntityLiving, par2, par3);
   }
}
