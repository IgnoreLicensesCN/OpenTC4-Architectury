package thaumcraft.client.renderers.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelTrunk;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;

public class RenderTravelingTrunk extends RenderLiving {
   private ModelTrunk trunkModel;
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/trunk.png");
   private static final ResourceLocation rl_a = new ResourceLocation("thaumcraft", "textures/models/trunkangry.png");

   public RenderTravelingTrunk(ModelBase modelbase, float f) {
      super(modelbase, f);
      this.trunkModel = (ModelTrunk)modelbase;
   }

   protected void adjustTrunk(EntityTravelingTrunk entity, float f) {
      int i = 2;
      float f1 = (entity.field_767_b + (entity.field_768_a - entity.field_767_b) * f) / ((float)i * 0.5F + 1.0F);
      float f2 = 1.0F / (f1 + 1.0F);
      float f3 = (float)i;
      f1 = (float)((double)f1 / (double)1.5F);
      f2 = (float)((double)f2 / 1.4);
      if (entity.getUpgrade() == 1) {
         f3 = (float)((double)f3 / 1.33);
      } else {
         f3 = (float)((double)f3 / (double)1.5F);
      }

      GL11.glScalef(f2 * f3, 0.5F / f2 * f3, f2 * f3);
      GL11.glTranslatef(-0.5F, 0.5F, -0.5F);
      f1 = 1.0F - entity.lidrot;
      f1 = 1.0F - f1 * f1 * f1;
      this.trunkModel.chestLid.rotateAngleX = -(f1 * 3.141593F / 2.0F);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float f) {
      this.adjustTrunk((EntityTravelingTrunk)par1EntityLivingBase, f);
   }

   public void doRender(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
      super.doRender(entityliving, d, d1, d2, f, f1);
      GL11.glTranslatef(0.0F, 0.0F, 0.0F);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return ((EntityTravelingTrunk)entity).getAnger() > 0 ? rl_a : rl;
   }
}
