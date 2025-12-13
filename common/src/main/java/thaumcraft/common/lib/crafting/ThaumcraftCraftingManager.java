package thaumcraft.common.lib.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingManager;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.jetbrains.annotations.Nullable;
import tc4tweak.modules.findRecipes.FindRecipes;
import tc4tweak.modules.objectTag.GetObjectTags;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.api.expands.aspects.item.ItemAspectBonusTagsCalculator;
import thaumcraft.common.items.wands.WandCastingItem;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThaumcraftCraftingManager {

    public static CrucibleRecipe findMatchingCrucibleRecipe(String username, AspectList aspects, ItemStack lastDrop) {
        int highest = Integer.MIN_VALUE;
        CrucibleRecipe resultRecipe = null;

        for (CrucibleRecipe recipe : ThaumcraftApi.getCrucibleRecipes()) {
            ItemStack temp = lastDrop.copy();
            temp.setCount(1);
            if (ResearchManager.isResearchComplete(username, recipe.key) && recipe.matches(aspects, temp)) {
                int result = recipe.aspects.size();
                if (result > highest) {
                    highest = result;
                    resultRecipe = recipe;
                }
            }
        }

        return resultRecipe;
    }

    public static ItemStack findMatchingArcaneRecipe(Container awb, Player player) {
        IArcaneRecipe recipe = FindRecipes.findArcaneRecipe(awb, player);
        return recipe == null ? null : recipe.getCraftingResult(awb);
//      int var2 = 0;
//      ItemStack var3 = null;
//      ItemStack var4 = null;
//
//      for(int var5 = 0; var5 < 9; ++var5) {
//         ItemStack var6 = awb.getStackInSlot(var5);
//         if (var6 != null) {
//            if (var2 == 0) {
//            }
//
//            if (var2 == 1) {
//            }
//
//            ++var2;
//         }
//      }
//
//      IArcaneRecipe var13 = null;
//
//      for(Object var11 : ThaumcraftApi.getCraftingRecipes()) {
//         if (var11 instanceof IArcaneRecipe && ((IArcaneRecipe)var11).matches(awb, player.level(), player)) {
//            var13 = (IArcaneRecipe)var11;
//            break;
//         }
//      }
//
//      return var13 == null ? null : var13.getCraftingResult(awb);
    }

    public static AspectList findMatchingArcaneRecipeAspects(Container awb, Player player) {
        IArcaneRecipe recipe = FindRecipes.findArcaneRecipe(awb, player);
        return recipe == null ? new AspectList() : recipe.getAspects() == null ? recipe.getAspects(awb) : recipe.getAspects();
//      int var2 = 0;
//      ItemStack var3 = null;
//      ItemStack var4 = null;
//
//      for(int var5 = 0; var5 < 9; ++var5) {
//         ItemStack var6 = awb.getStackInSlot(var5);
//         if (var6 != null) {
//            if (var2 == 0) {
//            }
//
//            if (var2 == 1) {
//            }
//
//            ++var2;
//         }
//      }
//
//      IArcaneRecipe var13 = null;
//
//      for(Object var11 : ThaumcraftApi.getCraftingRecipes()) {
//         if (var11 instanceof IArcaneRecipe && ((IArcaneRecipe)var11).matches(awb, player.level(), player)) {
//            var13 = (IArcaneRecipe)var11;
//            break;
//         }
//      }
//
//      return var13 == null ? new AspectList() : (var13.getAspects() != null ? var13.getAspects() : var13.getAspects(awb));
    }

    public static InfusionRecipe findMatchingInfusionRecipe(List<ItemStack> items, ItemStack input, Player player) {
        InfusionRecipe var13 = null;

        for (InfusionRecipe var11 : ThaumcraftApi.getInfusionRecipes()) {
            if (var11 instanceof InfusionRecipe && var11.matches(items, input, player.level(), player)) {
                var13 = var11;
                break;
            }
        }

        return var13;
    }

    public static InfusionEnchantmentRecipe findMatchingInfusionEnchantmentRecipe(List<ItemStack> items, ItemStack input, Player player) {
        InfusionEnchantmentRecipe var13 = null;

        for (InfusionEnchantmentRecipe var11 : ThaumcraftApi.getInfusionEnchantmentRecipes()) {
            if (var11 instanceof InfusionEnchantmentRecipe && var11.matches(items, input, player.level(), player)) {
                var13 = var11;
                break;
            }
        }

        return var13;
    }

    @Nullable
    public static AspectList getObjectTags(ItemStack itemstack) {
        return GetObjectTags.getObjectTags(itemstack);
    }

    //TODO:Create API to get aspect
    public static AspectList getObjectTagsOriginal(ItemStack itemstack) {
        Item item;
//      int meta;
        boolean ignoresDamageFlag;
        try {
            item = itemstack.getItem();
//         meta = itemstack.getDamageValue();
//         ignoresDamageFlag = ignoresDamage(itemstack);
        } catch (Exception var8) {
            return null;
        }

        AspectList tmp = ThaumcraftApi.objectTags.get(item);
        if (tmp == null) {
            tmp = generateTags(item);
        }

        if (itemstack.getItem() instanceof WandCastingItem wand) {
            if (tmp == null) {
                tmp = new AspectList();
            }

            tmp.merge(Aspect.MAGIC, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 2);
            tmp.merge(Aspect.TOOL, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 3);
        }

        if (item instanceof PotionItem potionItem) {
            tmp.merge(Aspect.WATER, 1);
            List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemstack);
            if (!effects.isEmpty()) {
                if (potionItem instanceof ThrowablePotionItem) {
                    tmp.merge(Aspect.ENTROPY, 2);
                }

                for (MobEffectInstance var6 : effects) {
                    tmp.merge(Aspect.MAGIC, (var6.getAmplifier() + 1) * 2);
                    MobEffect effect = var6.getEffect();
                    if (effect == MobEffects.BLINDNESS) {
                        tmp.merge(Aspect.DARKNESS, (var6.getAmplifier() + 1) * 3);
                    } else if (effect == MobEffects.CONFUSION) { // Potion.confusion
                        tmp.merge(Aspect.ELDRITCH, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DAMAGE_BOOST) { // Potion.damageBoost
                        tmp.merge(Aspect.WEAPON, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DIG_SLOWDOWN) { // Potion.digSlowdown
                        tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DIG_SPEED) { // Potion.digSpeed
                        tmp.merge(Aspect.TOOL, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.FIRE_RESISTANCE) { // Potion.fireResistance
                        tmp.merge(Aspect.ARMOR, var6.getAmplifier() + 1);
                        tmp.merge(Aspect.FIRE, (var6.getAmplifier() + 1) * 2);

                    } else if (effect == MobEffects.HARM) { // Potion.harm
                        tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.HEAL) { // Potion.heal
                        tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.HUNGER) { // Potion.hunger
                        tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.INVISIBILITY) { // Potion.invisibility
                        tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.JUMP) { // Potion.jump
                        tmp.merge(Aspect.FLIGHT, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.MOVEMENT_SLOWDOWN) { // Potion.moveSlowdown
                        tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.MOVEMENT_SPEED) { // Potion.moveSpeed
                        tmp.merge(Aspect.MOTION, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.NIGHT_VISION) { // Potion.nightVision
                        tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.POISON) { // Potion.poison
                        tmp.merge(Aspect.POISON, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.REGENERATION) { // Potion.regeneration
                        tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DAMAGE_RESISTANCE) { // Potion.resistance
                        tmp.merge(Aspect.ARMOR, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.WATER_BREATHING) { // Potion.waterBreathing
                        tmp.merge(Aspect.AIR, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.WEAKNESS) { // Potion.weakness
                        tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
                    }
                }
            }
        }

        return capAspects(tmp, 64);
    }

    private static AspectList capAspects(AspectList sourcetags, int amount) {
        if (sourcetags == null) {
            return sourcetags;
        } else {
            AspectList out = new AspectList();

            for (Aspect aspect : sourcetags.getAspectTypes()) {
                out.merge(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
            }

            return out;
        }
    }

    public static AspectList getBonusTags(ItemStack itemstack, AspectList sourcetags) {
        return ItemAspectBonusTagsCalculator.getBonusTags(itemstack, sourcetags);
    }

    public static AspectList generateTags(Item item) {
        return generateTags(item, new ArrayList<>());
    }

    public static AspectList generateTags(Item item, List<ItemStack> history) {


        if (ThaumcraftApi.exists(item)) {
            return getObjectTagsOriginal(new ItemStack(item));
        } else if (history.contains((item))) {
            return null;
        } else {
            history.add(item.getDefaultInstance());
            if (history.size() < 100) {
                AspectList ret = generateTagsFromRecipes(item, history);
                ret = capAspects(ret, 64);
                ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, tmeta), ret);
                return ret;
            } else {
                return null;
            }
        }
    }

    private static AspectList generateTagsFromCrucibleRecipes(Item item, List<ItemStack> history) {
        CrucibleRecipe cr = ThaumcraftApi.getCrucibleRecipe(new ItemStack(item, 1));
        if (cr != null) {
            AspectList ot = cr.aspects.copy();
            int ss = cr.getRecipeOutput().getCount();
            ItemStack cat = cr.catalyst.getAvailableItemStackSample().get(0);
//            if (cr.catalyst instanceof ItemStack) {
//                cat = (ItemStack) cr.catalyst;
//            } else if (cr.catalyst instanceof ArrayList && !((ArrayList) cr.catalyst).isEmpty()) {
//                cat = (ItemStack) ((ArrayList) cr.catalyst).get(0);
//            }

            AspectList ot2 = generateTags(cat.getItem(), history);
            AspectList out = new AspectList();
            if (ot2 != null && ot2.size() > 0) {
                for (Aspect tt : ot2.getAspectTypes()) {
                    out.add(tt, ot2.getAmount(tt));
                }
            }

            for (Aspect tt : ot.getAspectTypes()) {
                int amt = (int) (Math.sqrt(ot.getAmount(tt)) / (double) ss);
                out.add(tt, amt);
            }

            for (Aspect as : out.getAspectTypes()) {
                if (out.getAmount(as) <= 0) {
                    out.remove(as);
                }
            }

            return out;
        } else {
            return null;
        }
    }

    private static AspectList generateTagsFromArcaneRecipes(Item item, List<ItemStack> history) {
        AspectList ret = null;
        int value = 0;
        List recipeList = ThaumcraftApi.getCraftingRecipes();

        label173:
        for (Object o : recipeList) {
            if (o instanceof IArcaneRecipe recipe) {
                if (recipe.getRecipeOutput() != null) {
                    int idR = recipe.getRecipeOutput().getDamageValue() == 32767 ? 0 : recipe.getRecipeOutput().getDamageValue();
                    int idS = meta < 0 ? 0 : meta;
                    if (recipe.getRecipeOutput().getItem() == item && idR == idS) {
                        ArrayList<ItemStack> ingredients = new ArrayList<>();
                        new AspectList();
                        int cval = 0;

                        try {
                            if (o instanceof ShapedArcaneRecipe) {
                                int width = ((ShapedArcaneRecipe) o).width;
                                int height = ((ShapedArcaneRecipe) o).height;
                                Object[] items = ((ShapedArcaneRecipe) o).getInput();

                                for (int i = 0; i < width && i < 3; ++i) {
                                    for (int j = 0; j < height && j < 3; ++j) {
                                        if (items[i + j * width] != null) {
                                            if (items[i + j * width] instanceof ArrayList) {
                                                for (ItemStack it : (ArrayList<ItemStack>) items[i + j * width]) {
                                                    if (Utils.isEETransmutionItem(it.getItem())) {
                                                        continue label173;
                                                    }

                                                    AspectList obj = generateTags(it.getItem(), it.getDamageValue(), history);
                                                    if (obj != null && obj.size() > 0) {
                                                        ItemStack is = it.copy();
                                                        is.setCount(1);
                                                        ingredients.add(is);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                ItemStack it = (ItemStack) items[i + j * width];
                                                if (Utils.isEETransmutionItem(it.getItem())) {
                                                    continue label173;
                                                }

                                                ItemStack is = it.copy();
                                                is.setCount(1);
                                                ingredients.add(is);
                                            }
                                        }
                                    }
                                }
                            } else if (o instanceof ShapelessArcaneRecipe) {
                                ArrayList items = ((ShapelessArcaneRecipe) o).getInput();

                                for (int i = 0; i < items.size() && i < 9; ++i) {
                                    if (items.get(i) != null) {
                                        if (items.get(i) instanceof ArrayList) {
                                            for (ItemStack it : (ArrayList<ItemStack>) items.get(i)) {
                                                if (Utils.isEETransmutionItem(it.getItem())) {
                                                    continue label173;
                                                }

                                                AspectList obj = generateTags(it.getItem(), it.getDamageValue(), history);
                                                if (obj != null && obj.size() > 0) {
                                                    ItemStack is = it.copy();
                                                    is.setCount(1);
                                                    ingredients.add(is);
                                                    break;
                                                }
                                            }
                                        } else {
                                            ItemStack it = (ItemStack) items.get(i);
                                            if (Utils.isEETransmutionItem(it.getItem())) {
                                                continue label173;
                                            }

                                            ItemStack is = it.copy();
                                            is.setCount(1);
                                            ingredients.add(is);
                                        }
                                    }
                                }
                            }

                            AspectList ph = getAspectsFromIngredients(ingredients, recipe.getRecipeOutput(), history);
                            if (recipe.getAspects() != null) {
                                for (Aspect a : recipe.getAspects().getAspectTypes()) {
                                    ph.add(a, (int) (Math.sqrt(recipe.getAspects().getAmount(a)) / (double) ((float) recipe.getRecipeOutput().getCount())));
                                }
                            }

                            for (Aspect as : ph.copy().getAspectTypes()) {
                                if (ph.getAmount(as) <= 0) {
                                    ph.remove(as);
                                }
                            }

                            if (cval >= value) {
                                ret = ph;
                                value = cval;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return ret;
    }

    private static AspectList generateTagsFromInfusionRecipes(Item item, List<ItemStack> history) {
        InfusionRecipe cr = ThaumcraftApi.getInfusionRecipe(new ItemStack(item, 1));
        if (cr == null) {
            return null;
        } else {
            AspectList ot = cr.getAspects().copy();
            ArrayList<ItemStack> ingredients = new ArrayList<>();
            ItemStack is = cr.getRecipeInput().copy();
            is.setCount(1);
            ingredients.add(is);

            for (ItemStack cat : cr.getComponents()) {
                ItemStack is2 = cat.copy();
                is2.setCount(1);
                ingredients.add(is2);
            }

            AspectList out = new AspectList();
            AspectList ot2 = getAspectsFromIngredients(ingredients, cr.getRecipeOutput(), history);

            for (Aspect tt : ot2.getAspectTypes()) {
                out.add(tt, ot2.getAmount(tt));
            }

            for (Aspect tt : ot.getAspectTypes()) {
                int amt = (int) (Math.sqrt(ot.getAmount(tt)) / (double) cr.getRecipeOutput().getCount());
                out.add(tt, amt);
            }

            for (Aspect as : out.getAspectTypes()) {
                if (out.getAmount(as) <= 0) {
                    out.remove(as);
                }
            }

            return out;
        }
    }

    private static AspectList generateTagsFromCraftingRecipes(Item item, List<ItemStack> history) {
        AspectList ret = null;
        int value = Integer.MAX_VALUE;
        List recipeList = CraftingManager.getInstance().getRecipeList();

        label216:
        for (Object o : recipeList) {
            CraftingRecipe recipe = (CraftingRecipe) o;
            if (recipe != null && recipe.getRecipeOutput() != null && BuiltInRegistries.ITEM.getKey(recipe.getRecipeOutput().getItem()) > 0 && recipe.getRecipeOutput().getItem() != null) {
                int idR = recipe.getRecipeOutput().getDamageValue() == 32767 ? 0 : recipe.getRecipeOutput().getDamageValue();
                int idS = meta == 32767 ? 0 : meta;
                if (recipe.getRecipeOutput().getItem() == item && idR == idS) {
                    ArrayList<ItemStack> ingredients = new ArrayList<>();
                    new AspectList();
                    int cval = 0;

                    try {
                        if (o instanceof ShapedRecipes) {
                            int width = ((ShapedRecipes) o).recipeWidth;
                            int height = ((ShapedRecipes) o).recipeHeight;
                            ItemStack[] items = ((ShapedRecipes) o).recipeItems;

                            for (int i = 0; i < width && i < 3; ++i) {
                                for (int j = 0; j < height && j < 3; ++j) {
                                    if (items[i + j * width] != null) {
                                        if (Utils.isEETransmutionItem(items[i + j * width].getItem())) {
                                            continue label216;
                                        }

                                        ItemStack is = items[i + j * width].copy();
                                        is.setCount(1);
                                        ingredients.add(is);
                                    }
                                }
                            }
                        } else if (o instanceof ShapelessRecipes) {
                            List<ItemStack> items = ((ShapelessRecipes) o).recipeItems;

                            for (int i = 0; i < items.size() && i < 9; ++i) {
                                if (items.get(i) != null) {
                                    if (Utils.isEETransmutionItem(items.get(i).getItem())) {
                                        continue label216;
                                    }

                                    ItemStack is = items.get(i).copy();
                                    is.setCount(1);
                                    ingredients.add(is);
                                }
                            }
                        } else if (o instanceof ShapedOreRecipe) {
                            int size = ((ShapedOreRecipe) o).getRecipeSize();
                            Object[] items = ((ShapedOreRecipe) o).getInput();

                            for (int i = 0; i < size && i < 9; ++i) {
                                if (items[i] != null) {
                                    if (items[i] instanceof ArrayList) {
                                        for (ItemStack it : (ArrayList<ItemStack>) items[i]) {
                                            if (Utils.isEETransmutionItem(it.getItem())) {
                                                continue label216;
                                            }

                                            AspectList obj = generateTags(it.getItem(), it.getDamageValue(), history);
                                            if (obj != null && obj.size() > 0) {
                                                ItemStack is = it.copy();
                                                is.setCount(1);
                                                ingredients.add(is);
                                                break;
                                            }
                                        }
                                    } else {
                                        ItemStack it = (ItemStack) items[i];
                                        if (Utils.isEETransmutionItem(it.getItem())) {
                                            continue label216;
                                        }

                                        ItemStack is = it.copy();
                                        is.setCount(1);
                                        ingredients.add(is);
                                    }
                                }
                            }
                        } else if (o instanceof ShapelessOreRecipe) {
                            ArrayList items = ((ShapelessOreRecipe) o).getInput();

                            for (int i = 0; i < items.size() && i < 9; ++i) {
                                if (items.get(i) != null) {
                                    if (items.get(i) instanceof ArrayList) {
                                        for (ItemStack it : (ArrayList<ItemStack>) items.get(i)) {
                                            if (Utils.isEETransmutionItem(it.getItem())) {
                                                continue label216;
                                            }

                                            AspectList obj = generateTags(it.getItem(), it.getDamageValue(), history);
                                            if (obj != null && obj.size() > 0) {
                                                ItemStack is = it.copy();
                                                is.setCount(1);
                                                ingredients.add(is);
                                                break;
                                            }
                                        }
                                    } else {
                                        ItemStack it = (ItemStack) items.get(i);
                                        if (Utils.isEETransmutionItem(it.getItem())) {
                                            continue label216;
                                        }

                                        ItemStack is = it.copy();
                                        is.setCount(1);
                                        ingredients.add(is);
                                    }
                                }
                            }
                        }

                        AspectList ph = getAspectsFromIngredients(ingredients, recipe.getRecipeOutput(), history);

                        for (Aspect as : ph.copy().getAspectTypes()) {
                            if (ph.getAmount(as) <= 0) {
                                ph.remove(as);
                            }
                        }

                        if (ph.visSize() < value && ph.visSize() > 0) {
                            ret = ph;
                            value = ph.visSize();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return ret;
    }

    private static AspectList getAspectsFromIngredients(List<ItemStack> ingredients, ItemStack recipeOut, List<ItemStack> history) {
        AspectList out = new AspectList();
        AspectList mid = new AspectList();
        Iterator<ItemStack> i$ = ingredients.iterator();

        while (true) {
            AspectList obj;
            label57:
            while (true) {
                if (!i$.hasNext()) {
                    for (Aspect as : mid.getAspectTypes()) {
                        if (as != null) {
                            out.add(as, (int) ((float) mid.getAmount(as) * 0.75F / (float) recipeOut.getCount()));
                        }
                    }

                    for (Aspect as : out.getAspectTypes()) {
                        if (out.getAmount(as) <= 0) {
                            out.remove(as);
                        }
                    }

                    return out;
                }

                ItemStack is = i$.next();
                obj = generateTags(is.getItem(), history);
                if (is.getItem().getContainerItem() == null) {
                    break;
                }

                if (is.getItem().getContainerItem() != is.getItem()) {
                    AspectList objC = generateTags(is.getItem().getContainerItem(), 32767, history);
                    Aspect[] arr$ = objC.getAspectTypes();
                    int len$ = arr$.length;
                    int counter = 0;

                    while (true) {
                        if (counter >= len$) {
                            break label57;
                        }

                        Aspect as = arr$[counter];
                        out.reduce(as, objC.getAmount(as));
                        ++counter;
                    }
                }
            }

            if (obj != null) {
                for (Aspect as : obj.getAspectTypes()) {
                    if (as != null) {
                        mid.add(as, obj.getAmount(as));
                    }
                }
            }
        }
    }

    private static AspectList generateTagsFromRecipes(Item item, ArrayList<ItemStack> history) {
        AspectList ret;
        ret = generateTagsFromCrucibleRecipes(item, history);
        if (ret != null) {
            return ret;
        }

        ret = generateTagsFromArcaneRecipes(item, history);
        if (ret == null) {
            ret = generateTagsFromInfusionRecipes(item, history);
            if (ret == null) {
                ret = generateTagsFromCraftingRecipes(item, history);
            }
        }
        return ret;

    }
}
