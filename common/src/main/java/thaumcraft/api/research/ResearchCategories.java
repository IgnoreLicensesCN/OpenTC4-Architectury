package thaumcraft.api.research;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;

import static com.linearity.opentc4.OpenTC4.CHECK_RESEARCH_RENDER_LOCATION_SAME_FLAG;

public class ResearchCategories {
	
	//Research
	public static LinkedHashMap<ResearchCategoryResourceLocation,ResearchCategory> researchCategories = new LinkedHashMap<>();
	
	/**
	 * @param categoryKey
	 * @return the research item linked to this categoryKey
	 */
	public static ResearchCategory getResearchCategory(ResearchCategoryResourceLocation categoryKey) {
		return researchCategories.get(categoryKey);
	}
	
	/**
	 * @return the name of the research category linked to this key. 
	 * Must be stored as localization information in the LanguageRegistry.
	 */
	public static Component getCategoryName(ResearchCategory category) {
		return getCategoryName(category.categoryKey);
	}

	public static Component getCategoryName(ResearchCategoryResourceLocation key) {

		return Component.translatable("tc.research_category."+key.getNamespace()+"."+key.getPath());
	}

	/**
	 * This should only be done at the PostInit stage
	 * @param key the key used for this category
	 * @param icon the icon to be used for the research category tab
	 * @param background the resource location of the background image to use for this category
	 * @return the name of the research linked to this key
	 */
	public static void registerCategory(
			ResearchCategoryResourceLocation key,
			ResourceLocation icon,
			ResourceLocation background) {
		if (getResearchCategory(key)==null) {
			ResearchCategory rl = new ResearchCategory(key,icon, background);
			researchCategories.put(key, rl);
		}
	}
	
	public static void addResearchToItsCategory(
			ResearchItem researchItemToAdd,
			List<ShownInfoInResearchCategory> shownInfos) {
		for (var shownInfo : shownInfos) {
			var categoryKeyToAddInto = shownInfo.category();
			if (categoryKeyToAddInto==null) {
				continue;
			}
			var category = getResearchCategory(categoryKeyToAddInto);
			if (category==null) {
				OpenTC4.LOGGER.error("Category {} not found for {}",categoryKeyToAddInto,researchItemToAdd);
				continue;
			}
			if (CHECK_RESEARCH_RENDER_LOCATION_SAME_FLAG){
				for (var researchAndShowInfoEntry : category.researchesShownInfo.entrySet()) {
					var researchShowInfoAdded = researchAndShowInfoEntry.getValue();
					if (researchShowInfoAdded.row() == shownInfo.row()
					&& researchShowInfoAdded.column() == shownInfo.column()){
						OpenTC4.LOGGER.error("Research showInfo collision:{} and {}",researchAndShowInfoEntry.getKey(),researchItemToAdd.key);
					}
				}
			}
			category.addResearch(researchItemToAdd,shownInfo);
		}
//		ResearchCategory category = getResearchCategory(researchItemToAdd.category);
//		if (category!=null && !category.researches.containsKey(researchItemToAdd.key)) {
//
//			if (!researchItemToAdd.isVirtual()) {
//				for (ResearchItem rr:category.researches.values()) {
//					if (rr.displayColumn == researchItemToAdd.displayColumn && rr.displayRow == researchItemToAdd.displayRow) {
//						OpenTC4.LOGGER.log(Level.FATAL, "[Thaumcraft] Research ["+researchItemToAdd.getName()+"] not added as it overlaps with existing research ["+rr.getName()+"]");
//						return;
//					}
//				}
//			}
//
//
//			category.researches.put(researchItemToAdd.key, researchItemToAdd);
//		}
	}
}
