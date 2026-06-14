package thaumcraft.api.aspects.aspectlists.unmodifiable;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntBiConsumer;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import oshi.annotation.concurrent.NotThreadSafe;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.AspectListUnmodifiableDefault;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.codechicken.lib.util.Copyable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//usually it's not considered as a view so it shouldn't been changed
public class UnmodifiableAspectList<A extends Aspect> implements AspectListUnmodifiableDefault<A>, Copyable<AspectList<A>> {

    public static final UnmodifiableAspectList<Aspect> EMPTY = new UnmodifiableAspectList<>(new LinkedHashAspectList<>()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };
    public static final UnmodifiableAspectList<PrimalAspect> EMPTY_PRIMAL = new UnmodifiableAspectList<>(new LinkedHashAspectList<>()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };
    public static final UnmodifiableAspectList<CompoundAspect> EMPTY_COMPOUND = new UnmodifiableAspectList<>(new LinkedHashAspectList<>()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    protected final AspectList<A> internalList;

    private final int hash;

    protected UnmodifiableAspectList(@NotNull AspectList<A> viewingList) {
        this.internalList = viewingList.copy();
        //init hash
        {
            var hashTemp = new AtomicInteger(0);
            internalList.forEach(
                    (asp,amt) -> {
                        hashTemp.set(hashTemp.get() * 31);
                        hashTemp.addAndGet(asp.hashCode() + amt);
                    }
            );
            this.hash = hashTemp.get();
        }
    }
    protected UnmodifiableAspectList(@NotNull Object2IntLinkedOpenHashMap<A> aspects) {
        this.internalList = new LinkedHashAspectList<>(aspects).copy();
        //init hash
        {
            var hashTemp = new AtomicInteger(0);
            internalList.forEach(
                    (asp,amt) -> {
                        hashTemp.set(hashTemp.get() * 31);
                        hashTemp.addAndGet(asp.hashCode() + amt);
                    }
            );
            this.hash = hashTemp.get();
        }
    };

    public UnmodifiableAspectList<A> addAllAsNew(AspectList<A> aspects) {
        Object2IntLinkedOpenHashMap<A> resultMap = new Object2IntLinkedOpenHashMap<>(aspects.size() + this.size(),1);
        aspects.forEach((aspect, amount) -> resultMap.mergeInt(aspect, amount,Integer::sum));
        this.forEach((aspect, amount) -> resultMap.mergeInt(aspect, amount,Integer::sum));
        return new UnmodifiableAspectList<>(resultMap);
    }

    public UnmodifiableAspectList<A> multiplyAsNew(int multiplier) {
        Object2IntLinkedOpenHashMap<A> resultMap = new Object2IntLinkedOpenHashMap<>(this.size(),1);
        this.forEach((aspect, amount) -> resultMap.put(aspect, amount*multiplier));
        return new UnmodifiableAspectList<>(resultMap);
    }
    public UnmodifiableAspectList<A> divideAndCeilAsNew(int divisor) {
        if (divisor == 0){
            throw new IllegalArgumentException("divided by zero!");
        }
        Object2IntLinkedOpenHashMap<A> resultMap = new Object2IntLinkedOpenHashMap<>(this.size(),1);
        this.forEach((aspect, amount) -> resultMap.put(aspect, (amount + divisor - 1)/divisor));
        return new UnmodifiableAspectList<>(resultMap);
    }

    public static <A extends Aspect> UnmodifiableAspectList<A> combine(AspectList<A> aspectsA, AspectList<A> aspectsB) {
        if (aspectsA == null) {
            return new UnmodifiableAspectList<>(aspectsB);
        }
        if (aspectsB == null) {
            return new UnmodifiableAspectList<>(aspectsA);
        }
        Object2IntLinkedOpenHashMap<A> resultMap = new Object2IntLinkedOpenHashMap<>(aspectsA.size() + aspectsB.size(),1);
        aspectsA.forEach((aspect, amount) -> resultMap.mergeInt(aspect, amount,Integer::sum));
        aspectsB.forEach((aspect, amount) -> resultMap.mergeInt(aspect, amount,Integer::sum));
        return new UnmodifiableAspectList<>(resultMap);
    }


    private static final Map<Aspect,UnmodifiableAspectList<Aspect>> aspectMapOfSingleAspect = new HashMap<>();
    //reducing CME in case someone wants multi-threading
    static {
        Aspects.ALL_ASPECTS.forEach((aspectKey, aspect) -> {
            var map = new Object2IntLinkedOpenHashMap<Aspect>();
            map.put(aspect,1);
            var listToPut = new UnmodifiableAspectList<>(map);
            aspectMapOfSingleAspect.put(aspect,listToPut);
        });
    }
    @NotThreadSafe//at least for writing unless you've already created those instances in someway like during loading?
    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> ofSingle(Asp aspect){
        return (UnmodifiableAspectList<Asp>) aspectMapOfSingleAspect.computeIfAbsent(aspect, asp -> {
            var map = new Object2IntLinkedOpenHashMap<Aspect>();
            map.put(asp,1);
            return new UnmodifiableAspectList<>(map);
        });
    }
    private static final Map<Aspect, Int2ObjectMap<UnmodifiableAspectList<Aspect>>> aspectMapOfSingleAspectMultiValue = new ConcurrentHashMap<>();


    @Override
    public int hashCode() {
        return hash;
    }


    @Override
    public int getOrDefault(Aspect aspect, int defaultValue) {
        return internalList.getOrDefault(aspect,defaultValue);
    }

    @Override
    public AspectList<A> copy() {
        return internalList.copy();
    }

    @Override
    public @UnmodifiableView Set<A> keySet() {
        return internalList.keySet();
    }

    @Override
    public int size() {
        return internalList.size();
    }

    @Override
    public int visSize() {
        return internalList.visSize();
    }

    @Override
    public AspectList<PrimalAspect> getPrimalAspects() {
        return internalList.getPrimalAspects();
    }

    @Override
    public List<A> getAspectsSorted() {
        return internalList.getAspectsSorted();
    }

    @Override
    public List<A> getAspectsSortedAmount() {
        return internalList.getAspectsSortedAmount();
    }

    @Override
    public int get(Aspect key) {
        return internalList.get(key);
    }

    @Override
    public void forEach(ObjectIntBiConsumer<A> action) {
        internalList.forEach(action);
    }

    @Override
    public boolean forEachWithBreak(ObjInt2BooleanFunction<A> action) {
        return internalList.forEachWithBreak(action);
    }

    @Override
    public void acceptForIndex(int index, ObjectIntBiConsumer<A> action) {
        internalList.acceptForIndex(index,action);
    }

    @Override
    public @Nullable("if empty") A randomAspect(RandomSource randomSource) {
        return internalList.randomAspect(randomSource);
    }

    @Override
    public @Nullable("if empty") A randomWeightedAspect(RandomSource randomSource) {
        return internalList.randomWeightedAspect(randomSource);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull("empty -> empty(aspect)") A getFirstAspect() {
        return internalList.getFirstAspect();
    }

    @Override
    public boolean containsKey(Aspect aspect) {
        return internalList.containsKey(aspect);
    }

    @Override
    public String toString() {
        return "UnmodifiableAspectList{ " + internalList.toString() + " }";
    }




    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(){
        return (UnmodifiableAspectList<Asp>) UnmodifiableAspectList.EMPTY;
    }

    @SafeVarargs
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp... aspects){
        if (aspects.length == 1){
            return ofSingle(aspects[0]);
        }
        AspectList<Asp> out = new LinkedHashAspectList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return new UnmodifiableAspectList<>(out);
    }

    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(@NotNull AspectList<Asp> aspects) {
        if (aspects instanceof UnmodifiableAspectList<Asp> unmodifiableAspectList){
            return unmodifiableAspectList;
        }
        if (aspects.isEmpty()){
            return of();
        }
        return new UnmodifiableAspectList<>(aspects);
    }

    @NotThreadSafe//unless you've already created those instances via this.
    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value){
        if (value == 1){
            return ofSingle(aspect);
        }
        return (UnmodifiableAspectList<Asp>) aspectMapOfSingleAspectMultiValue
                .computeIfAbsent(aspect,_ignored -> new Int2ObjectOpenHashMap<>())
                .computeIfAbsent(value,aspectAmount -> {
                    var map = new Object2IntLinkedOpenHashMap<Aspect>();
                    map.put(aspect,aspectAmount);
                    return new UnmodifiableAspectList<>(map);
                });
    }

