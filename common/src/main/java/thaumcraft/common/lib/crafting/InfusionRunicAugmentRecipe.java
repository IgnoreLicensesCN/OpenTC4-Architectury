package thaumcraft.common.lib.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraft.init.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.events.EventHandlerRunic;

import java.util.ArrayList;

import static com.linearity.opentc4.utils.vanilla1710.Vanilla1710Utils.ignoresDamage;
import static com.linearity.opentc4.utils.vanilla1710.Vanilla1710Utils.setIgnoresDamage;

//TODO:Impl with current API
public class InfusionRunicAugmentRecipe /*extends InfusionRecipe*/ {
   private ItemStack[] components;

   public InfusionRunicAugmentRecipe() {
      super("RUNICAUGMENTATION", null, 0, null, null, new ItemStack[]{new ItemStack(Items.diamond), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)});
   }

   public InfusionRunicAugmentRecipe(ItemStack in) {
      super("RUNICAUGMENTATION", null, 0, null, in, new ItemStack[]{new ItemStack(Items.diamond), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)});
      this.components = new ItemStack[]{new ItemStack(Items.diamond), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)};
      int fc = EventHandlerRunic.getFinalCharge(in);
      if (fc > 0) {
         ArrayList<ItemStack> com = new ArrayList<>();
         com.add(new ItemStack(Items.diamond));
         com.add(new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1));
         int c = 0;

         while(c < fc) {
            ++c;
            com.add(new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1));
         }

         this.components = com.toArray(this.components);
      }

   }

   public boolean matches(ArrayList<ItemStack> input, ItemStack central, Level world, Player player) {
      if (!this.research.isEmpty() && !ThaumcraftApiHelper.isResearchComplete(player.getName().getString(), this.research)) {
         return false;
      } else if (!(central.getItem() instanceof IRunicArmor)) {
         return false;
      } else {
         ItemStack i2 = null;
         ArrayList<ItemStack> ii = new ArrayList<>();

         for(ItemStack is : input) {
            ii.add(is.copy());
         }

         for(ItemStack comp : this.getComponents(central)) {
            boolean b = false;

            for(int a = 0; a < ii.size(); ++a) {
               i2 = ii.get(a).copy();
               if (ignoresDamage(comp)) {
                  setIgnoresDamage(i2,true);
               }

               if (areItemStacksEqual(i2, comp, true)) {
                  ii.remove(a);
                  b = true;
                  break;
               }
            }

            if (!b) {
               return false;
            }
         }

         return ii.isEmpty();
      }
   }

   public Object getRecipeOutput(ItemStack input) {
      if (input == null) {
         return null;
      } else {
         ItemStack out = input.copy();
         int base = EventHandlerRunic.getHardening(input) + 1;
         out.setTagInfo("RS.HARDEN", new NBTTagByte((byte)base));
         return out;
      }
   }

   public AspectList getAspects(ItemStack input) {
      AspectList out = new AspectList();
      int vis = (int)((double)32.0F * Math.pow(2.0F, EventHandlerRunic.getFinalCharge(input)));
      if (vis > 0) {
         out.add(Aspect.ARMOR, vis / 2);
         out.add(Aspect.MAGIC, vis / 2);
         out.add(Aspect.ENERGY, vis);
      }

      return out;
   }

   public int getInstability(ItemStack input) {
      int i = 5 + EventHandlerRunic.getFinalCharge(input) / 2;
      return i;
   }

   public ItemStack[] getComponents(ItemStack input) {
      ArrayList<ItemStack> com = new ArrayList<>();
      com.add(new ItemStack(Items.diamond));
      com.add(new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1));
      int fc = EventHandlerRunic.getFinalCharge(input);
      if (fc > 0) {
         for(int c = 0; c < fc; ++c) {
            com.add(new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1));
         }
      }

      return com.toArray(new ItemStack[0]);
   }

   public ItemStack[] getComponents() {
      return this.components;
   }
}
