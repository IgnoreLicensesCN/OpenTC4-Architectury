package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelPech;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityPech;

@SideOnly(Side.CLIENT)
public class RenderPech extends RenderLiving {
   protected ModelPech modelMain;
   protected ModelPech modelOverlay;
   private static final ResourceLocation[] skin = new ResourceLocation[]{new ResourceLocation("thaumcraft", "textures/models/pech_forage.png"), new ResourceLocation("thaumcraft", "textures/models/pech_thaum.png"), new ResourceLocation("thaumcraft", "textures/models/pech_stalker.png")};

   public RenderPech(ModelPech par1ModelBiped, float par2) {
      super(par1ModelBiped, par2);
      this.modelMain = par1ModelBiped;
      this.modelOverlay = new ModelPech();
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return skin[((EntityPech)entity).getPechType()];
   }

   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      float f2 = 1.0F;
      GL11.glColor3f(f2, f2, f2);
      ItemStack itemstack = par1EntityLiving.getHeldItem();
      this.func_82420_a(par1EntityLiving, itemstack);
      double d3 = par4 - (double)par1EntityLiving.yOffset;
      if (par1EntityLiving.isSneaking()) {
         d3 -= 0.125F;
      }

      super.doRender(par1EntityLiving, par2, d3, par6, par8, par9);
   }

   protected ResourceLocation func_110856_a(EntityLiving par1EntityLiving) {
      return null;
   }

   protected void func_82420_a(EntityLiving par1EntityLiving, ItemStack par2ItemStack) {
   }

   protected void func_130005_c(EntityLiving par1EntityLiving, float par2) {
      float f1 = 1.0F;
      GL11.glColor3f(f1, f1, f1);
      super.renderEquippedItems(par1EntityLiving, par2);
      ItemStack itemstack = par1EntityLiving.getHeldItem();
      if (itemstack != null) {
         GL11.glPushMatrix();
         if (this.mainModel.isChild) {
            float f2 = 0.5F;
            GL11.glTranslatef(0.0F, 0.625F, 0.0F);
            GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
            GL11.glScalef(f2, f2, f2);
         }

         this.modelMain.RightArm.postRender(0.0625F);
         GL11.glTranslatef(-0.0625F, 0.3375F, 0.0625F);
         IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, ItemRenderType.EQUIPPED);
         boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack, ItemRendererHelper.BLOCK_3D);
         if (!(itemstack.getItem() instanceof ItemBlock) || !is3D && !RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
            if (itemstack.getItem() == Items.bow) {
               float f2 = 0.625F;
               GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
               GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
               GL11.glScalef(f2, -f2, f2);
               GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else if (itemstack.getItem().isFull3D()) {
               float f2 = 0.625F;
               if (itemstack.getItem() == ConfigItems.itemWandCasting) {
                  GL11.glTranslatef(0.0F, -0.125F, 0.0F);
               }

               if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
                  GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                  GL11.glTranslatef(0.0F, -0.125F, 0.0F);
               }

               this.func_82422_c();
               GL11.glScalef(f2, -f2, f2);
               GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
               float f2 = 0.375F;
               GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
               GL11.glScalef(f2, f2, f2);
               GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
               GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }
         } else {
            float f2 = 0.5F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            f2 *= 0.75F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-f2, -f2, f2);
         }

         this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 0);
         if (itemstack.getItem().requiresMultipleRenderPasses()) {
            for(int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++x) {
               this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, x);
            }
         }

         GL11.glPopMatrix();
      }

   }

   protected void func_82422_c() {
      GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
   }

   protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {
      this.func_130005_c((EntityLiving)par1EntityLivingBase, par2);
   }

   public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving((EntityLiving)par1EntityLivingBase, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
   }
}
