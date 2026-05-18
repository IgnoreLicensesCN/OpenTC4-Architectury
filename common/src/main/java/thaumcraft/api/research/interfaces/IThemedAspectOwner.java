package thaumcraft.api.research.interfaces;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IThemedAspectOwner {
    @NotNull("null->empty") Aspect getResearchThemedAspect();

}
