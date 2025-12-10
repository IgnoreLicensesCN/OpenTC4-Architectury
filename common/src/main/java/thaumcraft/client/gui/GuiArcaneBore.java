package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerArcaneBore;
import thaumcraft.common.items.equipment.ItemElementalPickaxe;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;
import thaumcraft.common.tiles.TileArcaneBore;

@SideOnly(Side.CLIENT)
public class GuiArcaneBore extends GuiContainer {
   private TileArcaneBore bore;

   public GuiArcaneBore(InventoryPlayer par1InventoryPlayer, TileArcaneBore e) {
      super(new ContainerArcaneBore(par1InventoryPlayer, e));
      this.bore = e;
      this.xSize = 176;
      this.ySize = 141;
   }

   protected void drawGuiContainerForegroundLayer() {
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      UtilsFX.bindTexture("textures/gui/gui_arcanebore.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = (this.width - this.xSize) / 2;
      int var6 = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
      if (this.bore.getStackInSlot(1) != null && this.bore.getStackInSlot(1).getItemDamage() + 1 >= this.bore.getStackInSlot(1).getMaxDamage()) {
         this.drawTexturedModalRect(var5 + 74, var6 + 18, 184, 0, 16, 16);
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)(var5 + 112), (float)(var6 + 8), 505.0F);
      GL11.glScalef(0.5F, 0.5F, 0.0F);
      String text = "Width: " + (1 + (this.bore.area + this.bore.maxRadius) * 2);
      this.fontRendererObj.drawStringWithShadow(text, 0, 0, 16777215);
      text = "Speed: +" + this.bore.speed;
      this.fontRendererObj.drawStringWithShadow(text, 0, 10, 16777215);
      text = "Other properties:";
      this.fontRendererObj.drawStringWithShadow(text, 0, 24, 16777215);
      int base = 0;
      if (this.bore.getStackInSlot(1) != null && this.bore.getStackInSlot(1).getItem() instanceof ItemElementalPickaxe || this.bore.getStackInSlot(0) != null && this.bore.getStackInSlot(0).getItem() instanceof ItemFocusBasic && ((ItemFocusBasic)this.bore.getStackInSlot(0).getItem()).isUpgradedWith(this.bore.getStackInSlot(0), ItemFocusExcavation.dowsing)) {
         text = "Native Clusters";
         this.fontRendererObj.drawStringWithShadow(text, 4, 34 + base, 12632256);
         base += 9;
      }

      if (this.bore.fortune > 0) {
         text = "Fortune " + this.bore.fortune;
         this.fontRendererObj.drawStringWithShadow(text, 4, 34 + base, 15648330);
         base += 9;
      }

      if (this.bore.getStackInSlot(1) != null && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, this.bore.getStackInSlot(1)) > 0 || this.bore.getStackInSlot(0) != null && this.bore.getStackInSlot(0).getItem() instanceof ItemFocusBasic && ((ItemFocusBasic)this.bore.getStackInSlot(0).getItem()).isUpgradedWith(this.bore.getStackInSlot(0), FocusUpgradeType.silktouch)) {
         text = "Silk Touch";
         this.fontRendererObj.drawStringWithShadow(text, 4, 34 + base, 8421631);
         base += 9;
      }

      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }
}
