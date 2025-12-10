package thaumcraft.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.lwjgl.opengl.GL11;

public class ItemBowBoneRenderer implements IItemRenderer {
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   private RenderManager renderManager;
   private Minecraft mc;
   private TextureManager texturemanager;

   public ItemBowBoneRenderer() {
      this.renderManager = RenderManager.instance;
      this.mc = Minecraft.getMinecraft();
      this.texturemanager = this.mc.getTextureManager();
   }

   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return type == ItemRenderType.EQUIPPED;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return false;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      EntityLivingBase entity = (EntityLivingBase)data[1];
      ItemRenderer irInstance = this.mc.entityRenderer.itemRenderer;
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      float f2 = 2.6666667F;
      GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScalef(f2, f2, f2);
      GL11.glTranslatef(-0.25F, -0.1875F, 0.1875F);
      float f3 = 0.625F;
      GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
      GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f3, -f3, f3);
      GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      this.renderItem(entity, item, 0);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
   }

   private void renderItem(EntityLivingBase par1EntityLiving, ItemStack par2ItemStack, int par3) {
      IIcon icon = par1EntityLiving.getItemIcon(par2ItemStack, par3);
      if (icon != null) {
         this.texturemanager.bindTexture(this.texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
         Tessellator tessellator = Tessellator.instance;
         float f = icon.getMinU();
         float f1 = icon.getMaxU();
         float f2 = icon.getMinV();
         float f3 = icon.getMaxV();
         float f4 = 0.0F;
         float f5 = 0.3F;
         GL11.glEnable(32826);
         GL11.glTranslatef(-f4, -f5, 0.0F);
         float f6 = 1.5F;
         GL11.glScalef(f6, f6, f6);
         GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
         ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
         if (par2ItemStack.hasEffect(par3)) {
            GL11.glDepthFunc(514);
            GL11.glDisable(2896);
            this.texturemanager.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(768, 1);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
         }

         GL11.glDisable(32826);
      }
   }
}
