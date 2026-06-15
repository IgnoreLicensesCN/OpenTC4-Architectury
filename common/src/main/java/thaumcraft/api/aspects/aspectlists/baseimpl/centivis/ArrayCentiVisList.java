package thaumcraft.api.aspects.aspectlists.baseimpl.centivis;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.ArrayAspectList;
import thaumcraft.codechicken.lib.util.Copyable;

public class ArrayCentiVisList<Asp extends Aspect> extends ArrayAspectList<Asp> implements CentiVisList<Asp>, Copyable<ArrayCentiVisList<Asp>> {
    public ArrayCentiVisList() {
        super(new Object2IntArrayMap<>());
    }
    public ArrayCentiVisList(@NotNull Object2IntOpenHashMap<Asp> aspects) {
        super(aspects);
    }

    public ArrayCentiVisList(@NotNull Object2IntMap<Asp> aspects) {
        super(aspects);
    }


    public ArrayCentiVisList<Asp> copy() {
        ArrayCentiVisList<Asp> out = new ArrayCentiVisList<>();
        for (var a:this.keySet())
            out.addAll(a, this.get(a));
        return out;
    }
    @SafeVarargs
    public static <Asp extends Aspect> ArrayCentiVisList<Asp> of(Asp... aspects){
        ArrayCentiVisList<Asp> out = new ArrayCentiVisList<>();
        for (var aspect : aspects){
            if (aspect != null){
                out.addAll(aspect,1);
            }
        }
        return out;
    }

    public static <Asp extends Aspect> ArrayCentiVisList<Asp> viewOf(Object2IntArrayMap<Asp> aspects) {
        return new ArrayCentiVisList<>(aspects);
    }


    public static <Asp extends Aspect> ArrayCentiVisList<Asp> of(Asp aspect, int amount){
        ArrayCentiVisList<Asp> out = new ArrayCentiVisList<>();
        out.addAll(aspect,amount);
        return out;
    }
    public static <Asp extends Aspect> ArrayCentiVisList<Asp> of(Asp aspect, int amount, Asp aspect2, int amount2){
        ArrayCentiVisList<Asp> out = new ArrayCentiVisList<>();
        out.addAll(aspect,amount);
        out.addAll(aspect2,amount2);
        return out;
    }
}
