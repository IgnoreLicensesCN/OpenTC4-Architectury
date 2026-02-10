package thaumcraft.api.aspects;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

//TODO:[maybe wont finished] change Aspect count to Rational(will surly shake the whole TC4)
//2026.Feb.4 now we have AspectList<PrimalAspect>
public class AspectList<Asp extends Aspect> implements Serializable {
	
	protected final LinkedHashMap<Asp,Integer> aspects;//aspects associated with this object
	public LinkedHashMap<Asp,Integer> getAspectsInternal(){
		return aspects;
	}
	public final Map<Asp,Integer> aspectView;
	
	/**
	 * this creates a new aspect list with preloaded values based off the aspects ofAspectVisList the given item.
	 * @param stack the itemstack ofAspectVisList the given item
	 */
	public static AspectList<Aspect> of(ItemStack stack) {
		var result = new AspectList<>();
		AspectList<Aspect> temp = ThaumcraftApiHelper.getObjectAspects(stack);
		if (temp!=null) {
			for (Aspect tag : temp.getAspectTypes()) {
				result.addAll(tag, temp.getAmount(tag));
			}
		}
		return result;
	}
	
	public AspectList() {
		this.aspects = new LinkedHashMap<>();
		this.aspectView = Collections.unmodifiableMap(this.aspects);
	}
	public AspectList(Asp aspect, int value) {
		this.aspects = new LinkedHashMap<>();
		this.aspects.put(aspect,value);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
	}
	protected AspectList(LinkedHashMap<Asp,Integer> aspects) {
		this.aspects = aspects;
		this.aspectView = Collections.unmodifiableMap(this.aspects);
	}
	public AspectList(Map<Asp,Integer> aspects) {
		this.aspects = new LinkedHashMap<>(aspects);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
	}

	public AspectList(int size,float loadFactor) {
		this.aspects = new LinkedHashMap<>(size,loadFactor);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
	}

	public AspectList(AspectList<Asp> another) {
		this.aspects = new LinkedHashMap<>();
		this.aspects.putAll(another.aspects);
		this.aspectView = Collections.unmodifiableMap(this.aspects);
	}


    public static <A extends Aspect> void addAspectDescriptionToList(AspectList<A> aspects, Player player, List<Component> aspectDescriptions) {
       if (aspects != null && !aspects.aspects.isEmpty()) {
          for(var aspect : aspects.getAspectsSorted()) {
             if (Thaumcraft.playerKnowledge.hasDiscoveredAspect(player.getGameProfile().getName(), aspect)) {
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

	public Set<Map.Entry<Asp,Integer>> entrySet(){
		return aspects.entrySet();
	}
	public Set<Asp> keySet() {
		return aspects.keySet();
	}

	public Integer get(Asp aspect) {
		return aspects.get(aspect);
	}

	public static <A extends Aspect> AspectList<A> of(Map<A,Integer> aspects) {
		AspectList<A> out = new AspectList<>();
		for (var entry:aspects.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();
			out.addAll(key, value);
		}
		return out;
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
		int q = 0;
		
		for (var as:aspects.keySet()) {
			q+=this.getAmount(as);
		}
		
		return q;
	}
	
	/**
	 * @return an array(a list now because Asp[] is not so fine) ofAspectVisList all the aspects in this collection
	 */
	public Set<Asp> getAspectTypes() {
		return aspects.keySet();
	}
	
	/**
	 * @return an array ofAspectVisList all the aspects in this collection
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
		return this.keySet().stream()
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
		return aspects.keySet().stream()
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
		if (getAmount(key)>=amount) {
			int am = getAmount(key)-amount;
			aspects.put(key, am);
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
	public AspectList<Asp> reduceAndRemoveIfNotPositive(Asp key, int amount) {
		int am = getAmount(key) - amount;
		if (am<=0) {
			aspects.remove(key);
		}
		else {
			this.aspects.put(key, am);
		}
		return this;
	}
	
	/**
	 * Simply removes the aspect from the list
	 * @param key
//	 * @param amount
	 * @return self
	 */
	public AspectList<Asp> remove(Asp key) {
		aspects.remove(key); 
		return this;
	}
	
	/**
	 * Adds this aspect and amount to the collection. 
	 * If the aspect exists then its value will be increased by the given amount.
	 * @param aspect to add
	 * @param amount to add
	 * @return self
	 */
	public AspectList<Asp> addAll(Asp aspect, int amount) {
		if (aspect == null){
			throw new NullPointerException("aspect is null");
		}
		if (this.aspects.containsKey(aspect)) {
			int oldamount = this.aspects.get(aspect);
			amount += oldamount;
		}
		this.aspects.put( aspect, amount );
		return this;
	}
	public AspectList<Asp> set(Asp aspect, int amount) {
		if (aspect == null){
			throw new NullPointerException("aspect is null");
		}
		this.aspects.put( aspect, amount );
		return this;
	}
	public AspectList<Asp> divideAndCeil(int divideBy){
		if (divideBy==0){
			throw new IllegalArgumentException("division by zero");
		}
		for (Map.Entry<Asp, Integer> entry : aspects.entrySet()) {
			int value = entry.getValue();
			// 除以 divideBy 向上取整
			int divided = (value + divideBy - 1) / divideBy; // ceil 整数除法
			entry.setValue(divided);
		}
		return this;
	}
	public AspectList<Asp> multiply(int multiplier){
		for (Map.Entry<Asp, Integer> entry : aspects.entrySet()) {
			int value = entry.getValue();
			entry.setValue(value*multiplier);
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
	public AspectList<Asp> mergeWithHighest(Asp aspect, int amount) {
		if (this.aspects.containsKey(aspect)) {
			int oldamount = this.aspects.get(aspect);
			if (amount<oldamount) amount=oldamount;
			
		}
		this.aspects.put( aspect, amount );
		return this;
	}
	public int merge(Asp aspect, int amount,BiFunction<Integer,Integer,Integer> chooser) {
		return this.aspects.merge(aspect, amount, chooser);
	}
	
	public AspectList<Asp> addAll(AspectList<Asp> in) {
		for (var a:in.getAspectTypes())
			this.addAll(a, in.getAmount(a));
		return this;
	}
	
	public AspectList<Asp> mergeWithHighest(AspectList<Asp> in) {
		for (var a:in.getAspectTypes())
			this.mergeWithHighest(a, in.getAmount(a));
		return this;
	}


	public void forEach(BiConsumer<Asp,Integer> action) {
		aspects.forEach(action);
	}

	public void putAllAspects(AspectList<Asp> aspects) {
		this.aspects.putAll(aspects.aspects);
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
}
