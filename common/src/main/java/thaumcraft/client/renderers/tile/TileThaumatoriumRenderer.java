package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileThaumatorium;

@SideOnly(Side.CLIENT)
public class TileThaumatoriumRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation TM = new ResourceLocation("thaumcraft", "textures/models/thaumatorium.obj");
   EntityItem entityitem = null;

   public TileThaumatoriumRenderer() {
      this.model = AdvancedModelLoader.loadModel(TM);
   }

   public void renderTileEntityAt(TileThaumatorium tile, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/models/thaumatorium.png");
      int md = 0;
      if (tile.getWorldObj() != null) {
         switch (tile.facing.ordinal()) {
            case 2:
               GL11.glRotatef(270.0F, 0.0F, 0.0F, 1.0F);
               break;
            case 3:
               GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            case 4:
            default:
               break;
            case 5:
               GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         }
      }

      this.model.renderAll();
      GL11.glPopMatrix();
      float ticks = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + par8;
      if (tile != null && tile.getWorldObj() != null && tile.recipeHash != null && !tile.recipeHash.isEmpty()) {
         int stack = Minecraft.getMinecraft().renderViewEntity.ticksExisted / 40 % tile.recipeHash.size();
         CrucibleRecipe recipe = ThaumcraftApi.getCrucibleRecipeFromHash(tile.recipeHash.get(stack));
         if (recipe != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par2 + 0.5F + (float)tile.facing.offsetX / 1.99F, (float)par4 + 1.325F, (float)par6 + 0.5F + (float)tile.facing.offsetZ / 1.99F);
            switch (tile.facing.ordinal()) {
               case 2:
                  GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
               case 3:
               default:
                  break;
               case 4:
                  GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                  break;
               case 5:
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            GL11.glScaled(0.75F, 0.75F, 0.75F);
            ItemStack is = recipe.getRecipeOutput().copy();
            is.stackSize = 1;
            this.entityitem = new EntityItem(tile.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
            this.entityitem.hoverStart = 0.0F;
            RenderItem.renderInFrame = true;
            RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            RenderItem.renderInFrame = false;
            GL11.glPopMatrix();
         }
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TileThaumatorium)tileEntity, par2, par4, par6, par8);
   }
}
