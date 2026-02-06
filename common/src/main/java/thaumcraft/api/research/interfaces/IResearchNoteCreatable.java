package thaumcraft.api.research.interfaces;

import org.jetbrains.annotations.Range;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IResearchNoteCreatable {
    boolean canPlayerCreateResearchNote(String playerName);
    @Range(from=1,to=3) int getComplexity();
    AspectList<Aspect> getResearchGivenAspects();
}
