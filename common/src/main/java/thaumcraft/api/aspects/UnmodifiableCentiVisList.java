package thaumcraft.api.aspects;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.function.IntBinaryOperator;

@UnmodifiableView
public class UnmodifiableCentiVisList<Asp extends Aspect> extends CentiVisList<Asp>{
    public static final UnmodifiableCentiVisList<Aspect> EMPTY = new UnmodifiableCentiVisList<>();
    public UnmodifiableCentiVisList() {
        super();
    }
    public UnmodifiableCentiVisList(AspectList<Asp> aspects) {
        super(aspects);
    }
    public UnmodifiableCentiVisList(Object2IntMap<Asp> aspects) {
        super(aspects);
    }
    @Override
    public Integer put(Asp aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean tryReduce(Asp key, int amount) {
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
    public int merge(Asp aspect, int amount, IntBinaryOperator chooser) {
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
    public void replaceAll(AspIntIntBiFunction<Asp> biFunction) {
        throw new RuntimeException("Unmodifiable!");
    }
}
