package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.ContainerPech;
import thaumcraft.common.entities.monster.EntityPech;

@SideOnly(Side.CLIENT)
public class GuiPech extends GuiContainer {
   EntityPech pech;

   public GuiPech(InventoryPlayer par1InventoryPlayer, World world, EntityPech pech) {
      super(new ContainerPech(par1InventoryPlayer, world, pech));
      this.xSize = 175;
      this.ySize = 232;
      this.pech = pech;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      UtilsFX.bindTexture("textures/gui/gui_pech.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = (this.width - this.xSize) / 2;
      int var6 = (this.height - this.ySize) / 2;
      GL11.glEnable(GL11.GL_BLEND);
      this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
      if (this.pech.isValued(this.inventorySlots.getSlot(0).getStack()) && this.inventorySlots.getSlot(0).getStack() != null && this.inventorySlots.getSlot(1).getStack() == null && this.inventorySlots.getSlot(2).getStack() == null && this.inventorySlots.getSlot(3).getStack() == null && this.inventorySlots.getSlot(4).getStack() == null) {
         this.drawTexturedModalRect(var5 + 67, var6 + 24, 176, 0, 25, 25);
      }

      GL11.glDisable(GL11.GL_BLEND);
   }

   protected void mouseClicked(int mx, int my, int par3) {
      super.mouseClicked(mx, my, par3);
      int gx = (this.width - this.xSize) / 2;
      int gy = (this.height - this.ySize) / 2;
      int var7 = mx - (gx + 67);
      int var8 = my - (gy + 24);
      if (var7 >= 0 && var8 >= 0 && var7 < 25 && var8 < 25 && this.pech.isValued(this.inventorySlots.getSlot(0).getStack()) && this.inventorySlots.getSlot(0).getStack() != null && this.inventorySlots.getSlot(1).getStack() == null && this.inventorySlots.getSlot(2).getStack() == null && this.inventorySlots.getSlot(3).getStack() == null && this.inventorySlots.getSlot(4).getStack() == null) {
         this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 0);
         this.playButton();
      }
   }

   private void playButton() {
      this.mc.renderViewEntity.worldObj.playSound(this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY, this.mc.renderViewEntity.posZ, "thaumcraft:pech_dice", 0.5F, 0.95F + this.mc.renderViewEntity.worldObj.rand.nextFloat() * 0.1F, false);
   }
}
