package thaumcraft.api.research;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.resources.ResourceLocation;
import com.linearity.opentc4.utils.StatCollector;
import org.apache.logging.log4j.Level;
import tc4tweak.modules.getResearch.GetResearch;

import java.util.LinkedHashMap;

public class ResearchCategories {
	
	//Research
	public static LinkedHashMap <ResourceLocation, ResearchCategoryList> researchCategories = new LinkedHashMap<>();
	
	/**
	 * @param categoryKey
	 * @return the research item linked to this categoryKey
	 */
	public static ResearchCategoryList getResearchList(ResourceLocation categoryKey) {
		return researchCategories.get(categoryKey);
	}
	
	/**
	 * @param key
	 * @return the name of the research category linked to this key. 
	 * Must be stored as localization information in the LanguageRegistry.
	 */
	public static String getCategoryName(ResourceLocation key) {
		return StatCollector.translateToLocal("tc.research_category."+key.getNamespace()+"."+key.getPath());
	}

	/**
	 * @param key the research key
	 * @return the ResearchItem object.
	 */
	public static ResearchItem getResearch(ResourceLocation key) {
		return GetResearch.getResearch(key);
//		Collection<ResearchCategoryList> rc = researchCategories.values();
//		for (ResearchCategoryList cat:rc) {
//			Collection<ResearchItem> rl = cat.research.values();
//			for (ResearchItem ri:rl) {
//				if (ri.key.equals(key)) {
//					return ri;
//				}
//			}
//		}
//		return null;
	}
	
	/**
	 * This should only be done at the PostInit stage
	 * @param key the key used for this category
	 * @param icon the icon to be used for the research category tab
	 * @param background the resource location of the background image to use for this category
	 * @return the name of the research linked to this key
	 */
	public static void registerCategory(ResourceLocation key, ResourceLocation icon, ResourceLocation background) {
		if (getResearchList(key)==null) {
			ResearchCategoryList rl = new ResearchCategoryList(icon, background);
			researchCategories.put(key, rl);
		}
	}
	
	public static void addResearch(ResearchItem ri) {
		ResearchCategoryList rl = getResearchList(ri.category);
		if (rl!=null && !rl.research.containsKey(ri.key)) {
			
			if (!ri.isVirtual()) {
				for (ResearchItem rr:rl.research.values()) {
					if (rr.displayColumn == ri.displayColumn && rr.displayRow == ri.displayRow) {
						OpenTC4.LOGGER.log(Level.FATAL, "[Thaumcraft] Research ["+ri.getName()+"] not added as it overlaps with existing research ["+rr.getName()+"]");
						return;
					}
				}
			}
			
			
			rl.research.put(ri.key, ri);
			
			if (ri.displayColumn < rl.minDisplayColumn) 
	        {
	            rl.minDisplayColumn = ri.displayColumn;
	        }

	        if (ri.displayRow < rl.minDisplayRow)
	        {
	            rl.minDisplayRow = ri.displayRow;
	        }

	        if (ri.displayColumn > rl.maxDisplayColumn)
	        {
	            rl.maxDisplayColumn = ri.displayColumn;
	        }

	        if (ri.displayRow > rl.maxDisplayRow)
	        {
	            rl.maxDisplayRow = ri.displayRow;
	        }
		}
	}
}
