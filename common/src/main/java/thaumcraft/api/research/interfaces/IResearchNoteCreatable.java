package thaumcraft.api.research.interfaces;

import org.jetbrains.annotations.Range;

public interface IResearchNoteCreatable {
    boolean canPlayerCreateResearchNote(String playerName);
    @Range(from=1,to=3) int getComplexity();
}
