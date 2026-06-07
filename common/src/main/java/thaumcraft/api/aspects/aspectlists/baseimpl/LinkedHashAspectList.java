package thaumcraft.api.aspects.aspectlists.baseimpl;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIntBiConsumer;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

import java.util.*;

//default impl
public class LinkedHashAspectList<Asp extends Aspect>
        extends AbstractAspectList<Asp,Object2IntLinkedOpenHashMap<Asp>> {


    public LinkedHashAspectList() {
        super(new Object2IntLinkedOpenHashMap<>());
    }

    protected LinkedHashAspectList(@NotNull Object2IntLinkedOpenHashMap<Asp> aspects) {
        super(aspects);
    }

    public LinkedHashAspectList(@NotNull Map<Asp, Integer> aspects) {
        super(new Object2IntLinkedOpenHashMap<>(aspects));
    }

    public LinkedHashAspectList(@NotNull Object2IntMap<Asp> aspects) {
        super(new Object2IntLinkedOpenHashMap<>(aspects));
    }

    public LinkedHashAspectList(int size, float loadFactor) {
        super(new Object2IntLinkedOpenHashMap<>(size, loadFactor));
    }

    public LinkedHashAspectList(@NotNull LinkedHashAspectList<Asp> another) {
        super(new Object2IntLinkedOpenHashMap<>(another.aspects));
    }

    public void forEach(ObjectIntBiConsumer<Asp> action) {
        aspects.object2IntEntrySet().fastForEach((entry) -> action.accept(entry.getKey(), entry.getIntValue()));
    }

    //true if action returns true(and will break loop)
    public boolean forEachWithBreak(ObjInt2BooleanFunction<Asp> action) {
        var iterator = aspects.object2IntEntrySet().fastIterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (action.accept(entry.getKey(), entry.getIntValue())) {
                return true;
            }
        }
        return false;
    }

    public void acceptForIndex(int index, ObjectIntBiConsumer<Asp> action) {
        if (aspects.size() <= index) {
            throw new IndexOutOfBoundsException(
                    "Index out of bound!Expected smaller than " + aspects.size() + ", got " + index
            );
        }
        var iterator = aspects.object2IntEntrySet().fastIterator();
        int indexCurrent = 0;
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (indexCurrent == index) {
                action.accept(entry.getKey(), entry.getIntValue());
                return;
            }
            indexCurrent += 1;
        }

    }

    @SafeVarargs
    public static <Asp extends Aspect> LinkedHashAspectList<Asp> of(Asp... aspects) {
        LinkedHashAspectList<Asp> out = new LinkedHashAspectList<>();
        for (var aspect : aspects) {
            if (aspect != null) {
                out.addAll(aspect, 1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> LinkedHashAspectList<Asp> viewOf(Object2IntLinkedOpenHashMap<Asp> aspects) {
        return new LinkedHashAspectList<>(aspects);
    }
}
