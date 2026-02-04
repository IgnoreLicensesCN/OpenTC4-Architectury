package thaumcraft.api.expands;

import com.linearity.opentc4.utils.CompoundTagHelper;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;

public class UnmodifiableAspectList<A extends Aspect> extends AspectList<A> {

    public static final AspectList<Aspect> EMPTY = new UnmodifiableAspectList(new AspectList());

    public UnmodifiableAspectList(AspectList<Aspect> viewingList) {
        if (viewingList != null) {
            this.aspects.putAll(viewingList.getAspects());
        }
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
    public AspectList<A> addAll(AspectList<A> in) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> addAll(Aspect aspect, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> reduceAndRemoveIfNegative(Aspect key) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> reduceAndRemoveIfNegative(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean reduce(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void loadFrom(CompoundTag tag) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void loadFrom(CompoundTag tag, CompoundTagHelper.ListTagAccessor accessor) {
        throw new RuntimeException("Unmodifiable!");
    }

    public void putAllAspects(AspectList aspects) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void replaceAll(BiFunction<Aspect, Integer, Integer> biFunction) {
        throw new RuntimeException("Unmodifiable!");
    }
}
