package thaumcraft.api.research.implexample;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IAspectUnlockableResearch;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class SimpleAspectUnlockedResearch extends ResearchItem implements IAspectUnlockableResearch {
    private final AspectList<Aspect> aspects;

    public SimpleAspectUnlockedResearch(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            AspectList<Aspect> aspectsCost
    ) {
        super(key);
        this.aspects = UnmodifiableAspectList.of(aspectsCost);
    }

    @Override
    public boolean canPlayerCompleteResearchWithAspect(Player player) {
        return true;
    }

    @Override
    public AspectList<Aspect> getAspectCost() {
        return aspects;
    }

    @Override
    public boolean canPlayerResearch(Player player) {
        return true;
    }
}
