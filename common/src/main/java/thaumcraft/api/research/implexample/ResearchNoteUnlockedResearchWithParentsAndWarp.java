package thaumcraft.api.research.implexample;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Range;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

import static thaumcraft.api.listeners.warp.WarpEventManager.addResearchWarpTo;

public abstract class ResearchNoteUnlockedResearchWithParentsAndWarp
        extends ResearchNoteUnlockedResearchWithParents {
    private final int warp;
    public ResearchNoteUnlockedResearchWithParentsAndWarp(
            ResearchItemResourceLocation key,
            @Range(from = 1, to = 3) int complexity,
            List<ResearchItemResourceLocation> parents,
            int warp) {
        super(key, complexity, parents);
        this.warp = warp;
    }

    public int getWarp() {
        return warp;
    }

    @Override
    public void completeResearchFor(Player player) {
        super.completeResearchFor(player);
        addResearchWarpTo(player,getWarp());
    }
}
