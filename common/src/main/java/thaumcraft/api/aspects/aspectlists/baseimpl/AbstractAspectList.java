package thaumcraft.api.aspects.aspectlists.baseimpl;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

public abstract class AbstractAspectList<Asp extends Aspect,MapClass extends Object2IntMap<Asp>> implements AspectList<Asp> {

    private int visSize;
    protected final MapClass aspects;

    private final Object2IntMap<Asp> aspectView;

    protected AbstractAspectList(@NotNull MapClass aspects) {
        this.aspects = aspects;
        this.aspectView = Object2IntMaps.unmodifiable(this.aspects);
        recalculateVisSize();
    }


//    @Deprecated(forRemoval = true, since = "implements IAspectDisplayItem")
//    public void addAspectDescriptionToList(@Nullable Player player, List<Component> aspectDescriptions) {
//        if (aspects != null && !this.aspects.isEmpty()) {
//            for (var aspect : this.getAspectsSorted()) {
//                if (player != null && !aspect.hasPlayerDiscovered(player)) {
//                    aspectDescriptions.add(Component.translatable("aspect.thaumcraft.unknown"));
//                } else {
//                    aspectDescriptions.add(Component.literal(aspect.getName() + " x " + this.get(aspect)));
//                }
//            }
//        }
//    }

    public int getOrDefault(Aspect aspect, int defaultValue) {
        return aspects.getOrDefault(aspect, defaultValue);
    }

    public int put(Asp aspect, int amount) {
        int decreased = aspects.put(aspect, amount);
        this.visSize += amount;
        this.visSize -= decreased;
        return decreased;
    }

    public AspectList<Asp> copy() {
        var out = new LinkedHashAspectList<Asp>();
        for (var a : this.keySet())
            out.addAll(a, this.get(a));
        return out;
    }

    @UnmodifiableView
    public Set<Asp> keySet() {
        return aspectView.keySet();
    }

    /**
     * @return the amount of different aspects in this collection
     */
    public int size() {
        return aspects.size();
    }

    /**
     * @return the amount of total vis in this collection
     */
    public int visSize() {
        return visSize;
    }

    /**
     * @return an array of all the primal aspects in this collection
     */
    public LinkedHashAspectList<PrimalAspect> getPrimalAspects() {
        LinkedHashAspectList<PrimalAspect> result = new LinkedHashAspectList<>();
        for (var aspectAndAmount : aspects.object2IntEntrySet()) {
            var aspect = aspectAndAmount.getKey();
            var amount = aspectAndAmount.getIntValue();
            if (aspect instanceof PrimalAspect primalAspect) {
                result.addAll(primalAspect, amount);
            }
        }
        return result;
    }

    /**
     * @return an array(a list now because Asp[] is not so fine) of all the aspects in this collection sorted by name
     * --from Hodgepodge
     */
    public List<Asp> getAspectsSorted() {
        return aspectView.keySet()
                .stream()
                .sorted(Comparator.comparing(
                        Aspect::getNameString,
                        String.CASE_INSENSITIVE_ORDER
                ))
                .toList();
    }

    /**
     * @return an array(a list now because Asp[] is not so fine) of all the aspects in this collection sorted by amount
     */
    public List<Asp> getAspectsSortedAmount() {
        return aspectView.keySet()
                .stream()
                .filter(a -> this.get(a) > 0)
                .sorted(Comparator.comparingInt(this::get).reversed())
                .toList();//azanor knows little about java's own sorting?
    }

    /**
     * @param key aspect as key
     * @return the amount associated with the given aspect in this collection
     */
    public int get(Aspect key) {
        return aspects.getInt(key);
    }

    /**
     * Reduces the amount of an aspect in this collection by the given amount.
     *
     * @param key    to remove
     * @param amount to remove
     * @return succeed(false if will lead to negative)
     */
    public boolean tryReduce(Asp key, int amount) {
        var currentAmount = this.get(key);
        if (currentAmount >= amount) {
            int am = currentAmount - amount;
            aspects.put(key, am);
            visSize -= amount;
            return true;
        }
        return false;
    }

    /**
     * Reduces the amount of an aspect in this collection by the given amount.
     * If reduced to 0 or less the aspect will be removed completely.
     *
     * @param key    to remove
     * @param amount to remove
     */

    public void reduceAndRemoveIfNotPositive(Asp key, int amount) {
        var currentAmount = this.get(key);
        int reducedResult = currentAmount - amount;
        if (reducedResult <= 0) {
            aspects.removeInt(key);
            this.visSize -= currentAmount;
        } else {
            this.aspects.put(key, reducedResult);
            this.visSize -= amount;
        }
    }

    /**
     * Simply removes the aspect from the list
     *
     * @param key //	 * @param amount
     *
     */

    public int remove(Asp key) {
        var result = aspects.removeInt(key);
        this.visSize -= result;
        return result;
    }

    public void removeIf(Predicate<Object2IntMap.Entry<Asp>> filter) {
        aspects.object2IntEntrySet().removeIf(entry -> {
            if (filter.test(entry)) {
                this.visSize -= entry.getIntValue();
                return true;
            }
            return false;
        });
    }

