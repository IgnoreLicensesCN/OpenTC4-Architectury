package thaumcraft.api.research.interfaces;

import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

//I have to say since ResearchItem classes can and should be extended,this one might not highly-needed.
public interface IResearchParentsOwner {
    List<ResearchItemResourceLocation> getParents();
}
