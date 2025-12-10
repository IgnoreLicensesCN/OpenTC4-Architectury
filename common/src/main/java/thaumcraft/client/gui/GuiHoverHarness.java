package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerHoverHarness;

@SideOnly(Side.CLIENT)
public class GuiHoverHarness extends GuiContainer {
   private int blockSlot;

   public GuiHoverHarness(InventoryPlayer par1InventoryPlayer, World world, int x, int y, int z) {
      super(new ContainerHoverHarness(par1InventoryPlayer, world, x, y, z));
      this.blockSlot = par1InventoryPlayer.currentItem;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      UtilsFX.bindTexture("textures/gui/guihoverharness.png");
      float t = this.zLevel;
      this.zLevel = 200.0F;
      GL11.glEnable(GL11.GL_BLEND);
      this.drawTexturedModalRect(8 + this.blockSlot * 18, 142, 240, 0, 16, 16);
      GL11.glDisable(GL11.GL_BLEND);
      this.zLevel = t;
   }

   protected boolean checkHotbarKeys(int par1) {
      return false;
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      if (this.mc.thePlayer.inventory.mainInventory[this.blockSlot] == null) {
         this.mc.thePlayer.closeScreen();
      }

      UtilsFX.bindTexture("textures/gui/guihoverharness.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = (this.width - this.xSize) / 2;
      int var6 = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
   }
}
