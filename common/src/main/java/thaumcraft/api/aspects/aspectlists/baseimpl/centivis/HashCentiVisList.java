package thaumcraft.api.aspects.aspectlists.baseimpl.centivis;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;
import thaumcraft.codechicken.lib.util.Copyable;

public class HashCentiVisList<Asp extends Aspect>
        extends HashAspectList<Asp>
        implements
        CentiVisList<Asp>,
        Copyable<HashCentiVisList<Asp>> {
    public HashCentiVisList() {
    }

    public HashCentiVisList(@NotNull Object2IntOpenHashMap<Asp> aspects) {
        super(aspects);
    }

    public HashCentiVisList(@NotNull Object2IntMap<Asp> aspects) {
        super(aspects);
    }


    public HashCentiVisList<Asp> copy() {
        HashCentiVisList<Asp> out = new HashCentiVisList<>();
        for (var a:this.keySet())
            out.addAll(a, this.get(a));
        return out;
    }
    @SafeVarargs
    public static <Asp extends Aspect> HashCentiVisList<Asp> of(Asp... aspects){
        HashCentiVisList<Asp> out = new HashCentiVisList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> HashCentiVisList<Asp> viewOf(Object2IntOpenHashMap<Asp> aspects) {
        return new HashCentiVisList<>(aspects);
    }


    public static <Asp extends Aspect> HashCentiVisList<Asp> of(Asp aspect, int amount){
        HashCentiVisList<Asp> out = new HashCentiVisList<>();
        out.addAll(aspect,amount);
        return out;
    }
    public static <Asp extends Aspect> HashCentiVisList<Asp> of(Asp aspect, int amount, Asp aspect2, int amount2){
        HashCentiVisList<Asp> out = new HashCentiVisList<>();
        out.addAll(aspect,amount);
        out.addAll(aspect2,amount2);
        return out;
    }

}
