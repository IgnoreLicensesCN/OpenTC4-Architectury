package thaumcraft.common.lib.crafting;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;

import static com.linearity.opentc4.SomeRecipeItemMatchers.*;

//TODO:Impl with current API
public class ArcaneWandRecipe /*implements IArcaneRecipe*/ {
    public static final ShapedArcaneRecipe INSTANCE = new ShapedArcaneRecipe(
            "SCEPTRE",
            arr -> {
                ItemStack centerStack = arr[4];
                int capCount = 0;
                //TODO:Finish

            },arr -> {

    },
            new RecipeItemMatcher[]{
                    EMPTY_MATCHER,EMPTY_MATCHER,WAND_CAP_MATCHER,
                    EMPTY_MATCHER,WAND_ROD_MATCHER,EMPTY_MATCHER,
                    WAND_CAP_MATCHER,EMPTY_MATCHER,EMPTY_MATCHER
            },3,3,SCEPTRE_MATCHER
    ){
        @Override
        public boolean matches(Container inv, Level world, Player player) {
            if (super.matches(inv, world, player)){
                return resultGenerator.apply(getInputItemStacks(inv)).isEmpty();
            }
            return false;
        }
    };
//   private static final int MAX_CRAFT_GRID_WIDTH = 3;
//   private static final int MAX_CRAFT_GRID_HEIGHT = 3;
//   private boolean mirrored = true;
//
//   public ItemStack getCraftingResult(Container inv) {
//      ItemStack out = null;
//      String bc = null;
//      String br = null;
//      int cc = 0;
//      int cr = 0;
//      ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
//      ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
//      ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
//      if (ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null) {
//         if (cap1 != null && cap2 != null && rod != null && this.checkItemEquals(cap1, cap2)) {
//            for(WandCap wc : WandCap.caps.values()) {
//               if (this.checkItemEquals(cap1, wc.getItem())) {
//                  bc = wc.getTag();
//                  cc = wc.getCraftCost();
//                  break;
//               }
//            }
//
//            for(WandRod wr : WandRod.rods.values()) {
//               if (this.checkItemEquals(rod, wr.getItem())) {
//                  br = wr.getTag();
//                  cr = wr.getCraftCost();
//                  break;
//               }
//            }
//
//            if (bc != null && br != null && (!br.equals("wood") || !bc.equals("iron"))) {
//               int cost = cc * cr;
//               out = new ItemStack(ConfigItems.WandCastingItem, 1, cost);
//               ((WandCastingItem)out.getItem()).setCap(out, WandCap.caps.get(bc));
//               ((WandCastingItem)out.getItem()).setRod(out, WandRod.rods.get(br));
//            }
//         }
//
//         return out;
//      } else {
//         return null;
//      }
//   }
//
//   public AspectList getAspects(Container inv) {
//      AspectList al = new AspectList();
//      int cc = -1;
//      int cr = -1;
//      ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
//      ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
//      ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
//      if (ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null) {
//         if (cap1 != null && cap2 != null && rod != null && this.checkItemEquals(cap1, cap2)) {
//            for(WandCap wc : WandCap.caps.values()) {
//               if (this.checkItemEquals(cap1, wc.getItem())) {
//                  cc = wc.getCraftCost();
//                  break;
//               }
//            }
//
//            for(WandRod wr : WandRod.rods.values()) {
//               if (this.checkItemEquals(rod, wr.getItem())) {
//                  cr = wr.getCraftCost();
//                  break;
//               }
//            }
//
//            if (cc >= 0 && cr >= 0) {
//               int cost = cc * cr;
//
//               for(Aspect as : Aspect.getPrimalAspects()) {
//                  al.add(as, cost);
//               }
//            }
//         }
//
//         return al;
//      } else {
//         return al;
//      }
//   }
//
//   public ItemStack getRecipeOutput() {
//      return null;
//   }
//
//   public boolean matches(Container inv, Level world, Player player) {
//      ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
//      ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
//      ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);
//      return ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 0) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 2) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 1) == null && ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 2) == null && this.checkMatch(cap1, cap2, rod, player);
//   }
//
//   private boolean checkMatch(ItemStack cap1, ItemStack cap2, ItemStack rod, Player player) {
//      boolean bc = false;
//      boolean br = false;
//      if (cap1 != null && cap2 != null && rod != null && this.checkItemEquals(cap1, cap2)) {
//         for(WandCap wc : WandCap.caps.values()) {
//            if (this.checkItemEquals(cap1, wc.getItem()) && ThaumcraftApiHelper.isResearchComplete(player.getName().getString(), wc.getResearch())) {
//               bc = true;
//               break;
//            }
//         }
//
//         for(WandRod wr : WandRod.rods.values()) {
//            if (this.checkItemEquals(rod, wr.getItem()) && ThaumcraftApiHelper.isResearchComplete(player.getName().getString(), wr.getResearch())) {
//               br = true;
//               break;
//            }
//         }
//      }
//
//      return br && bc;
//   }
//
//   private boolean checkItemEquals(ItemStack target, ItemStack input) {
//      if ((input != null || target == null) && (input == null || target != null)) {
//         return target.getItem() == input.getItem() && (!target.hasTagCompound() || ItemStack.areItemStackTagsEqual(target, input)) && (ignoresDamage(target) || target.getDamageValue() == input.getDamageValue());
//      } else {
//         return false;
//      }
//   }
//
//   public int getRecipeSize() {
//      return 9;
//   }
//
//   public AspectList getAspects() {
//      return null;
//   }
//
//   public String getResearch() {
//      return "";
//   }
}
