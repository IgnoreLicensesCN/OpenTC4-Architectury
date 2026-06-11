package thaumcraft.common.items.abstracts;

import com.linearity.opentc4.simpleutils.ObjectFloatPair;
import com.linearity.opentc4.simpleutils.SimplePair;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

//TODO Elemental pickaxe use this
public interface IDowsingTool {
    Map<Item, ObjectFloatPair<ItemStack>> dowsingToResultAndChance = new ConcurrentHashMap<>();
    Map<TagKey<Item>,ObjectFloatPair<ItemStack>> dowsingTagsToResultAndChance = new ConcurrentHashMap<>();

    static void addDowsingResult(Item item, ItemStack out, float chance) {
        dowsingToResultAndChance.put(item, new ObjectFloatPair<>(out, chance));
    }
    static void addDowsingResultForTag(TagKey<Item> tag, ItemStack out, float chance) {
        dowsingTagsToResultAndChance.put(tag, new ObjectFloatPair<>(out, chance));
    }

    default float getConvertChance(ItemStack stack,float baseChance) {

        return baseChance * (
                0.2F
                + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack) * 0.075F
        );
    }
    static @Nullable SimplePair<ItemStack /*notConverted*/ , List<ItemStack> /*converted*/> findDowsingResult(
            ItemStack dowsingStack,
            ItemStack toolStack,
            RandomSource rand
    ) {
        var item = dowsingStack.getItem();
        var toPair = dowsingToResultAndChance.get(item);
        if (toPair == null) {
            AtomicReference<ObjectFloatPair<ItemStack>> internalResult = new AtomicReference<>();

            if (dowsingStack.getTags().anyMatch(
                    tag -> {
                        var pairInternal = dowsingTagsToResultAndChance.get(tag);
                        if (pairInternal != null) {
                            internalResult.set(pairInternal);
                            return true;
                        }
                        return false;
                    }
            )){
                toPair = internalResult.get();
            }
        }

        if (toPair != null) {
            float toolDowsingChance = 0;
            int dowsingLevel = EnchantmentHelper.getItemEnchantmentLevel(ThaumcraftEnchantments.DOWSING, dowsingStack);
            if (dowsingLevel > 0){
                toolDowsingChance += toPair.value() * (
                        0.2F + dowsingLevel * 0.075F);
            }
            if (toolStack.getItem() instanceof IDowsingTool dowsingTool) {
                toolDowsingChance += dowsingTool.getConvertChance(toolStack,toPair.value());
            }
            if (toolDowsingChance <= 0){
                return null;
            }
            int amountRemaining = dowsingStack.getCount();
            float convertedAllParts = dowsingStack.getCount() * toolDowsingChance;
            int converted = (int) Math.floor(convertedAllParts);
            converted += rand.nextFloat() < (convertedAllParts-converted)?1:0;
            if (toolDowsingChance >= 1){
                amountRemaining = 0;
            } else {
                amountRemaining -= converted;
            }
            return new SimplePair<>(dowsingStack.copyWithCount(amountRemaining), InventoryUtils.stackOfAmount(toPair.obj(), converted));
        }
        return null;
    }

    default boolean canDowsing(ItemStack stack, @Nullable LivingEntity user){
        return true;
    }

}
