package thaumcraft.api.research.interfaces;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IAspectUnlockable extends IResearchable{
    //do not check if cost is enough here.
    boolean canPlayerCompleteResearchWithAspect(String playerName);
    AspectList<Aspect> getAspectCost();
}
