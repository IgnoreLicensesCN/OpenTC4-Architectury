package thaumcraft.api.aspects;

import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.stream.Collectors;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;

public class CentiVisList extends AspectList {//just mark we are using centiVis

    public CentiVisList(ItemStack stack) {
        super(stack);
    }
    public CentiVisList() {
        super();
    }

    public CentiVisList copy() {
        CentiVisList out = new CentiVisList();
        for (Aspect a:this.getAspectTypes())
            out.addAll(a, this.getAmount(a));
        return out;
    }

    public static CentiVisList of(Map<Aspect,Integer> aspects) {
        CentiVisList out = new CentiVisList();
        for (var entry:aspects.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            out.addAll(key, value);
        }
        return out;
    }

    public static CentiVisList of(AspectList aspects) {
        if (aspects instanceof CentiVisList centiVisList){
            return centiVisList;
        }
        return CentiVisList.of(
                aspects.getAspects()
                        .entrySet()
                        .stream()
                        .map(
                                entry -> Map.entry(
                                        entry.getKey(),
                                        entry.getValue()*CENTIVIS_MULTIPLIER)
                        ).collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue)
                        )
        );
    }

    public static CentiVisList of(Aspect... aspects){
        CentiVisList out = new CentiVisList();
        for (Aspect aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return out;
    }
    @Override
    public AspectList addAll(AspectList in) {
        return super.addAll(in);
    }
}
