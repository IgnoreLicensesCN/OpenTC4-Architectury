package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiMagicBox extends GuiContainer {
   private static final ResourceLocation field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
   private IInventory playerInventory;
   private IInventory lowerChestInventory;
   private int inventoryRows;

   public GuiMagicBox(IInventory par1IInventory, IInventory par2IInventory) {
      super(new ContainerChest(par1IInventory, par2IInventory));
      this.playerInventory = par1IInventory;
      this.lowerChestInventory = par2IInventory;
      this.allowUserInput = false;
      short short1 = 222;
      int i = short1 - 108;
      this.inventoryRows = par2IInventory.getSizeInventory() / 9;
      this.ySize = i + 54;
   }

   protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
      this.fontRendererObj.drawString(this.lowerChestInventory.hasCustomInventoryName() ? this.lowerChestInventory.getInventoryName() : I18n.format(this.lowerChestInventory.getInventoryName()), 8, 6, 4210752);
      this.fontRendererObj.drawString(this.playerInventory.hasCustomInventoryName() ? this.playerInventory.getInventoryName() : I18n.format(this.playerInventory.getInventoryName()), 8, this.ySize - 96 + 2, 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(field_147017_u);
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, 71);
      this.drawTexturedModalRect(k, l + 54 + 17, 0, 126, this.xSize, 96);
   }
}
