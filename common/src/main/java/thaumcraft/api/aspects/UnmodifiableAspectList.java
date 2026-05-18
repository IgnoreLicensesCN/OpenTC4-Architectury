package thaumcraft.api.aspects;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.NotNull;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntBinaryOperator;

//usually it's not considered as a view so it shouldn't been changed
public class UnmodifiableAspectList<A extends Aspect> extends AspectList<A> {

    public static final UnmodifiableAspectList<Aspect> EMPTY = new UnmodifiableAspectList<>(new AspectList<>());

    public UnmodifiableAspectList(@NotNull AspectList<A> viewingList) {
        super(viewingList);
    }
    public UnmodifiableAspectList(@NotNull Object2IntMap<A> aspects) {
        super(aspects);
    }
    protected UnmodifiableAspectList(@NotNull Object2IntLinkedOpenHashMap<A> aspects) {
        super(aspects);
    }
    public UnmodifiableAspectList(){
        super();
    }


    private final int hash;
    //init hash
    {
        int hashTemp = 0;
        for (var aspEntry:this.aspects.object2IntEntrySet()) {
            hashTemp *= 31;
            hashTemp += aspEntry.getKey().hashCode() * aspEntry.getIntValue();
        }
        this.hash = hashTemp;
    }
    @Override
    public int put(A aspect, int amount) {
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
    public int merge(A aspect, int amount, IntBinaryOperator chooser) {
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
    public AspectList<A> remove(Aspect key) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> reduceAndRemoveIfNotPositive(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public boolean tryReduce(Aspect key, int amount) throws RuntimeException {
        throw new RuntimeException("Unmodifiable!");
    }

    public void putAllAspects(AspectList<A> aspects) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public void replaceAll(AspIntIntBiFunction<A> aIntIntBiFunction) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> multiply(int multiplier) {
        throw new RuntimeException("Unmodifiable!");
    }

    @Override
    public AspectList<A> divideAndCeil(int divideBy) {
        throw new RuntimeException("Unmodifiable!");
    }

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

    @SuppressWarnings("unchecked")
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(){
        return (UnmodifiableAspectList<Asp>) UnmodifiableAspectList.EMPTY;
    }

    @SafeVarargs
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp... aspects){
        if (aspects.length == 1){
            return ofSingle(aspects[0]);
        }
        AspectList<Asp> out = new AspectList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return new UnmodifiableAspectList<>(out);
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

    @Override
    public int hashCode() {
        return hash;
    }

}
