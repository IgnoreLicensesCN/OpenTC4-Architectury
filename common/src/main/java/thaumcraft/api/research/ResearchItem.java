package thaumcraft.api.research;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ResearchItem
{

    /**
	 * A short string used as a key for this research. Must be unique
	 */
	public final ResearchItemResourceLocation key;

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
//    /**
//     * Research that can be directly purchased with RP in normal research difficulty.
//     */
//    private boolean isSecondary;
//
//
//    /**
//     * Stub research cannot be discovered by normal means, but can be unlocked via the sibling system.
//     */
//    private boolean isStub;
//
//    /**
//     * This indicated that the research is completely hidden and cannot be discovered by any
//     * player-controlled means. The recipes will never show up in the thaumonomicon.
//     * Usually used to unlock "hidden" recipes via sibling unlocking, like
//     * the various cap and rod combos for wands.
//     */
//    private boolean isVirtual;
//
//    /**
//     * Concealed research does not display in the thaumonomicon until parent researches are discovered.
//     */
//    private boolean showAfterParentDiscovered;
//
//    /**
//     * Hidden research can only be discovered via scanning or knowledge fragments
//     */
//    private boolean isHidden;
//
//    /**
//     * This is the same as isHidden, except it cannot be discovered with knowledge fragments, only scanning.
//     */
//    private boolean isLost;
//
//    /**
//     * These research items will automatically unlock for all players on game start
//     */
//    private boolean isAutoUnlock;
//
//    /**
//     * Scanning these items will have a chance of revealing hidden knowledge in the thaumonomicon
//     */
//    private ItemStack[] itemTriggers;
//
//    /**
//     * Scanning these entities will have a chance of revealing hidden knowledge in the thaumonomicon
//     */
//    private ResourceKey<EntityType<?>>[] entityTriggers;
//
//    /**
//     * Scanning things with these aspects will have a chance of revealing hidden knowledge in the thaumonomicon
//     */
//    private Aspect[] aspectTriggers;

//	private ResearchPage[] pages = null;
	
	public ResearchItem(ResearchItemResourceLocation key)
    {
    	this.key = key;
        registerResearchItem();
    }

//    public static boolean doesPlayerHaveRequisites(Player player, ResearchItemResourceLocation key) {
//        var research = getResearch(key);
//        return research.doesPlayerHaveRequisites(player);
//    }



    private void registerResearchItem(){
        if (researchItems.containsKey(this.key)){
            throw new IllegalStateException("ResearchItem already exists");
        }
        researchItems.put(this.key, this);
    }


    private static final Map<ResearchItemResourceLocation, ResearchItem> researchItems = new ConcurrentHashMap<>();
    /**
     * @param key the research key
     * @return the ResearchItem object.
     */
    public static @Nullable ResearchItem getResearch(ResearchItemResourceLocation key) {
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

//    public ResearchItem addCategory(ResearchCategoryResourceLocation categoryKey){
//        var category = ResearchCategory.getResearchCategory(categoryKey);
//        if (category == null){
//            OpenTC4.LOGGER.error("ResearchCategory {} does not exist",categoryKey);
//            return this;
//        }
//
//        category.addResearchAndShownInfo(this);
//        this.categoryInternal.add(categoryKey);
//        return this;
//    }

    @Override
    public String toString() {
        return "ResearchItem{" +
                "key=" + key +
//                ", category=" + category +
//                ", isSecondary=" + isSecondary +
//                ", isStub=" + isStub +
//                ", isVirtual=" + isVirtual +
//                ", showAfterParentDiscovered=" + showAfterParentDiscovered +
//                ", isHidden=" + isHidden +
//                ", isLost=" + isLost +
//                ", isAutoUnlock=" + isAutoUnlock +
//                ", itemTriggers=" + Arrays.toString(itemTriggers) +
//                ", entityTriggers=" + Arrays.toString(entityTriggers) +
//                ", aspectTriggers=" + Arrays.toString(aspectTriggers) +
//                ", pages=" + Arrays.toString(pages) +
                '}';
    }

    public boolean isLivingEntityCompletedResearch(LivingEntity living){
        var info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) return false;
        return info.hasResearchID(this.key);
    }

    public void completeResearchFor(LivingEntity living){
        var info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) return;
        info.addResearchID(this.key);
    }

    //keep for interface
    public ResearchItemResourceLocation getKey() {
        return key;
    }

}
