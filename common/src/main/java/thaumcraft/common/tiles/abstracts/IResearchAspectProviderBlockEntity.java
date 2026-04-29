package thaumcraft.common.tiles.abstracts;

import thaumcraft.api.aspects.Aspect;

public interface IResearchAspectProviderBlockEntity {
    int getAspectOwning(Aspect aspect);
    void costAspect(Aspect aspect, int cost);
}
