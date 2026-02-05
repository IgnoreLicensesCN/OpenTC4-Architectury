package thaumcraft.api.research;

import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TODO:Separate
public class ResearchItem
{

    /**
	 * A short string used as a key for this research. Must be unique
	 */
	public final ResearchItemResourceLocation key;
	
	//although we have ThaumcraftShownResearchItem it's still fine to add category here
	protected final List<ResearchCategoryResourceLocation> categoryInternal = new ArrayList<>();
    @UnmodifiableView
    public final List<ResearchCategoryResourceLocation> category = Collections.unmodifiableList(categoryInternal);

	/**
	 * The aspect tags and their values required to complete this research
	 */
	public final AspectList<Aspect> tags = new AspectList<>();
	
    /**
     * This links to any research that needs to be completed before this research can be discovered or learnt.
     */
    public ResearchItemResourceLocation[] parents = null;
    
    /**
     * Like parent above, but a line will not be displayed in the thaumonomicon linking them. Just used to prevent clutter.
     */
    public ResearchItemResourceLocation[] parentsHidden = null;
    /**
     * any research linked to this that will be unlocked automatically when this research is complete
     */
    public ResearchItemResourceLocation[] siblings = null;

    
    /**
     * How large the research grid is. Valid values are 1 to 3.
     */
    private @Range(from=1,to=3) int complexity;

    
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
    }
    
    public ResearchItem(ResearchItemResourceLocation key, ResearchCategoryResourceLocation category, AspectList<Aspect> tags, int complex)
    {
    	this.key = key;
        this.categoryInternal.add(category);
    	this.tags.addAll(tags);
        this.complexity = Math.min(Math.max(complex, 1),3);
    }
    public ResearchItem(ResearchItemResourceLocation key)
    {
        this.key = key;
        this.setVirtual();
    }

    public ResearchItem(ResearchItemResourceLocation key, AspectList<Aspect> tags, int complex)
    {
        this.key = key;
        this.tags.addAll(tags);
        this.complexity = Math.min(Math.max(complex, 1),3);
    }
    
    public ResearchItem setStub()
    {
        this.isStub = true;
        return this;
    }
    
    public ResearchItem setLost()
    {
        this.isLost = true;
        return this;
    }
    
    public ResearchItem setShowAfterParentDiscovered()
    {
        this.showAfterParentDiscovered = true;
        return this;
    }
    
    public ResearchItem setHidden()
    {
        this.isHidden = true;
        return this;
    }
    
    public ResearchItem setVirtual()
    {
        this.isVirtual = true;
        return this;
    }
    
    public ResearchItem setParents(ResearchItemResourceLocation... par)
    {
        this.parents = par;
        return this;
    }
    
    

	public ResearchItem setParentsHidden(ResearchItemResourceLocation... par)
    {
        this.parentsHidden = par;
        return this;
    }
    
    public ResearchItem setSiblings(ResearchItemResourceLocation... sib)
    {
        this.siblings = sib;
        return this;
    }
    
    public ResearchItem setPages(ResearchPage... par)
    {
        this.pages = par;
        return this;
    }
    
    public ResearchPage[] getPages() {
		return pages;
	}
    
    public ResearchItem setItemTriggers(ItemStack... par)
    {
        this.itemTriggers = par;
        return this;
    }
    
    @SafeVarargs
    public final ResearchItem setEntityTriggers(ResourceKey<EntityType<?>>... par)
    {
        this.entityTriggers = par;
        return this;
    }
    
    public ResearchItem setAspectTriggers(Aspect... par)
    {
        this.aspectTriggers = par;
        return this;
    }

    public ItemStack[] getItemTriggers() {
		return itemTriggers;
	}

	public ResourceKey<EntityType<?>>[] getEntityTriggers() {
		return entityTriggers;
	}
	
	public Aspect[] getAspectTriggers() {
		return aspectTriggers;
	}

	public ResearchItem registerResearchItem(List<ShownInfoInResearchCategory> shownInfos)
    {
        ResearchCategories.addResearchToItsCategory(this,shownInfos);
        return this;
    }

    public String getName()
    {
    	return StatCollector.translateToLocal("tc.research_name."+key);
    }
    
    public String getText()
    {
    	return StatCollector.translateToLocal("tc.research_text."+key);
    }
    
    public boolean isStub()
    {
        return this.isStub;
    }
        
    public boolean isLost()
    {
        return this.isLost;
    }
    
    public boolean wouldShowAfterParentDiscovered()
    {
        return this.showAfterParentDiscovered;
    }
    
    public boolean isHidden()
    {
        return this.isHidden;
    }
    
    public boolean isVirtual()
    {
        return this.isVirtual;
    }
    
    public boolean isAutoUnlock() {
		return isAutoUnlock;
	}
	
	public ResearchItem setAutoUnlock()
    {
        this.isAutoUnlock = true;
        return this;
    }
	
	public boolean isSecondary() {
		return isSecondary;
	}

	public ResearchItem setSecondary() {
		this.isSecondary = true;
		return this;
	}

	public int getComplexity() {
		return complexity;
	}

	public ResearchItem setComplexity(int complexity) {
		this.complexity = complexity;
		return this;
	}

	/**
	 * @return the aspect aspects ordinal with the highest value. Used to determine scroll color and similar things
	 */
    @NotNull("null->empty")
	public Aspect getResearchPrimaryTag() {
		Aspect aspect = Aspects.EMPTY;
		int highest=0;
        for (Aspect tag : tags.getAspectTypes()) {
            if (tags.getAmount(tag) > highest) {
                aspect = tag;
                highest = tags.getAmount(tag);
            }
        }
        return aspect;
	}

    public ResearchItem addCategory(ResearchCategoryResourceLocation category){
        this.categoryInternal.add(category);
        return this;
    }

    @Override
    public String toString() {
        return "ResearchItem{" +
                "key=" + key +
                ", categoryInternal=" + categoryInternal +
                ", category=" + category +
                ", tags=" + tags +
                ", parents=" + Arrays.toString(parents) +
                ", parentsHidden=" + Arrays.toString(parentsHidden) +
                ", siblings=" + Arrays.toString(siblings) +
                ", complexity=" + complexity +
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
}
