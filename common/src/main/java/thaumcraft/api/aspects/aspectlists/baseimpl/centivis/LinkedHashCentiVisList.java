package thaumcraft.api.aspects.aspectlists.baseimpl.centivis;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.codechicken.lib.util.Copyable;

import java.util.Map;

public class LinkedHashCentiVisList<Asp extends Aspect>
        extends LinkedHashAspectList<Asp>
        implements
        CentiVisList<Asp>,
        Copyable<LinkedHashCentiVisList<Asp>> {
    public LinkedHashCentiVisList() {
    }

    public LinkedHashCentiVisList(@NotNull Object2IntLinkedOpenHashMap<Asp> aspects) {
        super(aspects);
    }

    public LinkedHashCentiVisList(@NotNull Map<Asp, Integer> aspects) {
        super(aspects);
    }

    public LinkedHashCentiVisList(@NotNull Object2IntMap<Asp> aspects) {
        super(aspects);
    }

    public LinkedHashCentiVisList(int size, float loadFactor) {
        super(size, loadFactor);
    }

    public LinkedHashCentiVisList(@NotNull LinkedHashAspectList<Asp> another) {
        super(another);
    }

    public LinkedHashCentiVisList<Asp> copy() {
        LinkedHashCentiVisList<Asp> out = new LinkedHashCentiVisList<>();
        for (var a:this.keySet())
            out.addAll(a, this.get(a));
        return out;
    }
    @SafeVarargs
    public static <Asp extends Aspect> LinkedHashCentiVisList<Asp> of(Asp... aspects){
        LinkedHashCentiVisList<Asp> out = new LinkedHashCentiVisList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> LinkedHashCentiVisList<Asp> viewOf(Object2IntLinkedOpenHashMap<Asp> aspects) {
        return new LinkedHashCentiVisList<>(aspects);
    }


    public static <Asp extends Aspect> LinkedHashCentiVisList<Asp> of(Asp aspect, int amount){
        LinkedHashCentiVisList<Asp> out = new LinkedHashCentiVisList<>();
        out.addAll(aspect,amount);
        return out;
    }
    public static <Asp extends Aspect> LinkedHashCentiVisList<Asp> of(Asp aspect, int amount, Asp aspect2, int amount2){
        LinkedHashCentiVisList<Asp> out = new LinkedHashCentiVisList<>();
        out.addAll(aspect,amount);
        out.addAll(aspect2,amount2);
        return out;
    }

}
