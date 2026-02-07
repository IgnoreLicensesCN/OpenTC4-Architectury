package thaumcraft.api.research;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.linearity.opentc4.OpenTC4.CHECK_RESEARCH_RENDER_LOCATION_SAME_FLAG;

public class ResearchCategory {

    //Research
    public static LinkedHashMap<ResearchCategoryResourceLocation,ResearchCategory> researchCategories = new LinkedHashMap<>();
    public final ResearchCategoryResourceLocation categoryKey;
	/** Is the smallest column used on the GUI. */
    public int minDisplayColumn = 0;

    /** Is the smallest row used on the GUI. */
    public int minDisplayRow = 0;

    /** Is the biggest column used on the GUI. */
    public int maxDisplayColumn = 0;

    /** Is the biggest row used on the GUI. */
    public int maxDisplayRow = 0;
    
    /** display variables **/
    public ResourceLocation icon;
    public ResourceLocation background;


    //Research
    public final Map<ResearchItemResourceLocation, ResearchItem> researches = new HashMap<>();
    public final Map<ResearchItemResourceLocation, ShownInfoInResearchCategory> researchesShownInfo = new HashMap<>();

    public ResearchCategory(ResearchCategoryResourceLocation researchKey, ResourceLocation icon, ResourceLocation background) {
		this.icon = icon;
		this.background = background;
        this.categoryKey = researchKey;
	}
    /**
     * @param categoryKey
     * @return the research item linked to this categoryKey
     */
    public static ResearchCategory getResearchCategory(ResearchCategoryResourceLocation categoryKey) {
        return researchCategories.get(categoryKey);
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

    /**
     * @return the name of the research category linked to this key.
     * Must be stored as localization information in the LanguageRegistry.
     */
    public Component getCategoryName() {
        return Component.translatable("tc.research_category."+categoryKey.getNamespace()+"."+categoryKey.getPath());
    }

    public void addResearch(ResearchItem item){
        this.researches.put(item.key,item);
    }

    public void addResearchShownInfo(ResearchItem item, ShownInfoInResearchCategory shownInfo) {
        this.researchesShownInfo.put(item.key,shownInfo);
        this.minDisplayColumn = Math.min(shownInfo.column(), this.minDisplayColumn);
        this.minDisplayRow = Math.min(shownInfo.row(), this.minDisplayRow);
        this.maxDisplayColumn = Math.max(shownInfo.column(), this.maxDisplayColumn);
        this.maxDisplayRow = Math.max(shownInfo.row(), this.maxDisplayRow);

        if (CHECK_RESEARCH_RENDER_LOCATION_SAME_FLAG){
            for (var researchAndShowInfoEntry : this.researchesShownInfo.entrySet()) {
                var researchShowInfoAdded = researchAndShowInfoEntry.getValue();
                if (researchShowInfoAdded.row() == shownInfo.row()
                        && researchShowInfoAdded.column() == shownInfo.column()){
                    OpenTC4.LOGGER.error("Research showInfo collision:{} and {}",researchAndShowInfoEntry.getKey(),item.key);
                }
            }
        }
    }
    public void addResearchAndShownInfo(ResearchItem item, ShownInfoInResearchCategory shownInfo) {
        addResearch(item);
        addResearchShownInfo(item, shownInfo);
    }
}
