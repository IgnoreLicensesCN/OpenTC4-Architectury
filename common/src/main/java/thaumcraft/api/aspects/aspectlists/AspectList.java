package thaumcraft.api.aspects.aspectlists;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.PrimalAspect;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntBinaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

//TODO:[maybe wont finished] change Aspect count to Rational(will surly shake the whole TC4)
//TODO:[maybe wont finished] even faster impl(long[] intID and amount for each long)
//2026.Feb.4 now we have AspectList<PrimalAspect>
public interface AspectList<Asp extends Aspect> /*implements Serializable */{

	@Deprecated(forRemoval = true,since = "implements IAspectDisplayItem")
	void addAspectDescriptionToList(@Nullable Player player, List<Component> aspectDescriptions);

	int getOrDefault(Aspect aspect, int defaultValue);
	/**
	 * @param aspect aspect as key
	 * @return the amount associated with the given aspect in this collection
	 */
	default int get(Aspect aspect){
		return getOrDefault(aspect,0);
	}
	int put(Asp aspect, int amount);
	//for unmodifiable list(not view,something like record or whatever)it may avoid create new instance
	AspectList<Asp> copy();

	@UnmodifiableView//at least user shouldn't modify it
	Set<Asp> keySet();


	/**
	 * @return the amount of different aspects in this collection
	 */
	int size();

	/**
	 * @return the amount of total vis in this collection
	 */
	int visSize();


	/**
	 * @return an array of all the primal aspects in this collection
	 */
	@Unmodifiable
	AspectList<PrimalAspect> getPrimalAspects();

	/**
	 * @return an array(a list now because Asp[] is not so fine) of all the aspects in this collection sorted by name
	 * --from Hodgepodge
	 */
	List<Asp> getAspectsSorted();

	/**
	 * @return an array(a list now because Asp[] is not so fine) of all the aspects in this collection sorted by amount
	 */
	List<Asp> getAspectsSortedAmount();

	/**
	 * Reduces the amount of an aspect in this collection by the given amount.
	 * @param key to remove
	 * @param amount to remove
	 * @return succeed(false if will lead to negative)
	 */
	boolean tryReduce(Asp key, int amount);

	/**
	 * Reduces the amount of an aspect in this collection by the given amount.
	 * If reduced to 0 or less the aspect will be removed completely.
	 * @param key to remove
	 * @param amount to remove
	 *
	 */

	void reduceAndRemoveIfNotPositive(Asp key, int amount);

	/**
	 * Simply removes the aspect from the list
	 * @param key
//	 * @param amount
	 *
	 */

	void remove(Asp key);

	void removeIf(Predicate<Object2IntMap.Entry<Asp>> filter);

	default void removeIfNotPositive() {
		removeIf(aspectIntegerEntry -> aspectIntegerEntry.getIntValue() <= 0);
	}

	/**
	 * Adds this aspect and amount to the collection.
	 * <s>If the aspect exists then its value will be increased by the given amount.</s>
	 * @param aspect to add
	 * @param amount to add
	 *
	 */

	void addAll(Asp aspect, int amount);

	void set(Asp aspect, int amount);

	void divideAndCeil(int divideBy);

	void multiply(int multiplier);

	void multiplyAndCeil(float multiplier);


	/**
	 * Adds this aspect and amount to the collection.
	 * If the aspect exists then only the highest of the old or new amount will be used.
	 * @param aspect to merge
	 * @param amount to merge
	 *
	 */

	void mergeWithHighest(Asp aspect, int amount);

	int merge(Asp aspect, int amount, IntBinaryOperator chooser);


	void addAll(AspectList<Asp> in);


	void mergeWithHighest(AspectList<Asp> in);

	void forEach(ObjIntConsumer<Asp> action);
	//true if action returns true(and will break loop)
	boolean forEachWithBreak(ObjInt2BooleanFunction<Asp> action);
	void acceptForIndex(int index,ObjIntConsumer<Asp> action);

	void overrideAllAspects(AspectList<Asp> aspects);

	interface AspIntIntBiFunction<Asp extends Aspect> {

		/**
		 * Applies this function to the given arguments.
		 *
		 * @param t the first function argument
		 * @param u the second function argument
		 * @return the function result
		 */
		int apply(Asp t, int u);

	}
	void replaceAll(AspIntIntBiFunction<Asp> biFunction);

	@Nullable("if empty") Asp randomAspect(RandomSource randomSource);
	@Nullable("if empty") Asp randomWeightedAspect(RandomSource randomSource);


	boolean isEmpty();

	void clear();

	@NotNull("empty -> empty(aspect)") Asp getFirstAspect();

	default @NotNull("empty -> empty(aspect)") Asp getAspectAtIndexEnsureInBound(int index){
		return getAspectAtIndex((index&Integer.MAX_VALUE)%size());
	}
	//i say do we really need this?we should have better way.
	//make this AspectList ordered is just for UI performance i even want it's order not ruled.
	default @NotNull("empty -> empty(aspect)") Asp getAspectAtIndex(int index) {
		AtomicInteger i = new AtomicInteger(index);
		AtomicReference<Asp> currentAsp = new AtomicReference<>((Asp)Aspects.EMPTY);
		forEachWithBreak((asp,value) -> {
			currentAsp.set(asp);
            return i.getAndDecrement() == 0;
        });
		return currentAsp.get();
	}
	boolean containsKey(Aspect aspect);
	default boolean containsEnough(Aspect aspect,int amount){
		return getOrDefault(aspect,0) >= amount;
	}
}
