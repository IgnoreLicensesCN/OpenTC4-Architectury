package thaumcraft.common.items.consumable.lootbag;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import thaumcraft.api.internal.WeightedRandomCollection;

import java.util.function.Function;

import static thaumcraft.common.lootbag.ThaumcraftLootBags.LOOT_BAG_RARE_DROPS;

public class RareLootBagItem extends AbstractLootBagItem{
    public RareLootBagItem(Properties properties, WeightedRandomCollection<Function<RandomSource, ItemStack>> lootSource) {
        super(properties, lootSource);
    }
    public RareLootBagItem(){
        this(new Properties().stacksTo(16).rarity(Rarity.RARE),LOOT_BAG_RARE_DROPS);
    }
}
