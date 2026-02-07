package thaumcraft.api.research;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.research.interfaces.IRenderableResearch;
import thaumcraft.api.research.interfaces.IResearchParentsHiddenOwner;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//TODO:Separate
public abstract class ResearchItem
{

    /**
	 * A short string used as a key for this research. Must be unique
	 */
	public final ResearchItemResourceLocation key;
	
	//although we have ThaumcraftShownResearchItem it's still fine to add category here
	protected final List<ResearchCategoryResourceLocation> categoryInternal = new ArrayList<>();
    @UnmodifiableView
    public final List<ResearchCategoryResourceLocation> category = Collections.unmodifiableList(categoryInternal);
	
//    /**
//     * This links to any research that needs to be completed before this research can be discovered or learnt.
//     */
//    public ResearchItemResourceLocation[] parents = null;
//
//    /**
//     * Like parent above, but a line will not be displayed in the thaumonomicon linking them. Just used to prevent clutter.
//     */
//    public ResearchItemResourceLocation[] parentsHidden = null;
//    /**
//     * any research linked to this that will be unlocked automatically when this research is complete
//     */
//    public ResearchItemResourceLocation[] siblings = null;


    
    /**
     * Research that can be directly purchased with RP in normal research difficulty.
     */
    private boolean isSecondary;

    
    /**
     * Stub research cannot be discovered by normal means, but can be unlocked via the sibling system.
     */
    private boolean isStub;
    
    /**
     * This indicated that the research is completely hidden and cannot be discovered by any 
     * player-controlled means. The recipes will never show up in the thaumonomicon.
     * Usually used to unlock "hidden" recipes via sibling unlocking, like 
     * the various cap and rod combos for wands.
     */
    private boolean isVirtual;    

    /**
     * Concealed research does not display in the thaumonomicon until parent researches are discovered.
     */
    private boolean showAfterParentDiscovered;
    
    /**
     * Hidden research can only be discovered via scanning or knowledge fragments 
     */
    private boolean isHidden;
    
    /**
     * This is the same as isHidden, except it cannot be discovered with knowledge fragments, only scanning.  
     */
    private boolean isLost;
    
    /**
     * These research items will automatically unlock for all players on game start
     */
    private boolean isAutoUnlock;
    
    /**
     * Scanning these items will have a chance of revealing hidden knowledge in the thaumonomicon
     */
    private ItemStack[] itemTriggers;
    
    /**
     * Scanning these entities will have a chance of revealing hidden knowledge in the thaumonomicon
     */
    private ResourceKey<EntityType<?>>[] entityTriggers;
    
    /**
     * Scanning things with these aspects will have a chance of revealing hidden knowledge in the thaumonomicon
     */
    private Aspect[] aspectTriggers;

	private ResearchPage[] pages = null;
	
	public ResearchItem(ResearchItemResourceLocation key, ResearchCategoryResourceLocation category)
    {
    	this.key = key;
        this.categoryInternal.add(category);
        this.setVirtual();
        registerResearchItem();
    }

    public static boolean doesPlayerHaveRequisites(String playerName, ResearchItemResourceLocation key) {
        var research = getResearch(key);
        return research.doesPlayerHaveRequisites(playerName);
    }

    public boolean doesPlayerHaveRequisites(String playerName) {
        Set<ResearchItemResourceLocation> researched = new HashSet<>(ResearchManager.getResearchForPlayer(playerName));
        if (this instanceof IResearchParentsOwner parentsOwner) {
            if (!researched.containsAll(parentsOwner.getParents())){
                return false;
            }
        }
        if (this instanceof IResearchParentsHiddenOwner parentsHiddenOwner) {
            if (!researched.containsAll(parentsHiddenOwner.getParentsHidden())){
                return false;
            }
        }
        return true;
    }

    private void registerResearchItem(){
        if (researchItems.containsKey(this.key)){
            throw new IllegalStateException("ResearchItem already exists");
        }
        researchItems.put(this.key, this);
    }
    
    public ResearchItem(ResearchItemResourceLocation key, ResearchCategoryResourceLocation category, int complex)
    {
    	this.key = key;
        this.categoryInternal.add(category);
        registerResearchItem();
    }
    public ResearchItem(ResearchItemResourceLocation key)
    {
        this.key = key;
        this.setVirtual();
        registerResearchItem();
    }

    public ResearchItem(ResearchItemResourceLocation key, int complex)
    {
        this.key = key;
        registerResearchItem();
    }

    private static final Map<ResearchItemResourceLocation, ResearchItem> researchItems = new ConcurrentHashMap<>();
    /**
     * @param key the research key
     * @return the ResearchItem object.
     */
    public static ResearchItem getResearch(ResearchItemResourceLocation key) {
        return researchItems.get(key);
    }

    public Component getName()
    {
    	return Component.translatable("tc.research_name."+key);
    }
    
    public Component getText()
    {
    	return Component.translatable("tc.research_text."+key);
    }

    public ResearchItem addCategory(ResearchCategoryResourceLocation categoryKey){
        var category = ResearchCategory.getResearchCategory(categoryKey);
        if (category == null){
            OpenTC4.LOGGER.error("ResearchCategory {} does not exist",categoryKey);
            return this;
        }
        if (this instanceof IRenderableResearch renderableResearch){
            category.addResearchAndShownInfo(this,renderableResearch.getShownInfo(category));
        }else {
            category.researches.put(this.key,this);
        }
        this.categoryInternal.add(categoryKey);
        return this;
    }

    @Override
    public String toString() {
        return "ResearchItem{" +
                "key=" + key +
                ", categoryInternal=" + categoryInternal +
                ", category=" + category +
                ", isSecondary=" + isSecondary +
                ", isStub=" + isStub +
                ", isVirtual=" + isVirtual +
                ", showAfterParentDiscovered=" + showAfterParentDiscovered +
                ", isHidden=" + isHidden +
                ", isLost=" + isLost +
                ", isAutoUnlock=" + isAutoUnlock +
                ", itemTriggers=" + Arrays.toString(itemTriggers) +
                ", entityTriggers=" + Arrays.toString(entityTriggers) +
                ", aspectTriggers=" + Arrays.toString(aspectTriggers) +
                ", pages=" + Arrays.toString(pages) +
                '}';
    }

    public boolean isPlayerCompletedResearch(String playerName){
        return ResearchManager.getResearchForPlayer(playerName).contains(this.key);
    }
}
