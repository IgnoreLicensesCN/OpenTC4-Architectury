package thaumcraft.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileBanner;

public class ItemBannerRenderer implements IItemRenderer {
   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      if (item.hasTagCompound() && item.getItemDamage() == 8) {
         return item.stackTagCompound.getString("aspect") != null && item.stackTagCompound.getByte("color") >= 0;
      } else {
         return false;
      }
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return true;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      Minecraft mc = Minecraft.getMinecraft();
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      short var11 = 0;
      if (type != ItemRenderType.EQUIPPED && type != ItemRenderType.EQUIPPED_FIRST_PERSON) {
         GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
      } else {
         GL11.glTranslatef(1.0F, 1.0F, 1.0F);
      }

      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -1.0F, -0.5F);
      TileBanner tb = new TileBanner();
      tb.setColor(item.stackTagCompound.getByte("color"));
      if (item.stackTagCompound.getString("aspect") != null) {
         tb.setAspect(Aspect.getAspect(item.stackTagCompound.getString("aspect")));
      }

      TileEntityRendererDispatcher.instance.renderTileEntityAt(tb, 0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glPopMatrix();
   }
}
