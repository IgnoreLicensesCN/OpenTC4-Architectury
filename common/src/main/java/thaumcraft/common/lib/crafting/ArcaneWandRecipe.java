package thaumcraft.common.lib.crafting;

import net.minecraft.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ArcaneWandRecipe implements IArcaneRecipe {
   private static final int MAX_CRAFT_GRID_WIDTH = 3;
   private static final int MAX_CRAFT_GRID_HEIGHT = 3;
   private boolean mirrored = true;

   public ItemStack getCraftingResult(IInventory inv) {
      ItemStack out = null;
      String bc = null;
      String br = null;
      int cc = 0;
      int cr = 0;
      ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
      ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
      ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
      if (ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null) {
         if (cap1 != null && cap2 != null && rod != null && this.checkItemEquals(cap1, cap2)) {
            for(WandCap wc : WandCap.caps.values()) {
               if (this.checkItemEquals(cap1, wc.getItem())) {
                  bc = wc.getTag();
                  cc = wc.getCraftCost();
                  break;
               }
            }

            for(WandRod wr : WandRod.rods.values()) {
               if (this.checkItemEquals(rod, wr.getItem())) {
                  br = wr.getTag();
                  cr = wr.getCraftCost();
                  break;
               }
            }

            if (bc != null && br != null && (!br.equals("wood") || !bc.equals("iron"))) {
               int cost = cc * cr;
               out = new ItemStack(ConfigItems.itemWandCasting, 1, cost);
               ((ItemWandCasting)out.getItem()).setCap(out, WandCap.caps.get(bc));
               ((ItemWandCasting)out.getItem()).setRod(out, WandRod.rods.get(br));
            }
         }

         return out;
      } else {
         return null;
      }
   }

   public AspectList getAspects(IInventory inv) {
      AspectList al = new AspectList();
      int cc = -1;
      int cr = -1;
      ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
      ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
      ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
      if (ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null) {
         if (cap1 != null && cap2 != null && rod != null && this.checkItemEquals(cap1, cap2)) {
            for(WandCap wc : WandCap.caps.values()) {
               if (this.checkItemEquals(cap1, wc.getItem())) {
                  cc = wc.getCraftCost();
                  break;
               }
            }

            for(WandRod wr : WandRod.rods.values()) {
               if (this.checkItemEquals(rod, wr.getItem())) {
                  cr = wr.getCraftCost();
                  break;
               }
            }

            if (cc >= 0 && cr >= 0) {
               int cost = cc * cr;

               for(Aspect as : Aspect.getPrimalAspects()) {
                  al.add(as, cost);
               }
            }
         }

         return al;
      } else {
         return al;
      }
   }

   public ItemStack getRecipeOutput() {
      return null;
   }

   public boolean matches(IInventory inv, World world, Player player) {
      ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
      ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
      ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
      return ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null && this.checkMatch(cap1, cap2, rod, player);
   }

   private boolean checkMatch(ItemStack cap1, ItemStack cap2, ItemStack rod, Player player) {
      boolean bc = false;
      boolean br = false;
      if (cap1 != null && cap2 != null && rod != null && this.checkItemEquals(cap1, cap2)) {
         for(WandCap wc : WandCap.caps.values()) {
            if (this.checkItemEquals(cap1, wc.getItem()) && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), wc.getResearch())) {
               bc = true;
               break;
            }
         }

         for(WandRod wr : WandRod.rods.values()) {
            if (this.checkItemEquals(rod, wr.getItem()) && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), wr.getResearch())) {
               br = true;
               break;
            }
         }
      }

      return br && bc;
   }

   private boolean checkItemEquals(ItemStack target, ItemStack input) {
      if ((input != null || target == null) && (input == null || target != null)) {
         return target.getItem() == input.getItem() && (!target.hasTagCompound() || ItemStack.areItemStackTagsEqual(target, input)) && (target.getItemDamage() == 32767 || target.getItemDamage() == input.getItemDamage());
      } else {
         return false;
      }
   }

   public int getRecipeSize() {
      return 9;
   }

   public AspectList getAspects() {
      return null;
   }

   public String getResearch() {
      return "";
   }
}
