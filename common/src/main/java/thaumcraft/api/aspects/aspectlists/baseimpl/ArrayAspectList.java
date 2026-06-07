package thaumcraft.api.aspects.aspectlists.baseimpl;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIntBiConsumer;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public class ArrayAspectList<Asp extends Aspect> extends AbstractAspectList<Asp,Object2IntArrayMap<Asp>> {

    public ArrayAspectList() {
        super(new Object2IntArrayMap<>());
    }

    protected ArrayAspectList(Object2IntArrayMap<Asp> original) {
        super(original);
    }

    public ArrayAspectList(@NotNull Object2IntMap<Asp> aspects) {
        super(new Object2IntArrayMap<>(aspects));
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



    public static <Asp extends Aspect> ArrayAspectList<Asp> viewOf(Object2IntArrayMap<Asp> aspects) {
        return new ArrayAspectList<>(aspects);
    }

}
