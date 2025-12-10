package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.monster.EntityTaintSpider;

@SideOnly(Side.CLIENT)
public class RenderTaintSpider extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/taint_spider.png");

   public RenderTaintSpider() {
      super(new ModelSpider(), 0.5F);
      this.setRenderPassModel(new ModelSpider());
   }

   protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
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

   protected void scaleSpider(EntityTaintSpider par1EntitySpider, float par2) {
      float f1 = par1EntitySpider.spiderScaleAmount();
      GL11.glScalef(f1, f1 * 1.25F, f1);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      this.scaleSpider((EntityTaintSpider)par1EntityLiving, par2);
   }

   protected float getDeathMaxRotation(EntityLivingBase par1EntityLiving) {
      return this.setSpiderDeathMaxRotation((EntitySpider)par1EntityLiving);
   }

   protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return this.setSpiderEyeBrightness((EntitySpider)par1EntityLiving, par2, par3);
   }
}
