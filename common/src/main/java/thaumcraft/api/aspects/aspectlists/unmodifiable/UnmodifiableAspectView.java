package thaumcraft.api.aspects.aspectlists.unmodifiable;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntBiConsumer;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.AspectListUnmodifiableDefault;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;

import java.util.List;
import java.util.Set;

public class UnmodifiableAspectView<A extends Aspect> implements AspectListUnmodifiableDefault<A> {
    public static final UnmodifiableAspectView<Aspect> EMPTY = new UnmodifiableAspectView<>(UnmodifiableAspectList.EMPTY);
    private final AspectList<A> viewingList;
    public UnmodifiableAspectView(AspectList<A> viewingList) {
        this.viewingList = viewingList;
    }
    public UnmodifiableAspectView(Object2IntLinkedOpenHashMap<A> viewingMap) {
        this.viewingList = LinkedHashAspectList.viewOf(viewingMap);
    }

    @Override
    public @NotNull("empty -> empty(aspect)") A getFirstAspect() {
        return this.viewingList.getFirstAspect();
    }

    @Override
    public boolean containsKey(Aspect aspect) {
        return viewingList.containsKey(aspect);
    }

    @Override
    public String toString() {
        return "UnmodifiableAspectView(" + viewingList + ")";
    }

    @Override
    public boolean isEmpty() {
        return this.viewingList.isEmpty();
    }

    @Override
    public A randomAspect(RandomSource randomSource) {
        return this.viewingList.randomAspect(randomSource);
    }

    @Override
    public @Nullable("if empty") A randomWeightedAspect(RandomSource randomSource) {
        return viewingList.randomWeightedAspect(randomSource);
    }

    @Override
    public void forEach(ObjectIntBiConsumer<A> action) {
        this.viewingList.forEach(action);
    }

    @Override
    public boolean forEachWithBreak(ObjInt2BooleanFunction<A> action) {
        return viewingList.forEachWithBreak(action);
    }

    @Override
    public void acceptForIndex(int index, ObjectIntBiConsumer<A> action) {
        this.viewingList.acceptForIndex(index,action);
    }

    @Override
    public int get(Aspect key) {
        return this.viewingList.get(key);
    }

    @Override
    public List<A> getAspectsSortedAmount() {
        return this.viewingList.getAspectsSortedAmount();
    }

    @Override
    public List<A> getAspectsSorted() {
        return this.viewingList.getAspectsSorted();
    }

    @Override
    public AspectList<PrimalAspect> getPrimalAspects() {
        return this.viewingList.getPrimalAspects();
    }

    @Override
    public Set<A> keySet() {
        return this.viewingList.keySet();
    }

    @Override
    public int visSize() {
        return this.viewingList.visSize();
    }

    @Override
    public int size() {
        return this.viewingList.size();
    }

    @Override
    public int getOrDefault(Aspect aspect, int defaultValue) {
        return this.viewingList.getOrDefault(aspect, defaultValue);
    }

    @Override
    public AspectList<A> copy() {
        return this.viewingList.copy();
    }

}
