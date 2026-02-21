package thaumcraft.api.aspects;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.common.Thaumcraft;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

//TODO:[maybe wont finished] change Aspect count to Rational(will surly shake the whole TC4)
//2026.Feb.4 now we have AspectList<PrimalAspect>
public class AspectList<Asp extends Aspect> implements Serializable {

	private int visSize;//
	protected final LinkedHashMap<Asp,Integer> aspects;//aspects associated with this object
	private final Map<Asp,Integer> aspectView;
	public Map<Asp,Integer> getAspectView(){
		return aspectView;
	}
	
	public AspectList() {
		this.aspects = new LinkedHashMap<>();
		this.aspectView = Collections.unmodifiableMap(this.aspects);
		recalculateVisSize();
	}
	public AspectList(Asp aspect, int value) {
		this.aspects = new LinkedHashMap<>();
		this.aspects.put(aspect,value);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
		recalculateVisSize();
	}
	protected AspectList(LinkedHashMap<Asp,Integer> aspects) {
		this.aspects = aspects;
		this.aspectView = Collections.unmodifiableMap(this.aspects);
		recalculateVisSize();
	}
	public AspectList(Map<Asp,Integer> aspects) {
		this.aspects = new LinkedHashMap<>(aspects);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
		recalculateVisSize();
	}

	public AspectList(int size,float loadFactor) {
		this.aspects = new LinkedHashMap<>(size,loadFactor);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
		recalculateVisSize();
	}

	public AspectList(AspectList<Asp> another) {
		this.aspects = new LinkedHashMap<>();
		this.aspects.putAll(another.aspects);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
		recalculateVisSize();
	}


    public static <A extends Aspect> void addAspectDescriptionToList(AspectList<A> aspects, Player player, List<Component> aspectDescriptions) {
       if (aspects != null && !aspects.aspects.isEmpty()) {
          for(var aspect : aspects.getAspectsSorted()) {
             if (Thaumcraft.playerKnowledge.hasDiscoveredAspect(player, aspect)) {
                aspectDescriptions.add(Component.literal(aspect.getName() + " x " + aspects.getAmount(aspect)));
             } else {
                aspectDescriptions.add(Component.translatable("tc.aspect.unknown"));
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

	@UnmodifiableView
	public Set<Map.Entry<Asp,Integer>> entrySet(){
		return aspectView.entrySet();
	}

	@UnmodifiableView
	public Set<Asp> keySet() {
		return aspectView.keySet();
	}

	public Integer get(Asp aspect) {
		return aspects.get(aspect);
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
		for (var aspectAndAmount:aspects.entrySet()) {
			var aspect = aspectAndAmount.getKey();
			var amount = aspectAndAmount.getValue();
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
		return  aspects.get(key)==null?0:aspects.get(key);
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
			visSize += amount;;
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
		int reducedResult = getAmount(key) - amount;
		if (reducedResult<=0) {
			aspects.remove(key);
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
		var removed = aspects.remove(key);
		if (removed != null) {
			this.visSize -= removed;
		}
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> removeIf(Predicate<Map.Entry<Asp,Integer>> filter) {
		aspects.entrySet().removeIf(entry -> {
			if (filter.test(entry)) {
				this.visSize -= entry.getValue();
				return true;
			}
			return false;
		});
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> removeIfNotPositive() {
		removeIf(aspectIntegerEntry -> aspectIntegerEntry.getValue() <= 0);
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
		var oldValue = this.aspects.getOrDefault(aspect, 0);
		this.aspects.put( aspect, amount );
		this.visSize += amount - oldValue;
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> divideAndCeil(int divideBy){
		if (divideBy==0){
			throw new IllegalArgumentException("division by zero");
		}
		for (Map.Entry<Asp, Integer> entry : aspects.entrySet()) {
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
		for (Map.Entry<Asp, Integer> entry : aspects.entrySet()) {
			int oldValue = entry.getValue();
			int newValue = oldValue * multiplier;
			entry.setValue(newValue);
			this.visSize += newValue - oldValue;
		}
		return this;
	}
	@SuppressWarnings("UnusedReturnValue")
	public AspectList<Asp> multiplyAndCeil(float multiplier){
		for (Map.Entry<Asp, Integer> entry : aspects.entrySet()) {
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
	public int merge(Asp aspect, int amount,BiFunction<Integer,Integer,Integer> chooser) {
		var oldValue = this.aspects.getOrDefault(aspect, 0);
		int result = this.aspects.merge(aspect, amount, chooser);
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
		recalculateVisSize();//TODO:[maybe wont finished]Make faster
		return this;
	}


	public void forEach(BiConsumer<Asp,Integer> action) {
		aspectView.forEach(action);
	}

	public void putAllAspects(AspectList<Asp> aspects) {
		this.aspects.putAll(aspects.aspects);
		recalculateVisSize();//TODO:[maybe wont finished]Make faster
	}


	public void replaceAll(BiFunction<Asp,Integer,Integer> biFunction){
		aspects.replaceAll(biFunction);
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

	public Asp randomAspect(RandomSource randomSource){
		if (this.size() == 0){
			return null;
		}
		var list = this.aspects.keySet().stream().toList();
		return list.get(randomSource.nextInt(list.size()));
	}

	public boolean isEmpty(){
		return this.aspects.isEmpty();
	}

	@Override
	public String toString() {
		return this.getClass() + "{" +
				"aspects=" + aspects +
				", aspectView=" + Arrays.toString(aspects.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .toArray()) +
				'}';
	}

	private void recalculateVisSize(){
		this.visSize = 0;
		this.aspectView.values().forEach(i -> this.visSize += i);
	}
}
