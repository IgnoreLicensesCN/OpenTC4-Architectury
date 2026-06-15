package thaumcraft.common.lootbag;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.internal.WeightedRandomCollection;

import java.util.function.Function;

public class ThaumcraftLootBags {

    public static final WeightedRandomCollection<Function<RandomSource, ItemStack>> LOOT_BAG_COMMON_DROPS = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<Function<RandomSource,ItemStack>> LOOT_BAG_UNCOMMON_DROPS = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<Function<RandomSource,ItemStack>> LOOT_BAG_RARE_DROPS = new WeightedRandomCollection<>();

    public static void init(){
        LOOT_BAG_COMMON_DROPS.clear();
        LOOT_BAG_UNCOMMON_DROPS.clear();
        LOOT_BAG_RARE_DROPS.clear();
        //TODO
    }
    public static void onDatapackReload(){
        init();
    }
}
