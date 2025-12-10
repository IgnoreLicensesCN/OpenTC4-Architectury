package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelArcaneWorkbench;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@SideOnly(Side.CLIENT)
public class TileArcaneWorkbenchRenderer extends TileEntitySpecialRenderer {
   private ModelArcaneWorkbench tableModel = new ModelArcaneWorkbench();

   public void renderTileEntityAt(TileArcaneWorkbench table, double par2, double par4, double par6, float par8) {
      if (table == null){return;}
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/worktable.png");
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.tableModel.renderAll();
      GL11.glPopMatrix();
      if (table.getWorldObj() != null && table.getStackInSlot(10) != null && table.getStackInSlot(10).getItem() instanceof ItemWandCasting) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2 + 0.65F, (float)par4 + 1.0625F, (float)par6 + 0.25F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
         ItemStack is = table.getStackInSlot(10).copy();
         is.stackSize = 1;
         EntityItem entityitem = new EntityItem(table.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
         entityitem.hoverStart = 0.0F;
         RenderItem.renderInFrame = true;
         RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         RenderItem.renderInFrame = false;
         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      if (! (tileEntity instanceof TileArcaneWorkbench)) {return;}
      this.renderTileEntityAt((TileArcaneWorkbench)tileEntity, par2, par4, par6, par8);
   }
}