    /**
     * Adds this aspect and amount to the collection.
     * <s>If the aspect exists then its value will be increased by the given amount.</s>
     *
     * @param aspect to add
     * @param amount to add
     *
     */

    public void addAll(Asp aspect, int amount) {
        if (aspect == null || aspect.isEmpty()) {
            throw new NullPointerException("aspect is null or empty");
        }
        this.aspects.merge(aspect, amount, Integer::sum);
        this.visSize += amount;
    }

    public void set(Asp aspect, int amount) {
        if (aspect == null) {
            throw new NullPointerException("aspect is null");
        }
        var oldValue = this.aspects.put(aspect, amount);
        this.visSize += amount - oldValue;

    }

    public void divideAndCeil(int divideBy) {
        if (divideBy == 0) {
            throw new IllegalArgumentException("division by zero");
        }
        for (Map.Entry<Asp, Integer> entry : aspects.object2IntEntrySet()) {
            int oldValue = entry.getValue();
            int divided = Math.ceilDiv(oldValue,divideBy);
            entry.setValue(divided);
            this.visSize += divided - oldValue;
        }

    }

    public void multiply(int multiplier) {
        for (Map.Entry<Asp, Integer> entry : aspects.object2IntEntrySet()) {
            int oldValue = entry.getValue();
            int newValue = oldValue * multiplier;
            entry.setValue(newValue);
            this.visSize += newValue - oldValue;
        }

    }

    public void multiplyAndCeil(float multiplier) {
        for (Map.Entry<Asp, Integer> entry : aspects.object2IntEntrySet()) {
            int oldValue = entry.getValue();
            int newValue = (int) Math.ceil(oldValue * multiplier);
            entry.setValue(newValue);
            this.visSize += newValue - oldValue;
        }

    }


    /**
     * Adds this aspect and amount to the collection.
     * If the aspect exists then only the highest of the old or new amount will be used.
     *
     * @param aspect to merge
     * @param amount to merge
     *
     */

    public void mergeWithHighest(Asp aspect, int amount) {
        int oldValue = this.aspects.getOrDefault(aspect, 0);
        if (amount < oldValue) {
            amount = oldValue;
        }
        this.aspects.put(aspect, amount);
        this.visSize += amount - oldValue;

    }

    public int merge(Asp aspect, int amount, IntBinaryOperator chooser) {
        var oldValue = this.aspects.getOrDefault(aspect, 0);
        int result = this.aspects.mergeInt(aspect, amount, chooser);
        var newValue = this.aspects.getOrDefault(aspect, 0);
        this.visSize += newValue - oldValue;
        return result;
    }


    public void addAll(AspectList<Asp> in) {
        for (var a : in.keySet()) {
            this.addAll(a, in.get(a));
        }
    }


    public void mergeWithHighest(AspectList<Asp> in) {
        in.forEach(this::mergeWithHighest);
//		recalculateVisSize();//calculated each loop above

    }

    public void overrideAllAspects(AspectList<Asp> aspects) {
        AtomicInteger visSizeChange = new AtomicInteger();
        aspects.forEach(
                (aspect, amount) -> {
                    visSizeChange.addAndGet(amount);
                    visSizeChange.addAndGet(-this.aspects.put(aspect, amount));
                }
        );
        this.visSize += visSizeChange.get();
    }

    @Override
    public void replaceAll(AspIntIntBiFunction<Asp> biFunction) {
        aspects.object2IntEntrySet().forEach(entry -> {
            int newValue = biFunction.apply(entry.getKey(), entry.getIntValue());
            entry.setValue(newValue);
        });
    }


    public @Nullable("if empty") Asp randomAspect(RandomSource randomSource) {
        if (this.size() == 0) {
            return null;
        }
        var list = this.aspects.keySet().stream().toList();
        return list.get(randomSource.nextInt(list.size()));
    }

    public @Nullable("if empty") Asp randomWeightedAspect(RandomSource randomSource) {
        if (visSize <= 0 || aspects.isEmpty()) {
            return null;
        }
        int target = randomSource.nextInt(visSize);
        int currentSum = 0;
        for (var entry : aspects.object2IntEntrySet()) {
            currentSum += entry.getIntValue();
            if (target < currentSum) {
                return entry.getKey();
            }
        }

        return (Asp) aspects.keySet().toArray()[0];
    }


    public boolean isEmpty() {
        return this.aspects.isEmpty();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        aspects.object2IntEntrySet().forEach(entry ->
                joiner.add(entry.getKey() + ":" + entry.getIntValue())
        );
        return this.getClass().getSimpleName() + "{aspects=" + joiner + "}";
    }

    //do better not calc as possible
    protected void recalculateVisSize() {
        this.visSize = 0;
        this.aspectView.values().forEach(i -> this.visSize += i);
    }

    public void clear() {
        this.visSize = 0;
        this.aspects.clear();
    }


    public boolean containsKey(Aspect aspect) {
        return this.aspects.containsKey(aspect);
    }

}
