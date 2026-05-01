package thaumcraft.api.aspects;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.Serializable;
import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
//TODO: Check for Object2IntMap change
//TODO:[maybe wont finished] change Aspect count to Rational(will surly shake the whole TC4)
//2026.Feb.4 now we have AspectList<PrimalAspect>
public class AspectList<Asp extends Aspect> implements Serializable {

	private int visSize;//
	protected final Object2IntLinkedOpenHashMap<Asp> aspects;//aspects associated with this object
	private final Object2IntMap<Asp> aspectView;
	public Object2IntMap<Asp> getAspectView(){
		return aspectView;
	}
	
	public AspectList() {
		this.aspects = new Object2IntLinkedOpenHashMap<>();
		this.aspectView = Object2IntMaps.unmodifiable(aspects);
		recalculateVisSize();
	}
	public AspectList(Asp aspect, int value) {
		this.aspects = new Object2IntLinkedOpenHashMap<>();
		this.aspects.put(aspect,value);
		this.aspectView = Object2IntMaps.unmodifiable(aspects);
		recalculateVisSize();
	}
	protected AspectList(Object2IntLinkedOpenHashMap<Asp> aspects) {
		this.aspects = aspects;
		this.aspectView = Object2IntMaps.unmodifiable(aspects);
		recalculateVisSize();
	}
	public AspectList(Map<Asp,Integer> aspects) {
		this.aspects = new Object2IntLinkedOpenHashMap<>(aspects);
		this.aspectView = Object2IntMaps.unmodifiable(this.aspects);
		recalculateVisSize();
	}
	public AspectList(Object2IntMap<Asp> aspects) {
		this.aspects = new Object2IntLinkedOpenHashMap<>(aspects);
		this.aspectView = Object2IntMaps.unmodifiable(this.aspects);
		recalculateVisSize();
	}

	public AspectList(int size,float loadFactor) {
		this.aspects = new Object2IntLinkedOpenHashMap<>(size,loadFactor);
		this.aspectView = Object2IntMaps.unmodifiable(aspects);
		recalculateVisSize();
	}

	public AspectList(AspectList<Asp> another) {
		this.aspects = new Object2IntLinkedOpenHashMap<>(another.aspects);
		this.aspectView = Object2IntMaps.unmodifiable(aspects);
		recalculateVisSize();
	}


    public static <A extends Aspect> void addAspectDescriptionToList(AspectList<A> aspects, @Nullable Player player, List<Component> aspectDescriptions) {
       if (aspects != null && !aspects.aspects.isEmpty()) {
          for(var aspect : aspects.getAspectsSorted()) {
             if (player != null && !aspect.hasPlayerDiscovered(player)) {
				 aspectDescriptions.add(Component.translatable("tc.aspect.unknown"));
             } else {
				 aspectDescriptions.add(Component.literal(aspect.getName() + " x " + aspects.getAmount(aspect)));
             }
          }
       }
    }

	public int getOrDefault(Asp aspect, int defaultValue) {
		return aspects.getOrDefault(aspect,defaultValue);
	}
	public Integer put(Asp aspect, int amount) {
		return aspects.put(aspect,amount);
	}
    public AspectList<Asp> copy() {
		AspectList<Asp> out = new AspectList<>();
		for (var a:this.getAspectTypes())
			out.addAll(a, this.getAmount(a));
		return out;
	}

	public Object2IntSortedMap.FastSortedEntrySet<Asp> entrySet(){
		return aspects.object2IntEntrySet();
	}

	@UnmodifiableView
	public Set<Asp> keySet() {
		return aspectView.keySet();
	}

	public int get(Asp aspect) {
		return aspects.getInt(aspect);
	}
	
	/**
	 * @return the amount ofAspectVisList different aspects in this collection
	 */
	public int size() {
		return aspects.size();
	}
	
	/**
	 * @return the amount ofAspectVisList total vis in this collection
	 */
	public int visSize() {
		return visSize;
	}
	
	/**
	 * @return an array(a list now because Asp[] is not so fine) ofAspectVisList all the aspects in this collection
	 */
	public Set<Asp> getAspectTypes() {
		return aspects.keySet();
	}
	
