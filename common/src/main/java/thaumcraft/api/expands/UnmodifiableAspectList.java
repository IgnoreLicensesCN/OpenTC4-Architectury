package thaumcraft.api.expands;

import com.linearity.opentc4.utils.CompoundTagHelper;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;

public class UnmodifiableAspectList extends AspectList {

    public static final AspectList EMPTY = new UnmodifiableAspectList(new AspectList());

    public UnmodifiableAspectList(AspectList viewingList) {
        if (viewingList != null) {
            this.aspects.putAll(viewingList.getAspects());
        }
    }

    @Override
    public AspectList copy() {
        return super.copy();
    }

    @Override
    public AspectList merge(AspectList in) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList merge(Aspect aspect, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList add(AspectList in) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList add(Aspect aspect, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList remove(Aspect key) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList remove(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean reduce(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void load(CompoundTag tag) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void load(CompoundTag tag, CompoundTagHelper.ListTagAccessor accessor) {
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
