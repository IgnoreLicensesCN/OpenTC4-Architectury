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
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarNode;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileJar;
import thaumcraft.common.tiles.TileJarNode;

public class ItemJarNodeRenderer implements IItemRenderer {
   RenderBlocks rb = new RenderBlocks();

   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return item != null && item.getItem() == ConfigItems.itemJarNode;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return helper != ItemRendererHelper.EQUIPPED_BLOCK;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      if (item.getItem() == ConfigItems.itemJarNode) {
         if (type == ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
         } else if (type == ItemRenderType.EQUIPPED && data[1] instanceof EntityPlayer) {
            GL11.glTranslatef(0.0F, 0.0F, -0.5F);
         }

         TileJarNode tjf = new TileJarNode();
         if (item.hasTagCompound()) {
            AspectList aspects = ((ItemJarNode)item.getItem()).getAspects(item);
            if (aspects != null) {
               tjf.setAspects(aspects);
               tjf.setNodeType(((ItemJarNode)item.getItem()).getNodeType(item));
               tjf.setNodeModifier(((ItemJarNode)item.getItem()).getNodeModifier(item));
            }
         }

         tjf.blockType = ConfigBlocks.blockJar;
         tjf.blockMetadata = 2;
         GL11.glPushMatrix();
         TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileJar(), 0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslated(0.5F, 0.4, 0.5F);
         ItemNodeRenderer.renderItemNode(tjf);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         ItemNodeRenderer.renderItemNode(tjf);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         ItemNodeRenderer.renderItemNode(tjf);
         GL11.glPopMatrix();
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
