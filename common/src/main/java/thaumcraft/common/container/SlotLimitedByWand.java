package thaumcraft.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.items.wands.WandCastingItem;

public class SlotLimitedByWand extends Slot {
   int limit = 64;

   public SlotLimitedByWand(IInventory par2IInventory, int par3, int par4, int par5) {
      super(par2IInventory, par3, par4, par5);
   }

   public boolean isItemValid(ItemStack stack) {
      return stack != null && stack.getItem() != null && stack.getItem() instanceof WandCastingItem && !((WandCastingItem)stack.getItem()).isStaff(stack);
   }

   public int getSlotStackLimit() {
      return this.limit;
   }

   public void onPickupFromSlot(Player par1Player, ItemStack par2ItemStack) {
      super.onPickupFromSlot(par1Player, par2ItemStack);
   }
}
