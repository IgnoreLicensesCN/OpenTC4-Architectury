package thaumcraft.api.aspects.aspectlists;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import oshi.annotation.concurrent.NotThreadSafe;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.api.aspects.PrimalAspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UnmodifiableView
public class UnmodifiableCentiVisList<Asp extends Aspect> extends UnmodifiableAspectList<Asp> implements CentiVisList<Asp> {
    public static final UnmodifiableCentiVisList<Aspect> EMPTY = new UnmodifiableCentiVisList<>(new Object2IntLinkedOpenHashMap<>()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };
    public static final UnmodifiableCentiVisList<PrimalAspect> EMPTY_PRIMAL = new UnmodifiableCentiVisList<>(new Object2IntLinkedOpenHashMap<>()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };
    public static final UnmodifiableCentiVisList<CompoundAspect> EMPTY_COMPOUND = new UnmodifiableCentiVisList<>(new Object2IntLinkedOpenHashMap<>()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    protected UnmodifiableCentiVisList(@NotNull CentiVisList<Asp> viewingList) {
        super(viewingList);
    }
    protected UnmodifiableCentiVisList(@NotNull AspectList<Asp> viewingList) {
        super(viewingList);
    }

    protected UnmodifiableCentiVisList(@NotNull Object2IntLinkedOpenHashMap<Asp> aspects) {
        super(aspects);
    }

    private static final Map<Aspect,UnmodifiableCentiVisList<Aspect>> aspectMapOfSingleAspect = new HashMap<>();
    //reducing CME in case someone wants multi-threading
    static {
        Aspects.ALL_ASPECTS.forEach((aspectKey, aspect) -> {
            var map = new Object2IntLinkedOpenHashMap<Aspect>();
            map.put(aspect,1);
            var listToPut = new UnmodifiableCentiVisList<>(map);
            aspectMapOfSingleAspect.put(aspect,listToPut);
        });
    }
    @NotThreadSafe//at least for writing unless you've already created those instances in someway like during loading?
    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> ofSingle(Asp aspect){
        return (UnmodifiableCentiVisList<Asp>) aspectMapOfSingleAspect.computeIfAbsent(aspect, asp -> {
            var map = new Object2IntLinkedOpenHashMap<Aspect>();
            map.put(asp,1);
            return new UnmodifiableCentiVisList<>(map);
        });
    }
    private static final Map<Aspect, Int2ObjectMap<UnmodifiableCentiVisList<Aspect>>> aspectMapOfSingleAspectMultiValue = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(){
        return (UnmodifiableCentiVisList<Asp>) UnmodifiableCentiVisList.EMPTY;
    }

    @SafeVarargs
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(Asp... aspects){
        if (aspects.length == 1){
            return ofSingle(aspects[0]);
        }
        CentiVisList<Asp> out = new LinkedTreeCentiVisList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return new UnmodifiableCentiVisList<>(out);
    }

    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(@NotNull CentiVisList<Asp> aspects) {
        if (aspects instanceof UnmodifiableCentiVisList<Asp> UnmodifiableCentiVisList){
            return UnmodifiableCentiVisList;
        }
        if (aspects.isEmpty()){
            return of();
        }
        return new UnmodifiableCentiVisList<>(aspects);
    }
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> ofDirectly(@NotNull AspectList<Asp> aspects) {
        if (aspects instanceof UnmodifiableCentiVisList<Asp> UnmodifiableCentiVisList){
            return UnmodifiableCentiVisList;
        }
        if (aspects.isEmpty()){
            return of();
        }
        return new UnmodifiableCentiVisList<>(aspects);
    }

    @NotThreadSafe//unless you've already created those instances via this.
    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(Asp aspect,int value){
        if (value == 1){
            return ofSingle(aspect);
        }
        return (UnmodifiableCentiVisList<Asp>) aspectMapOfSingleAspectMultiValue
                .computeIfAbsent(aspect,_ignored -> new Int2ObjectOpenHashMap<>())
                .computeIfAbsent(value,aspectAmount -> {
                    var map = new Object2IntLinkedOpenHashMap<Aspect>();
                    map.put(aspect,aspectAmount);
                    return new UnmodifiableCentiVisList<>(map);
                });
    }

    //you should cache it yourself now.
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(Asp aspect,int value,Asp aspect2,int value2){

        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        return new UnmodifiableCentiVisList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(Asp aspect,int value,Asp aspect2,int value2,Asp aspect3,int value3){

        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        map.put(aspect3,value3);
        return new UnmodifiableCentiVisList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(
            Asp aspect,int value,
            Asp aspect2,int value2,
            Asp aspect3,int value3,
            Asp aspect4,int value4
    ){
        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        map.put(aspect3,value3);
        map.put(aspect4,value4);
        return new UnmodifiableCentiVisList<>(map);
    }

    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> of(Object2IntLinkedOpenHashMap<Asp> aspects) {
        return new UnmodifiableCentiVisList<>(aspects);
    }
//    public static <Asp extends Aspect> UnmodifiableCentiVisList<Asp> viewOf(CentiVisList<Asp> aspects) {
//        return new UnmodifiableCentiVisList<>(aspects.aspects);
//    }

    @Override
    public CentiVisList<Asp> copy() {
        return UnmodifiableCentiVisList.ofDirectly(internalList.copy());
    }
}
