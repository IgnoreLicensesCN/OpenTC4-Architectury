package thaumcraft.api.aspects.aspectlists.baseimpl;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

//with my anger of always ordered.
public class HashAspectList<Asp extends Aspect> extends AbstractAspectList<Asp, Object2IntOpenHashMap<Asp>> {

    public HashAspectList() {
        super(new Object2IntOpenHashMap<>());
    }

    protected HashAspectList(@NotNull Object2IntOpenHashMap<Asp> aspects) {
        super(aspects);
    }
    public HashAspectList(@NotNull Object2IntMap<Asp> aspects) {
        super(new Object2IntOpenHashMap<>(aspects));
    }


    public void forEach(ObjectIntBiConsumer<Asp> action) {
        aspects.forEach(action);
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

    public static <Asp extends Aspect> HashAspectList<Asp> viewOf(Object2IntOpenHashMap<Asp> aspects) {
        return new HashAspectList<>(aspects);
    }
}
