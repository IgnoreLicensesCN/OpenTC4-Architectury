package thaumcraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.golems.ContainerTravelingTrunk;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;

public class GuiTravelingTrunk extends GuiContainer {
   private EntityPlayer theplayer;
   private EntityTravelingTrunk themob;
   private int inventoryRows;

   public GuiTravelingTrunk(EntityPlayer p, EntityTravelingTrunk m) {
      super(new ContainerTravelingTrunk(p.inventory, p.worldObj, m));
      this.theplayer = p;
      this.themob = m;
      this.inventoryRows = m.inventory.slotCount / 9;
      this.ySize = 200;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      GL11.glPushMatrix();
      GL11.glScaled(0.5F, 0.5F, 0.5F);
      this.fontRendererObj.drawString(this.themob.func_152113_b() + StatCollector.translateToLocal("entity.trunk.guiname"), 8, 4, 12624112);
      GL11.glPopMatrix();
   }

   protected void drawGuiContainerBackgroundLayer(float f, int ii, int jj) {
      if (this.themob.isDead) {
         this.mc.thePlayer.closeScreen();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/gui/guitrunkbase.png");
      GL11.glEnable(GL11.GL_BLEND);
      int j = (this.width - this.xSize) / 2;
      int k = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
      int hp = Math.round(this.themob.getHealth() / this.themob.getMaxHealth() * 39.0F);
      this.drawTexturedModalRect(j + 134, k + 2, 176, 16, hp, 6);
      if (this.themob.getUpgrade() == 1) {
         this.drawTexturedModalRect(j, k + 80, 0, 206, this.xSize, 27);
      }

      if (this.themob.getStay()) {
         this.drawTexturedModalRect(j + 112, k, 176, 0, 10, 10);
      }

      GL11.glDisable(GL11.GL_BLEND);
   }

   protected void mouseClicked(int i, int j, int k) {
      super.mouseClicked(i, j, k);
      int sx = (this.width - this.xSize) / 2;
      int sy = (this.height - this.ySize) / 2;
      int k1 = i - (sx + 112);
      int l1 = j - (sy);
      if (k1 >= 0 && l1 >= 0 && k1 < 10 && l1 <= 10) {
         this.themob.worldObj.playSound(this.themob.posX, this.themob.posY, this.themob.posZ, "random.click", 0.3F, 0.6F + (this.themob.getStay() ? 0.0F : 0.2F), false);
         if (this.themob.getStay()) {
            this.theplayer.addChatMessage(new ChatComponentTranslation("entity.trunk.move"));
         } else {
            this.theplayer.addChatMessage(new ChatComponentTranslation("entity.trunk.stay"));
         }

         this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 1);
      }

   }

   public boolean doesGuiPauseGame() {
       return super.doesGuiPauseGame();
   }

   public void onGuiClosed() {
      this.themob.setOpen(false);
      super.onGuiClosed();
   }
}
