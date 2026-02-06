package thaumcraft.api.research.interfaces;

import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

public interface IResearchParentsOwner {
    List<ResearchItemResourceLocation> getParents();
}
