package thaumcraft.api.expands;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.LinkedHashMap;

public class UnmodifiableAspectList extends AspectList {

    public UnmodifiableAspectList(AspectList viewingList) {
        if (viewingList == null) {
            this.aspects = new LinkedHashMap<>();
        }
        else {
            this.aspects = viewingList.aspects;
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
}
