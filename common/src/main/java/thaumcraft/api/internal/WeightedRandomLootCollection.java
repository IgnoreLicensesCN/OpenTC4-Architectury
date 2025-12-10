package thaumcraft.api.internal;

import com.linearity.opentc4.utils.MutableWeightedRandomList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class WeightedRandomLootCollection /*extends WeightedRandom.Item*/ extends MutableWeightedRandomList<WeightedRandomLootCollection.WeightedRandomLoot> {


    public static final WeightedRandomLootCollection lootBagCommon = new WeightedRandomLootCollection.WeightedRandomLoot();
    public static final WeightedRandomLootCollection lootBagUncommon = new WeightedRandomLootCollection.WeightedRandomLoot();
    public static final WeightedRandomLootCollection lootBagRare = new WeightedRandomLootCollection.WeightedRandomLoot();

    public static class WeightedRandomLoot implements WeightedEntry {
        private final ItemStack data;
        private final Weight weight;

        public WeightedRandomLoot(ItemStack object, Weight weight) {
            this.data = object;
            this.weight = weight;
        }
        public WeightedRandomLoot(ItemStack object, int weight) {
            this.data = object;
            this.weight = Weight.of(weight);
        }

        public ItemStack getData() {
            return this.data;
        }

        @Override
        public @NotNull Weight getWeight() {
            return this.weight;
        }

    }
}
