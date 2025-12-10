package thaumcraft.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class SlotGhostFluid extends SlotGhost {
   public SlotGhostFluid(IInventory par1iInventory, int par2, int par3, int par4) {
      super(par1iInventory, par2, par3, par4);
   }

   public int getSlotStackLimit() {
      return 1;
   }

   public boolean isItemValid(ItemStack par1ItemStack) {
      return FluidContainerRegistry.isContainer(par1ItemStack);
   }

   public boolean canTakeStack(Player par1Player) {
       return super.canTakeStack(par1Player);
   }
}
