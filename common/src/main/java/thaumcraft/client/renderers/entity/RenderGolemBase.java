package thaumcraft.client.renderers.entity;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.entities.ModelGolem;
import thaumcraft.client.renderers.models.entities.ModelGolemAccessories;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class RenderGolemBase extends RenderLiving {
   ModelBase damage;
   ModelBase accessories;
   private static final ResourceLocation BUCKET = new ResourceLocation("thaumcraft", "textures/models/bucket.obj");
   IIcon icon = null;
   private IModelCustom model;
   private static final ResourceLocation clay = new ResourceLocation("thaumcraft", "textures/models/golem_clay.png");
   private static final ResourceLocation stone = new ResourceLocation("thaumcraft", "textures/models/golem_stone.png");
   private static final ResourceLocation wood = new ResourceLocation("thaumcraft", "textures/models/golem_wood.png");
   private static final ResourceLocation tallow = new ResourceLocation("thaumcraft", "textures/models/golem_tallow.png");
   private static final ResourceLocation iron = new ResourceLocation("thaumcraft", "textures/models/golem_iron.png");
   private static final ResourceLocation straw = new ResourceLocation("thaumcraft", "textures/models/golem_straw.png");
   private static final ResourceLocation flesh = new ResourceLocation("thaumcraft", "textures/models/golem_flesh.png");
   private static final ResourceLocation thaumium = new ResourceLocation("thaumcraft", "textures/models/golem_thaumium.png");

   public RenderGolemBase(ModelBase par1ModelBase) {
      super(par1ModelBase, 0.25F);
      if (par1ModelBase instanceof ModelGolem) {
         ModelGolem mg = new ModelGolem(false);
         mg.pass = 2;
         this.damage = mg;
      }

      this.accessories = new ModelGolemAccessories(0.0F, 30.0F);
      this.model = AdvancedModelLoader.loadModel(BUCKET);
   }

   public void render(EntityGolemBase e, double par2, double par4, double par6, float par8, float par9) {
      super.doRender(e, par2, par4, par6, par8, par9);
   }

   protected int shouldRenderPass(EntityLivingBase entity, int pass, float par3) {
      if (pass == 0) {
         String deco = ((EntityGolemBase)entity).getGolemDecoration();
         if (((EntityGolemBase)entity).getCore() > -1) {
            GL11.glPushMatrix();
            GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0875F, -0.96F, 0.15F + (deco.contains("P") ? 0.03F : 0.0F));
            GL11.glScaled(0.175, 0.175, 0.175);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            Tessellator tessellator = Tessellator.instance;
            IIcon icon = ConfigItems.itemGolemCore.getIconFromDamage(((EntityGolemBase)entity).getCore());
            float f1 = icon.getMaxU();
            float f2 = icon.getMinV();
            float f3 = icon.getMinU();
            float f4 = icon.getMaxV();
            this.renderManager.renderEngine.bindTexture(TextureMap.locationItemsTexture);
            ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.2F);
            GL11.glPopMatrix();
         }

         int upgrades = ((EntityGolemBase)entity).upgrades.length;
         float shift = 0.08F;
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);

         for(int a = 0; a < upgrades; ++a) {
            GL11.glPushMatrix();
            GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.05F - shift * (float)(upgrades - 1) / 2.0F + shift * (float)a, -1.106F, 0.099F);
            GL11.glScaled(0.1, 0.1, 0.1);
            Tessellator tessellator = Tessellator.instance;
            IIcon icon = ConfigItems.itemGolemUpgrade.getIconFromDamage(((EntityGolemBase)entity).getUpgrade(a));
            float f1 = icon.getMaxU();
            float f2 = icon.getMinV();
            float f3 = icon.getMinU();
            float f4 = icon.getMaxV();
            this.renderManager.renderEngine.bindTexture(TextureMap.locationItemsTexture);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(0.0F, 0.0F, 0.0F, f1, f4);
            tessellator.addVertexWithUV(1.0F, 0.0F, 0.0F, f3, f4);
            tessellator.addVertexWithUV(1.0F, 1.0F, 0.0F, f3, f2);
            tessellator.addVertexWithUV(0.0F, 1.0F, 0.0F, f1, f2);
            tessellator.draw();
            GL11.glPopMatrix();
         }

         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
      } else {
         if (pass == 1 && (!((EntityGolemBase) entity).getGolemDecoration().isEmpty() || ((EntityGolemBase)entity).advanced)) {
            UtilsFX.bindTexture("textures/models/golem_decoration.png");
            this.setRenderPassModel(this.accessories);
            return 1;
         }

         if (pass == 2 && ((EntityGolemBase)entity).getHealthPercentage() < 1.0F) {
            UtilsFX.bindTexture("textures/models/golem_damage.png");
            this.setRenderPassModel(this.damage);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F - ((EntityGolemBase)entity).getHealthPercentage());
            return 2;
         }
      }

      return -1;
   }

   protected void renderWithSway(EntityGolemBase e, float par2, float par3, float par4) {
      super.rotateCorpse(e, par2, par3, par4);
      if ((double)e.limbSwingAmount >= 0.01) {
         float var5 = 13.0F;
         float var6 = e.limbSwing - e.limbSwingAmount * (1.0F - par4) + 6.0F;
         float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
         GL11.glRotatef(6.5F * var7, 0.0F, 0.0F, 1.0F);
      }

   }

   protected void renderCarriedItems(EntityGolemBase e, float par2) {
      ItemStack var3 = e.getCarriedForDisplay();
      if (e.getCore() == 11) {
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         float fs = 0.66F;
         this.renderManager.renderEngine.bindTexture(TextureMap.locationItemsTexture);
         GL11.glRotatef(5.0F + 90.0F * ((ModelGolem)this.mainModel).golemRightArm.rotateAngleX / (float)Math.PI, 1.0F, 0.0F, 0.0F);
         GL11.glTranslatef(-0.26875F, 1.6F, -0.53F);
         GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
         GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
         GL11.glScalef(fs, -fs, fs);
         IIcon ic = Items.fishing_rod.func_94597_g();
         float f = ic.getMinU();
         float f1 = ic.getMaxU();
         float f2 = ic.getMinV();
         float f3 = ic.getMaxV();
         Tessellator.instance.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
         ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, ic.getIconWidth(), ic.getIconHeight(), 0.0625F);
         GL11.glScaled(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      if (var3 != null && e.deathTime == 0 && e.getCore() != 5) {
         GL11.glPushMatrix();
         GL11.glScaled(0.4, 0.4, 0.4);
         IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(var3, ItemRenderType.EQUIPPED);
         boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, var3, ItemRendererHelper.BLOCK_3D);
         if (!(var3.getItem() instanceof ItemBlock) || !is3D && !RenderBlocks.renderItemIn3d(Block.getBlockFromItem(var3.getItem()).getRenderType())) {
            if (var3.getItem() instanceof ItemJarFilled) {
               GL11.glTranslatef(0.0F, 2.5F, -1.0F);
               if (e.getCore() == 6) {
                  double s = (double)0.5F + (double)Math.min(64, e.getCarryLimit()) / (double)128.0F;
                  GL11.glScaled(s, s, s);
               }

               GL11.glTranslatef(-0.5F, 0.0F, -0.8F);
               GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(335.0F, 0.0F, 0.0F, -1.0F);
               GL11.glRotatef(50.0F, 0.0F, -1.0F, 0.0F);
            } else {
               GL11.glTranslatef(-0.5F, 2.5F, -1.25F);
               GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
               if (!var3.getItem().requiresMultipleRenderPasses()) {
                  GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                  GL11.glRotatef(335.0F, 0.0F, 0.0F, -1.0F);
                  GL11.glRotatef(50.0F, 0.0F, -1.0F, 0.0F);
               }
            }
         } else {
            GL11.glTranslatef(0.0F, 2.5F, -1.25F);
            GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         }

         if (var3.getItem().requiresMultipleRenderPasses()) {
            int renderPass = 0;

            do {
               IIcon icon = var3.getItem().getIcon(var3, renderPass);
               if (icon != null) {
                  Color color = new Color(var3.getItem().getColorFromItemStack(var3, renderPass));
                  GL11.glColor3ub((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
                  float f = icon.getMinU();
                  float f1 = icon.getMaxU();
                  float f2 = icon.getMinV();
                  float f3 = icon.getMaxV();
                  ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
                  GL11.glColor3f(1.0F, 1.0F, 1.0F);
               }

               ++renderPass;
            } while(renderPass < var3.getItem().getRenderPasses(var3.getItemDamage()));
         } else {
            int i = var3.getItem().getColorFromItemStack(var3, 0);
            float f7 = (float)(i >> 16 & 255) / 255.0F;
            float f8 = (float)(i >> 8 & 255) / 255.0F;
            float f6 = (float)(i & 255) / 255.0F;
            GL11.glColor4f(f7, f8, f6, 1.0F);
            this.renderManager.itemRenderer.renderItem(e, var3, 0);
         }

         GL11.glScaled(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      } else if (e.getCore() == 5) {
         GL11.glPushMatrix();
         GL11.glScaled(0.4, 0.4, 0.4);
         GL11.glTranslatef(0.0F, 3.0F, -1.1F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         UtilsFX.bindTexture("textures/models/bucket.png");
         this.model.renderPart("Bucket");
         if (e.getCarriedForDisplay() != null) {
            Fluid fluid = FluidRegistry.getFluid(Item.getIdFromItem(e.getCarriedForDisplay().getItem()));
            float max = (float)Math.max(e.getCarriedForDisplay().getItemDamage(), e.getFluidCarryLimit());
            float fill = (float)e.getCarriedForDisplay().getItemDamage() / max;
            if (fluid != null) {
               GL11.glTranslatef(0.0F, 0.0F, 0.2F + 0.8F * fill);
               GL11.glScaled(0.8, 0.8, 0.8);
               this.icon = fluid.getIcon();
               int q = 15728640 | fluid.getLuminosity() << 4;
               int b = Math.max(e.getBrightnessForRender(par2), q);
               UtilsFX.renderQuadCenteredFromIcon(true, this.icon, 1.0F, 1.0F, 1.0F, 1.0F, b, 771, 1.0F);
            }
         }

         GL11.glPopMatrix();
      }

   }

   protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2) {
      this.renderCarriedItems((EntityGolemBase)par1EntityLiving, par2);
   }

   protected void rotateCorpse(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      this.renderWithSway((EntityGolemBase)par1EntityLiving, par2, par3, par4);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.render((EntityGolemBase)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.render((EntityGolemBase)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      switch (((EntityGolemBase)entity).getGolemType()) {
         case STRAW:
            return straw;
         case WOOD:
            return wood;
         case CLAY:
            return clay;
         case STONE:
            return stone;
         case IRON:
            return iron;
         case TALLOW:
            return tallow;
         case FLESH:
            return flesh;
         case THAUMIUM:
            return thaumium;
         default:
            return AbstractClientPlayer.locationStevePng;
      }
   }
}
