package thaumcraft.api.aspects.aspectlists;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.PrimalAspect;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

//default impl
public class LinkedHashAspectList<Asp extends Aspect>
        implements AspectList<Asp> /*implements Serializable */ {

    private int visSize;
    //i say do we really need this?we should have better way.
    //make this AspectList ordered is just for UI performance i even want it's order not ruled.
    protected final Object2IntLinkedOpenHashMap<Asp> aspects;//aspects associated with this object

    private final Object2IntMap<Asp> aspectView;

    public LinkedHashAspectList() {
        this.aspects = new Object2IntLinkedOpenHashMap<>();
        this.aspectView = Object2IntMaps.unmodifiable(aspects);
        recalculateVisSize();
    }

    protected LinkedHashAspectList(@NotNull Object2IntLinkedOpenHashMap<Asp> aspects) {
        this.aspects = aspects;
        this.aspectView = Object2IntMaps.unmodifiable(aspects);
        recalculateVisSize();
    }

    public LinkedHashAspectList(@NotNull Map<Asp, Integer> aspects) {
        this.aspects = new Object2IntLinkedOpenHashMap<>(aspects);
        this.aspectView = Object2IntMaps.unmodifiable(this.aspects);
        recalculateVisSize();
    }

    public LinkedHashAspectList(@NotNull Object2IntMap<Asp> aspects) {
        this.aspects = new Object2IntLinkedOpenHashMap<>(aspects);
        this.aspectView = Object2IntMaps.unmodifiable(this.aspects);
        recalculateVisSize();
    }

    public LinkedHashAspectList(int size, float loadFactor) {
        this.aspects = new Object2IntLinkedOpenHashMap<>(size, loadFactor);
        this.aspectView = Object2IntMaps.unmodifiable(aspects);
        recalculateVisSize();
    }

    public LinkedHashAspectList(@NotNull LinkedHashAspectList<Asp> another) {
        this.aspects = new Object2IntLinkedOpenHashMap<>(another.aspects);
        this.aspectView = Object2IntMaps.unmodifiable(aspects);
        recalculateVisSize();
    }

    @Deprecated(forRemoval = true, since = "implements IAspectDisplayItem")
    public void addAspectDescriptionToList(@Nullable Player player, List<Component> aspectDescriptions) {
        if (aspects != null && !this.aspects.isEmpty()) {
            for (var aspect : this.getAspectsSorted()) {
                if (player != null && !aspect.hasPlayerDiscovered(player)) {
                    aspectDescriptions.add(Component.translatable("tc.aspect.unknown"));
                } else {
                    aspectDescriptions.add(Component.literal(aspect.getName() + " x " + this.get(aspect)));
                }
            }
        }
    }

    public int getOrDefault(Asp aspect, int defaultValue) {
        return aspects.getOrDefault(aspect, defaultValue);
    }

    public int put(Asp aspect, int amount) {
        return aspects.put(aspect, amount);
    }

    public LinkedHashAspectList<Asp> copy() {
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
        return aspectView.keySet().stream()
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
        return aspectView.keySet().stream()
                .filter(a -> this.get(a) > 0)
                .sorted(Comparator.comparingInt(this::get).reversed())
                .toList();//azanor knows little about java's own sorting?
    }

    /**
     * @param key aspect as key
     * @return the amount associated with the given aspect in this collection
     */
    public int get(Asp key) {
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

    public void remove(Asp key) {
        this.visSize -= aspects.removeInt(key);

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
            // 除以 divideBy 向上取整
            int divided = (oldValue + divideBy - 1) / divideBy; // ceil 整数除法
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
        int result = this.aspects.mergeInt(aspect, amount, chooser::applyAsInt);
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
        for (var a : in.keySet()) {
            this.mergeWithHighest(a, in.get(a));
        }
//		recalculateVisSize();//calculated each loop above

    }

    public void forEach(ObjIntConsumer<Asp> action) {
        var iterator = aspects.object2IntEntrySet().fastIterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            action.accept(entry.getKey(), entry.getIntValue());
        }
    }

    //true if action returns true(and will break loop)
    public boolean forEachWithBreak(ObjInt2BooleanFunction<Asp> action) {
        var iterator = aspects.object2IntEntrySet().fastIterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (action.accept(entry.getKey(), entry.getIntValue())) {
                return true;
            }
        }
        return false;
    }

    public void acceptForIndex(int index, ObjIntConsumer<Asp> action) {
        if (aspects.size() <= index) {
            throw new IndexOutOfBoundsException(
                    "Index out of bound!Expected smaller than " + aspects.size() + ", got " + index
            );
        }
        var iterator = aspects.object2IntEntrySet().fastIterator();
        int indexCurrent = 0;
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (indexCurrent == index) {
                action.accept(entry.getKey(), entry.getIntValue());
                return;
            }
            indexCurrent += 1;
        }

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

    @SafeVarargs
    public static <Asp extends Aspect> LinkedHashAspectList<Asp> of(Asp... aspects) {
        LinkedHashAspectList<Asp> out = new LinkedHashAspectList<>();
        for (var aspect : aspects) {
            if (aspect != null) {
                out.addAll(aspect, 1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> LinkedHashAspectList<Asp> viewOf(Object2IntLinkedOpenHashMap<Asp> aspects) {
        return new LinkedHashAspectList<>(aspects);
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
            return null; // 或者返回你的 Aspect.EMPTY
        }
        int target = randomSource.nextInt(visSize);
        int currentSum = 0;
        for (var entry : aspects.object2IntEntrySet()) {
            currentSum += entry.getIntValue();
            if (target < currentSum) {
                return entry.getKey();
            }
        }

        return aspects.firstKey();
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

    public @NotNull("empty -> empty(aspect)") Asp getFirstAspect() {
        if (aspects.isEmpty()) return (Asp) Aspects.EMPTY;
        return aspects.firstKey();
    }


    public boolean containsKey(Asp aspect) {
        return this.aspects.containsKey(aspect);
    }


}