	/**
	 * @return an array of all the primal aspects in this collection
	 */
	public AspectList<PrimalAspect> getPrimalAspects() {
		AspectList<PrimalAspect> result = new AspectList<>();
		for (var aspectAndAmount:aspects.object2IntEntrySet()) {
			var aspect = aspectAndAmount.getKey();
			var amount = aspectAndAmount.getIntValue();
			if (aspect instanceof PrimalAspect primalAspect) {
				result.addAll(primalAspect,amount);
			}
		}
		return result;
	}
	
	/**
	 * @return an array(a list now because Asp[] is not so fine) ofAspectVisList all the aspects in this collection sorted by name
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
	 * @return an array(a list now because Asp[] is not so fine) ofAspectVisList all the aspects in this collection sorted by amount
	 */
	public List<Asp> getAspectsSortedAmount() {
		return aspectView.keySet().stream()
				.filter(a -> getAmount(a) > 0)
				.sorted(Comparator.comparingInt(this::getAmount).reversed())
				.toList();//anazor knows little about java's own sorting?
	}
	
	/**
	 * @param key aspect as key
	 * @return the amount associated with the given aspect in this collection
	 */
	public int getAmount(Asp key) {
		return aspects.getInt(key);
	}
	
	/**
	 * Reduces the amount ofAspectVisList an aspect in this collection by the given amount.
	 * @param key to remove
	 * @param amount to remove
	 * @return succeed(false if will lead to negative)
	 */
	public boolean tryReduce(Asp key, int amount) {
		var currentAmount = getAmount(key);
		if (currentAmount>=amount) {
			int am = getAmount(key)-amount;
			aspects.put(key, am);
			visSize -= currentAmount;
			visSize += amount;
			return true;
		}
		return false;
	}
	
	/**
	 * Reduces the amount ofAspectVisList an aspect in this collection by the given amount.
	 * If reduced to 0 or less the aspect will be removed completely. 
	 * @param key to remove
	 * @param amount to remove
	 * @return slef
	 */
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> reduceAndRemoveIfNotPositive(Asp key, int amount) {
		var currentAmount = getAmount(key);
		int reducedResult = currentAmount - amount;
		if (reducedResult<=0) {
			aspects.removeInt(key);
			this.visSize -= currentAmount;
		}
		else {
			this.aspects.put(key, reducedResult);
			this.visSize -= amount;
		}
		return this;
	}
	
