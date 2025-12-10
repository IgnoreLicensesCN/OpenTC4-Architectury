package thaumcraft.common.container;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class SlotCraftingArcaneWorkbench extends SlotCrafting {
   private final IInventory craftMatrix;
   private EntityPlayer thePlayer;
   private int amountCrafted;

   public SlotCraftingArcaneWorkbench(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
      super(par1EntityPlayer, par2IInventory, par3IInventory, par4, par5, par6);
      this.thePlayer = par1EntityPlayer;
      this.craftMatrix = par2IInventory;
   }

   public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par1ItemStack) {
      FMLCommonHandler.instance().firePlayerCraftingEvent(this.thePlayer, par1ItemStack, this.craftMatrix);
      this.onCrafting(par1ItemStack);
      AspectList aspects = ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this.craftMatrix, this.thePlayer);
      if (aspects.size() > 0 && this.craftMatrix.getStackInSlot(10) != null) {
         ItemWandCasting wand = (ItemWandCasting)this.craftMatrix.getStackInSlot(10).getItem();
         wand.consumeAllVisCrafting(this.craftMatrix.getStackInSlot(10), par1EntityPlayer, aspects, true);
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
