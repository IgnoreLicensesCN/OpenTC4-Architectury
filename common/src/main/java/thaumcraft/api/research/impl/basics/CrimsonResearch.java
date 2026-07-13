package thaumcraft.api.research.impl.basics;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchWarpOwner;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import static thaumcraft.api.listeners.warp.WarpEventManager.addResearchWarpTo;

public final class CrimsonResearch extends ResearchItem implements IResearchWarpOwner {
    public CrimsonResearch(){
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "crimson"));
    }

    @Override
    public int getWarp() {
        return 3;
    }

    @Override
    public void completeResearchFor(Player player) {
        super.completeResearchFor(player);
        addResearchWarpTo(player,getWarp());
    }
}
