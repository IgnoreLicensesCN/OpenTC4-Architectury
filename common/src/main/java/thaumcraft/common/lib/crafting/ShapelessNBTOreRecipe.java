package thaumcraft.common.lib.crafting;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;

public class ShapelessNBTOreRecipe extends ShapelessOreRecipe {
   private ItemStack output;
   private ArrayList<Object/*ItemStack or ArrayList<ItemStack>*/> input;

   public ShapelessNBTOreRecipe(Block result, Object... recipe) {
      this(new ItemStack(result), recipe);
   }

   public ShapelessNBTOreRecipe(Item result, Object... recipe) {
      this(new ItemStack(result), recipe);
   }

   public ShapelessNBTOreRecipe(ItemStack result, Object... recipe) {
      super(result, recipe);
      this.output = null;
      this.input = new ArrayList<>();
      this.output = result.copy();

      for(Object in : recipe) {
         if (in instanceof ItemStack) {
            this.input.add(((ItemStack)in).copy());
         } else if (in instanceof Item) {
            this.input.add(new ItemStack((Item)in));
         } else if (in instanceof Block) {
            this.input.add(new ItemStack((Block)in));
         } else {
            if (!(in instanceof String)) {
               StringBuilder ret = new StringBuilder("Invalid shapeless ore recipe: ");

               for(Object tmp : recipe) {
                  ret.append(tmp).append(", ");
               }

               ret.append(this.output);
               throw new RuntimeException(ret.toString());
            }

            this.input.add(OreDictionary.getOres((String)in));
         }
      }

   }

   public int getRecipeSize() {
      return this.input.size();
   }

   public ItemStack getRecipeOutput() {
      return this.output;
   }

   public ItemStack getCraftingResult(InventoryCrafting var1) {
      return this.output.copy();
   }

   public boolean matches(InventoryCrafting var1, World world) {
      ArrayList<Object/*ItemStack or ArrayList<ItemStack>*/> required = new ArrayList<>(this.input);

      for(int x = 0; x < var1.getSizeInventory(); ++x) {
         ItemStack slot = var1.getStackInSlot(x);
         if (slot != null) {
            boolean inRecipe = false;
             /*ItemStack or ArrayList<ItemStack>*/

             for (Object o : required) {
                 boolean match = false;
                 Object/*ItemStack or ArrayList<ItemStack>*/ next = o;
                 if (next instanceof ItemStack) {
                     match = this.checkItemEquals((ItemStack) next, slot);
                 } else if (next instanceof ArrayList) {
                     for (ItemStack item : (ArrayList<ItemStack>) next) {
                         match = match || this.checkItemEquals(item, slot);
                     }
                 }

                 if (match) {
                     inRecipe = true;
                     required.remove(next);
                     break;
                 }
             }

            if (!inRecipe) {
               return false;
            }
         }
      }

      return required.isEmpty();
   }

   private boolean checkItemEquals(ItemStack target, ItemStack input) {
      return target.getItem() == input.getItem() && ItemStack.areItemStackTagsEqual(target, input) && (target.getItemDamage() == 32767 || target.getItemDamage() == input.getItemDamage());
   }

   public ArrayList<Object/*ItemStack or ArrayList<ItemStack>*/> getInput() {
      return this.input;
   }
}
