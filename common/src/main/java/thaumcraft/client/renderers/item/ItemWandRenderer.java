package thaumcraft.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.gear.ModelWand;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemWandRenderer implements IItemRenderer {
   private ModelWand model = new ModelWand();

   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return true;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return helper != ItemRendererHelper.BLOCK_3D;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      Minecraft mc = Minecraft.getMinecraft();
      if (item != null && item.getItem() instanceof ItemWandCasting) {
         ItemWandCasting wand = (ItemWandCasting)item.getItem();
         ItemStack focusStack = wand.getFocusItem(item);
         boolean staff = wand.isStaff(item);
         float pt = UtilsFX.getTimer(mc).renderPartialTicks;
         ItemRenderer ir = RenderManager.instance.itemRenderer;
         EntityLivingBase wielder = null;
         if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            wielder = (EntityLivingBase)data[1];
         }

         GL11.glPushMatrix();
         if (staff) {
            GL11.glTranslated(0.0F, 0.5F, 0.0F);
         }

         if (type != ItemRenderType.INVENTORY) {
            if (type == ItemRenderType.ENTITY) {
               if (staff) {
                  GL11.glTranslated(0.0F, 1.5F, 0.0F);
                  GL11.glScaled(0.9, 0.9, 0.9);
               } else {
                  GL11.glTranslated(0.0F, 1.0F, 0.0F);
               }
            } else {
               GL11.glTranslated(0.5F, 1.5F, 0.5F);
               if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                  GL11.glScaled(1.0F, 1.1, 1.0F);
               }
            }
         } else {
            if (staff) {
               GL11.glScaled(0.8, 0.8, 0.8);
            }

            GL11.glRotatef(66.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslated(0.0F, 0.6, 0.0F);
            if (staff) {
               GL11.glTranslated(-0.7, 0.6, 0.0F);
            }
         }

         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         if (wielder instanceof EntityPlayer && ((EntityPlayer) wielder).getItemInUse() != null) {
            float t = (float)((EntityPlayer)wielder).getItemInUseDuration() + pt;
            if (t > 3.0F) {
               t = 3.0F;
            }

            GL11.glTranslated(0.0F, 1.0F, 0.0F);
            if (type != ItemRenderType.EQUIPPED_FIRST_PERSON) {
               GL11.glRotatef(33.0F, 0.0F, 0.0F, 1.0F);
            } else {
               GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glRotatef(60.0F * (t / 3.0F), -1.0F, 0.0F, 0.0F);
            if (wand.animation != ItemFocusBasic.WandFocusAnimation.WAVE && (wand.getFocus(item) == null || wand.getFocus(item).getAnimation(focusStack) != ItemFocusBasic.WandFocusAnimation.WAVE)) {
               if (wand.getFocus(item) != null && wand.getFocus(item).getAnimation(focusStack) == ItemFocusBasic.WandFocusAnimation.CHARGE) {
                  float wave = MathHelper.sin(((float) ((EntityPlayer) wielder).getItemInUseDuration() + pt) / 0.8F);
                  GL11.glRotatef(wave, 0.0F, 0.0F, 1.0F);
                  wave = MathHelper.sin(((float) ((EntityPlayer) wielder).getItemInUseDuration() + pt) / 0.7F);
                  GL11.glRotatef(wave, 1.0F, 0.0F, 0.0F);
               }
            } else {
               float wave = MathHelper.sin(((float)((EntityPlayer)wielder).getItemInUseDuration() + pt) / 10.0F) * 10.0F;
               GL11.glRotatef(wave, 0.0F, 0.0F, 1.0F);
               wave = MathHelper.sin(((float)((EntityPlayer)wielder).getItemInUseDuration() + pt) / 15.0F) * 10.0F;
               GL11.glRotatef(wave, 1.0F, 0.0F, 0.0F);
            }

            GL11.glTranslated(0.0F, -1.0F, 0.0F);
         }

         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         this.model.render(item);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
      }
   }
}
