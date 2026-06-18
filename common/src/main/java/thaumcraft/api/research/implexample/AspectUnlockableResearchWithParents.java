package thaumcraft.api.research.implexample;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IAspectUnlockableResearch;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

public class AspectUnlockableResearchWithParents extends SimpleAspectUnlockedResearch
        implements IAspectUnlockableResearch, IResearchParentsOwner {

    private final List<ResearchItemResourceLocation> parents;
    public AspectUnlockableResearchWithParents(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            @Unmodifiable AspectList<Aspect> aspectsCost,
            List<ResearchItemResourceLocation> parents
    ) {
        super(key, category,aspectsCost);
        this.parents = parents;
    }

    @Override
    public boolean canPlayerCompleteResearchWithAspect(Player player) {
        for (var parentKey : getParents()) {
            var parentResearch = ResearchItem.getResearch(parentKey);
            if (parentResearch != null) {
                if (!parentResearch.isLivingEntityCompletedResearch(player)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<ResearchItemResourceLocation> getParents() {
        return parents;
    }

    @Override
    public boolean canPlayerResearch(Player player) {
        return canPlayerCompleteResearchWithAspect(player);
    }
}
