package thaumcraft.api.internal;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NotThreadSafe//at least for writing
public class WeightedRandomCollection<Obj>{
    public WeightedRandomCollection() {}
    protected @NotNull List<ObjectIntPair<Obj>> internalContainer = new ArrayList<>();
    protected boolean sorted = false;
    protected int totalWeight = 0;
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
        int pickValue = rand.nextInt(totalWeight + 1);
        for (var item : internalContainer) {
            pickValue -= item.rightInt();
            if (pickValue <= 0) {
                return item.left();
            }
        }
        return internalContainer.getFirst().left();
    }
    public void clear(){
        internalContainer.clear();
        totalWeight = 0;
    }
    public int getTotalWeight() {
        return totalWeight;
    }
    public void sort(){
        internalContainer.sort((a,b) -> -Integer.compare(a.rightInt(), b.rightInt()));
        sorted = true;
    }
    public @UnmodifiableView List<ObjectIntPair<Obj>> getInternalContainerView() {
        return Collections.unmodifiableList(internalContainer);
    }
}
