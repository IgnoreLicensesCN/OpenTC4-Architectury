package thaumcraft.api.internal;

import com.linearity.opentc4.simpleutils.ObjectIntPair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NotThreadSafe//at least for writing
public class WeightedRandomCollection<Obj>{
    public WeightedRandomCollection() {}
    protected @NotNull List<ObjectIntPair<Obj>> internalContainer = new ArrayList<>();
    protected boolean sorted = false;
    protected int totalWeight = 0;
    public static final WeightedRandomCollection<Function<RandomSource,ItemStack>> lootBagCommon = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<Function<RandomSource,ItemStack>> lootBagUncommon = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<Function<RandomSource,ItemStack>> lootBagRare = new WeightedRandomCollection<>();


    public void add(Obj obj, int weight) {
        internalContainer.add(new ObjectIntPair<>(obj,weight));
        sorted = false;
        if (Integer.MAX_VALUE - totalWeight > weight) {
            throw new IllegalArgumentException("total weight out of int limit.");
        }
        totalWeight += weight;
    }
    public Obj getRandom(RandomSource rand) {
        if (!sorted){
            sort();
        }
        int pickValue = rand.nextInt(totalWeight);
        for (var item : internalContainer) {
            pickValue -= item.value();
            if (pickValue < 0) {
                return item.obj();
            }
        }
        return internalContainer.getFirst().obj();
    }
    public void clear(){
        internalContainer.clear();
    }

    public int getTotalWeight() {
        return totalWeight;
    }
    public void sort(){
        internalContainer.sort((a,b) -> -Integer.compare(a.value(), b.value()));
        sorted = true;
    }
}
