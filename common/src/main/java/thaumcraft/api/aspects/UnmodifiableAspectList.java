package thaumcraft.api.aspects;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class UnmodifiableAspectList<A extends Aspect> extends AspectList<A> {

    public static final UnmodifiableAspectList<Aspect> EMPTY = new UnmodifiableAspectList<>(new AspectList<>());

    public UnmodifiableAspectList(AspectList<A> viewingList) {
        if (viewingList != null) {
            this.aspects.putAll(viewingList.aspectView);
        }
    }
    public UnmodifiableAspectList(LinkedHashMap<A, Integer> aspects) {
        super(aspects);
    }
    public UnmodifiableAspectList(Map<A, Integer> aspects) {
        super(aspects);
    }
    public UnmodifiableAspectList(){
        super();
    }

    @Override
    public Integer put(A aspect, int amount) {
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
    public int merge(A aspect, int amount, BiFunction<Integer, Integer, Integer> chooser) {
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
    public void replaceAll(BiFunction<A, Integer, Integer> biFunction) {
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
        LinkedHashMap<A, Integer> resultMap = new LinkedHashMap<>(this.aspectView);
        aspects.aspectView.forEach((aspect, amount) -> resultMap.merge(aspect, amount,Integer::sum));
        return new UnmodifiableAspectList<A>(resultMap);
    }

    public UnmodifiableAspectList<A> multiplyAsNew(int multiplier) {
        LinkedHashMap<A, Integer> resultMap = new LinkedHashMap<>(this.aspectView.size(),1);
        this.aspectView.forEach((aspect, amount) -> {
            resultMap.put(aspect, amount*multiplier);
        });
        return new UnmodifiableAspectList<>(resultMap);
    }
    public UnmodifiableAspectList<A> divideAndCeilAsNew(int divisor) {
        if (divisor == 0){
            throw new IllegalArgumentException("divided by zero!");
        }
        LinkedHashMap<A, Integer> resultMap = new LinkedHashMap<>(this.aspectView.size(),1);
        this.aspectView.forEach((aspect, amount) -> {
            resultMap.put(aspect, (amount + divisor - 1)/divisor);
        });
        return new UnmodifiableAspectList<>(resultMap);
    }

    public static <A extends Aspect> UnmodifiableAspectList<A> combine(AspectList<A> aspectsA, AspectList<A> aspectsB) {
        if (aspectsA == null) {
            return new UnmodifiableAspectList<>(aspectsB);
        }
        if (aspectsB == null) {
            return new UnmodifiableAspectList<>(aspectsA);
        }
        LinkedHashMap<A, Integer> resultMap = new LinkedHashMap<>(aspectsA.aspectView);
        aspectsB.aspectView.forEach((aspect, amount) -> resultMap.merge(aspect, amount,Integer::sum));
        return new UnmodifiableAspectList<>(resultMap);
    }


    @SafeVarargs
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp... aspects){
        AspectList<Asp> out = new AspectList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return new UnmodifiableAspectList<>(out);
    }

    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value){
        UnmodifiableAspectList<Asp> out = new UnmodifiableAspectList<>();
        out.aspects.put(aspect,value);

        return out;
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value,Asp aspect2,int value2){
        UnmodifiableAspectList<Asp> out = new UnmodifiableAspectList<>();
        out.aspects.put(aspect,value);
        out.aspects.put(aspect2,value2);

        return out;
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value,Asp aspect2,int value2,Asp aspect3,int value3){
        UnmodifiableAspectList<Asp> out = new UnmodifiableAspectList<>();
        out.aspects.put(aspect,value);
        out.aspects.put(aspect2,value2);
        out.aspects.put(aspect3,value3);

        return out;
    }
    public static <Asp extends Aspect> UnmodifiableAspectList<Asp> of(Asp aspect,int value,Asp aspect2,int value2,Asp aspect3,int value3,Asp aspect4,int value4){
        UnmodifiableAspectList<Asp> out = new UnmodifiableAspectList<>();
        out.aspects.put(aspect,value);
        out.aspects.put(aspect2,value2);
        out.aspects.put(aspect3,value3);
        out.aspects.put(aspect4,value4);

        return out;
    }
}
