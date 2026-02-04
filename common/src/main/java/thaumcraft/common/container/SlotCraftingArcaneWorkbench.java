package thaumcraft.common.container;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class SlotCraftingArcaneWorkbench extends SlotCrafting {
   private final IInventory craftMatrix;
   private Player thePlayer;
   private int amountCrafted;

   public SlotCraftingArcaneWorkbench(Player par1Player, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
      super(par1Player, par2IInventory, par3IInventory, par4, par5, par6);
      this.thePlayer = par1Player;
      this.craftMatrix = par2IInventory;
   }

   public void onPickupFromSlot(Player par1Player, ItemStack par1ItemStack) {
      FMLCommonHandler.instance().firePlayerCraftingEvent(this.thePlayer, par1ItemStack, this.craftMatrix);
      this.onCrafting(par1ItemStack);
      AspectList aspects = ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this.craftMatrix, this.thePlayer);
      if (aspects.size() > 0 && this.craftMatrix.getStackInSlot(10) != null) {
         WandCastingItem wand = (WandCastingItem)this.craftMatrix.getStackInSlot(10).getItem();
         wand.consumeAllCentiVisCrafting(this.craftMatrix.getStackInSlot(10), par1Player, aspects, true);
      }

      for(int var2 = 0; var2 < 9; ++var2) {
         ItemStack var3 = this.craftMatrix.getStackInSlot(var2);
         if (var3 != null) {
            this.craftMatrix.decrStackSize(var2, 1);
            if (var3.getItem().hasContainerItem(var3)) {
               ItemStack var4 = var3.getItem().getContainerItem(var3);
               if (var4 != null && var4.isItemStackDamageable() && var4.getItemDamage() > var4.getMaxDamage()) {
                  MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.thePlayer, var4));
               } else if (!var3.getItem().doesContainerItemLeaveCraftingGrid(var3) || !this.thePlayer.inventory.addItemStackToInventory(var4)) {
                  if (this.craftMatrix.getStackInSlot(var2) == null) {
                     this.craftMatrix.setInventorySlotContents(var2, var4);
                  } else {
                     this.thePlayer.dropPlayerItemWithRandomChoice(var4, false);
                  }
               }
            }
         }
      }

   }
}
