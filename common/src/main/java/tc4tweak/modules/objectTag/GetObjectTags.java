package tc4tweak.modules.objectTag;


import net.minecraft.world.item.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class GetObjectTags {
    static final Logger log = LogManager.getLogger("GetObjectTags");
    private static final ObjectTagsCache cache = new ObjectTagsCache();


//    public static Stream<Entry<ItemStack, AspectList>> stream() {
//        if (cache.isEnabled())
//            return cache.getCache().entrySet().stream()
//                    .flatMap(e -> StreamSupport.stream(Spliterators.spliterator(iterate(e.getKey(), e.getValue()), e.getValue().size(), Spliterator.DISTINCT | Spliterator.NONNULL), false));
//        return Stream.empty();
//    }

    public static Stream<Entry<ItemStack, AspectList>> stream() {
        if (!cache.isEnabled()) return Stream.empty();

        return cache.getCache().entrySet().stream()
                .map(e -> Map.entry(e.getKey().getDefaultInstance(), e.getValue()));
    }

    @Nullable
    public static AspectList getObjectTags(@NotNull ItemStack itemstack) {
        Item item = itemstack.getItem();

        AspectList tmp = getBaseObjectTags(item);
        if (tmp == null) {
            // cache disabled, try find it as is
            tmp = ThaumcraftApi.objectTags.get(item);
        }
        if (tmp == null) {
            tmp = ThaumcraftCraftingManager.generateTags(item);
        }

        if (itemstack.getItem() instanceof WandCastingItem) {
            WandCastingItem wand = (WandCastingItem) itemstack.getItem();
            if (tmp == null) tmp = new AspectList();
            addWandTags(itemstack, tmp, wand);
        } else if (item instanceof PotionItem potionItem) {
            if (tmp == null) tmp = new AspectList();
            addPotionTags(itemstack, potionItem, tmp);
        }

        if (tmp == null) {
            return null;
        }
        for (Integer size : tmp.getAspects().values()) {
            if (size > 64) {
                return truncateAspectList(tmp);
            }
        }
        // no need to truncate - return as is
        return tmp;
    }

    private static AspectList truncateAspectList(AspectList tmp) {
        AspectList out = tmp.copy();
        out.replaceAll((a, n) -> Math.min(64, n));
        return out;
    }

    /**
     * Add wand related aspects
     */
    private static void addWandTags(ItemStack itemstack, AspectList tmp, WandCastingItem wand) {
        tmp.mergeWithHighest(Aspects.MAGIC, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 2);
        tmp.mergeWithHighest(Aspects.TOOL, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 3);
    }

    /**
     * Add potion related aspects
     */
    //TODO:API adds aspects for effects and item types
    @SuppressWarnings("unchecked")
    private static void addPotionTags(ItemStack itemstack, PotionItem item, AspectList tmp) {
        tmp.mergeWithHighest(Aspects.WATER, 1);
        List<MobEffectInstance> effects =  PotionUtils.getMobEffects(itemstack);
        if (!effects.isEmpty()) {
            if (item instanceof ThrowablePotionItem) {
                tmp.mergeWithHighest(Aspects.ENTROPY, 2);
            }

            for (MobEffectInstance effect : effects) {
                int amplifier = effect.getAmplifier();
                int potionID = effect.getPotionID();
                tmp.mergeWithHighest(Aspects.MAGIC, (amplifier + 1) * 2);
                if (potionID == Potion.blindness.id) {
                    tmp.mergeWithHighest(Aspects.DARKNESS, (amplifier + 1) * 3);
                } else if (potionID == Potion.confusion.id) {
                    tmp.mergeWithHighest(Aspects.ELDRITCH, (amplifier + 1) * 3);
                } else if (potionID == Potion.damageBoost.id) {
                    tmp.mergeWithHighest(Aspects.WEAPON, (amplifier + 1) * 3);
                } else if (potionID == Potion.digSlowdown.id) {
                    tmp.mergeWithHighest(Aspects.TRAP, (amplifier + 1) * 3);
                } else if (potionID == Potion.digSpeed.id) {
                    tmp.mergeWithHighest(Aspects.TOOL, (amplifier + 1) * 3);
                } else if (potionID == Potion.fireResistance.id) {
                    tmp.mergeWithHighest(Aspects.ARMOR, amplifier + 1);
                    tmp.mergeWithHighest(Aspects.FIRE, (amplifier + 1) * 2);
                } else if (potionID == Potion.harm.id) {
                    tmp.mergeWithHighest(Aspects.DEATH, (amplifier + 1) * 3);
                } else if (potionID == Potion.heal.id) {
                    tmp.mergeWithHighest(Aspects.HEAL, (amplifier + 1) * 3);
                } else if (potionID == Potion.hunger.id) {
                    tmp.mergeWithHighest(Aspects.DEATH, (amplifier + 1) * 3);
                } else if (potionID == Potion.invisibility.id) {
                    tmp.mergeWithHighest(Aspects.SENSES, (amplifier + 1) * 3);
                } else if (potionID == Potion.jump.id) {
                    tmp.mergeWithHighest(Aspects.FLIGHT, (amplifier + 1) * 3);
                } else if (potionID == Potion.moveSlowdown.id) {
                    tmp.mergeWithHighest(Aspects.TRAP, (amplifier + 1) * 3);
                } else if (potionID == Potion.moveSpeed.id) {
                    tmp.mergeWithHighest(Aspects.MOTION, (amplifier + 1) * 3);
                } else if (potionID == Potion.nightVision.id) {
                    tmp.mergeWithHighest(Aspects.SENSES, (amplifier + 1) * 3);
                } else if (potionID == Potion.poison.id) {
                    tmp.mergeWithHighest(Aspects.POISON, (amplifier + 1) * 3);
                } else if (potionID == Potion.regeneration.id) {
                    tmp.mergeWithHighest(Aspects.HEAL, (amplifier + 1) * 3);
                } else if (potionID == Potion.resistance.id) {
                    tmp.mergeWithHighest(Aspects.ARMOR, (amplifier + 1) * 3);
                } else if (potionID == Potion.waterBreathing.id) {
                    tmp.mergeWithHighest(Aspects.AIR, (amplifier + 1) * 3);
                } else if (potionID == Potion.weakness.id) {
                    tmp.mergeWithHighest(Aspects.DEATH, (amplifier + 1) * 3);
                }
            }
        }
    }

    /**
     * Get base object tags (no enchantment, potion, etc) from the cache. MAY return null if cache is temporarily disabled.
     *
     * @return null if cache disabled. non null if cache enabled. might be an empty aspect list if the generateTag failed.
     */
    @Nullable
    private static AspectList getBaseObjectTags(Item item) {
        ConcurrentMap<Item, AspectList> cache = GetObjectTags.cache.getCache();
        if (cache == null)
            return null;

        AspectList aspect = cache.get(item);
        if (aspect != null)
            return aspect;

        // 新版 generateTags 不再接收 meta，因为 meta 已废除
        aspect = ThaumcraftCraftingManager.generateTags(item);

        // 不返回 null，因为 null 表示 cache disabled
        return aspect == null ? new AspectList() : aspect;
    }
}