    //you should cache it yourself now.
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value,Asp aspect2,int value2){

        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        return new UnmodifiableAspectList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value,Asp aspect2,int value2,Asp aspect3,int value3){

        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        map.put(aspect3,value3);
        return new UnmodifiableAspectList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(
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
        return new UnmodifiableAspectList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(
            Asp aspect,int value,
            Asp aspect2,int value2,
            Asp aspect3,int value3,
            Asp aspect4,int value4,
            Asp aspect5,int value5
    ){
        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        map.put(aspect3,value3);
        map.put(aspect4,value4);
        map.put(aspect5,value5);
        return new UnmodifiableAspectList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(
            Asp aspect,int value,
            Asp aspect2,int value2,
            Asp aspect3,int value3,
            Asp aspect4,int value4,
            Asp aspect5,int value5,
            Asp aspect6,int value6
    ){
        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        map.put(aspect3,value3);
        map.put(aspect4,value4);
        map.put(aspect5,value5);
        map.put(aspect6,value6);
        return new UnmodifiableAspectList<>(map);
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(
            Asp aspect,int value,
            Asp aspect2,int value2,
            Asp aspect3,int value3,
            Asp aspect4,int value4,
            Asp aspect5,int value5,
            Asp aspect6,int value6,
            Asp aspect7,int value7
    ){
        var map = new Object2IntLinkedOpenHashMap<Asp>();
        map.put(aspect,value);
        map.put(aspect2,value2);
        map.put(aspect3,value3);
        map.put(aspect4,value4);
        map.put(aspect5,value5);
        map.put(aspect6,value6);
        map.put(aspect7,value7);
        return new UnmodifiableAspectList<>(map);
    }


}
