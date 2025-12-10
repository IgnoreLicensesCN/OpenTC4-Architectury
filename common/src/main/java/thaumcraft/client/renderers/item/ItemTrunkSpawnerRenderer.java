package thaumcraft.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

public class ItemTrunkSpawnerRenderer implements IItemRenderer {
   private ModelChest chest = new ModelChest();

   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return true;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return true;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      Minecraft mc = Minecraft.getMinecraft();
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/trunk.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glScalef(1.0F, -1.0F, -1.0F);
      short var11 = 0;
      if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
         GL11.glTranslatef(-0.25F, -0.5F, -0.25F);
         if (type == ItemRenderType.EQUIPPED && type != ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
         }
      }

      GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      this.chest.renderAll();
      GL11.glPopMatrix();
   }
}
