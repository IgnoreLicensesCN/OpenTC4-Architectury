package thaumcraft.api.aspects;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Set;
import java.util.function.IntBinaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

public class UnmodifiableAspectView<A extends Aspect> extends AspectList<A> {
    private final AspectList<A> viewingList;
    public UnmodifiableAspectView(AspectList<A> viewingList) {
        this.viewingList = viewingList;
    }

    @Override
    public @NotNull("empty -> empty(aspect)") A getFirstAspect() {
        return this.viewingList.getFirstAspect();
    }

    @Override
    public void clear() {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public String toString() {
        return this.viewingList.toString();
    }

    @Override
    public boolean isEmpty() {
        return this.viewingList.isEmpty();
    }

    @Override
    public A randomAspect(RandomSource randomSource) {
        return this.viewingList.randomAspect(randomSource);
    }

    @Override
    public void forEach(ObjIntConsumer<A> action) {
        this.viewingList.forEach(action);
    }

    @Override
    public AspectList<A> multiplyAndCeil(float multiplier) {
        return this.viewingList.multiplyAndCeil(multiplier);
    }

    @Override
    public AspectList<A> set(A aspect, int amount) {
        return this.viewingList.set(aspect, amount);
    }

    @Override
    public AspectList<A> removeIfNotPositive() {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> removeIf(Predicate<Object2IntMap.Entry<A>> filter) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public int getAmount(A key) {
        return this.viewingList.getAmount(key);
    }

    @Override
    public List<A> getAspectsSortedAmount() {
        return this.viewingList.getAspectsSortedAmount();
    }

    @Override
    public List<A> getAspectsSorted() {
        return this.viewingList.getAspectsSorted();
    }

    @Override
    public AspectList<PrimalAspect> getPrimalAspects() {
        return this.viewingList.getPrimalAspects();
    }

    @Override
    public Set<A> getAspectTypes() {
        return this.viewingList.getAspectTypes();
    }

    @Override
    public int visSize() {
        return this.viewingList.visSize();
    }

    @Override
    public int size() {
        return this.viewingList.size();
    }

    @Override
    public int get(A aspect) {
        return this.viewingList.get(aspect);
    }

    @Override
    public @UnmodifiableView Set<A> keySet() {
        return this.viewingList.keySet();
    }

    @Override
    public Object2IntSortedMap.FastSortedEntrySet<A> entrySet() {
        return this.viewingList.entrySet();
    }

    @Override
    public int getOrDefault(A aspect, int defaultValue) {
        return this.viewingList.getOrDefault(aspect, defaultValue);
    }

    @Override
    public Object2IntMap<A> getAspectView() {
        return this.viewingList.getAspectView();
    }

    @Override
    public Integer put(A aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> copy() {
        return this.viewingList.copy();
    }

    @Override
    public AspectList<A> mergeWithHighest(AspectList<A> in) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> mergeWithHighest(Aspect aspect, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public int merge(A aspect, int amount, IntBinaryOperator chooser) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> addAll(AspectList<A> in) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> addAll(Aspect aspect, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> remove(Aspect key) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> reduceAndRemoveIfNotPositive(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean tryReduce(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    public void putAllAspects(AspectList<A> aspects) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void replaceAll(AspIntIntBiFunction<A> aIntIntBiFunction) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> multiply(int multiplier) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> divideAndCeil(int divideBy) {
        throw new RuntimeException("Unmodifiable!");
    }

}
