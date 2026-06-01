package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;

public interface IAspectUnlockableResearch extends IResearchableResearch {
    //do not check if cost is enough here.
    boolean canPlayerCompleteResearchWithAspect(Player player);
    AspectList<Aspect> getAspectCost();
}
