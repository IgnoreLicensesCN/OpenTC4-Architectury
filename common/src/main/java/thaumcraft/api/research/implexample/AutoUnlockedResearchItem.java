package thaumcraft.api.research.implexample;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class AutoUnlockedResearchItem extends ResearchItem {
    public AutoUnlockedResearchItem(ResearchItemResourceLocation key) {
        super(key);
    }

    @Override
    public boolean isPlayerCompletedResearch(Player player) {
        return true;
    }

    @Override
    public void completeResearchFor(Player player) {

    }
}
