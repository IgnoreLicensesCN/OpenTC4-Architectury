package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileJarFillableVoid;

public class ItemJarFilledRenderer implements IItemRenderer {
   RenderBlocks rb = new RenderBlocks();

   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return item != null && item.getItem() == ConfigItems.itemJarFilled;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return helper != ItemRendererHelper.EQUIPPED_BLOCK;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      if (item.getItem() == ConfigItems.itemJarFilled) {
         if (type == ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
         } else if (type == ItemRenderType.EQUIPPED && data[1] instanceof EntityPlayer) {
            GL11.glTranslatef(0.0F, 0.0F, -0.5F);
         }

         TileJarFillable tjf = new TileJarFillable();
         if (item.hasTagCompound()) {
            if (item.getItemDamage() == 3) {
               tjf = new TileJarFillableVoid();
            }

            AspectList aspects = ((ItemJarFilled)item.getItem()).getAspects(item);
            if (aspects != null && aspects.size() == 1) {
               tjf.amount = aspects.getAmount(aspects.getAspects()[0]);
               tjf.aspect = aspects.getAspects()[0];
            }

            String tf = item.stackTagCompound.getString("AspectFilter");
            if (tf != null) {
               tjf.aspectFilter = Aspect.getAspect(tf);
            }
         }

         tjf.facing = 5;
         tjf.blockType = ConfigBlocks.blockJar;
         tjf.blockMetadata = 0;
         TileEntityRendererDispatcher.instance.renderTileEntityAt(tjf, 0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         this.rb.useInventoryTint = true;
         this.rb.renderBlockAsItem(ConfigBlocks.blockJar, item.getItemDamage(), 1.0F);
         GL11.glPopMatrix();
         GL11.glEnable(32826);
      }

   }
}
