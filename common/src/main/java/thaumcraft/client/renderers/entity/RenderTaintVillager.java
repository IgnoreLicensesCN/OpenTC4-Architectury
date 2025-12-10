package thaumcraft.client.renderers.entity;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityTaintVillager;

public class RenderTaintVillager extends RenderLiving {
   protected ModelVillager field_40295_c;
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/villager.png");

   public RenderTaintVillager() {
      super(new ModelVillager(0.0F), 0.5F);
      this.field_40295_c = (ModelVillager)this.mainModel;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }

   protected int func_40293_a(EntityTaintVillager par1EntityVillager, int par2, float par3) {
      return -1;
   }

   public void renderVillager(EntityTaintVillager par1EntityVillager, double par2, double par4, double par6, float par8, float par9) {
      super.doRender(par1EntityVillager, par2, par4, par6, par8, par9);
   }

   protected void func_40290_a(EntityTaintVillager par1EntityVillager, double par2, double par4, double par6) {
   }

   protected void func_40291_a(EntityTaintVillager par1EntityVillager, float par2) {
      super.renderEquippedItems(par1EntityVillager, par2);
   }

   protected void func_40292_b(EntityTaintVillager par1EntityVillager, float par2) {
      float var3 = 0.9375F;
      this.shadowSize = 0.5F;
      GL11.glScalef(var3, var3, var3);
   }

   protected void passSpecialRender(EntityLivingBase par1EntityLiving, double par2, double par4, double par6) {
      this.func_40290_a((EntityTaintVillager)par1EntityLiving, par2, par4, par6);
   }

   protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
      this.func_40292_b((EntityTaintVillager)par1EntityLiving, par2);
   }

   protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return this.func_40293_a((EntityTaintVillager)par1EntityLiving, par2, par3);
   }

   protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2) {
      this.func_40291_a((EntityTaintVillager)par1EntityLiving, par2);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderVillager((EntityTaintVillager)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderVillager((EntityTaintVillager)par1Entity, par2, par4, par6, par8, par9);
   }
}
