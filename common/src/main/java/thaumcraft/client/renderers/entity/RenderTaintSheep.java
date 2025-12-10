package thaumcraft.client.renderers.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.monster.EntityTaintSheep;

public class RenderTaintSheep extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/sheep.png");

   public RenderTaintSheep(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
      super(par1ModelBase, par3);
      this.setRenderPassModel(par2ModelBase);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }

   protected int setWoolColorAndRender(EntityTaintSheep par1EntitySheep, int par2, float par3) {
      if (par2 == 0 && !par1EntitySheep.getSheared()) {
         UtilsFX.bindTexture("textures/models/sheep_fur.png");
         float var4 = 1.0F;
         return 1;
      } else {
         return -1;
      }
   }

   public void func_40271_a(EntityTaintSheep par1EntitySheep, double par2, double par4, double par6, float par8, float par9) {
      super.doRender(par1EntitySheep, par2, par4, par6, par8, par9);
   }

   protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return this.setWoolColorAndRender((EntityTaintSheep)par1EntityLiving, par2, par3);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.func_40271_a((EntityTaintSheep)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.func_40271_a((EntityTaintSheep)par1Entity, par2, par4, par6, par8, par9);
   }
}
