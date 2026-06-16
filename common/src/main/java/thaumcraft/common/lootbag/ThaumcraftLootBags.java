package thaumcraft.common.lootbag;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.ArrayCentiVisList;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.items.ThaumcraftItemInstances;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static thaumcraft.common.items.ThaumcraftItemInstances.*;

public class ThaumcraftLootBags {

    //should always return new stack
    public static final WeightedRandomCollection<Function<RandomSource, ItemStack>> LOOT_BAG_COMMON_DROPS = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<Function<RandomSource, ItemStack>> LOOT_BAG_UNCOMMON_DROPS = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<Function<RandomSource, ItemStack>> LOOT_BAG_RARE_DROPS = new WeightedRandomCollection<>();

    private static List<Potion> potions;
    public static void init() {
        potions = BuiltInRegistries.POTION.stream().filter(
                p -> (p != Potions.EMPTY && p != Potions.WATER)
                        && !(p.getEffects().isEmpty())
                        && BuiltInRegistries.POTION.getKey(p).getNamespace().equals("minecraft")
        ).collect(Collectors.toList());
        initCommonDrops();
        initUncommonDrops();
        initRareDrops();
    }

    public static void initCommonDrops() {
        LOOT_BAG_COMMON_DROPS.clear();
        LOOT_BAG_COMMON_DROPS.add((random) -> GOLD_COIN().getDefaultInstance(), 2500);
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.GOLD_INGOT.getDefaultInstance(), 100);
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.ENDER_PEARL.getDefaultInstance(), 100);
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.EMERALD.getDefaultInstance(), 25);
        LOOT_BAG_COMMON_DROPS.add((random) -> KNOWLEDGE_FRAGMENT().getDefaultInstance(), 25);
        LOOT_BAG_COMMON_DROPS.add((random) -> {
            var result = new ItemStack(Items.BOOK);
            EnchantmentHelper.enchantItem(
                    random,
                    result,
                    (int) (5.0F),
                    true
            );
            return result;
        }, 10);
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.DIAMOND.getDefaultInstance(), 10);
        LOOT_BAG_COMMON_DROPS.add((random) -> MUNDANE_AMULET().getDefaultInstance(), 10);
        LOOT_BAG_COMMON_DROPS.add((random) -> MUNDANE_BELT().getDefaultInstance(), 10);
        LOOT_BAG_COMMON_DROPS.add((random) -> MUNDANE_RING().getDefaultInstance(), 10);
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.EXPERIENCE_BOTTLE.getDefaultInstance(), 5);
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.GOLDEN_APPLE.getDefaultInstance(), 3);

        potions.forEach((potion) -> {
            LOOT_BAG_COMMON_DROPS.add(random -> {
                var stack = random.nextBoolean() ? new ItemStack(Items.POTION) : new ItemStack(Items.SPLASH_POTION);
                PotionUtils.setPotion(stack, potion);

                return stack;
            },potion.getEffects().stream().mapToInt(effectInstance -> effectInstance.getAmplifier()+1).sum());
        });
        LOOT_BAG_COMMON_DROPS.add((random) -> Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), 1);

        LOOT_BAG_COMMON_DROPS.sort();
    }

    public static void initUncommonDrops() {
        LOOT_BAG_UNCOMMON_DROPS.clear();
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> new ItemStack(GOLD_COIN(), 2), 2250);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.GOLD_INGOT.getDefaultInstance(), 100);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.ENDER_PEARL.getDefaultInstance(), 100);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.EMERALD.getDefaultInstance(), 75);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> genGear(1,random), 67);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.DIAMOND.getDefaultInstance(), 50);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> KNOWLEDGE_FRAGMENT().getDefaultInstance(), 25);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> {
            var result = new ItemStack(Items.BOOK);
            EnchantmentHelper.enchantItem(
                    random,
                    result,
                    (int) (5.0F + 1 * 0.75F * random.nextInt(18)),
                    true
            );
            return result;
        }, 10);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.EXPERIENCE_BOTTLE.getDefaultInstance(), 10);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.GOLDEN_APPLE.getDefaultInstance(), 6);
        LOOT_BAG_UNCOMMON_DROPS.add(ThaumcraftLootBags::getVisAmulet, 6);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> AIR_APPRENTICES_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> WATER_APPRENTICES_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> FIRE_APPRENTICES_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> EARTH_APPRENTICES_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> ORDER_APPRENTICES_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> ENTROPY_APPRENTICES_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> PROTECTION_RING().getDefaultInstance(), 5);
        LOOT_BAG_UNCOMMON_DROPS.add((random) -> Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), 2);

        potions.forEach((potion) -> {
            LOOT_BAG_UNCOMMON_DROPS.add(random -> {
                var stack = random.nextBoolean() ? new ItemStack(Items.POTION) : new ItemStack(Items.SPLASH_POTION);
                PotionUtils.setPotion(stack, potion);

                return stack;
            },potion.getEffects().stream().mapToInt(effectInstance -> effectInstance.getAmplifier()+1).sum());
        });

        LOOT_BAG_UNCOMMON_DROPS.sort();
    }

    public static void initRareDrops() {
        LOOT_BAG_RARE_DROPS.clear();
        LOOT_BAG_RARE_DROPS.add((random) -> new ItemStack(GOLD_COIN(), 3), 2000);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.GOLD_INGOT.getDefaultInstance(), 100);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.ENDER_PEARL.getDefaultInstance(), 100);
        LOOT_BAG_RARE_DROPS.add((random) -> genGear(1,random), 125);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.EMERALD.getDefaultInstance(), 75);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.DIAMOND.getDefaultInstance(), 50);
        LOOT_BAG_RARE_DROPS.add((random) -> KNOWLEDGE_FRAGMENT().getDefaultInstance(), 25);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.EXPERIENCE_BOTTLE.getDefaultInstance(), 20);
        LOOT_BAG_RARE_DROPS.add((random) -> {
            var result = new ItemStack(Items.BOOK);
            EnchantmentHelper.enchantItem(
                    random,
                    result,
                    (int) (5.0F + 2 * 0.75F * random.nextInt(18)),
                    true
            );
            return result;
        }, 10);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.GOLDEN_APPLE.getDefaultInstance(), 9);
        LOOT_BAG_RARE_DROPS.add((random) -> AIR_APPRENTICES_RING().getDefaultInstance(), 7);
        LOOT_BAG_RARE_DROPS.add((random) -> WATER_APPRENTICES_RING().getDefaultInstance(), 7);
        LOOT_BAG_RARE_DROPS.add((random) -> FIRE_APPRENTICES_RING().getDefaultInstance(), 7);
        LOOT_BAG_RARE_DROPS.add((random) -> EARTH_APPRENTICES_RING().getDefaultInstance(), 7);
        LOOT_BAG_RARE_DROPS.add((random) -> ORDER_APPRENTICES_RING().getDefaultInstance(), 7);
        LOOT_BAG_RARE_DROPS.add((random) -> ENTROPY_APPRENTICES_RING().getDefaultInstance(), 7);
        LOOT_BAG_RARE_DROPS.add(ThaumcraftLootBags::getVisAmulet, 6);
        LOOT_BAG_RARE_DROPS.add((random) -> PROTECTION_RING().getDefaultInstance(), 5);
        LOOT_BAG_RARE_DROPS.add((random) -> Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), 3);

        potions.forEach((potion) -> LOOT_BAG_RARE_DROPS.add(random -> {
            var stack = random.nextBoolean() ? new ItemStack(Items.POTION) : new ItemStack(Items.SPLASH_POTION);
            PotionUtils.setPotion(stack, potion);

            return stack;
        },potion.getEffects().stream().mapToInt(effectInstance -> effectInstance.getAmplifier()+1).sum()));

        LOOT_BAG_RARE_DROPS.add((random) -> Items.NETHER_STAR.getDefaultInstance(), 1);
        LOOT_BAG_RARE_DROPS.add((random) -> PRIME_PEARL().getDefaultInstance(), 1);//yes you can get pearl at home.

        LOOT_BAG_RARE_DROPS.sort();
    }

    public static void onDatapackReload() {
        init();
    }

    public static ItemStack getVisAmulet(RandomSource random) {
        var result = VIS_AMULET().getDefaultInstance();
        if (!ConfigurationHandler.INSTANCE.isMoreRandomizedLoot()) {
            return result;//.copy();
        }
        CentiVisList<Aspect> centiVisOwning = new ArrayCentiVisList<>();
        for (Aspect a : Aspects.getPrimalAspects()) {
            centiVisOwning.addAll(
                    a, random.nextInt(500)
            );
        }
        VIS_AMULET().storeCentiVisOwning(
                result, centiVisOwning
        );
        return result;
    }

    //rarity > 0 && rand.nextFloat() < 0.025F * (float) rarity
    //0,0.025,0.05
    public static ItemStack genGear(int rarity, RandomSource random) {


        int quality = random.nextInt(2);
        if (random.nextFloat() < 0.2F) {
            ++quality;
        }

        if (random.nextFloat() < 0.15F) {
            ++quality;
        }

        if (random.nextFloat() < 0.1F) {
            ++quality;
        }

        if (random.nextFloat() < 0.095F) {
            ++quality;
        }

        if (random.nextFloat() < 0.095F) {
            ++quality;
        }

        Item item = getGearItemForSlot(random.nextInt(5), quality);
        if (item != null) {
            var is = new ItemStack(item);
            is.setDamageValue(random.nextInt(1 + item.getMaxDamage() / 6));
            if (random.nextInt(4) < rarity) {
                EnchantmentHelper.enchantItem(
                        random,
                        is,
                        (int) (5.0F + rarity * 0.75F * random.nextInt(18)),
                        false
                );
            }

            return is.copy();
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static Item getGearItemForSlot(int slot, int quality) {
        switch (slot) {
            case 4: // 头盔
                if (quality == 0) return Items.LEATHER_HELMET;
                else if (quality == 1) return Items.GOLDEN_HELMET;
                else if (quality == 2) return Items.CHAINMAIL_HELMET;
                else if (quality == 3) return Items.IRON_HELMET;
                else if (quality == 4) return ThaumcraftItemInstances.THAUMIUM_HELMET();
                else if (quality == 5) return Items.DIAMOND_HELMET;
                else if (quality == 6) return ThaumcraftItemInstances.VOID_HELMET();
                break;
            case 3: // 胸甲
                if (quality == 0) return Items.LEATHER_CHESTPLATE;
                else if (quality == 1) return Items.GOLDEN_CHESTPLATE;
                else if (quality == 2) return Items.CHAINMAIL_CHESTPLATE;
                else if (quality == 3) return Items.IRON_CHESTPLATE;
                else if (quality == 4) return ThaumcraftItemInstances.THAUMIUM_CHESTPLATE();
                else if (quality == 5) return Items.DIAMOND_CHESTPLATE;
                else if (quality == 6) return ThaumcraftItemInstances.VOID_CHESTPLATE();
                break;
            case 2: // 护腿
                if (quality == 0) return Items.LEATHER_LEGGINGS;
                else if (quality == 1) return Items.GOLDEN_LEGGINGS;
                else if (quality == 2) return Items.CHAINMAIL_LEGGINGS;
                else if (quality == 3) return Items.IRON_LEGGINGS;
                else if (quality == 4) return ThaumcraftItemInstances.THAUMIUM_LEGGINGS();
                else if (quality == 5) return Items.DIAMOND_LEGGINGS;
                else if (quality == 6) return ThaumcraftItemInstances.VOID_LEGGINGS();
                break;
            case 1:
                if (quality == 0) return Items.LEATHER_BOOTS;
                else if (quality == 1) return Items.GOLDEN_BOOTS;
                else if (quality == 2) return Items.CHAINMAIL_BOOTS;
                else if (quality == 3) return Items.IRON_BOOTS;
                else if (quality == 4) return ThaumcraftItemInstances.THAUMIUM_BOOTS();
                else if (quality == 5) return Items.DIAMOND_BOOTS;
                else if (quality == 6) return ThaumcraftItemInstances.VOID_BOOTS();
                break;
            case 0:
                if (quality == 0) return Items.IRON_AXE;
                else if (quality == 1) return Items.IRON_SWORD;
                else if (quality == 2) return Items.GOLDEN_AXE;
                else if (quality == 3) return Items.GOLDEN_SWORD;
                else if (quality == 4) return ThaumcraftItemInstances.THAUMIUM_SWORD();
                else if (quality == 5) return Items.DIAMOND_SWORD;
                else if (quality == 6) return ThaumcraftItemInstances.VOID_SWORD();
                break;
            default:
                return null;
        }
        return null;
    }

}
