package thaumcraft.api.research.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

//research category is just rendering logic
//TODO:make definition in resource pack(not datapack)
@RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.CLIENT)
public class ResearchCategoryManager {
    public static final Multimap<ResearchItemResourceLocation, ResearchCategoryResourceLocation> researchCategoryMap = HashMultimap.create();
    public static final Multimap<ResearchCategoryResourceLocation, ResearchItemResourceLocation> researchItemMap = HashMultimap.create();
    //TODO:Listen resource pack reload
}
