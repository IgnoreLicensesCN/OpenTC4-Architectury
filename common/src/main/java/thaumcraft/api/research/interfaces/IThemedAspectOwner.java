package thaumcraft.api.research.interfaces;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public interface IThemedAspectOwner {
    @NotNull("null->empty") Aspect getResearchThemedAspect();
}
