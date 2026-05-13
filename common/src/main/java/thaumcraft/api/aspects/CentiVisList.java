package thaumcraft.api.aspects;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.Map;
import java.util.stream.Collectors;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

//you can see that i didn't make Asp PrimalAspect,maybe someone wants special cases.Remember TODO:Render as many aspects as possible in some rules in workbench
public class CentiVisList<Asp extends Aspect> extends AspectList<Asp> {//just mark we are using centiVis
//    public static CentiVisList<Aspect> fromAspectVisList(ItemStack stack) {
//        var result = new CentiVisList<>();
//        AspectList<Aspect> temp = ThaumcraftApiHelper.getObjectAspects(stack);
//        if (temp!=null) {
//            for (Aspect tag : temp.getAspectTypes()) {
//                result.addAll(tag, temp.getAmount(tag));
//            }
//        }
//        return result;
//    }
    public CentiVisList() {
        super();
    }
    public CentiVisList(AspectList<Asp> aspects) {
        super(aspects);
    }
    public CentiVisList(Map<Asp, Integer> aspects) {
        super(aspects);
    }
    public CentiVisList(Object2IntMap<Asp> aspects) {
        super(aspects);
    }
    protected CentiVisList(Object2IntLinkedOpenHashMap<Asp> aspects) {
        super(aspects);
    }
    public CentiVisList(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }
    public CentiVisList(Asp aspect,int value){
        super(aspect,value);
    }

    public CentiVisList<Asp> copy() {
        CentiVisList<Asp> out = new CentiVisList<>();
        for (var a:this.getAspectTypes())
            out.addAll(a, this.getAmount(a));
        return out;
    }

    public static <Asp extends Aspect> CentiVisList<Asp> of(Map<Asp,Integer> aspects) {
        CentiVisList<Asp> out = new CentiVisList<>();
        for (var entry:aspects.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            out.addAll(key, value);
        }
        return out;
    }

    public static <Asp extends Aspect> CentiVisList<Asp> fromAspectVisList(AspectList<Asp> aspects) {
        if (aspects instanceof CentiVisList<Asp> centiVisList){
            return centiVisList;
        }
        Object2IntLinkedOpenHashMap<Asp> centiVisCapacity = new Object2IntLinkedOpenHashMap<>(aspects.size(),1);
        aspects.forEach((key, value) -> centiVisCapacity.put(key,value*CENTIVIS_MULTIPLIER));
        return new CentiVisList<>(centiVisCapacity);
    }

    @SafeVarargs
    public static <Asp extends Aspect> CentiVisList<Asp> of(Asp... aspects){
        CentiVisList<Asp> out = new CentiVisList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> CentiVisList<Asp> viewOf(Object2IntLinkedOpenHashMap<Asp> aspects) {
        return new CentiVisList<>(aspects);
    }


    public static <Asp extends Aspect> CentiVisList<Asp> of(Asp aspect,int amount){
        CentiVisList<Asp> out = new CentiVisList<>();
        out.addAll(aspect,amount);
        return out;
    }
    public static <Asp extends Aspect> CentiVisList<Asp> of(Asp aspect,int amount,Asp aspect2,int amount2){
        CentiVisList<Asp> out = new CentiVisList<>();
        out.addAll(aspect,amount);
        out.addAll(aspect2,amount2);
        return out;
    }
}
