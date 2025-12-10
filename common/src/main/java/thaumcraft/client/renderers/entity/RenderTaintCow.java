package thaumcraft.client.renderers.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.monster.EntityTaintCow;

public class RenderTaintCow extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/cow.png");

   public RenderTaintCow(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }

   public void renderCow(EntityTaintCow par1EntityCow, double par2, double par4, double par6, float par8, float par9) {
      super.doRender(par1EntityCow, par2, par4, par6, par8, par9);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderCow((EntityTaintCow)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderCow((EntityTaintCow)par1Entity, par2, par4, par6, par8, par9);
   }
}
