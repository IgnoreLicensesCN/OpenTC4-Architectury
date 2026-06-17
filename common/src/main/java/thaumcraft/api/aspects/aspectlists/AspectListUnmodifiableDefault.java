package thaumcraft.api.aspects.aspectlists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import thaumcraft.api.aspects.Aspect;

import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

public interface AspectListUnmodifiableDefault<Asp extends Aspect> extends AspectList<Asp>{
    @Override
    default boolean tryReduce(Asp key, int amount){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void reduceAndRemoveIfNotPositive(Asp key, int amount){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default int remove(Asp key){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void removeIf(Predicate<Object2IntMap.Entry<Asp>> filter){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void removeIfNotPositive() {
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void replaceAll(AspIntIntBiFunction<Asp> biFunction){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void addAll(AspectList<Asp> in){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void addAll(Asp aspect, int amount){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void set(Asp aspect, int amount){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void divideAndCeil(int divideBy){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void multiply(int multiplier){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void multiplyAndCeil(float multiplier){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void mergeWithHighest(AspectList<Asp> in){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default int merge(Asp aspect, int amount, IntBinaryOperator chooser){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void mergeWithHighest(Asp aspect, int amount){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void overrideAllAspects(AspectList<Asp> aspects){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default void clear(){
        throw new UnsupportedOperationException("Unmodifiable!");
    }

    @Override
    default int put(Asp aspect, int amount){
        throw new UnsupportedOperationException("Unmodifiable!");
    }
}
