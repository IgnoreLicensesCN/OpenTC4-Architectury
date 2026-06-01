package thaumcraft.api.aspects.aspectlists;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.codechicken.lib.util.Copyable;

import java.util.Map;

public class LinkedTreeCentiVisList<Asp extends Aspect>
        extends LinkedHashAspectList<Asp>
        implements
        CentiVisList<Asp>,
        Copyable<LinkedTreeCentiVisList<Asp>> {
    public LinkedTreeCentiVisList() {
    }

    public LinkedTreeCentiVisList(@NotNull Object2IntLinkedOpenHashMap<Asp> aspects) {
        super(aspects);
    }

    public LinkedTreeCentiVisList(@NotNull Map<Asp, Integer> aspects) {
        super(aspects);
    }

    public LinkedTreeCentiVisList(@NotNull Object2IntMap<Asp> aspects) {
        super(aspects);
    }

    public LinkedTreeCentiVisList(int size, float loadFactor) {
        super(size, loadFactor);
    }

    public LinkedTreeCentiVisList(@NotNull LinkedHashAspectList<Asp> another) {
        super(another);
    }

    public LinkedTreeCentiVisList<Asp> copy() {
        LinkedTreeCentiVisList<Asp> out = new LinkedTreeCentiVisList<>();
        for (var a:this.keySet())
            out.addAll(a, this.get(a));
        return out;
    }
    @SafeVarargs
    public static <Asp extends Aspect> LinkedTreeCentiVisList<Asp> of(Asp... aspects){
        LinkedTreeCentiVisList<Asp> out = new LinkedTreeCentiVisList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> LinkedTreeCentiVisList<Asp> viewOf(Object2IntLinkedOpenHashMap<Asp> aspects) {
        return new LinkedTreeCentiVisList<>(aspects);
    }


    public static <Asp extends Aspect> LinkedTreeCentiVisList<Asp> of(Asp aspect,int amount){
        LinkedTreeCentiVisList<Asp> out = new LinkedTreeCentiVisList<>();
        out.addAll(aspect,amount);
        return out;
    }
    public static <Asp extends Aspect> LinkedTreeCentiVisList<Asp> of(Asp aspect,int amount,Asp aspect2,int amount2){
        LinkedTreeCentiVisList<Asp> out = new LinkedTreeCentiVisList<>();
        out.addAll(aspect,amount);
        out.addAll(aspect2,amount2);
        return out;
    }

}
