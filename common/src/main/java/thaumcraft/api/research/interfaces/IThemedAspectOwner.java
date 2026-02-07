package thaumcraft.api.research.interfaces;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IThemedAspectOwner {
    @NotNull("null->empty") Aspect getResearchThemedAspect();

    static Aspect getResearchThemedAspectViaList(AspectList<Aspect> aspects){
        Aspect aspect=null;
        int highest=0;
        if (aspects!=null) {
            for (var entry : aspects.entrySet()) {
                var tag = entry.getKey();
                if (aspects.getAmount(tag) > highest) {
                    aspect = tag;
                    highest = aspects.getAmount(tag);
                }
            }
        }
        return aspect;
    }
}