	/**
	 * Simply removes the aspect from the list
	 * @param key
//	 * @param amount
	 * @return self
	 */
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> remove(Asp key) {
		this.visSize -= aspects.removeInt(key);
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> removeIf(Predicate<Object2IntMap.Entry<Asp>> filter) {
		aspects.object2IntEntrySet().removeIf(entry -> {
			if (filter.test(entry)) {
				this.visSize -= entry.getIntValue();
				return true;
			}
			return false;
		});
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> removeIfNotPositive() {
		removeIf(aspectIntegerEntry -> aspectIntegerEntry.getIntValue() <= 0);
		return this;
	}
	
	/**
	 * Adds this aspect and amount to the collection. 
	 * If the aspect exists then its value will be increased by the given amount.
	 * @param aspect to add
	 * @param amount to add
	 * @return self
	 */
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> addAll(Asp aspect, int amount) {
		if (aspect == null || aspect.isEmpty()){
			throw new NullPointerException("aspect is null or empty");
		}
		this.aspects.merge(aspect, amount, Integer::sum);
		this.visSize += amount;
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> set(Asp aspect, int amount) {
		if (aspect == null){
			throw new NullPointerException("aspect is null");
		}
		var oldValue = this.aspects.put( aspect, amount );
		this.visSize += amount - oldValue;
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> divideAndCeil(int divideBy){
		if (divideBy==0){
			throw new IllegalArgumentException("division by zero");
		}
		for (Map.Entry<Asp, Integer> entry : aspects.object2IntEntrySet()) {
			int oldValue = entry.getValue();
			// 除以 divideBy 向上取整
			int divided = (oldValue + divideBy - 1) / divideBy; // ceil 整数除法
			entry.setValue(divided);
			this.visSize += divided - oldValue;
		}
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> multiply(int multiplier){
		for (Map.Entry<Asp, Integer> entry : aspects.object2IntEntrySet()) {
			int oldValue = entry.getValue();
			int newValue = oldValue * multiplier;
			entry.setValue(newValue);
			this.visSize += newValue - oldValue;
		}
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> multiplyAndCeil(float multiplier){
		for (Map.Entry<Asp, Integer> entry : aspects.object2IntEntrySet()) {
			int oldValue = entry.getValue();
			int newValue = (int)Math.ceil(oldValue*multiplier);
			entry.setValue(newValue);
			this.visSize += newValue - oldValue;
		}
		return this;
	}

	
	/**
	 * Adds this aspect and amount to the collection. 
	 * If the aspect exists then only the highest ofAspectVisList the old or new amount will be used.
	 * @param aspect to merge
	 * @param amount to merge
	 * @return self
	 */
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> mergeWithHighest(Asp aspect, int amount) {
		if (amount < 0){return this;}
		int oldValue = this.aspects.getOrDefault(aspect, 0);
		if (amount < oldValue) {
			amount = oldValue;
		}
		this.aspects.put( aspect, amount );
		this.visSize += amount - oldValue;
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public int merge(Asp aspect, int amount, IntBinaryOperator chooser) {
		var oldValue = this.aspects.getOrDefault(aspect, 0);
		int result = this.aspects.mergeInt(aspect, amount, chooser::applyAsInt);
		var newValue = this.aspects.getOrDefault(aspect, 0);
		this.visSize += newValue - oldValue;
		return result;
	}

	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> addAll(AspectList<Asp> in) {
		for (var a:in.getAspectTypes()) {
			this.addAll(a, in.getAmount(a));
		}
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> mergeWithHighest(AspectList<Asp> in) {
		for (var a:in.getAspectTypes()) {
			this.mergeWithHighest(a, in.getAmount(a));
		}
//		recalculateVisSize();//calculated each loop above
		return this;
	}

	public void forEach(java.util.function.ObjIntConsumer<Asp> action) {
		var iterator = aspects.object2IntEntrySet().fastIterator();
		while (iterator.hasNext()) {
			var entry = iterator.next();
			action.accept(entry.getKey(), entry.getIntValue());
		}
	}

	public void putAllAspects(AspectList<Asp> aspects) {
		int visSizeChange = 0;
		for (var entry:aspects.aspects.object2IntEntrySet()){
			visSizeChange += entry.getIntValue();
			visSizeChange -= this.aspects.put(entry.getKey(), entry.getIntValue());
		}
		this.visSize += visSizeChange;
	}


	public interface AspIntIntBiFunction<Asp extends Aspect> {

		/**
		 * Applies this function to the given arguments.
		 *
		 * @param t the first function argument
		 * @param u the second function argument
		 * @return the function result
		 */
		int apply(Asp t, int u);

	}
	public void replaceAll(AspIntIntBiFunction<Asp> biFunction){
		aspects.object2IntEntrySet().forEach(entry -> {
			int newValue = biFunction.apply(entry.getKey(), entry.getIntValue());
			entry.setValue(newValue);
		});
	}

	@SafeVarargs
	public static <Asp extends Aspect> AspectList<Asp> of(Asp... aspects){
		AspectList<Asp> out = new AspectList<>();
		for (var aspect : aspects){
			if (aspect != null){
				out.addAll(aspect,1);
			}
		}
		return out;
	}

	public @Nullable("if empty") Asp randomAspect(RandomSource randomSource){
		if (this.size() == 0){
			return null;
		}
		var list = this.aspects.keySet().stream().toList();
		return list.get(randomSource.nextInt(list.size()));
	}
	public @Nullable("if empty") Asp randomWeightedAspect(RandomSource randomSource){
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


	public boolean isEmpty(){
		return this.aspects.isEmpty();
	}

	@Override
	public String toString() {
		return this.getClass() + "{" +
				"aspects=" + aspects +
				", aspectView=" + Arrays.toString(aspects.object2IntEntrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getIntValue())
                .toArray()) +
				'}';
	}

	private void recalculateVisSize(){
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


}
