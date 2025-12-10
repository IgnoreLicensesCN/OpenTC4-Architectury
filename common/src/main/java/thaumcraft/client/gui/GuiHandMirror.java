package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerHandMirror;

@SideOnly(Side.CLIENT)
public class GuiHandMirror extends GuiContainer {
   public GuiHandMirror(InventoryPlayer par1InventoryPlayer, World world, int x, int y, int z) {
      super(new ContainerHandMirror(par1InventoryPlayer, world, x, y, z));
   }

   protected void drawGuiContainerForegroundLayer() {
   }

   protected boolean checkHotbarKeys(int par1) {
      return false;
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      UtilsFX.bindTexture("textures/gui/guihandmirror.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = (this.width - this.xSize) / 2;
      int var6 = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
   }
}
