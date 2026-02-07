package thaumcraft.api.research.interfaces;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IResearchNoteCopyable {
    boolean canPlayerCopyResearch(String playerName);
    @UnmodifiableView
    AspectList<Aspect> getCopyResearchBaseAspects();
}
