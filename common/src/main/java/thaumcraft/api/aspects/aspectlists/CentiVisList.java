package thaumcraft.api.aspects.aspectlists;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import thaumcraft.api.aspects.Aspect;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

//you can see that i didn't make Asp PrimalAspect,maybe someone wants special cases.Remember TODO:Render as many aspects as possible in some rules in workbench
public interface CentiVisList<Asp extends Aspect> extends AspectList<Asp> {//just mark we are using centiVis
//    public static CentiVisList<Aspect> of(ItemStack stack) {
//        var result = new CentiVisList<>();
//        AspectList<Aspect> temp = ThaumcraftApiHelper.getObjectAspects(stack);
//        if (temp!=null) {
//            for (Aspect tag : temp.getAspectTypes()) {
//                result.addAll(tag, temp.getAmount(tag));
//            }
//        }
//        return result;
//    }
//    public CentiVisList() {
//        super();
//    }
//    public CentiVisList(AspectList<Asp> aspects) {
//        super(aspects);
//    }
//    public CentiVisList(Map<Asp, Integer> aspects) {
//        super(aspects);
//    }
//    public CentiVisList(Object2IntMap<Asp> aspects) {
//        super(aspects);
//    }
//    protected CentiVisList(Object2IntLinkedOpenHashMap<Asp> aspects) {
//        super(aspects);
//    }
//    public CentiVisList(int capacity, float loadFactor) {
//        super(capacity, loadFactor);
//    }
//    public CentiVisList(Asp aspect,int value){
//        super(aspect,value);
//    }



    static <Asp extends Aspect> CentiVisList<Asp> fromAspectVisList(AspectList<Asp> aspects) {
        if (aspects instanceof CentiVisList<Asp> centiVisList){
            return centiVisList;
        }
        Object2IntLinkedOpenHashMap<Asp> centiVisCapacity = new Object2IntLinkedOpenHashMap<>(aspects.size(),1);
        aspects.forEach((key, value) -> centiVisCapacity.put(key,value*CENTIVIS_MULTIPLIER));
        return new LinkedTreeCentiVisList<>(centiVisCapacity);
    }
}
