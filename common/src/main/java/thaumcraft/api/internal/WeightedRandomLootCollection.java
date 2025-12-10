package thaumcraft.api.internal;

import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.ai.behavior.ShufflingList;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import java.util.ArrayList;


public class WeightedRandomLoot /*extends WeightedRandom.Item*/ {

    public final WeightedRandomList<WeightedEntry.Wrapper<ItemStack>> content;

    public WeightedRandomLoot(Pair<ItemStack, Integer>[] loot)
    {
        WeightedEntry.Wrapper<ItemStack>[] contentArr = new WeightedEntry.Wrapper[loot.length];
        for (int i = 0; i < loot.length; i++){
            contentArr[i] = WeightedEntry.wrap(loot[i].getA(), loot[i].getB());
        }
        content = WeightedRandomList.create(contentArr);
    }
    
    public static final WeightedRandomLoot lootBagCommon = new ArrayList<>();
    public static final WeightedRandomLoot lootBagUncommon = new ArrayList<>();
    public static final WeightedRandomLoot lootBagRare = new ArrayList<>();
    
}
