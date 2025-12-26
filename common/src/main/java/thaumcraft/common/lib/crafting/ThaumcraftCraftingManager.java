package thaumcraft.common.lib.crafting;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import tc4tweak.modules.findRecipes.FindRecipes;
import tc4tweak.modules.objectTag.GetObjectTags;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.api.expands.aspects.item.ItemAspectBonusTagsCalculator;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.linearity.opentc4.OpenTC4.platformUtils;

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
        return recipe == null ? new AspectList() :
                recipe.getAspects() == null
                        ? recipe.getAspects(awb)
                        : recipe.getAspects();
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
        item = itemstack.getItem();

        AspectList tmp = ThaumcraftApi.objectTags.get(item);
        if (tmp == null) {
            tmp = generateTags(item);
        }

        //TODO:Separate to wand additional aspects to API
        if (itemstack.getItem() instanceof WandCastingItem wand) {
            if (tmp == null) {
                tmp = new AspectList();
            }
            var totalAvgAspects = 0;
            for (var componentItem : wand.getWandComponents(itemstack)) {
                var craftCostTotalAspect = 0;
                var aspectCount = 0;
                if (componentItem instanceof ICraftingCostAspectOwner costAspectOwner) {
                    var aspectMap = costAspectOwner.getCraftingCostAspect();
                    for (var aspectValue : aspectMap.values()) {
                        craftCostTotalAspect += aspectValue;
                        aspectCount += 1;
                    }
                }
                totalAvgAspects += craftCostTotalAspect / aspectCount;
            }

            tmp.mergeWithHighest(Aspect.MAGIC, (totalAvgAspects) / 2);
            tmp.mergeWithHighest(Aspect.TOOL, (totalAvgAspects) / 3);
        }

        //TODO:Potion effect aspects to API
        if (item instanceof PotionItem potionItem) {
            tmp.mergeWithHighest(Aspect.WATER, 1);
            List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemstack);
            if (!effects.isEmpty()) {
                if (potionItem instanceof ThrowablePotionItem) {
                    tmp.mergeWithHighest(Aspect.ENTROPY, 2);
                }

                for (MobEffectInstance var6 : effects) {
                    tmp.mergeWithHighest(Aspect.MAGIC, (var6.getAmplifier() + 1) * 2);
                    MobEffect effect = var6.getEffect();
                    if (effect == MobEffects.BLINDNESS) {
                        tmp.mergeWithHighest(Aspect.DARKNESS, (var6.getAmplifier() + 1) * 3);
                    } else if (effect == MobEffects.CONFUSION) { // Potion.confusion
                        tmp.mergeWithHighest(Aspect.ELDRITCH, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DAMAGE_BOOST) { // Potion.damageBoost
                        tmp.mergeWithHighest(Aspect.WEAPON, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DIG_SLOWDOWN) { // Potion.digSlowdown
                        tmp.mergeWithHighest(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DIG_SPEED) { // Potion.digSpeed
                        tmp.mergeWithHighest(Aspect.TOOL, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.FIRE_RESISTANCE) { // Potion.fireResistance
                        tmp.mergeWithHighest(Aspect.ARMOR, var6.getAmplifier() + 1);
                        tmp.mergeWithHighest(Aspect.FIRE, (var6.getAmplifier() + 1) * 2);

                    } else if (effect == MobEffects.HARM) { // Potion.harm
                        tmp.mergeWithHighest(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.HEAL) { // Potion.heal
                        tmp.mergeWithHighest(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.HUNGER) { // Potion.hunger
                        tmp.mergeWithHighest(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.INVISIBILITY) { // Potion.invisibility
                        tmp.mergeWithHighest(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.JUMP) { // Potion.jump
                        tmp.mergeWithHighest(Aspect.FLIGHT, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.MOVEMENT_SLOWDOWN) { // Potion.moveSlowdown
                        tmp.mergeWithHighest(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.MOVEMENT_SPEED) { // Potion.moveSpeed
                        tmp.mergeWithHighest(Aspect.MOTION, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.NIGHT_VISION) { // Potion.nightVision
                        tmp.mergeWithHighest(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.POISON) { // Potion.poison
                        tmp.mergeWithHighest(Aspect.POISON, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.REGENERATION) { // Potion.regeneration
                        tmp.mergeWithHighest(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.DAMAGE_RESISTANCE) { // Potion.resistance
                        tmp.mergeWithHighest(Aspect.ARMOR, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.WATER_BREATHING) { // Potion.waterBreathing
                        tmp.mergeWithHighest(Aspect.AIR, (var6.getAmplifier() + 1) * 3);

                    } else if (effect == MobEffects.WEAKNESS) { // Potion.weakness
                        tmp.mergeWithHighest(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
                    }
                }
            }
        }

        return capAspects(tmp, 64);
    }

    //set aspect no more than (amount)
    private static AspectList capAspects(AspectList sourcetags, int amount) {
        if (sourcetags == null) {
            return sourcetags;
        } else {
            AspectList out = new AspectList();

            for (Aspect aspect : sourcetags.getAspectTypes()) {
                out.mergeWithHighest(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
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
        } else if (history.contains((item.getDefaultInstance()))) {
            return null;
        } else {
            history.add(item.getDefaultInstance());
            if (history.size() < 100) {
                AspectList ret = generateTagsFromRecipes(item, history);
                ret = capAspects(ret, 64);
                ThaumcraftApi.registerObjectTag(new ItemStack(item), ret);
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
            int ss = cr.getRecipeOutput()
                    .getCount();
            AspectList ot2 = null;
            for (var cat:cr.catalyst.getAvailableItemStackSample()){
                ot2 = generateTags(cat.getItem(), history);
            }
            AspectList out = new AspectList();
            if (ot2 != null && ot2.size() > 0) {
                for (Aspect tt : ot2.getAspectTypes()) {
                    out.addAll(tt, ot2.getAmount(tt));
                }
            }

            for (Aspect tt : ot.getAspectTypes()) {
                int amt = (int) (Math.sqrt(ot.getAmount(tt)) / (double) ss);
                out.addAll(tt, amt);
            }

            for (Aspect as : out.getAspectTypes()) {
                if (out.getAmount(as) <= 0) {
                    out.reduceAndRemoveIfNegative(as);
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
        List<IArcaneRecipe> recipeList = ThaumcraftApi.getIArcaneRecipes();

        for (var arcaneRecipe : recipeList) {
            if (arcaneRecipe.getRecipeOutput() != null) {
                if (arcaneRecipe.getRecipeOutput()
                        .getItem() == item) {
                    ArrayList<ItemStack> ingredients = new ArrayList<>();
                    new AspectList();

                    try {
                        for (var stackArr:arcaneRecipe.getAllInputSample()){
                            for (var stack : stackArr){
                                if (stack == null || stack.isEmpty()) continue;
                                AspectList obj = generateTags(stack.getItem(), history);
                                if (obj != null && obj.size() > 0) {
                                    ItemStack is = stack.copy();
                                    is.setCount(1);
                                    ingredients.add(is);
                                    break;
                                }
                            }
                        }
                        AspectList ph = getAspectsFromIngredients(ingredients, arcaneRecipe.getRecipeOutput(), history);
                        if (arcaneRecipe.getAspects() != null) {
                            for (Aspect a : arcaneRecipe.getAspects()
                                    .getAspectTypes()) {
                                ph.addAll(
                                        a, (int) (Math.sqrt(arcaneRecipe.getAspects()
                                                .getAmount(a)) / (double) ((float) arcaneRecipe.getRecipeOutput()
                                                .getCount()))
                                );
                            }
                        }

                        for (Aspect as : ph.copy()
                                .getAspectTypes()) {
                            //remove <=0(why this will be executed?)
                            if (ph.getAmount(as) <= 0) {
                                ph.reduceAndRemoveIfNegative(as);
                            }
                        }

                        //pick minimum AspectList
                        if (ph.visSize() < value && ph.visSize() > 0) {
                            ret = ph;
                            value= ph.visSize() ;
                        }

//                            if (cval >= value) {
//                                ret = ph;
//                                value = cval;
//                            }//anazor drunk too much?
                    } catch (Exception e) {
                        OpenTC4.LOGGER.error(e);
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
                out.addAll(tt, ot2.getAmount(tt));
            }

            for (Aspect tt : ot.getAspectTypes()) {
                int amt = (int) (Math.sqrt(ot.getAmount(tt)) / (double) cr.getRecipeOutput()
                        .getCount());
                out.addAll(tt, amt);
            }

            for (Aspect as : out.getAspectTypes()) {
                if (out.getAmount(as) <= 0) {
                    out.reduceAndRemoveIfNegative(as);
                }
            }

            return out;
        }
    }

    private static AspectList generateTagsFromCraftingRecipes(Item item, List<ItemStack> history) {
        AtomicReference<AspectList> ret = new AtomicReference<>();
        AtomicInteger value = new AtomicInteger(Integer.MAX_VALUE);
        var server = platformUtils.getServer();


        server.getAllLevels()
                .forEach(level -> {
                    var manager = level.getRecipeManager();
                    manager.getAllRecipesFor(RecipeType.CRAFTING)
                            .forEach(recipe -> {
                                var resultStack = recipe.getResultItem(level.registryAccess());
                                if (!resultStack.is(item)) {return;}//of course recipe need to match item we expect for.
                                List<ItemStack> ingredients = new ArrayList<>();
                                NonNullList<Ingredient> ingredientsInternal = recipe.getIngredients();
                                for (var ingredientInner : ingredientsInternal) {
                                    for (ItemStack stack : ingredientInner.getItems()) {
                                        AspectList obj = generateTags(stack.getItem(), history);
                                        if (obj != null && obj.size() > 0) {
                                            ItemStack is = stack.copy();
                                            is.setCount(1);
                                            ingredients.add(is);
                                            break;
                                        }
                                    }
                                }
                                AspectList ph = getAspectsFromIngredients(ingredients, resultStack, history);

                                for (Aspect as : ph.copy()
                                        .getAspectTypes()) {
                                    if (ph.getAmount(as) <= 0) {
                                        ph.reduceAndRemoveIfNegative(as);
                                    }
                                }

                                if (ph.visSize() < value.get() && ph.visSize() > 0) {
                                    ret.set(ph);
                                    value.set(ph.visSize());
                                }
                            });
                });
        return ret.get();
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
                            out.addAll(as, (int) ((float) mid.getAmount(as) * 0.75F / (float) recipeOut.getCount()));
                        }
                    }

                    for (Aspect as : out.getAspectTypes()) {
                        if (out.getAmount(as) <= 0) {
                            out.reduceAndRemoveIfNegative(as);
                        }
                    }

                    return out;
                }

                ItemStack is = i$.next();
                obj = generateTags(is.getItem(), history);
                if (!is.getItem()
                        .hasCraftingRemainingItem()) {
                    break;
                }

                if (is.getItem()
                        .getCraftingRemainingItem() != is.getItem()) {
                    AspectList objC = generateTags(
                            is.getItem()
                                    .getCraftingRemainingItem(), history
                    );
                    if (objC != null && objC.size() > 0) {
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
            }

            if (obj != null) {
                for (Aspect as : obj.getAspectTypes()) {
                    if (as != null) {
                        mid.addAll(as, obj.getAmount(as));
                    }
                }
            }
        }
    }

    private static AspectList generateTagsFromRecipes(Item item, List<ItemStack> history) {
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
