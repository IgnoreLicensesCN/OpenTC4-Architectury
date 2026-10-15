package thaumcraft.common.lib.resourcelocations;

import thaumcraft.api.research.ResearchItem;
//#include VariedResourceLocationImports
public class ResearchItemResourceLocation extends VariedResourceLocation<ResearchItem,ResearchItemResourceLocation> {
    //#include VariedResourceLocationSource("ResearchItemResourceLocation", "ResearchItem")
    public ClueResourceLocation convertToClueResLoc() {
        return ClueResourceLocation.of(getNamespace(), getPath());
    }
}
