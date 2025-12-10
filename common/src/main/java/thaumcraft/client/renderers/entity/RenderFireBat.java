package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelFireBat;
import thaumcraft.common.entities.monster.EntityFireBat;

@SideOnly(Side.CLIENT)
public class RenderFireBat extends RenderLiving {
   private int renderedBatSize;
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/firebat.png");
   private static final ResourceLocation rl2 = new ResourceLocation("thaumcraft", "textures/models/vampirebat.png");

   public RenderFireBat() {
      super(new ModelFireBat(), 0.25F);
      this.renderedBatSize = ((ModelFireBat)this.mainModel).getBatSize();
   }

   public void func_82443_a(EntityFireBat par1EntityBat, double par2, double par4, double par6, float par8, float par9) {
      int var10 = ((ModelFireBat)this.mainModel).getBatSize();
      if (var10 != this.renderedBatSize) {
         this.renderedBatSize = var10;
         this.mainModel = new ModelBat();
      }

      super.doRender(par1EntityBat, par2, par4, par6, par8, par9);
   }

   protected void func_82442_a(EntityFireBat par1EntityBat, float par2) {
      if (!par1EntityBat.getIsDevil() && !par1EntityBat.getIsVampire()) {
         GL11.glScalef(0.35F, 0.35F, 0.35F);
      } else {
         GL11.glScalef(0.6F, 0.6F, 0.6F);
      }

   }

   protected void func_82445_a(EntityFireBat par1EntityBat, double par2, double par4, double par6) {
      super.renderLivingAt(par1EntityBat, par2, par4, par6);
   }

   protected void func_82444_a(EntityFireBat par1EntityBat, float par2, float par3, float par4) {
      if (!par1EntityBat.getIsBatHanging()) {
         GL11.glTranslatef(0.0F, MathHelper.cos(par2 * 0.3F) * 0.1F, 0.0F);
      } else {
         GL11.glTranslatef(0.0F, -0.1F, 0.0F);
      }

      super.rotateCorpse(par1EntityBat, par2, par3, par4);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      this.func_82442_a((EntityFireBat)par1EntityLiving, par2);
   }

   protected void rotateCorpse(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      this.func_82444_a((EntityFireBat)par1EntityLiving, par2, par3, par4);
   }

   protected void renderLivingAt(EntityLivingBase par1EntityLiving, double par2, double par4, double par6) {
      this.func_82445_a((EntityFireBat)par1EntityLiving, par2, par4, par6);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.func_82443_a((EntityFireBat)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.func_82443_a((EntityFireBat)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return entity instanceof EntityFireBat && ((EntityFireBat)entity).getIsVampire() ? rl2 : rl;
   }
}
