package thaumcraft.api.research;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ResearchCategory {

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
	
	public ResearchCategory(ResearchCategoryResourceLocation researchKey, ResourceLocation icon, ResourceLocation background) {
		this.icon = icon;
		this.background = background;
        this.categoryKey = researchKey;
	}

	//Research
	public final Map<ResearchItemResourceLocation, ResearchItem> researches = new HashMap<>();
    public final Map<ResearchItemResourceLocation, ShownInfoInResearchCategory> researchesShownInfo = new HashMap<>();

    public void addResearch(ResearchItem item, ShownInfoInResearchCategory shownInfo) {
        this.researches.put(item.key,item);
        this.researchesShownInfo.put(item.key,shownInfo);
        this.minDisplayColumn = Math.min(shownInfo.column(), this.minDisplayColumn);
        this.minDisplayRow = Math.min(shownInfo.row(), this.minDisplayRow);
        this.maxDisplayColumn = Math.max(shownInfo.column(), this.maxDisplayColumn);
        this.maxDisplayRow = Math.max(shownInfo.row(), this.maxDisplayRow);
    }
}
