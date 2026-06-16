package thaumcraft.common.blocks.crafted.loot;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.lootbag.ThaumcraftLootBags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
        COMMON, UNCOMMON, RARE;
    }

    public static final Int2ObjectMap<WeightedRandomCollection<Function<RandomSource, ItemStack>>> lootMapping = new Int2ObjectOpenHashMap<>();

    static {
        lootMapping.put(Rarity.COMMON.ordinal(), ThaumcraftLootBags.LOOT_BAG_COMMON_DROPS);
        lootMapping.put(Rarity.UNCOMMON.ordinal(), ThaumcraftLootBags.LOOT_BAG_UNCOMMON_DROPS);
        lootMapping.put(Rarity.RARE.ordinal(), ThaumcraftLootBags.LOOT_BAG_RARE_DROPS);
    }


    public static ItemStack generateLoot(int rarity, RandomSource rand) {
        //if no rarity just throw
        var isFunction = lootMapping.get(rarity).getRandom(rand);
        return isFunction.apply(rand);
    }


}
