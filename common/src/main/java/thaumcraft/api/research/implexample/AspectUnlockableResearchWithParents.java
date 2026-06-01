package thaumcraft.api.research.implexample;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.api.research.interfaces.IAspectUnlockableResearch;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AspectUnlockableResearchWithParents extends SimpleAspectUnlockedResearch implements IAspectUnlockableResearch, IResearchParentsOwner {

    private final List<ResearchItemResourceLocation> parents;
    public AspectUnlockableResearchWithParents(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            UnmodifiableAspectList<Aspect> aspectsCost,
            List<ResearchItemResourceLocation> parents
    ) {
        super(key, category,aspectsCost);
        this.parents = parents;
    }

    @Override
    public boolean canPlayerCompleteResearchWithAspect(Player player) {
        Set<ResearchItemResourceLocation> researched = new HashSet<>(ResearchManager.getResearchForPlayer(player));
        return researched.containsAll(getParents());
    }

    @Override
    public List<ResearchItemResourceLocation> getParents() {
        return parents;
    }

    @Override
    public boolean canPlayerResearch(Player player) {
        Set<ResearchItemResourceLocation> researched = new HashSet<>(ResearchManager.getResearchForPlayer(player));
        return researched.containsAll(getParents());
    }
}
