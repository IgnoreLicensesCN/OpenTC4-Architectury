package thaumcraft.api.expands;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.function.BiFunction;

public class UnmodifiableAspectList<A extends Aspect> extends AspectList<A> {

    public static final AspectList<Aspect> EMPTY = new UnmodifiableAspectList<>(new AspectList<>());

    public UnmodifiableAspectList(AspectList<A> viewingList) {
        if (viewingList != null) {
            this.aspects.putAll(viewingList.aspectView);
        }
    }

    @Override
    public Integer put(A aspect, int amount) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> copy() {
        return super.copy();
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
    public int merge(A aspect, int amount, BiFunction<Integer, Integer, Integer> chooser) {
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
    public AspectList<A> reduceAndRemoveIfNotPositive(Aspect key) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> reduceAndRemoveIfNotPositive(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean reduce(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    public void putAllAspects(AspectList<A> aspects) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void replaceAll(BiFunction<A, Integer, Integer> biFunction) {
        throw new RuntimeException("Unmodifiable!");
    }
}
