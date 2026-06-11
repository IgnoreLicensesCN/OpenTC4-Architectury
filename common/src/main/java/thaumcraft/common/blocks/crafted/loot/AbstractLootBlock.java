package thaumcraft.common.blocks.crafted.loot;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.baubles.ItemAmuletVis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static thaumcraft.api.internal.WeightedRandomCollection.*;

public abstract class AbstractLootBlock extends SuppressedWarningBlock {
    public AbstractLootBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        var level = builder.getLevel();
        var random = level.random;
        var rarity = getRarityFromState(blockState);
        var dropCount = rarity + 1 + random.nextInt(3);
        var result = new ArrayList<ItemStack>(dropCount);
        for (int i = 0; i < dropCount; i++) {
            result.add(generateLoot(rarity, random));
        }
        return result;
    }

    public abstract int getRarityFromState(BlockState state);

    //dont be too serious for "this is not extendable" since this part is just "TC4 Internal"
    public enum Rarity {
        COMMON,UNCOMMON,RARE;
    }

    public static final Int2ObjectMap<WeightedRandomCollection<Function<RandomSource, ItemStack>>> lootMapping = new Int2ObjectOpenHashMap<>();
    static {
        lootMapping.put(Rarity.COMMON.ordinal(), lootBagCommon);
        lootMapping.put(Rarity.UNCOMMON.ordinal(), lootBagUncommon);
        lootMapping.put(Rarity.RARE.ordinal(), lootBagRare);
    }

    public static ItemStack mutateGeneratedLoot(ItemStack stack, RandomSource random) {
        if (!ConfigurationHandler.INSTANCE.isMoreRandomizedLoot()) return stack;//.copy();
        if (stack.getItem() == ConfigItems.itemAmuletVis) {
            ItemAmuletVis ai = (ItemAmuletVis) stack.getItem();

            for (Aspect a : Aspects.getPrimalAspects()) {
                ai.storeVis(
                        stack, a, random.nextInt(5) * 100
                );
            }
        }
        return stack;
    }

    public static ItemStack generateLoot(int rarity, RandomSource rand) {
        //if no rarity just throw
        var isFunction = lootMapping.get(rarity).getRandom(rand);
        var is = isFunction.apply(rand);

        if (is == null) {
            is = generateLoot(rarity, rand);
        }

        is = is.copy();
        if (is.getItem() == Items.BOOK) {

            EnchantmentHelper.enchantItem(
                    rand,
                    is,
                    (int) (5.0F + rarity * 0.75F * rand.nextInt(18)),
                    true
            );
        }

        return mutateGeneratedLoot(is,rand);
    }

    //rarity > 0 && rand.nextFloat() < 0.025F * (float) rarity
    //0,0.025,0.05
    public static Function<RandomSource, ItemStack> getGenerateGearFunction(int rarity){
        return rand -> {

            int quality = rand.nextInt(2);
            if (rand.nextFloat() < 0.2F) {
                ++quality;
            }

            if (rand.nextFloat() < 0.15F) {
                ++quality;
            }

            if (rand.nextFloat() < 0.1F) {
                ++quality;
            }

            if (rand.nextFloat() < 0.095F) {
                ++quality;
            }

            if (rand.nextFloat() < 0.095F) {
                ++quality;
            }

            Item item = getGearItemForSlot(rand.nextInt(5), quality);
            if (item != null) {
                var is = new ItemStack(item);
                is.setDamageValue(rand.nextInt(1 + item.getMaxDamage() / 6));
                if (rand.nextInt(4) < rarity) {
                    EnchantmentHelper.enchantItem(
                            rand,
                            is,
                            (int) (5.0F + rarity * 0.75F * rand.nextInt(18)),
                            false
                    );
                }

                return is.copy();
            } else {
                return null;
            }
        };
    }

    private static Item getGearItemForSlot(int slot, int quality) {
        switch (slot) {
            case 4: // 头盔
                if (quality == 0) return Items.LEATHER_HELMET;
                else if (quality == 1) return Items.GOLDEN_HELMET;
                else if (quality == 2) return Items.CHAINMAIL_HELMET;
                else if (quality == 3) return Items.IRON_HELMET;
                else if (quality == 4) return ThaumcraftItems.ThaumcraftItemInstances.THAUMIUM_HELMET;
                else if (quality == 5) return Items.DIAMOND_HELMET;
                else if (quality == 6) return ThaumcraftItems.ThaumcraftItemInstances.VOID_HELMET;
                break;
            case 3: // 胸甲
                if (quality == 0) return Items.LEATHER_CHESTPLATE;
                else if (quality == 1) return Items.GOLDEN_CHESTPLATE;
                else if (quality == 2) return Items.CHAINMAIL_CHESTPLATE;
                else if (quality == 3) return Items.IRON_CHESTPLATE;
                else if (quality == 4) return ThaumcraftItems.ThaumcraftItemInstances.THAUMIUM_CHESTPLATE;
                else if (quality == 5) return Items.DIAMOND_CHESTPLATE;
                else if (quality == 6) return ThaumcraftItems.ThaumcraftItemInstances.VOID_CHESTPLATE;
                break;
            case 2: // 护腿
                if (quality == 0) return Items.LEATHER_LEGGINGS;
                else if (quality == 1) return Items.GOLDEN_LEGGINGS;
                else if (quality == 2) return Items.CHAINMAIL_LEGGINGS;
                else if (quality == 3) return Items.IRON_LEGGINGS;
                else if (quality == 4) return ThaumcraftItems.ThaumcraftItemInstances.THAUMIUM_LEGGINGS;
                else if (quality == 5) return Items.DIAMOND_LEGGINGS;
                else if (quality == 6) return ThaumcraftItems.ThaumcraftItemInstances.VOID_LEGGINGS;
                break;
            case 1:
                if (quality == 0) return Items.LEATHER_BOOTS;
                else if (quality == 1) return Items.GOLDEN_BOOTS;
                else if (quality == 2) return Items.CHAINMAIL_BOOTS;
                else if (quality == 3) return Items.IRON_BOOTS;
                else if (quality == 4) return ThaumcraftItems.ThaumcraftItemInstances.THAUMIUM_BOOTS;
                else if (quality == 5) return Items.DIAMOND_BOOTS;
                else if (quality == 6) return ThaumcraftItems.ThaumcraftItemInstances.VOID_BOOTS;
                break;
            case 0:
                if (quality == 0) return Items.IRON_AXE;
                else if (quality == 1) return Items.IRON_SWORD;
                else if (quality == 2) return Items.GOLDEN_AXE;
                else if (quality == 3) return Items.GOLDEN_SWORD;
                else if (quality == 4) return ThaumcraftItems.ThaumcraftItemInstances.THAUMIUM_SWORD;
                else if (quality == 5) return Items.DIAMOND_SWORD;
                else if (quality == 6) return ThaumcraftItems.ThaumcraftItemInstances.VOID_SWORD;
                break;
            default:
                return null;
        }
        return null;
    }

}
