package thaumcraft.api.aspects;

import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;
import java.util.function.BiFunction;

@UnmodifiableView
public class UnmodifiableCentiVisList<Asp extends Aspect> extends CentiVisList<Asp>{
    public static final UnmodifiableCentiVisList<Aspect> EMPTY = new UnmodifiableCentiVisList<Aspect>();
    public UnmodifiableCentiVisList() {
        super();
    }
    public UnmodifiableCentiVisList(Map<Asp, Integer> aspects) {
        super(aspects);
    }
    @Override
    public Integer put(Asp aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean reduce(Asp key, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> reduceAndRemoveIfNotPositive(Asp key, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> remove(Asp key) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> addAll(Asp aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> set(Asp aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> mergeWithHighest(Asp aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public int merge(Asp aspect, int amount, BiFunction<Integer, Integer, Integer> chooser) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> addAll(AspectList<Asp> in) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<Asp> mergeWithHighest(AspectList<Asp> in) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void putAllAspects(AspectList<Asp> aspects) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void replaceAll(BiFunction<Asp, Integer, Integer> biFunction) {
        throw new RuntimeException("Unmodifiable!");
    }
}
